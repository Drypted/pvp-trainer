package com.drypted.pvpTrainer.client.config;

import com.drypted.pvpTrainer.client.config.gui.ButtonWidget;
import com.drypted.pvpTrainer.client.config.gui.ScrollBoxWidget;
import com.drypted.pvpTrainer.client.hudOverlay.PVPLabels;
import com.drypted.pvpTrainer.client.hudOverlay.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

class ModConfigScreen extends Screen
{
    private final Screen parent;

    private static final int BUTTONS_COUNT = 4;

    private final List<ButtonWidget> topLeftLabels = new ArrayList<>(BUTTONS_COUNT);
    private final List<ButtonWidget> topRightLabels = new ArrayList<>(BUTTONS_COUNT);
    private final List<ButtonWidget> bottomLeftLabels = new ArrayList<>(BUTTONS_COUNT);
    private final List<ButtonWidget> bottomRightLabels = new ArrayList<>(BUTTONS_COUNT);

    private ScrollBoxWidget topLeftBox;
    private ScrollBoxWidget topRightBox;
    private ScrollBoxWidget bottomLeftBox;
    private ScrollBoxWidget bottomRightBox;

    private final int margin = 6;
    private final int buttonWidth = 40;


    public ModConfigScreen(Screen parent)
    {
        super(Component.translatable("com.drypted.pvptrainer.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init()
    {
        init_labels();
        init_scrollboxes();
        init_scrollboxes_callback();
        init_render();
    }

    private void init_labels()
    {
        final int labelGap = 6;

        // labels for each corner
        int top_left_cursor = margin;
        int top_right_cursor = margin;
        int bottom_left_cursor = SharedConstants.GetScreenH() - margin;
        int bottom_right_cursor = SharedConstants.GetScreenH() - margin;

        for (int i = 0; i < BUTTONS_COUNT; i++)
        {
            ButtonWidget top_left_label = ButtonWidget.builder(margin, top_left_cursor, "").build();
            ButtonWidget top_right_label = ButtonWidget.builder(0, top_right_cursor, "").build();
            ButtonWidget bottom_left_label = ButtonWidget.builder(margin, bottom_left_cursor, "").build();
            ButtonWidget bottom_right_label = ButtonWidget.builder(0, bottom_right_cursor, "").build();

            top_left_label.setWidth(buttonWidth);
            top_right_label.setWidth(buttonWidth);
            bottom_left_label.setWidth(buttonWidth);
            bottom_right_label.setWidth(buttonWidth);

            top_right_label.setX(SharedConstants.GetScreenW() - top_right_label.getWidth() - margin);
            bottom_right_label.setX(SharedConstants.GetScreenW() - top_right_label.getWidth() - margin);

            bottom_left_label.setY(bottom_left_label.getY() - bottom_left_label.getHeight());
            bottom_right_label.setY(bottom_right_label.getY() - bottom_right_label.getHeight());

            top_left_cursor += top_left_label.getHeight() + labelGap;
            top_right_cursor += top_right_label.getHeight() + labelGap;
            bottom_left_cursor -= bottom_left_label.getHeight() + labelGap;
            bottom_right_cursor -= bottom_right_label.getHeight() + labelGap;

            topLeftLabels.addLast(top_left_label);
            topRightLabels.addLast(top_right_label);
            bottomLeftLabels.addLast(bottom_left_label);
            bottomRightLabels.addLast(bottom_right_label);
        }
    }

    private void init_scrollboxes()
    {
        final int top_left_cursor = topLeftLabels.getLast().getY() + topLeftLabels.getLast().getHeight();
        final int top_right_cursor = topRightLabels.getLast().getY() + topRightLabels.getLast().getHeight();
        final int bottom_left_cursor = bottomLeftLabels.getLast().getY();
        final int bottom_right_cursor = bottomRightLabels.getLast().getY();

        final int scrollBoxWidth = 100;

        topLeftBox = ScrollBoxWidget.builder(margin + buttonWidth + margin, margin, scrollBoxWidth, top_left_cursor - margin)
                .build();
        topRightBox = ScrollBoxWidget.builder(
                SharedConstants.GetScreenW() - margin - buttonWidth - margin - scrollBoxWidth,
                margin,
                scrollBoxWidth,
                top_right_cursor - margin
        ).build();
        bottomLeftBox = ScrollBoxWidget.builder(
                margin + buttonWidth + margin,
                bottom_left_cursor,
                scrollBoxWidth,
                SharedConstants.GetScreenH() - margin - bottom_left_cursor
        ).build();
        bottomRightBox = ScrollBoxWidget.builder(
                SharedConstants.GetScreenW() - margin - buttonWidth - margin - scrollBoxWidth,
                bottom_left_cursor,
                scrollBoxWidth,
                SharedConstants.GetScreenH() - margin - bottom_right_cursor
        ).build();

        topLeftBox.visible = false;
        topRightBox.visible = false;
        bottomLeftBox.visible = false;
        bottomRightBox.visible = false;
    }

    private void init_scrollboxes_callback()
    {
        __add_callback(topLeftLabels, topLeftBox);
        __add_callback(topRightLabels, topRightBox);
        __add_callback(bottomLeftLabels, bottomLeftBox);
        __add_callback(bottomRightLabels, bottomRightBox);
    }

    private void __add_callback(List<ButtonWidget> labels, ScrollBoxWidget box)
    {
        for (ButtonWidget buttonWidget : labels)
        {
            buttonWidget.setOnClick(mouseButtonEvent -> {
                boolean anyPressed = false;

                for (ButtonWidget other : labels)
                {
                    if (other.isPressed())
                    {
                        other.setPressed(false);
                        anyPressed = true;
                    }
                }

                // only press if another was active
                if (anyPressed) buttonWidget.setPressed(true);

                box.visible = anyPressed;
            });
        }
    }

    private void init_render()
    {
        topLeftLabels.forEach(this::addRenderableWidget);
        topRightLabels.forEach(this::addRenderableWidget);
        bottomLeftLabels.forEach(this::addRenderableWidget);
        bottomRightLabels.forEach(this::addRenderableWidget);

        this.addRenderableWidget(topLeftBox);
        this.addRenderableWidget(topRightBox);
        this.addRenderableWidget(bottomLeftBox);
        this.addRenderableWidget(bottomRightBox);

        // back button at last
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
