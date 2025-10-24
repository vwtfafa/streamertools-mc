package org.vwtfafa.streamertools;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.vwtfafa.streamertools.config.ModConfig;
import org.vwtfafa.streamertools.twitch.TwitchChatManager;

public class StreamerToolsMod implements ModInitializer {
    public static final String MOD_ID = "streamertools";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    
    private static StreamerToolsMod instance;
    private ModConfig config;
    private TwitchChatManager twitchChatManager;
    private MinecraftServer server;
    private boolean initialized = false;

    @Override
    public void onInitialize() {
        try {
            LOGGER.info("Initializing Streamer Tools Mod...");
            
            // Set instance early for other classes to access
            instance = this;
            
            // Register configuration
            LOGGER.info("Registering configuration...");
            AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
            config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
            
            // Register server lifecycle events
            LOGGER.info("Registering server lifecycle events...");
            ServerLifecycleEvents.SERVER_STARTING.register(this::onServerStarting);
            ServerLifecycleEvents.SERVER_STOPPING.register(this::onServerStopping);
            
            // Register tick events
            LOGGER.info("Registering tick events...");
            ServerTickEvents.END_SERVER_TICK.register(this::onServerTick);
            
            // Mark as initialized
            initialized = true;
            LOGGER.info("Streamer Tools mod initialized successfully!");
            
        } catch (Exception e) {
            LOGGER.error("Failed to initialize Streamer Tools mod!", e);
            throw new RuntimeException("Failed to initialize Streamer Tools mod", e);
        }
    }
    
    public static boolean isInitialized() {
        return instance != null && instance.initialized;
    }
    
    private void onServerStarting(MinecraftServer server) {
        this.server = server;
        
        // Initialize Twitch chat if enabled
        if (config != null && config.getTwitchConfig() != null && config.getTwitchConfig().isEnabled()) {
            String oauthToken = config.getTwitchConfig().getOauthToken();
            if (oauthToken != null && !oauthToken.isEmpty()) {
                try {
                    twitchChatManager = new TwitchChatManager(config.getTwitchConfig());
                    twitchChatManager.connect();
                    LOGGER.info("Twitch chat manager initialized and connected");
                } catch (Exception e) {
                    LOGGER.error("Failed to initialize Twitch chat manager", e);
                }
            } else {
                LOGGER.warn("Twitch chat is enabled but OAuth token is missing");
            }
        }
    }
    
    private void onServerStopping(MinecraftServer server) {
        if (twitchChatManager != null) {
            try {
                twitchChatManager.disconnect();
            } catch (Exception e) {
                LOGGER.error("Error disconnecting Twitch chat manager", e);
            } finally {
                twitchChatManager = null;
            }
        }
        this.server = null;
    }
    
    private void onServerTick(MinecraftServer server) {
        // Handle tick-based updates
        if (twitchChatManager != null) {
            twitchChatManager.tick();
        }
    }
    
    public static StreamerToolsMod getInstance() {
        return instance;
    }
    
    public ModConfig getConfig() {
        return config;
    }
    
    public MinecraftServer getServer() {
        return server;
    }
    
    public void saveConfig() {
        AutoConfig.getConfigHolder(ModConfig.class).save();
    }
}
