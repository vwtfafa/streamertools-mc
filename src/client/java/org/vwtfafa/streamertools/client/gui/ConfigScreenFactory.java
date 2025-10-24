package org.vwtfafa.streamertools.client.gui;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.vwtfafa.streamertools.StreamerToolsMod;
import org.vwtfafa.streamertools.config.ModConfig;
import org.vwtfafa.streamertools.config.TwitchConfig;

/**
 * Factory for creating the mod configuration screen
 */
public class ConfigScreenFactory {
    /**
     * Creates the main configuration screen for Streamer Tools
     * @param parent The parent screen to return to
     * @return The configuration screen
     */
    public static Screen createConfigScreen(Screen parent) {
        // Get the config instance first
        StreamerToolsMod mod = StreamerToolsMod.getInstance();
        if (mod == null) {
            return parent;
        }
        
        ModConfig config = mod.getConfig();
        if (config == null) {
            return parent;
        }
        
        TwitchConfig twitchConfig = config.getTwitchConfig();
        if (twitchConfig == null) {
            return parent;
        }
        
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("title.streamertools.config"))
                .setSavingRunnable(() -> {
                    // Save the config when done
                    StreamerToolsMod.getInstance().saveConfig();
                });
        
        // Create config entries
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        
        // Twitch Settings Category
        ConfigCategory twitchCategory = builder.getOrCreateCategory(Component.translatable("category.streamertools.twitch"));
        twitchCategory.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.streamertools.twitch.enabled"), twitchConfig.isEnabled())
                .setDefaultValue(false)
                .setSaveConsumer(twitchConfig::setEnabled)
                .build());
                
        twitchCategory.addEntry(entryBuilder.startStrField(Component.translatable("option.streamertools.twitch.username"), twitchConfig.getUsername())
                .setDefaultValue("")
                .setSaveConsumer(twitchConfig::setUsername)
                .build());
                
        twitchCategory.addEntry(entryBuilder.startStrField(Component.translatable("option.streamertools.twitch.oauth"), twitchConfig.getOauthToken())
                .setDefaultValue("")
                .setSaveConsumer(twitchConfig::setOauthToken)
                .build());
                
        twitchCategory.addEntry(entryBuilder.startStrField(Component.translatable("option.streamertools.twitch.channel"), twitchConfig.getChannel())
                .setDefaultValue("")
                .setSaveConsumer(twitchConfig::setChannel)
                .build());
                
        twitchCategory.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.streamertools.twitch.show_chat"), twitchConfig.isShowChatInGame())
                .setDefaultValue(true)
                .setSaveConsumer(twitchConfig::setShowChatInGame)
                .build());
        
        // Chat Commands Category
        ConfigCategory commandsCategory = builder.getOrCreateCategory(Component.translatable("category.streamertools.commands"));
        commandsCategory.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.streamertools.commands.enabled"), config.isEnableChatCommands())
                .setDefaultValue(true)
                .setSaveConsumer(config::setEnableChatCommands)
                .build());
                
        commandsCategory.addEntry(entryBuilder.startIntSlider(Component.translatable("option.streamertools.commands.cooldown"), config.getDefaultCooldown(), 0, 300)
                .setDefaultValue(30)
                .setSaveConsumer(config::setDefaultCooldown)
                .build());
                
        commandsCategory.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.streamertools.commands.require_follow"), config.isRequireFollow())
                .setDefaultValue(false)
                .setSaveConsumer(config::setRequireFollow)
                .build());
                
        commandsCategory.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.streamertools.commands.require_sub"), config.isRequireSubscription())
                .setDefaultValue(false)
                .setSaveConsumer(config::setRequireSubscription)
                .build());
        
        // Alerts Category
        ConfigCategory alertsCategory = builder.getOrCreateCategory(Component.translatable("category.streamertools.alerts"));
        alertsCategory.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.streamertools.alerts.enabled"), config.isEnableAlerts())
                .setDefaultValue(true)
                .setSaveConsumer(config::setEnableAlerts)
                .build());
                
        alertsCategory.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.streamertools.alerts.sounds"), config.isPlaySounds())
                .setDefaultValue(true)
                .setSaveConsumer(config::setPlaySounds)
                .build());
                
        alertsCategory.addEntry(entryBuilder.startFloatField(Component.translatable("option.streamertools.alerts.volume"), config.getAlertVolume())
                .setDefaultValue(1.0f)
                .setMin(0.0f)
                .setMax(1.0f)
                .setSaveConsumer(config::setAlertVolume)
                .build());
        
        return builder.build();
    }
}
