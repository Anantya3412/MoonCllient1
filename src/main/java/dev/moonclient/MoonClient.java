package dev.moonclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayDeque;
import java.util.Deque;

public final class MoonClient implements ClientModInitializer {
    public static KeyBinding OPEN_MENU;
    public static KeyBinding TOGGLE_SPRINT;
    private static final Deque<Long> LEFT_CLICKS = new ArrayDeque<>();
    private static final Deque<Long> RIGHT_CLICKS = new ArrayDeque<>();
    private static boolean sprintToggled;
    private static boolean lastLeft;
    private static boolean lastRight;

    @Override
    public void onInitializeClient() {
        MoonConfig.load();
        OPEN_MENU = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.moonclient.open_menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT,
                KeyBinding.Category.MISC));
        TOGGLE_SPRINT = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.moonclient.toggle_sprint", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN,
                KeyBinding.Category.MISC));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (OPEN_MENU.wasPressed()) {
                if (client.currentScreen instanceof MoonScreen) client.setScreen(null);
                else if (client.currentScreen == null) client.setScreen(new MoonScreen());
            }
            while (TOGGLE_SPRINT.wasPressed()) {
                sprintToggled = !sprintToggled;
                if (client.player != null) client.player.displayClientMessage(
                        Text.literal("Moon Sprint: " + (sprintToggled ? "ON" : "OFF")), true);
            }
            if (MoonConfig.toggleSprint && sprintToggled && client.player != null && client.currentScreen == null) {
                client.player.setSprinting(true);
            }
            if (client.getWindow() != null && client.currentScreen == null) {
                boolean left = GLFW.glfwGetMouseButton(client.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_1) == GLFW.GLFW_PRESS;
                boolean right = GLFW.glfwGetMouseButton(client.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_2) == GLFW.GLFW_PRESS;
                long now = System.currentTimeMillis();
                if (left && !lastLeft) LEFT_CLICKS.addLast(now);
                if (right && !lastRight) RIGHT_CLICKS.addLast(now);
                lastLeft = left; lastRight = right;
            }
            prune(LEFT_CLICKS); prune(RIGHT_CLICKS);
        });

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> MoonHud.render(drawContext));
    }

    private static void prune(Deque<Long> clicks) {
        long cutoff = System.currentTimeMillis() - 1000;
        while (!clicks.isEmpty() && clicks.peekFirst() < cutoff) clicks.removeFirst();
    }

    public static int leftCps() { prune(LEFT_CLICKS); return LEFT_CLICKS.size(); }
    public static int rightCps() { prune(RIGHT_CLICKS); return RIGHT_CLICKS.size(); }
}
