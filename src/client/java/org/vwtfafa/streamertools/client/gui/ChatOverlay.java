package org.vwtfafa.streamertools.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.vwtfafa.streamertools.StreamerToolsMod;
import org.vwtfafa.streamertools.config.ModConfig;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

public class ChatOverlay {
    private static final int MAX_MESSAGES = 20;
    private static final int MESSAGE_LIFETIME = 10000; // 10 seconds
    private static final int FADE_OUT_TIME = 2000; // 2 seconds
    private static final int MAX_MESSAGE_WIDTH = 200; // Maximum width of the chat overlay
    
    private final Queue<ChatMessage> messageQueue = new ArrayDeque<>();
    private final Minecraft client;
    private final Font font;
    
    public ChatOverlay() {
        this.client = Minecraft.getInstance();
        this.font = client.font;
    }
    
    public void addMessage(String username, String message) {
        if (username == null || message == null) {
            return;
        }
        
        // Format message with username
        String formatted = String.format("<%s> %s", username, message);
        
        // Split into multiple lines if needed and add to queue
        for (FormattedCharSequence line : font.split(Component.literal(formatted), MAX_MESSAGE_WIDTH)) {
            messageQueue.add(new ChatMessage(line));
        }
        
        // Remove oldest messages if we exceed the limit
        while (messageQueue.size() > MAX_MESSAGES) {
            messageQueue.poll();
        }
    }
    
    /**
     * Renders the chat overlay with all visible messages
     * @param context The draw context for rendering
     * @param tickDelta The time since the last tick in seconds
     */
    public void render(GuiGraphics context, float tickDelta) {
        // Early exit if there are no messages or client is not available
        if (messageQueue.isEmpty() || client == null) {
            return;
        }
        
        // Check if chat should be shown based on config
        ModConfig config = StreamerToolsMod.getInstance().getConfig();
        if (config == null || config.getTwitchConfig() == null || !config.getTwitchConfig().isShowChatInGame()) {
            return;
        }
        
        // Get screen dimensions
        int screenWidth = client.getWindow().getGuiScaledWidth();
        int x = screenWidth - 10; // Right side with 10px padding
        int y = 30; // Top position below hotbar
        
        // Prepare for rendering
        long currentTime = System.currentTimeMillis();
        int messageY = y;
        
        // Use an iterator to safely remove messages while iterating
        Iterator<ChatMessage> iterator = messageQueue.iterator();
        while (iterator.hasNext()) {
            ChatMessage chatMessage = iterator.next();
            
            // Remove messages that are too old
            if (currentTime - chatMessage.timestamp > MESSAGE_LIFETIME + FADE_OUT_TIME) {
                iterator.remove();
                continue;
            }
            
            // Calculate alpha based on time remaining
            long timeRemaining = (chatMessage.timestamp + MESSAGE_LIFETIME) - currentTime;
            float alpha = calculateAlpha(timeRemaining);
            
            // Skip rendering if completely transparent
            if (alpha <= 0.01f) {
                continue;
            }
            
            // Draw the message
            renderMessage(context, chatMessage.text, x, messageY, alpha);
            
            // Move up for the next message
            messageY += font.lineHeight + 2;
            
            // Stop if we're running out of vertical space
            if (messageY > client.getWindow().getGuiScaledHeight() - 10) {
                break;
            }
        }
    }
    
    /**
     * Calculates the alpha value for fading out messages
     * @param timeRemaining Time remaining before message starts fading (in milliseconds)
     * @return Alpha value between 0.0 and 1.0
     */
    private float calculateAlpha(long timeRemaining) {
        if (timeRemaining <= 0) {
            return 0.0f;
        } else if (timeRemaining >= FADE_OUT_TIME) {
            return 1.0f;
        }
        return timeRemaining / (float) FADE_OUT_TIME;
    }
    
    /**
     * Renders a single chat message
     * @param context The draw context
     * @param text The text to render
     * @param x Right-aligned x position
     * @param y Top y position
     * @param alpha The alpha value (0.0 to 1.0)
     */
    private void renderMessage(GuiGraphics context, FormattedCharSequence text, int x, int y, float alpha) {
        int textWidth = font.width(text);
        int color = 0xFFFFFF | (Mth.floor(alpha * 255.0F) << 24);
        
        context.drawString(
            font,
            text,
            x - textWidth - 2, // Right-align the text
            y,
            color,
            true
        );
    }
    
    private static class ChatMessage {
        public final FormattedCharSequence text;
        public final long timestamp;
        
        public ChatMessage(FormattedCharSequence text) {
            this.text = text;
            this.timestamp = System.currentTimeMillis();
        }
    }
}
