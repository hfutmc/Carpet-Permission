# Carpet Permissions

Adds fine-grained permission checks to Carpet commands via [fabric-permissions-api](https://github.com/lucko/fabric-permissions-api), enabling LuckPerms-based access control for Carpet mod commands.

## How It Works

This mod intercepts Carpet's `CommandHelper.canUseCommand()` and replaces the default op-level check with a `fabric-permissions-api` permission check for player sources.

- **Player sources**: Permission is checked against the calling command's class name as the permission node (e.g. `carpet.commands.PlayerCommand`)
- **Non-player sources** (console, command blocks): Carpet's original logic applies (no change)
- **Carpet rules with `commandLevel=true/false`**: Respected as-is; permission check only applies to level-based rules (`ops`, `0`-`4`)

### Supported Carpet Extensions

All Carpet add-ons that use `CommandHelper.canUseCommand()` are automatically covered, including:

- Carpet-TIS-Addition
- Carpet-LMS-Addition
- GUGCarpetAddition
- Any other Carpet extension

## Setup

1. Install [Fabric Loader](https://fabricmc.net/use/), [Fabric API](https://modrinth.com/mod/fabric-api), [Carpet](https://modrinth.com/mod/carpet), and [LuckPerms](https://modrinth.com/mod/luckperms)
2. Place this mod in the `mods` folder
3. Grant permissions in LuckPerms:

```
/lp user <player> permission set carpet.commands.PlayerCommand true
/lp group admin permission set carpet.commands.* true
```

### Finding Permission Nodes

When a player is denied a Carpet command, the server log shows:

```
[carpet-permissions] DENIED | PlayerName | node='carpet.commands.PlayerCommand' | Grant: /lp user PlayerName permission set carpet.commands.PlayerCommand true
```

Use the logged node to grant the permission.

## Supported Versions

| Minecraft | Java | Build Variant |
|-----------|------|---------------|
| 1.21.9 – 1.21.11 | 21 | Mojang Official Mappings (`fabric-loom-remap`) |
| 26.1 – 26.2 | 25 | Unobfuscated (`fabric-loom`) |

## Building

```bash
./gradlew buildAll
```

Output jars are in `versions/<version>/build/libs/`.

## Version History

### 1.1.1

- Restructured to multi-version build system (common + version-specific modules)
- Added Minecraft 26.1/26.2 support (Java 25, unobfuscated)
- Switched 1.21.11 to Mojang Official Mappings
- Fixed permission check now respects Carpet `commandLevel=true/false` (only intercepts level-based checks like `ops`, `0`-`4`)
- Fixed non-player sources correctly fall through to Carpet's original logic
- Added robust error handling for `getPlayer()` null, `Permissions.check()` exceptions, and stack trace derivation
- Reduced log noise: ALLOWED results at DEBUG level, only DENIED at INFO
- Compatible with all Carpet add-ons via `CommandHelper.canUseCommand()` interception

### 1.0.0

- Initial release
- Basic `fabric-permissions-api` integration for Carpet commands
- Minecraft 1.21.x support with Yarn mappings
