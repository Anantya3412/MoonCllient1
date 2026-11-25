# Moon Client

A Feather-style, client-side Fabric utility mod for **Minecraft 1.21.11** and **Java 21**. It is not a cheat client and does not add combat automation.

## Included in this source

- Dark charcoal / subtle purple Moon Client menu, opened with **Right Shift**.
- Home, Mods, HUD, Cosmetics and Settings pages with clickable navigation.
- Persistent settings in `config/moonclient.properties`.
- Live HUD elements: FPS, left/right CPS, server ping, player coordinates, active potion effects and armor icons.
- Keystrokes display for WASD and mouse buttons.
- Optional purple crosshair overlay.
- Toggle Sprint keybind (assign it in Options → Controls → Key Binds; it starts unbound).
- Compact HUD and minimal-menu preferences.
- GitHub Actions workflow that builds the mod and uploads the JAR artifact.

## Honest feature notes

- The Cosmetics page explains that the mod uses the player's normal Minecraft account skin. It does not implement a custom skin-file uploader, custom cape service, or skin hosting.
- The Reduced Particles switch currently stores a preference only; it does not override Minecraft's particle setting.
- The HUD is a simple fixed-position overlay. A drag-and-drop HUD editor, minimap, waypoints, zoom, freelook, resource-pack browser and integrated mod downloader are not part of this version.
- FPS gains are not guaranteed. This mod focuses on utility and does not bundle third-party optimization mods.

## Build with GitHub Actions (no local Maven/Gradle installation needed)

1. Create or open your GitHub repository.
2. Upload the **contents** of this folder to the repository root (so `build.gradle` and `settings.gradle` are at the root).
3. Open **Actions → Build Moon Client → Run workflow**.
4. If the build succeeds, open the workflow run and download the `Moon-Client-1.21.11` artifact.
5. Put the built JAR in `.minecraft/mods` in a Fabric 1.21.11 installation. Fabric API 0.141.1+1.21.11 or newer for 1.21.11 is required.

## Controls

- **Right Shift**: open/close Moon Client.
- **Toggle Sprint**: assign a key under Options → Controls → Key Binds → Moon Client.

## Build details

- Minecraft: 1.21.11
- Java: 21
- Fabric Loader: 0.18.1
- Fabric API: 0.141.1+1.21.11
- Fabric Loom: 1.14-SNAPSHOT
- Yarn mappings: 1.21.11+build.4

The source has not been compiled in this environment. Use the included GitHub Actions build as the compile check; if it reports an error, share the complete log so the exact dependency/API issue can be corrected.
