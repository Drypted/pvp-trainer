package com.drypted.pvpTrainer.client.config;

import com.drypted.pvpTrainer.client.config.gui.LabelWidget;
import com.drypted.pvpTrainer.client.hudOverlay.PVPLabels;
import com.drypted.pvpTrainer.client.hudOverlay.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
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
            LabelWidget top_left_label = LabelWidget.create(margin, top_left_cursor, "");
            LabelWidget top_right_label = LabelWidget.create(0, top_right_cursor, "");
            LabelWidget bottom_left_label = LabelWidget.create(margin, bottom_left_cursor, "");
            LabelWidget bottom_right_label = LabelWidget.create(0, bottom_right_cursor, "");

            top_right_label.setX(SharedConstants.GetScreenW() - top_right_label.getWidth() - margin);
            bottom_right_label.setX(SharedConstants.GetScreenW() - top_right_label.getWidth() - margin);

            bottom_left_label.setY(bottom_left_label.getY() - bottom_left_label.getHeight());
            bottom_right_label.setY(bottom_right_label.getY() - bottom_right_label.getHeight());

            top_left_cursor += top_left_label.getHeight() + labelGap;
            top_right_cursor += top_right_label.getHeight() + labelGap;
            bottom_left_cursor -= bottom_left_label.getHeight() + labelGap;
            bottom_right_cursor -= bottom_right_label.getHeight() + labelGap;

            top_left_label.setWidth(20);
            top_right_label.setWidth(20);
            bottom_left_label.setWidth(20);
            bottom_right_label.setWidth(20);

            this.addRenderableWidget(top_left_label);
            this.addRenderableWidget(top_right_label);
            this.addRenderableWidget(bottom_left_label);
            this.addRenderableWidget(bottom_right_label);
        }

    }

    private void _addBackButton()
    {
        final int buttonWidth = 80;
        final int buttonHeight = 20;
        final int x = (this.width - buttonWidth) / 2;
        final int y = 10;

        this.addRenderableWidget(Button.builder(Component.literal("Back"), button -> Minecraft.getInstance().setScreen(parent))
                                         .bounds(x, y, buttonWidth, buttonHeight)
                                         .build());
    }

    @Override
    public void onClose()
    {
        PVPLabels.refreshHotbarKeys();
        Minecraft.getInstance().setScreen(parent);
    }
}
