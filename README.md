# 🎮 Streamer Tools - Minecraft Fabric Mod

![Status](https://img.shields.io/badge/Status-Production%20Ready-brightgreen)
![Version](https://img.shields.io/badge/Version-1.0.0-blue)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.8-green)
![Fabric](https://img.shields.io/badge/Fabric-0.136.0-orange)
![Java](https://img.shields.io/badge/Java-21-blueviolet)

Ein leistungsstarker Minecraft Fabric Mod, der Twitch-Chat direkt in dein Spiel integriert – sowohl im Einzelspieler als auch auf Servern!

## 🌟 Features

### ✨ Hauptfunktionen
- 🎮 **Live Twitch Chat im Spiel** - Sehe Chat-Nachrichten in Echtzeit
- 🔐 **OAuth2 Authentifizierung** - Sichere Verbindung zu Twitch
- ⚙️ **Vollständige Konfiguration** - Cloth Config Integration
- 🎨 **Schönes Chat-Overlay** - Automatisches Fade-Out
- 💬 **Chat-Befehle** - Verarbeite Befehle von Zuschauern
- 🌐 **Multiplayer** - 
- 🚀 **Leistungsoptimiert** - Minimaler Einfluss auf die Spielleistung

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
- Java 21 oder höher (empfohlen: OpenJDK 21)
- Minecraft 1.21.8
- Fabric Loader 0.17.3 oder höher
- Fabric API 0.136.0 oder höher

### Installation

## 🚀 Installation

### 1. Mod herunterladen
- Lade die neueste Version von [Releases](https://github.com/vwtfafa/streamertools-mc/releases)
- Oder baue selbst:
  ```bash
  git clone https://github.com/vwtfafa/streamertools-mc.git
  cd streamertools-mc
  gradle clean build
  ```

### 2. Installation
- Kopiere `build/libs/streamertools-1.0.0.jar` in deinen `.minecraft/mods/` Ordner
- Stelle sicher, dass Fabric Loader installiert ist

### 3. Twitch Token einrichten
1. Besuche [Twitch Token Generator](https://twitchtokengenerator.com/)
2. Wähle "Custom Scope"
3. Füge folgende Berechtigungen hinzu:
   - `chat:read`
   - `chat:edit`
   - `channel:moderate`
4. Kopiere den generierten OAuth Token

### 4. Konfiguration
1. Starte Minecraft mit dem Fabric Loader
2. Drücke **P** um das Konfigurationsmenü zu öffnen
3. Gehe zum Tab "Twitch Einstellungen"
4. Aktiviere die Twitch-Integration
5. Füge deinen Twitch-Benutzernamen und den OAuth Token ein
6. Klicke auf "Speichern"

### 5. Fertig! 🎉
- Starte das Spiel neu
- Der Twitch-Chat sollte nun oben rechts erscheinen
- Im Einzelspieler und auf Servern verfügbar

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

## 🎮 Steuerung

### Tastaturbefehle
| Taste | Funktion |
|-------|----------|
| **P** | Konfigurationsmenü öffnen |
| **Umschalt + C** | Chat-Overlay ein-/ausblenden |
| **R** | Chat-Overlay zurücksetzen |

### Chat-Befehle
| Befehl | Beschreibung |
|--------|--------------|
| `!hello` | Testbefehl |
| `!follow` | Zeigt Follower-Informationen |
| `!uptime` | Zeigt die aktuelle Streamzeit |

## ⚙️ Konfiguration

Die Mod verwendet Cloth Config für eine benutzerfreundliche Konfiguration:

### Allgemein
- **Chat-Position**: Anpassung der Position des Chat-Overlays
- **Textgröße**: Größe des Chat-Textes
- **Hintergrund-Transparenz**: Anpassung der Deckkraft des Hintergrunds

### Twitch Einstellungen
- **Twitch Benutzername**: Dein Twitch-Benutzername
- **OAuth Token**: Dein persönlicher OAuth-Token
- **Automatische Verbindung**: Automatische Verbindung beim Start
- **Chat-Nachrichten anzeigen**: Zeigt Chat-Nachrichten an
- **Befehle aktivieren**: Aktiviert die Verarbeitung von Chat-Befehlen

## 🏗️ Projektstruktur

```
streamertools/
├── src/
│   ├── main/
│   │   ├── java/org/vwtfafa/streamertools/
│   │   │   ├── StreamerToolsMod.java          # Hauptmod-Klasse
│   │   │   ├── client/
│   │   │   │   ├── StreamerToolsModClient.java # Client-seitige Initialisierung
│   │   │   │   └── gui/
│   │   │   │       ├── ChatOverlay.java       # Chat-Overlay Rendering
│   │   │   │       └── ConfigScreenFactory.java # Konfigurationsbildschirm
│   │   │   ├── twitch/
│   │   │   │   └── TwitchChatManager.java     # Twitch-Integration
│   │   │   └── config/
│   │   │       ├── ModConfig.java             # Hauptkonfiguration
│   │   │       └── TwitchConfig.java          # Twitch-spezifische Einstellungen
│   │   └── resources/
│   │       ├── fabric.mod.json                # Mod-Metadaten
│   │       └── assets/streamertools/           # Assets (Texturen, Übersetzungen)
│   └── client/                                # Client-spezifischer Code
│       └── java/org/vwtfafa/streamertools/client/
│           └── ...
├── build.gradle                              # Build-Konfiguration
└── README.md                                # Diese Datei
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
## ✅ Features

### 🔧 Kernfunktionen
- [x] Live Twitch Chat im Spiel
- [x] OAuth2 Authentifizierung
- [x] Konfigurierbare Einstellungen
- [x] Chat-Befehle verarbeiten
- [x] Automatisches Fade-Out
- [x] Einzelspieler-Unterstützung
- [x] Multiplayer-Kompatibilität

### 🎨 Benutzeroberfläche
- [x] Anpassbares Chat-Overlay
- [x] Intuitive Konfiguration
- [x] Tastaturbefehle
- [x] Responsive Design

### ⚡ Leistung
- [x] Effiziente Ressourcennutzung
- [x] Thread-sichere Implementierung
- [x] Automatische Wiederverbindung
- [x] Fehlerbehandlung und Logging

## 📝 Lizenz

Dieses Projekt steht unter der MIT-Lizenz. Weitere Informationen finden Sie in der [LICENSE](LICENSE)-Datei.

## 🤝 Mitwirken

Beiträge sind willkommen! Bitte lesen Sie unsere [Beitragsrichtlinien](CONTRIBUTING.md) für weitere Informationen.

## 📞 Unterstützung

Fragen oder Probleme? Eröffnen Sie ein [Issue](https://github.com/vwtfafa/streamertools-mc/issues) oder besuchen Sie unseren [Discord-Server](https://discord.gg/h4uZv4Y3jd).

## 📚 Weitere Ressourcen

- [Twitch Developer Portal](https://dev.twitch.tv/)
- [Fabric Wiki](https://fabricmc.net/wiki/start)
- [Cloth Config Dokumentation](https://shedaniel.gitbook.io/cloth-config/)

---

<p align="center">
  <a href="https://github.com/vwtfafa/streamertools-mc">
    <img src="https://img.shields.io/github/stars/vwtfafa/streamertools-mc?style=social" alt="GitHub Stars">
  </a>
  <a href="https://discord.gg/h4uZv4Y3jd">
    <img src="https://img.shields.io/discord/your-discord-server-id?label=Discord&logo=discord" alt="Discord">
  </a>
</p>
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
