package org.vwtfafa.streamertools.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.vwtfafa.streamertools.StreamerToolsMod;
import org.vwtfafa.streamertools.client.gui.ChatOverlay;
import org.vwtfafa.streamertools.client.gui.ConfigScreenFactory;
import org.vwtfafa.streamertools.config.ModConfig;

/**
 * Client-side mod initializer for Streamer Tools
 * Handles key bindings, HUD rendering, and chat overlay
 */
@Environment(EnvType.CLIENT)
public class StreamerToolsModClient implements ClientModInitializer {
    private static final Logger LOGGER = LogManager.getLogger("StreamerTools/Client");
    private static StreamerToolsModClient instance;
    private Minecraft client;
    private KeyMapping configKeyBinding;
    private KeyMapping toggleChatKeyBinding;
    private ChatOverlay chatOverlay;
    private boolean showChatOverlay = true;
    
    public StreamerToolsModClient() {
        this.client = Minecraft.getInstance();
    }
    
    @Override
    public void onInitializeClient() {
        try {
            LOGGER.info("Initializing Streamer Tools Client...");
            
            instance = this;
            
            if (client == null) {
                client = Minecraft.getInstance();
                if (client == null) {
                    throw new IllegalStateException("Failed to get Minecraft client instance");
                }
            }
            
            // Verify mod is initialized
            if (!StreamerToolsMod.isInitialized()) {
                LOGGER.warn("Main mod not initialized yet, waiting...");
                // Try to initialize the main mod
                new StreamerToolsMod().onInitialize();
            }
            
            // Register key bindings
            LOGGER.info("Registering key bindings...");
            configKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                    "key.streamertools.config",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_P,
                    "category.streamertools.main"
            ));
            
            toggleChatKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.streamertools.toggle_chat",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "category.streamertools.main"
        ));
        
        // Register client tick event
        ClientTickEvents.END_CLIENT_TICK.register(tickClient -> {
            if (tickClient == null || tickClient.player == null) {
                return;
            }
            
            // Handle key bindings
            while (configKeyBinding != null && configKeyBinding.consumeClick()) {
                tickClient.setScreen(ConfigScreenFactory.createConfigScreen(null));
            }
            
            while (toggleChatKeyBinding != null && toggleChatKeyBinding.consumeClick()) {
                showChatOverlay = !showChatOverlay;
                if (tickClient.player != null) {
                    tickClient.player.displayClientMessage(Component.translatable(
                            showChatOverlay ? "message.streamertools.chat_shown" : "message.streamertools.chat_hidden"
                    ), false);
                }
            }
        });
        
        // Initialize chat overlay
        chatOverlay = new ChatOverlay();
        
        // Register HUD render callback
        HudRenderCallback.EVENT.register((guiGraphics, deltaTracker) -> {
            if (showChatOverlay && client != null && client.player != null && chatOverlay != null) {
                chatOverlay.render(guiGraphics, deltaTracker.getGameTimeDeltaTicks());
            }
        });
        
            StreamerToolsMod.LOGGER.info("Streamer Tools client initialized!");
        } catch (Exception e) {
            LOGGER.error("Failed to initialize Streamer Tools client", e);
            throw new RuntimeException("Failed to initialize Streamer Tools client", e);
        }
    }

    public static StreamerToolsModClient getInstance() {
        return instance;
    }

    public ChatOverlay getChatOverlay() {
        return chatOverlay;
    }

    public boolean isShowChatOverlay() {
        return showChatOverlay;
    }

    public void setShowChatOverlay(boolean showChatOverlay) {
        this.showChatOverlay = showChatOverlay;
    }

    public ModConfig getConfig() {
        return StreamerToolsMod.getInstance().getConfig();
    }
}