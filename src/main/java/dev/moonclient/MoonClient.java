package dev.moonclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class MoonClient implements ClientModInitializer {
    public static KeyBinding OPEN_MENU;

    @Override
    public void onInitializeClient() {
        MoonConfig.load();
        OPEN_MENU = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.moonclient.open_menu",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            KeyBinding.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (OPEN_MENU.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new MoonScreen());
                } else if (client.currentScreen instanceof MoonScreen) {
                    client.setScreen(null);
                }
            }
        });

        MoonHud.init();
        MoonSkinSettings.init();
    }
}
