package dev.moonclient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public final class MoonHud {
    private static final int PURPLE = 0xFFB79AFF;
    private static final int WHITE = 0xFFF2EFF8;
    private static final int MUTED = 0xFFB7B3C2;
    private MoonHud() {}

    public static void render(DrawContext ctx) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (!MoonConfig.hudEnabled || mc.player == null || mc.options.hudHidden) return;
        int x = 8, y = 8, line = MoonConfig.compactHud ? 11 : 13;
        if (MoonConfig.showFps) y = row(ctx, "FPS  " + mc.getCurrentFps(), x, y, PURPLE, line);
        if (MoonConfig.showCps) y = row(ctx, "CPS  " + MoonClient.leftCps() + " | " + MoonClient.rightCps(), x, y, WHITE, line);
        if (MoonConfig.showPing) {
            PlayerListEntry entry = mc.getNetworkHandler() == null ? null : mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
            if (entry != null) y = row(ctx, "PING  " + entry.getLatency() + " ms", x, y, WHITE, line);
        }
        if (MoonConfig.showCoordinates) {
            var p = mc.player.getBlockPos();
            y = row(ctx, "XYZ  " + p.getX() + "  " + p.getY() + "  " + p.getZ(), x, y, WHITE, line);
        }
        if (MoonConfig.showPotionEffects && !mc.player.getActiveStatusEffects().isEmpty()) {
            y += 3;
            for (var effect : mc.player.getActiveStatusEffects().values()) {
                String name = effect.getEffectType().value().getName().getString();
                int seconds = effect.getDuration() / 20;
                y = row(ctx, name + "  " + seconds + "s", x, y, MUTED, line);
            }
        }
        if (MoonConfig.showKeystrokes) drawKeys(ctx, mc, mc.getWindow().getScaledWidth() - 72, 8);
        if (MoonConfig.showArmor) drawArmor(ctx, mc, 8, mc.getWindow().getScaledHeight() - 24);
        if (MoonConfig.customCrosshair && mc.crosshairTarget != null) {
            int cx = mc.getWindow().getScaledWidth() / 2, cy = mc.getWindow().getScaledHeight() / 2;
            ctx.fill(cx - 5, cy, cx + 6, cy + 1, PURPLE);
            ctx.fill(cx, cy - 5, cx + 1, cy + 6, PURPLE);
        }
    }

    private static int row(DrawContext ctx, String text, int x, int y, int color, int line) {
        int width = MinecraftClient.getInstance().textRenderer.getWidth(text) + 10;
        ctx.fill(x - 3, y - 2, x + width, y + line - 1, 0x990D0E14);
        ctx.drawText(MinecraftClient.getInstance().textRenderer, Text.literal(text), x + 2, y, color, true);
        return y + line;
    }

    private static void drawKeys(DrawContext ctx, MinecraftClient mc, int x, int y) {
        int w = 20, h = 18, gap = 2;
        key(ctx, mc, "W", x + w + gap, y, mc.options.forwardKey.isPressed());
        key(ctx, mc, "A", x, y + h + gap, mc.options.leftKey.isPressed());
        key(ctx, mc, "S", x + w + gap, y + h + gap, mc.options.backKey.isPressed());
        key(ctx, mc, "D", x + (w + gap) * 2, y + h + gap, mc.options.rightKey.isPressed());
        key(ctx, mc, "L", x, y + (h + gap) * 2, GLFWState.left(mc));
        key(ctx, "R", x + w + gap, y + (h + gap) * 2, GLFWState.right(mc));
    }

    private static void key(DrawContext ctx, MinecraftClient mc, String label, int x, int y, boolean down) {
        key(ctx, label, x, y, down);
    }
    private static void key(DrawContext ctx, String label, int x, int y, boolean down) {
        ctx.fill(x, y, x + 20, y + 18, down ? 0xFF69538F : 0xCC171821);
        ctx.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, label, x + 10, y + 5, WHITE);
    }

    private static void drawArmor(DrawContext ctx, MinecraftClient mc, int x, int y) {
        int i = 0;
        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD}) {
            ItemStack stack = mc.player.getEquippedStack(slot);
            if (!stack.isEmpty()) {
                ctx.drawItem(stack, x + i * 20, y);
                ctx.drawStackOverlay(mc.textRenderer, stack, x + i * 20, y);
            }
            i++;
        }
    }

    private static final class GLFWState {
        static boolean left(MinecraftClient mc) { return org.lwjgl.glfw.GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), 0) == 1; }
        static boolean right(MinecraftClient mc) { return org.lwjgl.glfw.GLFW.glfwGetMouseButton(mc.getWindow().getHandle(), 1) == 1; }
    }
}
