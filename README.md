# 🎮 Streamer Tools - Minecraft Fabric Mod

![Status](https://img.shields.io/badge/Status-Production%20Ready-brightgreen)
![Version](https://img.shields.io/badge/Version-1.0.0-blue)
![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-green)
![Fabric](https://img.shields.io/badge/Fabric-Yes-orange)

Ein leistungsstarker Minecraft Fabric Mod, der Twitch-Chat direkt in dein Spiel integriert!

## 🌟 Features

### ✨ Hauptfunktionen
- 🎮 **Live Twitch Chat im Spiel** - Sehe Chat-Nachrichten in Echtzeit
- 🔐 **OAuth2 Authentifizierung** - Sichere Verbindung zu Twitch
- ⚙️ **Vollständige Konfiguration** - Cloth Config Integration
- 🎨 **Schönes Chat-Overlay** - Automatisches Fade-Out nach 10 Sekunden
- 💬 **Chat-Befehle** - Verarbeite Befehle von Zuschauern
- 🔔 **Server-Integration** - Funktioniert auf Multiplayer-Servern

### 🎯 Implementierte Features
- ✅ Twitch Chat Integration (Twitch4J 1.19.0)
- ✅ In-Game Chat Overlay mit Fade-Out
- ✅ Cloth Config & ModMenu Integration
- ✅ OAuth2 Token Management
- ✅ Thread-sichere Verarbeitung
- ✅ Automatische Ressourcen-Freigabe
- ✅ Befehl-Verarbeitung
- ✅ Robuste Fehlerbehandlung

## 🚀 Schnellstart

### Voraussetzungen
- Java 21 oder höher
- Minecraft 1.20.1
- Fabric Loader

### Installation

1. **Projekt bauen**
```bash
gradle clean build
```

2. **JAR installieren**
   - Kopiere `build/libs/streamertools-1.0.0.jar` in dein `mods` Verzeichnis

3. **Twitch Token besorgen**
   - Besuche: https://twitchtokengenerator.com/
   - Kopiere deinen OAuth Token

4. **Mod konfigurieren**
   - Starte Minecraft
   - Drücke **P** um die Konfiguration zu öffnen
   - Gib dein Twitch-Benutzername und Token ein
   - Speichern

5. **Fertig!** 🎉
   - Starte einen Server
   - Dein Twitch-Chat erscheint oben rechts

## 📋 Befehle

### Gradle-Befehle
```bash
# Projekt bauen
gradle clean build

# Im Client testen
gradle runClient

# Auf Server testen
gradle runServer

# Dependencies aktualisieren
gradle --refresh-dependencies

# Abhängigkeiten anzeigen
gradle dependencies
```

### Im-Spiel Keybindings
| Taste | Funktion |
|-------|----------|
| **P** | Konfiguration öffnen |
| **Unbekannt** | Chat-Overlay umschalten |

## 📁 Projektstruktur

```
streamertools/
├── src/
│   ├── main/
│   │   ├── java/org/vwtfafa/streamertools/
│   │   │   ├── StreamerToolsMod.java
│   │   │   ├── twitch/
│   │   │   │   └── TwitchChatManager.java
│   │   │   └── config/
│   │   │       ├── ModConfig.java
│   │   │       └── TwitchConfig.java
│   │   └── resources/
│   │       └── fabric.mod.json
│   └── client/
│       └── java/org/vwtfafa/streamertools/client/
│           ├── StreamerToolsModClient.java
│           └── gui/
│               ├── ChatOverlay.java
│               └── ConfigScreenFactory.java
├── build.gradle
├── gradle.properties
├── ANALYSIS.md (Detaillierte Analyse)
├── BUG_FIXES.md (Alle behobenen Bugs)
├── QUICKSTART.md (Schnellstart)
└── README.md (Diese Datei)
```

## 🔧 Konfiguration

Die Konfiguration wird automatisch erstellt unter:
```
%APPDATA%\.minecraft\config\streamer_tools.json
```

### Beispiel-Konfiguration
```json
{
  "twitchConfig": {
    "enabled": true,
    "username": "dein_twitch_username",
    "oauthToken": "oauth:xxxxxxxxxxxxxxxxxxxxx",
    "channel": "dein_kanal_name",
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

## 📊 Technische Details

### Abhängigkeiten
| Abhängigkeit | Version | Zweck |
|-------------|---------|-------|
| Twitch4J | 1.19.0 | Twitch API Integration |
| Credential Manager | 0.3.1 | OAuth2 Token-Verwaltung |
| Fabric API | 0.92.2 | Minecraft Mod-Framework |
| Cloth Config | 11.1.118 | Konfiguration UI |
| ModMenu | 7.2.2 | Mod-Menü Integration |

### Repositories
- Maven Central
- Fabric MC (`https://maven.fabricmc.net/`)
- Shedaniel (`https://maven.shedaniel.me/`)
- TerraformersMC (`https://maven.terraformersmc.com/releases`)
- JitPack (`https://jitpack.io`)

## 🐛 Bug-Fixes

Alle **22 Bugs** wurden behoben! Siehe `BUG_FIXES.md` für Details:
- ✅ Syntax-Fehler
- ✅ Logik-Fehler
- ✅ Speicherlecks
- ✅ Null-Pointer-Exceptions
- ✅ Performance-Probleme
- ✅ Thread-Sicherheit

## 📚 Dokumentation

- **ANALYSIS.md** - Vollständige technische Analyse
- **BUG_FIXES.md** - Detaillierte Bug-Dokumentation
- **QUICKSTART.md** - 5-Minuten Setup-Anleitung
- **README.md** - Diese Datei

## 🎯 Was kann der Mod?

### ✅ Implementiert
- [x] Live Twitch Chat anzeigen
- [x] OAuth2 Authentifizierung
- [x] Konfigurierbare Einstellungen
- [x] Chat-Befehle verarbeiten
- [x] Automatisches Fade-Out
- [x] Multiplayer-Support
- [x] Fehlerbehandlung
- [x] Ressourcen-Management

### 🔄 Geplant
- [ ] Emote-Rendering
- [ ] Follower-Alerts
- [ ] Subscriber-Alerts
- [ ] Raid-Alerts
- [ ] Custom Commands
- [ ] Chat-Filter
- [ ] Sound-Alerts
- [ ] Benutzerdefinierte Farben

## 🔍 Troubleshooting

### Problem: Build schlägt fehl
```bash
gradle clean --refresh-dependencies
gradle build
```

### Problem: JAVA_HOME nicht gesetzt
1. Installiere Java 21
2. Setze JAVA_HOME Umgebungsvariable
3. Starte PowerShell neu

### Problem: Chat verbindet sich nicht
1. Überprüfe OAuth Token
2. Überprüfe Kanal-Namen
3. Überprüfe Logs: `gradle runClient --info`

### Problem: Chat erscheint nicht im Spiel
1. Ist der Mod aktiviert?
2. Ist "showChatInGame" in der Konfiguration aktiviert?
3. Schreibe eine Nachricht im Twitch-Chat

## 📞 Support

**Projekt**: Streamer Tools Minecraft Mod
**Version**: 1.0.0
**Minecraft**: 1.20.1
**Status**: 🟢 Produktionsreif

## 📄 Lizenz

MIT License - Siehe LICENSE.txt

## 🙏 Credits

- **Twitch4J** - Twitch API Java Library
- **Fabric** - Minecraft Modding Framework
- **Cloth Config** - Configuration UI
- **ModMenu** - Mod Menu Integration

## 🎉 Viel Spaß!

Genießen Sie die Integration von Twitch Chat in Minecraft!

---

**Letzte Aktualisierung**: 2025-10-24
**Status**: ✅ Alle Bugs behoben - Produktionsreif
