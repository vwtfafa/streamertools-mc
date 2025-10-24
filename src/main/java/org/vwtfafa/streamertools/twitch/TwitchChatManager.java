package org.vwtfafa.streamertools.twitch;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.vwtfafa.streamertools.StreamerToolsMod;
import org.vwtfafa.streamertools.config.TwitchConfig;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class TwitchChatManager {
    private static final Logger LOGGER = LogManager.getLogger("TwitchChatManager");
    private static final int MAX_TASKS_PER_TICK = 100;
    private static final int MESSAGE_RATE_LIMIT = 10; // messages per second
    private static final int COMMAND_RATE_LIMIT = 5; // commands per second
    
    private final TwitchConfig config;
    private final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
    private final AtomicBoolean connected = new AtomicBoolean(false);
    private final RateLimiter messageRateLimiter = new RateLimiter(MESSAGE_RATE_LIMIT, 1, TimeUnit.SECONDS);
    private final RateLimiter commandRateLimiter = new RateLimiter(COMMAND_RATE_LIMIT, 1, TimeUnit.SECONDS);
    
    private volatile TwitchClient twitchClient;
    
    public TwitchChatManager(TwitchConfig config) {
        this.config = config;
    }
    
    @Environment(EnvType.SERVER)
    public synchronized void connect() {
        if (connected.get()) {
            LOGGER.warn("Already connected to Twitch chat");
            return;
        }
        
        // Validate config is not null
        if (config == null) {
            LOGGER.error("Cannot connect to Twitch: TwitchConfig is null");
            return;
        }
        
        // Validate OAuth token
        String token = config.getOauthToken();
        if (token == null || token.trim().isEmpty()) {
            LOGGER.error("Cannot connect to Twitch: OAuth token is missing or empty");
            return;
        }
        
        // Validate channel name
        String channel = validateAndNormalizeChannel(config.getChannel());
        if (channel == null) {
            LOGGER.error("Cannot connect to Twitch: Invalid channel name");
            return;
        }
        
        TwitchClient client = null;
        try {
            // Create OAuth credential
            OAuth2Credential credential = new OAuth2Credential("twitch", token.trim());
            
            // Build and create Twitch client
            client = TwitchClientBuilder.builder()
                    .withEnableChat(true)
                    .withChatAccount(credential)
                    .withEnableHelix(true)
                    .build();
            
            if (client == null) {
                LOGGER.error("Failed to create Twitch client: builder returned null");
                return;
            }
            
            // Register event listener for chat messages
            if (client.getEventManager() != null) {
                client.getEventManager().onEvent(ChannelMessageEvent.class, this::onChannelMessage);
            } else {
                LOGGER.warn("Event manager is null, chat events may not be received");
            }
            
            // Join the channel
            if (client.getChat() != null) {
                client.getChat().joinChannel(channel);
            } else {
                LOGGER.error("Chat module is null, cannot join channel");
                client.close();
                return;
            }
            
            // Update state only after successful connection
            this.twitchClient = client;
            connected.set(true);
            LOGGER.info("Successfully connected to Twitch chat as {} in channel {}", 
                config.getUsername(), channel);
            
        } catch (Exception e) {
            LOGGER.error("Failed to connect to Twitch chat: {}", e.getMessage(), e);
            // Clean up client if connection failed
            if (client != null) {
                try {
                    client.close();
                } catch (Exception closeEx) {
                    LOGGER.warn("Error closing Twitch client after failed connection: {}", closeEx.getMessage());
                }
            }
            connected.set(false);
            this.twitchClient = null;
        }
    }
    
    @Environment(EnvType.SERVER)
    public synchronized void disconnect() {
        // Check if already disconnected
        if (!connected.get() && twitchClient == null) {
            LOGGER.debug("Already disconnected from Twitch chat");
            return;
        }
        
        // Get reference to client and clear field
        TwitchClient client = this.twitchClient;
        this.twitchClient = null;
        connected.set(false);
        
        // If no client to disconnect, just clean up
        if (client == null) {
            LOGGER.info("Disconnected from Twitch chat (no active client)");
            taskQueue.clear();
            return;
        }
        
        try {
            // Try to leave channel gracefully
            try {
                if (config != null && config.getChannel() != null) {
                    String channel = validateAndNormalizeChannel(config.getChannel());
                    if (channel != null) {
                        if (client.getChat() != null) {
                            client.getChat().leaveChannel(channel);
                            LOGGER.debug("Left Twitch channel: {}", channel);
                        } else {
                            LOGGER.warn("Chat module is null, cannot leave channel");
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.warn("Error leaving Twitch channel: {}", e.getMessage());
            }
            
            // Close the client
            try {
                client.close();
                LOGGER.info("Disconnected from Twitch chat");
            } catch (Exception e) {
                LOGGER.error("Error closing Twitch client: {}", e.getMessage(), e);
            }
        } finally {
            // Always clear pending tasks
            try {
                taskQueue.clear();
            } catch (Exception e) {
                LOGGER.warn("Error clearing task queue: {}", e.getMessage());
            }
        }
    }
    
    @Environment(EnvType.SERVER)
    public void sendMessage(@NotNull String message) {
        sendMessageInternal(message, true);
    }
    
    private void sendMessageInternal(String message, boolean enforceRateLimit) {
        // Validate input
        if (message == null) {
            LOGGER.warn("Cannot send null message to Twitch chat");
            return;
        }
        
        String trimmedMessage = message.trim();
        if (trimmedMessage.isEmpty()) {
            LOGGER.warn("Cannot send empty message to Twitch chat");
            return;
        }
        
        // Check connection state
        if (!connected.get()) {
            LOGGER.warn("Cannot send message: Not connected to Twitch chat");
            return;
        }
        
        TwitchClient clientSnapshot = this.twitchClient;
        if (clientSnapshot == null) {
            LOGGER.warn("Cannot send message: Twitch client is null");
            return;
        }
        
        if (config == null) {
            LOGGER.warn("Cannot send message: Config is null");
            return;
        }
        
        // Validate and get channel
        final String channel = validateAndNormalizeChannel(config.getChannel());
        if (channel == null) {
            LOGGER.warn("Cannot send message: Invalid channel name");
            return;
        }
        
        // Apply rate limiting if requested
        if (enforceRateLimit && !messageRateLimiter.tryAcquire()) {
            LOGGER.debug("Message rate limit exceeded. Message not sent: {}", trimmedMessage);
            return;
        }
        
        // Limit message length
        String finalMessage = trimmedMessage.length() > 500 ?
                trimmedMessage.substring(0, 497) + "..." : trimmedMessage;
        
        boolean queued = taskQueue.offer(() -> {
            try {
                TwitchClient currentClient = twitchClient;
                if (currentClient != null && currentClient.getChat() != null) {
                    currentClient.getChat().sendMessage(channel, finalMessage);
                    LOGGER.debug("Message sent to Twitch chat: {}", finalMessage);
                } else {
                    LOGGER.warn("Cannot send message: Twitch client or chat module is null");
                }
            } catch (Exception e) {
                LOGGER.error("Failed to send message to Twitch chat: {}", e.getMessage(), e);
            }
        });
        
        if (!queued) {
            LOGGER.warn("Failed to queue Twitch chat message for sending");
        }
    }
    
    private void onChannelMessage(ChannelMessageEvent event) {
        // Validate event
        if (event == null) {
            LOGGER.debug("Received null chat event");
            return;
        }
        
        // Check if enabled and connected
        if (!connected.get()) {
            LOGGER.debug("Ignoring chat event: not connected");
            return;
        }
        
        if (config == null) {
            LOGGER.warn("Config is null in onChannelMessage");
            return;
        }
        
        if (!config.isEnabled()) {
            LOGGER.debug("Ignoring chat event: feature disabled");
            return;
        }
        
        try {
            // Extract message
            String message = event.getMessage();
            if (message == null || message.trim().isEmpty()) {
                LOGGER.debug("Received empty message from Twitch");
                return;
            }
            
            // Extract username
            String username = null;
            if (event.getUser() != null && event.getUser().getName() != null) {
                username = event.getUser().getName().trim();
            }
            
            if (username == null || username.isEmpty()) {
                LOGGER.debug("Received message with invalid username");
                return;
            }
            
            // Queue message processing on main thread
            String finalUsername = username;
            String finalMessage = message.trim();
            
            boolean offered = taskQueue.offer(() -> processIncomingMessage(finalUsername, finalMessage));
            if (!offered) {
                LOGGER.warn("Failed to queue message processing: queue is full");
            }
            
        } catch (Exception e) {
            LOGGER.error("Error handling Twitch chat event: {}", e.getMessage(), e);
        }
    }
    
    private void processIncomingMessage(String username, String message) {
        // Validate parameters
        if (username == null || message == null || config == null) {
            LOGGER.warn("Invalid parameters for processIncomingMessage");
            return;
        }
        
        try {
            // Process chat commands if enabled
            if (config.isAllowChatCommands() && message.startsWith("!")) {
                // Validate message length before substring
                if (message.length() > 1) {
                    String command = message.substring(1).trim();
                    if (!command.isEmpty()) {
                        processChatCommand(username, command);
                    }
                }
            }
            
            // Add to in-game chat if enabled
            if (config.isShowChatInGame()) {
                // Get Minecraft server instance
                StreamerToolsMod modInstance = StreamerToolsMod.getInstance();
                if (modInstance == null) {
                    LOGGER.warn("StreamerToolsMod instance is null");
                    return;
                }
                
                MinecraftServer server = modInstance.getServer();
                if (server == null) {
                    LOGGER.debug("Minecraft server is null");
                    return;
                }
                
                if (server.getPlayerList() == null) {
                    LOGGER.warn("Player manager is null");
                    return;
                }
                
                // Sanitize message to prevent Minecraft formatting exploits
                String sanitizedMessage = message
                    .replaceAll("§[0-9a-fklmnor]", "")
                    .replace("\n", " ")
                    .replace("\r", "")
                    .trim();
                
                // Skip if message is empty after sanitization
                if (sanitizedMessage.isEmpty()) {
                    LOGGER.debug("Message is empty after sanitization");
                    return;
                }
                
                // Truncate message if too long
                if (sanitizedMessage.length() > 256) {
                    sanitizedMessage = sanitizedMessage.substring(0, 253) + "...";
                }
                
                // Sanitize username
                String sanitizedUsername = username.replaceAll("[^a-zA-Z0-9_]", "");
                if (sanitizedUsername == null || sanitizedUsername.isEmpty()) {
                    sanitizedUsername = "Unknown";
                }
                
                try {
                    // Validate sanitized values before creating message
                    if (sanitizedUsername == null || sanitizedUsername.isEmpty()) {
                        LOGGER.warn("Sanitized username is null or empty");
                        return;
                    }
                    
                    if (sanitizedMessage == null || sanitizedMessage.isEmpty()) {
                        LOGGER.warn("Sanitized message is null or empty");
                        return;
                    }
                    
                    // Create formatted message string
                    String formattedMessage = String.format("[Twitch] <%s> %s", 
                        sanitizedUsername, sanitizedMessage);
                    
                    if (formattedMessage == null || formattedMessage.isEmpty()) {
                        LOGGER.warn("Formatted message is null or empty");
                        return;
                    }
                    
                    // Broadcast message to all players using server command
                    try {
                        if (server.getPlayerList() != null) {
                            // Execute tellraw command to send colored message to all players
                            String tellrawCommand = String.format(
                                "tellraw @a {\"text\":\"[Twitch] <%s> %s\",\"color\":\"aqua\"}",
                                sanitizedUsername.replace("\"", "\\\""), 
                                sanitizedMessage.replace("\"", "\\\"")
                            );
                            server.getCommands().performPrefixedCommand(
                                server.createCommandSourceStack(), 
                                tellrawCommand
                            );
                            LOGGER.debug("Broadcasted Twitch message from {}", sanitizedUsername);
                        } else {
                            LOGGER.warn("Player manager is null, cannot broadcast message");
                        }
                    } catch (Exception broadcastEx) {
                        LOGGER.error("Error broadcasting Twitch message: {}", broadcastEx.getMessage(), broadcastEx);
                    }
                } catch (Exception messageEx) {
                    LOGGER.error("Error creating Twitch chat message: {}", messageEx.getMessage(), messageEx);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error processing Twitch message from {}: {}", 
                username, e.getMessage(), e);
        }
    }
    
    /**
     * Processes chat commands from Twitch chat
     * @param username The username of the command sender
     * @param command The command to process (without the ! prefix)
     */
    private void processChatCommand(String username, String command) {
        // Validate parameters
        if (username == null || username.isEmpty()) {
            LOGGER.debug("Cannot process command: username is null or empty");
            return;
        }
        
        if (command == null || command.isEmpty()) {
            LOGGER.debug("Cannot process command: command is null or empty");
            return;
        }
        
        String trimmedUsername = username.trim();
        String trimmedCommand = command.trim();
        
        if (trimmedUsername.isEmpty() || trimmedCommand.isEmpty()) {
            LOGGER.debug("Cannot process command: trimmed parameters are empty");
            return;
        }
        
        LOGGER.info("Processing command '{}' from user: {}", trimmedCommand, trimmedUsername);
        
        try {
            // Apply rate limiting to command execution
            if (!commandRateLimiter.tryAcquire()) {
                LOGGER.debug("Command rate limit exceeded for '{}' from {}", trimmedCommand, trimmedUsername);
                return;
            }
            
            // Process the command (case-insensitive)
            String lowerCommand = trimmedCommand.toLowerCase();
            String response = null;
            
            switch (lowerCommand) {
                case "heal" -> response = String.format("@%s used heal command!", trimmedUsername);
                case "status" -> response = String.format("@%s Server is running!", trimmedUsername);
                case "help" -> response = "Available commands: !heal, !status, !help";
                default -> LOGGER.debug("Unknown command: {}", trimmedCommand);
            }
            
            // Send response if available
            if (response != null && !response.isEmpty()) {
                sendMessageInternal(response, false);
            }
            
        } catch (Exception e) {
            LOGGER.error("Error processing command '{}' from {}: {}", 
                trimmedCommand, trimmedUsername, e.getMessage(), e);
        }
    }
    
    /**
     * Processes pending tasks from the Twitch chat queue
     * Should be called once per server tick
     */
    @Environment(EnvType.SERVER)
    public void tick() {
        // Skip if not connected or queue is empty
        if (!connected.get() || taskQueue.isEmpty()) {
            return;
        }
        
        // Process pending tasks on the main thread with timeout to prevent blocking
        int processed = 0;
        long startTime = System.nanoTime();
        final long maxTimeNanos = TimeUnit.MILLISECONDS.toNanos(10); // Max 10ms per tick
        
        while (!taskQueue.isEmpty() && 
               processed < MAX_TASKS_PER_TICK && 
               System.nanoTime() - startTime < maxTimeNanos) {
            
            Runnable task = null;
            try {
                // Poll task from queue
                task = taskQueue.poll();
                
                if (task != null) {
                    try {
                        // Execute task
                        task.run();
                        processed++;
                    } catch (Exception e) {
                        LOGGER.error("Error executing Twitch chat task: {}", e.getMessage(), e);
                    }
                } else {
                    // Queue is empty
                    break;
                }
            } catch (Exception e) {
                LOGGER.error("Unexpected error in Twitch chat task processing: {}", e.getMessage(), e);
                break; // Prevent infinite loop on persistent errors
            }
        }
        
        // Log performance metrics
        if (processed > 0) {
            long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
            LOGGER.trace("Processed {} Twitch chat tasks in {} ms", processed, elapsedMs);
        }
    }
    
    /**
     * Validates and normalizes a Twitch channel name.
     * @param channel The channel name to validate
     * @return Normalized channel name without leading #, or null if invalid
     */
    private String validateAndNormalizeChannel(String channel) {
        // Check for null
        if (channel == null) {
            LOGGER.debug("Channel name is null");
            return null;
        }
        
        // Trim and check for empty
        String trimmed = channel.trim();
        if (trimmed.isEmpty()) {
            LOGGER.debug("Channel name is empty");
            return null;
        }
        
        // Normalize: lowercase and remove leading #
        String normalized = trimmed.toLowerCase().replaceAll("^#+", "");
        
        // Check if normalized is empty (was only # characters)
        if (normalized.isEmpty()) {
            LOGGER.warn("Invalid Twitch channel name (only # characters): {}", channel);
            return null;
        }
        
        // Validate channel name format
        // Twitch channel names: 4-25 characters, alphanumeric and underscores
        if (!normalized.matches("^[a-z0-9_]{4,25}$")) {
            LOGGER.warn("Invalid Twitch channel name format: {} (normalized: {})", channel, normalized);
            return null;
        }
        
        return normalized;
    }
    
    /**
     * Simple rate limiter for chat messages and commands
     * Allows up to maxPermits requests per period
     */
    private static class RateLimiter {
        private final int maxPermits;
        private final long periodNanos;
        private long nextFreeTicketNanos;
        private int permitsUsed;
        private final Object lock = new Object();
        
        /**
         * Creates a rate limiter
         * @param maxPermits Maximum number of permits per period
         * @param period Time period value
         * @param timeUnit Time unit for the period
         */
        public RateLimiter(int maxPermits, long period, TimeUnit timeUnit) {
            if (maxPermits <= 0) {
                throw new IllegalArgumentException("maxPermits must be positive");
            }
            if (period <= 0) {
                throw new IllegalArgumentException("period must be positive");
            }
            if (timeUnit == null) {
                throw new IllegalArgumentException("timeUnit cannot be null");
            }
            
            this.maxPermits = maxPermits;
            this.periodNanos = timeUnit.toNanos(period);
            this.nextFreeTicketNanos = System.nanoTime();
            this.permitsUsed = 0;
        }
        
        /**
         * Tries to acquire a permit
         * @return true if permit was acquired, false if rate limit exceeded
         */
        public boolean tryAcquire() {
            synchronized (lock) {
                long now = System.nanoTime();
                
                // Check if period has elapsed
                if (now >= nextFreeTicketNanos) {
                    // Reset for new period
                    nextFreeTicketNanos = now + periodNanos;
                    permitsUsed = 1;
                    return true;
                }
                
                // Check if we have permits left in current period
                if (permitsUsed < maxPermits) {
                    permitsUsed++;
                    return true;
                }
                
                // Rate limit exceeded
                return false;
            }
        }
        
        /**
         * Resets the rate limiter
         */
        public void reset() {
            synchronized (lock) {
                nextFreeTicketNanos = System.nanoTime();
                permitsUsed = 0;
            }
        }
    }
    
    /**
     * Check if the manager is currently connected to Twitch
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        return connected.get();
    }
}
