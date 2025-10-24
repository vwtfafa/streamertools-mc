package org.vwtfafa.streamertools.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "twitch")
public class TwitchConfig implements ConfigData {
    private boolean enabled = false;
    private String username = "";
    private String oauthToken = "";
    private String channel = "";
    private boolean autoConnect = true;
    private boolean showChatInGame = true;
    private int chatMaxMessages = 20;
    private boolean allowChatCommands = true;
    private int commandCooldown = 5;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOauthToken() {
        return oauthToken;
    }

    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public boolean isAutoConnect() {
        return autoConnect;
    }

    public void setAutoConnect(boolean autoConnect) {
        this.autoConnect = autoConnect;
    }

    public boolean isShowChatInGame() {
        return showChatInGame;
    }

    public void setShowChatInGame(boolean showChatInGame) {
        this.showChatInGame = showChatInGame;
    }

    public int getChatMaxMessages() {
        return chatMaxMessages;
    }

    public void setChatMaxMessages(int chatMaxMessages) {
        this.chatMaxMessages = chatMaxMessages;
    }

    public boolean isAllowChatCommands() {
        return allowChatCommands;
    }

    public void setAllowChatCommands(boolean allowChatCommands) {
        this.allowChatCommands = allowChatCommands;
    }

    public int getCommandCooldown() {
        return commandCooldown;
    }

    public void setCommandCooldown(int commandCooldown) {
        this.commandCooldown = commandCooldown;
    }
}
