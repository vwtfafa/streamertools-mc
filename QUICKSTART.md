# 🚀 Streamer Tools - Schnellstart Anleitung

## ⚡ 5-Minuten Setup

### Schritt 1: Projekt Bauen
```powershell
cd C:\Users\Fabrice\IdeaProjects\streamertools
gradle clean build
```

### Schritt 2: OAuth Token Besorgen
1. Besuche: https://twitchtokengenerator.com/
2. Melde dich an
3. Kopiere den Token (sieht aus wie: `oauth:xxxxxxxxxxxxx`)

### Schritt 3: Mod Installieren
1. Baue das Projekt: `gradle build`
2. Die JAR-Datei findest du unter: `build/libs/streamertools-1.0.0.jar`
3. Kopiere sie in dein Minecraft `mods` Verzeichnis

### Schritt 4: Konfigurieren
1. Starte Minecraft
2. Drücke **P** im Spiel
3. Gib ein:
   - Twitch Username
   - OAuth Token
   - Kanal-Name
4. Speichern

### Schritt 5: Fertig! 🎉
- Starte einen Server
- Der Chat sollte oben rechts erscheinen

---

## 📋 Alle Gradle-Befehle

| Befehl | Beschreibung |
|--------|-------------|
| `gradle clean` | Löscht alte Build-Dateien |
| `gradle build` | Baut das Projekt |
| `gradle clean build` | Sauberer Build |
| `gradle runClient` | Testet im Client |
| `gradle runServer` | Testet auf Server |
| `gradle --refresh-dependencies` | Aktualisiert Dependencies |
| `gradle dependencies` | Zeigt alle Dependencies |
| `gradle --version` | Zeigt Gradle-Version |

---

## 🎮 Im-Spiel Befehle

### Keybindings
- **P** = Konfiguration öffnen
- **Unbekannt** = Chat-Overlay umschalten

### Chat-Befehle (von Twitch)
- `!heal` = Heal-Befehl (Beispiel)

---

## 🔧 Troubleshooting

### Problem: "Could not resolve dependencies"
**Lösung:**
```powershell
gradle clean --refresh-dependencies
gradle build
```

### Problem: "JAVA_HOME not set"
**Lösung:**
1. Installiere Java 21
2. Setze JAVA_HOME Umgebungsvariable
3. Starte PowerShell neu

### Problem: "OAuth Token ungültig"
**Lösung:**
1. Generiere neuen Token: https://twitchtokengenerator.com/
2. Kopiere den kompletten Token (mit `oauth:` Präfix)
3. Speichere in der Konfiguration

### Problem: "Chat erscheint nicht im Spiel"
**Lösung:**
1. Überprüfe: Ist der Mod aktiviert?
2. Überprüfe: Ist Twitch verbunden?
3. Überprüfe: Ist "showChatInGame" aktiviert?
4. Schreibe eine Nachricht im Twitch-Chat

---

## 📊 Features Übersicht

### ✅ Implementiert
- Twitch Chat Integration
- In-Game Chat Overlay
- Konfiguration (Cloth Config)
- ModMenu Integration
- OAuth2 Authentication
- Thread-sichere Verarbeitung
- Automatisches Fade-Out
- Befehl-Verarbeitung

### 🔄 Geplant
- Emote-Rendering
- Alerts (Follower, Subscriber)
- Custom Commands
- Sound Effects
- Moderator Commands

---

## 📁 Wichtige Dateien

| Datei | Zweck |
|-------|-------|
| `build.gradle` | Gradle-Konfiguration |
| `gradle.properties` | Versions-Verwaltung |
| `StreamerToolsMod.java` | Hauptklasse |
| `TwitchChatManager.java` | Twitch-Integration |
| `ChatOverlay.java` | Chat-Anzeige |
| `ModConfig.java` | Konfiguration |

---

## 💾 Konfigurationsdatei

Speicherort:
```
%APPDATA%\.minecraft\config\streamer_tools.json
```

Wichtigste Einstellungen:
```json
{
  "twitchConfig": {
    "enabled": true,
    "username": "dein_username",
    "oauthToken": "oauth:xxxxx",
    "channel": "dein_kanal",
    "showChatInGame": true
  }
}
```

---

## 🎯 Nächste Schritte

1. ✅ Projekt bauen: `gradle clean build`
2. ✅ JAR installieren
3. ✅ Minecraft starten
4. ✅ Konfigurieren (P-Taste)
5. ✅ Server starten
6. ✅ Chat im Spiel sehen!

---

## 📞 Häufig Gestellte Fragen

**F: Funktioniert das auch auf Multiplayer-Servern?**
A: Ja, aber nur der Server-Owner sieht den Chat (aktuell).

**F: Kann ich die Chat-Farbe ändern?**
A: Ja, in der Konfiguration unter "Chat Overlay".

**F: Funktioniert es mit anderen Mods?**
A: Ja, es ist kompatibel mit den meisten Fabric-Mods.

**F: Wie oft wird der Chat aktualisiert?**
A: In Echtzeit, sobald eine Nachricht im Twitch-Chat gesendet wird.

---

## ✨ Viel Spaß mit Streamer Tools! 🎉
