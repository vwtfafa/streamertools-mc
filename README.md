# 🎮 Streamer Tools - Minecraft Fabric Mod

![Status](https://img.shields.io/badge/Status-Production%20Ready-brightgreen)
![Version](https://img.shields.io/badge/Version-1.0.0-blue)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.8-green)
![Fabric](https://img.shields.io/badge/Fabric-0.136.0-orange)
![Java](https://img.shields.io/badge/Java-21-blueviolet)

A powerful Minecraft Fabric Mod that integrates Twitch Chat directly into your game – both in singleplayer and on servers!

## 🌟 Features

### ✨ Main Features
- 🎮 **Live Twitch Chat in-game** - See chat messages in real-time
- 🔐 **OAuth2 Authentication** - Secure connection to Twitch
- ⚙️ **Complete Configuration** - Cloth Config Integration
- 🎨 **Beautiful Chat Overlay** - Automatic Fade-Out
- 💬 **Chat Commands** - Process commands from viewers
- 🌐 **Multiplayer** - 
- 🚀 **Performance Optimized** - Minimal impact on game performance

### 🎯 Implemented Features
- ✅ Twitch Chat Integration (Twitch4J 1.19.0)
- ✅ In-Game Chat Overlay with Fade-Out
- ✅ Cloth Config & ModMenu Integration
- ✅ OAuth2 Token Management
- ✅ Thread-safe Processing
- ✅ Automatic Resource Cleanup
- ✅ Command Processing
- ✅ Robust Error Handling

## 🚀 Quick Start

### Requirements
- Java 21 or higher (recommended: OpenJDK 21)
- Minecraft 1.21.8
- Fabric Loader 0.17.3 or higher
- Fabric API 0.136.0 or higher

### Installation

## 🚀 Installation

### 1. Download the Mod
- Download the latest version from [Releases](https://github.com/vwtfafa/streamertools-mc/releases)
- Or build it yourself:
  ```bash
  git clone https://github.com/vwtfafa/streamertools-mc.git
  cd streamertools-mc
  gradle clean build
  ```

### 2. Installation
https://modrinth.com/project/streamertools 

### 3. Set Up Twitch Token
1. Visit [Twitch Token Generator](https://twitchtokengenerator.com/)
2. Select "Custom Scope"
3. Add the following permissions:
   - `chat:read`
   - `chat:edit`
   - `channel:moderate`
4. Copy the generated OAuth Token

### 4. Configuration
1. Start Minecraft with the Fabric Loader
2. Press **P** to open the configuration menu
3. Go to the "Twitch Settings" tab
4. Enable Twitch Integration
5. Enter your Twitch username and OAuth Token
6. Click "Save"

### 5. Done! 🎉
- Restart the game
- The Twitch Chat should now appear in the top right corner
- Available in both singleplayer and on servers

## 📋 Commands

### Gradle Commands
```bash
# Build the project
gradle clean build

# Test in client
gradle runClient

# Test on server
gradle runServer

# Update dependencies
gradle --refresh-dependencies

# Show dependencies
gradle dependencies
```

## 🎮 Controls

### Keyboard Shortcuts
| Key | Function |
|-------|----------|
| **P** | Open configuration menu |
| **Shift + C** | Toggle chat overlay |
| **R** | Reset chat overlay |

### Chat Commands
| Command | Description |
|--------|--------------|
| `!hello` | Test command |
| `!follow` | Shows follower information |
| `!uptime` | Shows current stream time |

## ⚙️ Configuration

The mod uses Cloth Config for user-friendly configuration:

### General
- **Chat Position**: Adjust the position of the chat overlay
- **Text Size**: Size of the chat text
- **Background Transparency**: Adjust the opacity of the background

### Twitch Settings
- **Twitch Username**: Your Twitch username
- **OAuth Token**: Your personal OAuth token
- **Auto Connect**: Automatically connect on startup
- **Show Chat Messages**: Display chat messages
- **Enable Commands**: Enable processing of chat commands

## 🏗️ Project Structure

```
streamertools/
├── src/
│   ├── main/
│   │   ├── java/org/vwtfafa/streamertools/
│   │   │   ├── StreamerToolsMod.java          # Main mod class
│   │   │   ├── client/
│   │   │   │   ├── StreamerToolsModClient.java # Client-side initialization
│   │   │   │   └── gui/
│   │   │   │       ├── ChatOverlay.java       # Chat overlay rendering
│   │   │   │       └── ConfigScreenFactory.java # Configuration screen
│   │   │   ├── twitch/
│   │   │   │   └── TwitchChatManager.java     # Twitch integration
│   │   │   └── config/
│   │   │       ├── ModConfig.java             # Main configuration
│   │   │       └── TwitchConfig.java          # Twitch-specific settings
│   │   └── resources/
│   │       ├── fabric.mod.json                # Mod metadata
│   │       └── assets/streamertools/           # Assets (textures, translations)
│   └── client/                                # Client-specific code
│       └── java/org/vwtfafa/streamertools/client/
│           └── ...
├── build.gradle                              # Build configuration
└── README.md                                # This file
│   │       └── fabric.mod.json
│   └── client/
│       └── java/org/vwtfafa/streamertools/client/
│           ├── StreamerToolsModClient.java
│           └── gui/
│               ├── ChatOverlay.java
│               └── ConfigScreenFactory.java
├── build.gradle
├── gradle.properties
├── ANALYSIS.md (Detailed analysis)
├── BUG_FIXES.md (All fixed bugs)
├── QUICKSTART.md (Quick start)
└── README.md (This file)
```

## 🔧 Configuration

The configuration is automatically created at:
```
%APPDATA%\.minecraft\config\streamer_tools.json
```

### Example Configuration
```json
{
  "twitchConfig": {
    "enabled": true,
    "username": "your_twitch_username",
    "oauthToken": "oauth:xxxxxxxxxxxxxxxxxxxxx",
    "channel": "your_channel_name",
    "autoConnect": true,
    "showChatInGame": true,
    "chatMaxMessages": 20,
    "allowChatCommands": true,
    "commandCooldown": 5
  },
  "enableChatCommands": true,
  "defaultCooldown": 30,
  "requireFollow": false,
  "requireSubscription": false,
  "enableAlerts": true,
  "playSounds": true,
  "alertVolume": 1.0
}
```


### Repositories
- Maven Central
- Fabric MC (`https://maven.fabricmc.net/`)
- Shedaniel (`https://maven.shedaniel.me/`)
- TerraformersMC (`https://maven.terraformersmc.com/releases`)
- JitPack (`https://jitpack.io`)




## 🎯 What can the mod do?

### ✅ Implemented
## ✅ Features

### 🔧 Core Features
- [x] Live Twitch Chat in-game
- [x] OAuth2 Authentication
- [x] Configurable settings
- [x] Process chat commands
- [x] Automatic fade-out
- [x] Singleplayer support
- [x] Multiplayer compatibility

### 🎨 User Interface
- [x] Customizable chat overlay
- [x] Intuitive configuration
- [x] Keyboard shortcuts
- [x] Responsive design

### ⚡ Performance
- [x] Efficient resource usage
- [x] Thread-safe implementation
- [x] Automatic reconnection
- [x] Error handling and logging

## 📝 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more information.

## 🤝 Contributing

Contributions are welcome! Please read our [contribution guidelines](CONTRIBUTING.md) for more information.

## 📞 Support

Questions or issues? Open an [Issue](https://github.com/vwtfafa/streamertools-mc/issues) or visit our [Discord Server](https://discord.gg/h4uZv4Y3jd).

## 📚 Additional Resources

- [Twitch Developer Portal](https://dev.twitch.tv/)
- [Fabric Wiki](https://fabricmc.net/wiki/start)
- [Cloth Config Documentation](https://shedaniel.gitbook.io/cloth-config/)

---

<p align="center">
  <a href="https://github.com/vwtfafa/streamertools-mc">
    <img src="https://img.shields.io/github/stars/vwtfafa/streamertools-mc?style=social" alt="GitHub Stars">
  </a>
  <a href="https://discord.gg/h4uZv4Y3jd">
    <img src="https://img.shields.io/discord/your-discord-server-id?label=Discord&logo=discord" alt="Discord">
  </a>
</p>

- [x] Resource Management

### Planned
- [ ] Emote Rendering
- [ ] Follower Alerts
- [ ] Subscriber Alerts
- [ ] Raid Alerts
- [ ] Custom Commands
- [ ] Chat Filter
- [ ] Sound Alerts
- [ ] Custom Colors

## Troubleshooting

### Issue: Build fails
```bash
{{ ... }}
gradle build
```

### Issue: JAVA_HOME not set
1. Install Java 21
2. Set JAVA_HOME environment variable
3. Restart PowerShell

### Issue: Chat won't connect
1. Check OAuth Token
2. Verify channel name
3. Check logs: `gradle runClient --info`

### Issue: Chat doesn't appear in-game
1. Is the mod enabled?
2. Is "showChatInGame" enabled in the config?
3. Try sending a message in the Twitch chat

## 📞 Support

**Project**: Streamer Tools Minecraft Mod
**Version**: 1.0.0
**Minecraft**: 1.21.8
**Status**: 🟢 Production Ready

## 📄 License

MIT License - See LICENSE.txt

## 🙏 Credits

- **Twitch4J** - Twitch API Java Library
- **Fabric** - Minecraft Modding Framework
- **Cloth Config** - Configuration UI
- **ModMenu** - Mod Menu Integration

## 🎉 Have Fun!

Enjoy the integration of Twitch Chat in Minecraft!

---

**Last Updated**: 2025-10-27
**Status**: ✅ All Bugs Fixed - Production Ready
