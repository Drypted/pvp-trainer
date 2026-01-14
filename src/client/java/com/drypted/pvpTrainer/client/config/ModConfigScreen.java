package com.drypted.pvpTrainer.client.config;

import com.drypted.pvpTrainer.client.config.gui.ButtonWidget;
import com.drypted.pvpTrainer.client.config.gui.ScrollBoxWidget;
import com.drypted.pvpTrainer.client.hudOverlay.PVPLabels;
import com.drypted.pvpTrainer.client.hudOverlay.SharedConstants;
import com.drypted.pvpTrainer.client.utils.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

class ModConfigScreen extends Screen
{
    private final Screen parent;

    public ModConfigScreen(Screen parent)
    {
        super(Component.translatable("com.drypted.pvptrainer.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init()
    {
        _addBackButton();

        int margin = 6;
        int labelGap = 6;

        // 4 labels for each corner
        int top_left_cursor = margin;
        int top_right_cursor = margin;
        int bottom_left_cursor = SharedConstants.GetScreenH() - margin;
        int bottom_right_cursor = SharedConstants.GetScreenH() - margin;
        for (int i = 0; i < 4; i++)
        {
            ButtonWidget top_left_label = ButtonWidget.builder(margin, top_left_cursor, "").build();
            ButtonWidget top_right_label = ButtonWidget.builder(0, top_right_cursor, "").build();
            ButtonWidget bottom_left_label = ButtonWidget.builder(margin, bottom_left_cursor, "").build();
            ButtonWidget bottom_right_label = ButtonWidget.builder(0, bottom_right_cursor, "").build();

            top_left_label.setWidth(40);
            top_right_label.setWidth(40);
            bottom_left_label.setWidth(40);
            bottom_right_label.setWidth(40);

            top_right_label.setX(SharedConstants.GetScreenW() - top_right_label.getWidth() - margin);
            bottom_right_label.setX(SharedConstants.GetScreenW() - top_right_label.getWidth() - margin);

            bottom_left_label.setY(bottom_left_label.getY() - bottom_left_label.getHeight());
            bottom_right_label.setY(bottom_right_label.getY() - bottom_right_label.getHeight());

            top_left_cursor += top_left_label.getHeight() + labelGap;
            top_right_cursor += top_right_label.getHeight() + labelGap;
            bottom_left_cursor -= bottom_left_label.getHeight() + labelGap;
            bottom_right_cursor -= bottom_right_label.getHeight() + labelGap;

            this.addRenderableWidget(top_left_label);
            this.addRenderableWidget(top_right_label);
            this.addRenderableWidget(bottom_left_label);
            this.addRenderableWidget(bottom_right_label);
        }


        ScrollBoxWidget box = ScrollBoxWidget.builder(10, 20, 120, 150).bgColor(Colors.BLACK.withAlpha(128)).padding(6).build();

        int scrollBoxChildCursor = 5;
        for (int i = 1; i <= 10; i++)
        {
            ButtonWidget button = ButtonWidget.builder(0, 0, "Button " + i).toggleButton(false).build();
            box.addChild(button, 5, scrollBoxChildCursor);
            scrollBoxChildCursor += button.getHeight() + 5;
        }

        this.addRenderableWidget(box);
    }

    private void _addBackButton()
    {
        final int buttonWidth = 80;
        final int buttonHeight = 20;
        final int x = (this.width - buttonWidth) / 2;
        final int y = 10;

        ButtonWidget backButton = ButtonWidget.builder(x, y, "Back")
                .centeredText(true)
                .toggleButton(false)
                .onClick(mouseEvent -> Minecraft.getInstance().setScreen(parent))
                .build();
        backButton.setWidth(buttonWidth);
        backButton.setHeight(buttonHeight);
        this.addRenderableWidget(backButton);
    }

    @Override
    public void onClose()
    {
        PVPLabels.refreshHotbarKeys();
        Minecraft.getInstance().setScreen(parent);
    }
}
