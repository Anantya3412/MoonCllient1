package dev.moonclient;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class MoonScreen extends Screen {
    private static final int PANEL_WIDTH = 320;
    private static final int PANEL_HEIGHT = 230;

    public MoonScreen() {
        super(Text.literal("Moon Client"));
    }

    @Override
    protected void init() {
        int left = (this.width - PANEL_WIDTH) / 2;
        int top = (this.height - PANEL_HEIGHT) / 2;
        int x = left + 20;
        int width = PANEL_WIDTH - 40;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("HUD Settings"), b -> {})
            .dimensions(x, top + 48, width, 20).build());
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Visual Settings"), b -> {})
            .dimensions(x, top + 74, width, 20).build());
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Skin Settings"), b -> {})
            .dimensions(x, top + 100, width, 20).build());
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Performance"), b -> {})
            .dimensions(x, top + 126, width, 20).build());
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Close"), b -> this.close())
            .dimensions(x, top + 164, width, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        int left = (this.width - PANEL_WIDTH) / 2;
        int top = (this.height - PANEL_HEIGHT) / 2;

        context.fill(left, top, left + PANEL_WIDTH, top + PANEL_HEIGHT, 0xF0181820);
        context.fill(left, top, left + PANEL_WIDTH, top + 2, 0xFF9270E8);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.literal("Moon Client"),
            this.width / 2, top + 18, 0xFFE6DFFF);
        context.drawCenteredTextWithShadow(this.textRenderer,
            Text.literal("Simple  •  Lightweight  •  Customizable"),
            this.width / 2, top + 33, 0xFFB8B2C8);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
