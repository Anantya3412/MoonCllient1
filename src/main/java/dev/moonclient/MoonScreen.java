package dev.moonclient;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import java.util.ArrayList;
import java.util.List;

public final class MoonScreen extends Screen {
    private enum Page { HOME, MODS, HUD, COSMETICS, SETTINGS }
    private record Click(int x, int y, int w, int h, Runnable action) {
        boolean inside(double mx, double my) { return mx >= x && mx < x + w && my >= y && my < y + h; }
    }
    private final List<Click> clicks = new ArrayList<>();
    private Page page = Page.HOME;
    private int left, top, panelW, panelH, sideW, cx, cw;

    public MoonScreen() { super(Text.literal("Moon Client")); }
    private void rect(DrawContext c, int x, int y, int w, int h, int color) { c.fill(x, y, x + w, y + h, color); }
    private void text(DrawContext c, String s, int x, int y, int color) { c.drawText(textRenderer, Text.literal(s), x, y, color, false); }
    private void click(int x, int y, int w, int h, Runnable action) { clicks.add(new Click(x, y, w, h, action)); }
    private void card(DrawContext c, String title, String detail, int x, int y, int w, int h) {
        rect(c, x, y, w, h, 0xFF20212A); rect(c, x, y, w, 1, 0xFF393746);
        text(c, title, x + 12, y + 10, 0xFFF1EDF9); text(c, detail, x + 12, y + 28, 0xFF9D9AA9);
    }
    private void toggle(DrawContext c, String title, String detail, boolean value, int y, Runnable action) {
        rect(c, cx, y, cw, 39, 0xFF20212A); text(c, title, cx + 11, y + 7, 0xFFECE8F4);
        text(c, detail, cx + 11, y + 22, 0xFF9996A5);
        int sx = cx + cw - 48; rect(c, sx, y + 11, 36, 17, value ? 0xFF715B9D : 0xFF454650);
        rect(c, sx + (value ? 21 : 3), y + 14, 11, 11, 0xFFF5F1FC);
        click(cx, y, cw, 39, () -> { action.run(); MoonConfig.save(); });
    }
    private void button(DrawContext c, String label, int x, int y, int w, Runnable action) {
        rect(c, x, y, w, 28, 0xFF392F4B); rect(c, x, y, w, 1, 0xFF8D72BE);
        text(c, label, x + 10, y + 9, 0xFFF0E9FC); click(x, y, w, 28, action);
    }

    @Override public void render(DrawContext c, int mouseX, int mouseY, float delta) {
        renderBackground(c, mouseX, mouseY, delta);
        clicks.clear();
        panelW = Math.min(780, width - 24); panelH = Math.min(450, height - 24);
        left = (width - panelW) / 2; top = (height - panelH) / 2; sideW = 168;
        cx = left + sideW + 24; cw = panelW - sideW - 48;
        rect(c, left, top, panelW, panelH, 0xF014151B); rect(c, left, top, panelW, 3, 0xFFAA8AEF);
        rect(c, left, top + 3, sideW, panelH - 3, 0xFF191A22);
        text(c, "◐  MOON CLIENT", left + 17, top + 20, 0xFFF0ECFA);
        text(c, "PLAY BETTER. FEEL GREATER.", left + 18, top + 39, 0xFF898796);
        rect(c, left + 14, top + 59, sideW - 28, 1, 0xFF393744);
        String[] names = {"HOME", "MODS", "HUD", "COSMETICS", "SETTINGS"};
        Page[] pages = Page.values();
        for (int i = 0; i < pages.length; i++) {
            int yy = top + 78 + i * 39;
            if (page == pages[i]) { rect(c, left + 9, yy - 7, sideW - 18, 29, 0xFF30283F); rect(c, left + 9, yy - 7, 3, 29, 0xFFB493F5); }
            text(c, names[i], left + 23, yy + 2, page == pages[i] ? 0xFFF0E7FF : 0xFFA09DAC);
            Page target = pages[i]; click(left + 9, yy - 7, sideW - 18, 29, () -> page = target);
        }
        text(c, "ZypherVision", left + 18, top + panelH - 43, 0xFFEAE5F3);
        text(c, "MOON CLIENT  •  1.21.11", left + 18, top + panelH - 27, 0xFF898796);
        String title = switch (page) { case HOME -> "Home"; case MODS -> "Mods"; case HUD -> "HUD"; case COSMETICS -> "Cosmetics"; case SETTINGS -> "Settings"; };
        text(c, title, cx, top + 20, 0xFFF1EDF9);
        text(c, "A clean, lightweight Minecraft experience.", cx, top + 39, 0xFF9D9AA9);
        switch (page) {
            case HOME -> home(c);
            case MODS -> mods(c);
            case HUD -> hud(c);
            case COSMETICS -> cosmetics(c);
            case SETTINGS -> settings(c);
        }
        text(c, "RIGHT SHIFT  •  CLOSE", left + panelW - 160, top + panelH - 17, 0xFF777582);
        super.render(c, mouseX, mouseY, delta);
    }

    private void home(DrawContext c) {
        int y = top + 70, gap = 10, w = (cw - gap * 2) / 3;
        card(c, "VERSION", "Minecraft 1.21.11", cx, y, w, 58);
        card(c, "HUD", MoonConfig.hudEnabled ? "Enabled" : "Disabled", cx + w + gap, y, w, 58);
        card(c, "PROFILE", "ZypherVision", cx + (w + gap) * 2, y, w, 58);
        card(c, "QUICK SETTINGS", "Frequently used controls", cx, y + 72, cw, 174);
        toggle(c, "HUD Overlay", "Show Moon Client HUD elements", MoonConfig.hudEnabled, y + 111, () -> MoonConfig.hudEnabled = !MoonConfig.hudEnabled);
        toggle(c, "Toggle Sprint", "Enable the sprint keybind", MoonConfig.toggleSprint, y + 156, () -> MoonConfig.toggleSprint = !MoonConfig.toggleSprint);
        card(c, "WELCOME TO MOON", "Lightweight tools, useful HUD and a simple interface.", cx, y + 258, cw, 58);
    }
    private void mods(DrawContext c) {
        int y = top + 70;
        toggle(c, "Toggle Sprint", "Toggle sprint with the keybind configured in Controls", MoonConfig.toggleSprint, y, () -> MoonConfig.toggleSprint = !MoonConfig.toggleSprint); y += 46;
        toggle(c, "Custom Crosshair", "Draw a subtle purple crosshair overlay", MoonConfig.customCrosshair, y, () -> MoonConfig.customCrosshair = !MoonConfig.customCrosshair); y += 46;
        toggle(c, "Reduced Particles", "Saved preference; vanilla particle setting is unchanged", MoonConfig.reducedParticles, y, () -> MoonConfig.reducedParticles = !MoonConfig.reducedParticles);
        card(c, "MORE CLIENT MODS", "Additional modules can be added as separate, tested features.", cx, y + 58, cw, 58);
    }
    private void hud(DrawContext c) {
        int y = top + 70;
        toggle(c, "HUD Enabled", "Master switch for Moon HUD", MoonConfig.hudEnabled, y, () -> MoonConfig.hudEnabled = !MoonConfig.hudEnabled); y += 44;
        toggle(c, "FPS Counter", "Current frames per second", MoonConfig.showFps, y, () -> MoonConfig.showFps = !MoonConfig.showFps); y += 44;
        toggle(c, "CPS Counter", "Left and right clicks per second", MoonConfig.showCps, y, () -> MoonConfig.showCps = !MoonConfig.showCps); y += 44;
        toggle(c, "Keystrokes", "Movement and mouse button display", MoonConfig.showKeystrokes, y, () -> MoonConfig.showKeystrokes = !MoonConfig.showKeystrokes); y += 44;
        toggle(c, "Coordinates", "Player block coordinates", MoonConfig.showCoordinates, y, () -> MoonConfig.showCoordinates = !MoonConfig.showCoordinates); y += 44;
        toggle(c, "Ping", "Your current server latency", MoonConfig.showPing, y, () -> MoonConfig.showPing = !MoonConfig.showPing); y += 44;
        toggle(c, "Armor", "Equipped armor icons", MoonConfig.showArmor, y, () -> MoonConfig.showArmor = !MoonConfig.showArmor); y += 44;
        toggle(c, "Potion Effects", "Active effects and remaining seconds", MoonConfig.showPotionEffects, y, () -> MoonConfig.showPotionEffects = !MoonConfig.showPotionEffects);
    }
    private void cosmetics(DrawContext c) {
        int y = top + 75;
        card(c, "PLAYER SKIN", "Moon Client uses your Minecraft account skin.", cx, y, cw, 78);
        text(c, "To change your skin, use the official Minecraft Launcher or account profile.", cx + 12, y + 48, 0xFFB9A6E8);
        card(c, "CLIENT THEME", "Charcoal panels  •  Soft purple accents  •  Minimal motion", cx, y + 92, cw, 68);
        card(c, "COSMETICS STATUS", "Custom cape and skin-file importing are not included in this build.", cx, y + 174, cw, 68);
    }
    private void settings(DrawContext c) {
        int y = top + 70;
        toggle(c, "Compact HUD", "Use tighter HUD spacing", MoonConfig.compactHud, y, () -> MoonConfig.compactHud = !MoonConfig.compactHud); y += 46;
        toggle(c, "Minimal Animations", "Keep the interface static and lightweight", MoonConfig.minimalAnimations, y, () -> MoonConfig.minimalAnimations = !MoonConfig.minimalAnimations); y += 55;
        button(c, "SAVE SETTINGS", cx, y, 150, MoonConfig::save);
        button(c, "CLOSE MENU", cx + 162, y, 130, this::close);
        text(c, "Settings are stored in config/moonclient.properties", cx, y + 43, 0xFF9996A5);
    }

    @Override public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) for (int i = clicks.size() - 1; i >= 0; i--) {
            Click target = clicks.get(i);
            if (target.inside(mouseX, mouseY)) { target.action().run(); return true; }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    @Override public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) { close(); return true; }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    @Override public boolean shouldPause() { return false; }
}
