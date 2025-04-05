# 📦 ChestPlus - Advanced Personal Chest System

> **Built for Minecraft 1.20.1 / Paper**  
> A flexible, async-safe, and user-friendly storage plugin.

---

## 🧩 Features

- ✅ **Expandable Personal Storage** — Up to 5 individual chest slots per player
- 🔐 **Per-Player Locking System** — Prevents concurrent access with automatic unlock timeout
- 💾 **Async Saving with Cache** — Lag-free, reliable inventory saving
- 🔒 **Permission-Based Chest Access** — Limit access with `chestplus.chest.1` through `5`
- 🧰 **Auto Save & Auto Delete** — Periodically backs up and removes expired files
- 👥 **View Other Players' Chests** — Admins can inspect others' inventories
- 🧪 **GUI-Driven Interface** — Access via the `/chest` command

---

## 📸 Previews

| Main GUI | View Other Player | Chest Locked |
|--|--|-------------|
| Not prepared | Not prepared | Not prepared            |

---

## 🔧 Commands

| Command | Description |
|--------|-------------|
| `/chest` | Open your personal chest GUI |
| `/chest open <player> <id>` | View another player's chest |
| `/chest reload` | Reload config, GUI, and message files |

---

## ⚙️ Permissions

| Node | Description |
|------|-------------|
| `chestplus.open` | Open your own chests |
| `chestplus.open.other` | View others' chests |
| `chestplus.chest.1` to `5` | Permission per chest ID |

---

## 🛠 Installation

1. Download the latest `ChestPlus-x.x.x.jar`
2. Drop it into your server's `/plugins` folder
3. Restart or reload the server
4. Use `/chest` to get started

---

## 📁 Plugin File Structure

```plaintext
ChestPlus/
├── config.yml               # Plugin configuration
├── gui.yml                  # GUI item layout and visuals
├── messages.yml             # All messages (fully customizable)
├── data/
│   └── <UUID>_1.yml         # Chest contents (async saved)
└── autosave/
    └── backup_TIMESTAMP.yml # Auto-saved backups
