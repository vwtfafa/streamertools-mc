package org.vwtfafa.streamertools.client.gui;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ModMenu integration for Streamer Tools
 * Provides the configuration screen in the ModMenu interface
 */
public class ModMenuIntegration implements ModMenuApi {
    private static final Logger LOGGER = LogManager.getLogger("StreamerTools/ModMenu");
    
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        LOGGER.info("ModMenu integration initialized");
        return parent -> {
            try {
                LOGGER.info("Creating config screen");
                // Create and return the config screen using our ConfigScreenFactory
                return org.vwtfafa.streamertools.client.gui.ConfigScreenFactory.createConfigScreen(parent);
            } catch (Exception e) {
                LOGGER.error("Failed to create config screen", e);
                return parent;
            }
        };
    }
}
