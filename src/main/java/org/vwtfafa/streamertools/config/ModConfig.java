package org.vwtfafa.streamertools.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "streamer_tools")
public class ModConfig implements ConfigData {
    @ConfigEntry.Category("twitch")
    private TwitchConfig twitchConfig = new TwitchConfig();
    
    @ConfigEntry.Category("chat_commands")
    private boolean enableChatCommands = true;
    
    @ConfigEntry.Category("chat_commands")
    private int defaultCooldown = 30;
    
    @ConfigEntry.Category("chat_commands")
    private boolean requireFollow = false;
    
    @ConfigEntry.Category("chat_commands")
    private boolean requireSubscription = false;
    
    @ConfigEntry.Category("alerts")
    private boolean enableAlerts = true;
    
    @ConfigEntry.Category("alerts")
    private boolean playSounds = true;
    
    @ConfigEntry.Category("alerts")
    private float alertVolume = 1.0f;

    public TwitchConfig getTwitchConfig() {
        return twitchConfig;
    }

    public boolean isEnableChatCommands() {
        return enableChatCommands;
    }

    public void setEnableChatCommands(boolean enableChatCommands) {
        this.enableChatCommands = enableChatCommands;
    }

    public int getDefaultCooldown() {
        return defaultCooldown;
    }

    public void setDefaultCooldown(int defaultCooldown) {
        this.defaultCooldown = defaultCooldown;
    }

    public boolean isRequireFollow() {
        return requireFollow;
    }

    public void setRequireFollow(boolean requireFollow) {
        this.requireFollow = requireFollow;
    }

    public boolean isRequireSubscription() {
        return requireSubscription;
    }

    public void setRequireSubscription(boolean requireSubscription) {
        this.requireSubscription = requireSubscription;
    }

    public boolean isEnableAlerts() {
        return enableAlerts;
    }

    public void setEnableAlerts(boolean enableAlerts) {
        this.enableAlerts = enableAlerts;
    }

    public boolean isPlaySounds() {
        return playSounds;
    }

    public void setPlaySounds(boolean playSounds) {
        this.playSounds = playSounds;
    }

    public float getAlertVolume() {
        return alertVolume;
    }

    public void setAlertVolume(float alertVolume) {
        this.alertVolume = Math.max(0.0f, Math.min(1.0f, alertVolume));
    }
}
