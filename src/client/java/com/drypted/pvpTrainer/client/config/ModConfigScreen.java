package com.drypted.pvpTrainer.client.config;

import com.drypted.pvpTrainer.client.config.gui.ButtonWidget;
import com.drypted.pvpTrainer.client.config.gui.ScrollBoxWidget;
import com.drypted.pvpTrainer.client.hudOverlay.PVPLabels;
import com.drypted.pvpTrainer.client.hudOverlay.SharedConstants;
import com.drypted.pvpTrainer.client.utils.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

class ModConfigScreen extends Screen
{
    private final Screen parent;

    private static final int BUTTONS_COUNT = 4;
    public static final int BUTTON_WIDTH = 40;
    public static final int BUTTON_HEIGHT = 20;
    private final int margin = 6;

    private final List<ScreenLabel> topLeftScreenLabels = new ArrayList<>(BUTTONS_COUNT);
    private final List<ScreenLabel> topRightScreenLabels = new ArrayList<>(BUTTONS_COUNT);
    private final List<ScreenLabel> bottomLeftScreenLabels = new ArrayList<>(BUTTONS_COUNT);
    private final List<ScreenLabel> bottomRightScreenLabels = new ArrayList<>(BUTTONS_COUNT);

    private ScrollBoxWidget topLeftBox;
    private ScrollBoxWidget topRightBox;
    private ScrollBoxWidget bottomLeftBox;
    private ScrollBoxWidget bottomRightBox;

    private int top_left_end;
    private int top_right_end;
    private int bottom_left_start;
    private int bottom_right_start;

    public ModConfigScreen(Screen parent)
    {
        super(Component.translatable("com.drypted.pvptrainer.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init()
    {
        clearState();

        init_labels();
        init_scrollboxes();
        init_scrollboxes_callback();
        init_render();
    }

    private void clearState()
    {
        // clear widget lists
        topLeftScreenLabels.clear();
        topRightScreenLabels.clear();
        bottomLeftScreenLabels.clear();
        bottomRightScreenLabels.clear();

        // clear renderables already added to the screen
        this.clearWidgets();

        // clear references
        topLeftBox = null;
        topRightBox = null;
        bottomLeftBox = null;
        bottomRightBox = null;
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
            ScreenLabel top_left = new ScreenLabel(LabelType.MOVE_STATE);
            ScreenLabel top_right = new ScreenLabel(LabelType.MOVE_STATE);
            ScreenLabel bottom_left = new ScreenLabel(LabelType.MOVE_STATE);
            ScreenLabel bottom_right = new ScreenLabel(LabelType.MOVE_STATE);

            String top_left_button_text = getLabelTextIfAvailable(topLeftScreenLabels.get(i));
            top_left.getButton().setX(margin);
            top_left.getButton().setY(top_left_cursor);
            top_left.getButton().setText(top_left_button_text);

            String top_right_button_text = getLabelTextIfAvailable(topRightScreenLabels.get(i));
            top_right.getButton().setX(SharedConstants.GetScreenW() - BUTTON_WIDTH - margin);
            top_right.getButton().setY(top_right_cursor);
            top_right.getButton().setText(top_right_button_text);

            String bottom_left_button_text = getLabelTextIfAvailable(bottomLeftScreenLabels.get(i));
            bottom_left.getButton().setX(margin);
            bottom_left.getButton().setY(bottom_left_cursor - BUTTON_HEIGHT);
            bottom_left.getButton().setText(bottom_left_button_text);

            String bottom_right_button_text = getLabelTextIfAvailable(bottomRightScreenLabels.get(i));
            bottom_right.getButton().setX(SharedConstants.GetScreenW() - BUTTON_WIDTH - margin);
            bottom_right.getButton().setY(bottom_right_cursor - BUTTON_HEIGHT);
            bottom_right.getButton().setText(bottom_right_button_text);

            top_left_cursor += BUTTON_HEIGHT + labelGap;
            top_right_cursor += BUTTON_HEIGHT + labelGap;
            bottom_left_cursor -= BUTTON_HEIGHT + labelGap;
            bottom_right_cursor -= BUTTON_HEIGHT + labelGap;

            topLeftScreenLabels.set(i, top_left);
            topRightScreenLabels.set(i, top_right);
            bottomLeftScreenLabels.set(i, bottom_left);
            bottomRightScreenLabels.set(i, bottom_right);
        }

        this.top_left_end = top_left_cursor;
        this.top_right_end = top_right_cursor;
        this.bottom_left_start = bottom_left_cursor;
        this.bottom_right_start = bottom_right_cursor;
    }

    private String getLabelTextIfAvailable(@Nullable ScreenLabel label)
    {
        if (label == null)
        {
            return LabelType.MOVE_STATE.getName();
        }
        return label.label.getName();
    }

    private void init_scrollboxes()
    {
        // final int top_left_cursor = topLeftScreenLabels.getLast().getButton().getY() + topLeftScreenLabels.getLast()
        //         .getButton()
        //         .getHeight();
        // final int top_right_cursor = topRightScreenLabels.getLast().getButton().getY() + topRightScreenLabels.getLast()
        //         .getButton()
        //         .getHeight();
        // final int bottom_left_cursor = bottomLeftScreenLabels.getLast().getButton().getY();
        // final int bottom_right_cursor = bottomRightScreenLabels.getLast().getButton().getY();

        final int scrollBoxWidth = 100;

        topLeftBox = ScrollBoxWidget.builder(margin + BUTTON_WIDTH + margin, margin, scrollBoxWidth, top_left_end - margin)
                .build();
        topRightBox = ScrollBoxWidget.builder(
                SharedConstants.GetScreenW() - margin - BUTTON_WIDTH - margin - scrollBoxWidth,
                margin,
                scrollBoxWidth,
                top_right_end - margin
        ).build();
        bottomLeftBox = ScrollBoxWidget.builder(
                margin + BUTTON_WIDTH + margin,
                bottom_left_start,
                scrollBoxWidth,
                SharedConstants.GetScreenH() - margin - bottom_left_start
        ).build();
        bottomRightBox = ScrollBoxWidget.builder(
                SharedConstants.GetScreenW() - margin - BUTTON_WIDTH - margin - scrollBoxWidth,
                bottom_left_start,
                scrollBoxWidth,
                SharedConstants.GetScreenH() - margin - bottom_right_start
        ).build();

        ScrollBoxWidget[] scrollBoxWidgets = new ScrollBoxWidget[]{topLeftBox, topRightBox, bottomLeftBox, bottomRightBox};

        for (int i = 0; i < 4; i++)
        {
            ScrollBoxWidget box = scrollBoxWidgets[i];

            // options
            for (LabelType labelType : LabelType.values())
            {
                box.addChildRow(ButtonWidget.builder(0, 0, labelType.getName()).width(80).centeredText(true).build());
            }
            // hidden by default
            box.visible = false;
        }
    }

    private void init_scrollboxes_callback()
    {
        __add_callback(topLeftScreenLabels, topLeftBox);
        __add_callback(topRightScreenLabels, topRightBox);
        __add_callback(bottomLeftScreenLabels, bottomLeftBox);
        __add_callback(bottomRightScreenLabels, bottomRightBox);
    }

    private void __add_callback(List<ScreenLabel> labels, ScrollBoxWidget box)
    {
        for (ScreenLabel label : labels)
        {
            label.getButton().setOnClick(mouseButtonEvent -> {
                boolean anyPressed = false;

                for (ScreenLabel other : labels)
                {
                    if (other.getButton().isPressed())
                    {
                        other.getButton().setPressed(false);
                        anyPressed = true;
                    }
                }

                // only press if another was active
                if (anyPressed) label.getButton().setPressed(true);

                box.visible = anyPressed;
            });
        }
    }

    private void init_render()
    {
        // topLeftScreenLabels.forEach(this::addRenderableWidget);
        // topRightScreenLabels.forEach(this::addRenderableWidget);
        // bottomLeftScreenLabels.forEach(this::addRenderableWidget);
        // bottomRightScreenLabels.forEach(this::addRenderableWidget);
        topLeftScreenLabels.forEach(label -> this.addRenderableWidget(label.getButton()));
        topRightScreenLabels.forEach(label -> this.addRenderableWidget(label.getButton()));
        bottomLeftScreenLabels.forEach(label -> this.addRenderableWidget(label.getButton()));
        bottomRightScreenLabels.forEach(label -> this.addRenderableWidget(label.getButton()));

        this.addRenderableWidget(topLeftBox);
        this.addRenderableWidget(topRightBox);
        this.addRenderableWidget(bottomLeftBox);
        this.addRenderableWidget(bottomRightBox);

        // add three buttons: creative config, done, cancel
        int buttonHeight = 20;
        int buttonWidth = 80;
        int spacing = 10;
        int xPos = (SharedConstants.GetScreenW() - buttonWidth) / 2;
        final int topPadding = 10;

        // Creative Config button
        ButtonWidget creativeConfigButton = ButtonWidget.builder(xPos, topPadding, "Creative")
                .centeredText(true)
                .toggleButton(true)
                .build();

        creativeConfigButton.setWidth(buttonWidth);
        creativeConfigButton.setHeight(buttonHeight);
        creativeConfigButton.setOnClick(mouseButtonEvent -> {
        });

        // Done button
        ButtonWidget doneButton = ButtonWidget.builder(xPos, topPadding + buttonHeight + spacing, "Done")
                .bgColor(Colors.GREEN.withAlpha(64))
                .hoverColor(Colors.GREEN.withAlpha(128))
                .clickColor(Colors.WHITE)
                .centeredText(true)
                .toggleButton(false)
                .build();
        doneButton.setWidth(buttonWidth);
        doneButton.setHeight(buttonHeight);
        doneButton.setOnClick(mouseButtonEvent -> onClose());

        // Cancel button
        ButtonWidget cancelButton = ButtonWidget.builder(xPos, topPadding + 2 * (buttonHeight + spacing), "Cancel")
                .bgColor(Colors.RED.withAlpha(64))
                .hoverColor(Colors.RED.withAlpha(72))
                .clickColor(Colors.WHITE)
                .centeredText(true)
                .toggleButton(false)
                .build();
        cancelButton.setWidth(buttonWidth);
        cancelButton.setHeight(buttonHeight);
        cancelButton.setOnClick(mouseButtonEvent -> onClose());

        this.addRenderableWidget(creativeConfigButton);
        this.addRenderableWidget(doneButton);
        this.addRenderableWidget(cancelButton);
    }

    @Override
    public void onClose()
    {
        PVPLabels.refreshHotbarKeys();
        Minecraft.getInstance().setScreen(parent);
    }

    private enum LabelType
    {
        MOVE_STATE("Move State"),
        PITCH_ANGLE("Pitch Angle"),
        KEY_PRESS("Key Press");

        private final String name;

        LabelType(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }
    }

    private static final class ScreenLabel
    {
        private LabelType label;

        private final ButtonWidget button = ButtonWidget.builder(0, 0, "")
                .width(BUTTON_WIDTH)
                .centeredText(true)
                .toggleButton(true)
                .build();

        public ScreenLabel(LabelType label)
        {
            this.label = label;
        }

        public ButtonWidget getButton()
        {
            return button;
        }

        public LabelType getLabelType()
        {
            return label;
        }

        public void setLabelType(LabelType label)
        {
            this.label = label;
        }

        public void setButtonText(String text)
        {
            this.button.setText(text);
        }

    }
}
