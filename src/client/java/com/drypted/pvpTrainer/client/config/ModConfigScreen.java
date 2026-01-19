package com.drypted.pvpTrainer.client.config;

import com.drypted.pvpTrainer.client.PvpTrainerClient;
import com.drypted.pvpTrainer.client.config.gui.ButtonWidget;
import com.drypted.pvpTrainer.client.config.gui.ScrollBoxWidget;
import com.drypted.pvpTrainer.client.hudOverlay.PVPLabels;
import com.drypted.pvpTrainer.client.hudOverlay.SharedConstants;
import com.drypted.pvpTrainer.client.utils.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

import static com.drypted.pvpTrainer.client.PvpTrainerClient.CONFIG;

class ModConfigScreen extends Screen
{
    private final Screen parent;
    private ModConfig config;

    public static final int LABELS_PER_CORNER = 4;
    public static final int BUTTON_WIDTH = 80;
    public static final int BUTTON_HEIGHT = 20;
    public static final int SCREEN_MARGIN = 6;
    public static final int SCROLL_BOX_MARGIN = 4;
    public static final int SCROLL_BOX_BUTTON_WIDTH = 80;
    private static final int SCROLL_BOX_WIDTH = SCROLL_BOX_BUTTON_WIDTH + (SCROLL_BOX_MARGIN * 2);

    private final List<ScreenLabel> topLeftScreenLabels = new ArrayList<>(LABELS_PER_CORNER);
    private final List<ScreenLabel> topRightScreenLabels = new ArrayList<>(LABELS_PER_CORNER);
    private final List<ScreenLabel> bottomLeftScreenLabels = new ArrayList<>(LABELS_PER_CORNER);
    private final List<ScreenLabel> bottomRightScreenLabels = new ArrayList<>(LABELS_PER_CORNER);

    private ScrollBoxWidget topLeftBox;
    private ScrollBoxWidget topRightBox;
    private ScrollBoxWidget bottomLeftBox;
    private ScrollBoxWidget bottomRightBox;

    private ScreenLabel selectedLabel;

    public ModConfigScreen(Screen parent)
    {
        super(Component.translatable("com.drypted.pvptrainer.config.title"));
        this.parent = parent;
        this.config = CONFIG;
    }

    @Override
    protected void init()
    {
        clearState();

        init_labels();
        init_scrollboxes();
        init_scrollboxes_callback();
        init_render();

        updateScreen();
    }

    private void clearState()
    {
        // clear widget lists
        topLeftScreenLabels.clear();
        topRightScreenLabels.clear();
        bottomLeftScreenLabels.clear();
        bottomRightScreenLabels.clear();

        // pre-fill with nulls
        for (int i = 0; i < LABELS_PER_CORNER; i++)
        {
            topLeftScreenLabels.add(null);
            topRightScreenLabels.add(null);
            bottomLeftScreenLabels.add(null);
            bottomRightScreenLabels.add(null);
        }

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
        int top_left_cursor = SCREEN_MARGIN;
        int top_right_cursor = SCREEN_MARGIN;
        int bottom_left_cursor = SharedConstants.GetScreenH() - SCREEN_MARGIN;
        int bottom_right_cursor = SharedConstants.GetScreenH() - SCREEN_MARGIN;

        for (int i = 0; i < LABELS_PER_CORNER; i++)
        {
            ScreenLabel top_left = new ScreenLabel(ModConfig.LabelCorner.TOP_LEFT, i);
            ScreenLabel top_right = new ScreenLabel(ModConfig.LabelCorner.TOP_RIGHT, i);
            ScreenLabel bottom_left = new ScreenLabel(ModConfig.LabelCorner.BOTTOM_LEFT, i);
            ScreenLabel bottom_right = new ScreenLabel(ModConfig.LabelCorner.BOTTOM_RIGHT, i);

            String top_left_button_text = ModConfig.LabelConfig.getLabelName(ModConfig.LabelCorner.TOP_LEFT, i);
            top_left.getButton().setX(SCREEN_MARGIN);
            top_left.getButton().setY(top_left_cursor);
            top_left.getButton().setText(top_left_button_text);

            String top_right_button_text = ModConfig.LabelConfig.getLabelName(ModConfig.LabelCorner.TOP_RIGHT, i);
            top_right.getButton().setX(SharedConstants.GetScreenW() - BUTTON_WIDTH - SCREEN_MARGIN);
            top_right.getButton().setY(top_right_cursor);
            top_right.getButton().setText(top_right_button_text);

            String bottom_left_button_text = ModConfig.LabelConfig.getLabelName(ModConfig.LabelCorner.BOTTOM_LEFT, i);
            bottom_left.getButton().setX(SCREEN_MARGIN);
            bottom_left.getButton().setY(bottom_left_cursor - BUTTON_HEIGHT);
            bottom_left.getButton().setText(bottom_left_button_text);

            String bottom_right_button_text = ModConfig.LabelConfig.getLabelName(ModConfig.LabelCorner.BOTTOM_RIGHT, i);
            bottom_right.getButton().setX(SharedConstants.GetScreenW() - BUTTON_WIDTH - SCREEN_MARGIN);
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
    }

    private void init_scrollboxes()
    {
        final int top_left_cursor = topLeftScreenLabels.getLast().getButton().getY() + topLeftScreenLabels.getLast()
                .getButton()
                .getHeight();
        final int top_right_cursor = topRightScreenLabels.getLast().getButton().getY() + topRightScreenLabels.getLast()
                .getButton()
                .getHeight();
        final int bottom_left_cursor = bottomLeftScreenLabels.getLast().getButton().getY();
        final int bottom_right_cursor = bottomRightScreenLabels.getLast().getButton().getY();

        topLeftBox = ScrollBoxWidget.builder(
                        SCREEN_MARGIN + BUTTON_WIDTH + SCREEN_MARGIN,
                        SCREEN_MARGIN,
                        SCROLL_BOX_WIDTH,
                        top_left_cursor - SCREEN_MARGIN
                )
                .margin(SCROLL_BOX_MARGIN)
                .build();
        topRightBox = ScrollBoxWidget.builder(
                SharedConstants.GetScreenW() - SCREEN_MARGIN - BUTTON_WIDTH - SCREEN_MARGIN - SCROLL_BOX_WIDTH,
                SCREEN_MARGIN,
                SCROLL_BOX_WIDTH,
                top_right_cursor - SCREEN_MARGIN
        ).margin(SCROLL_BOX_MARGIN).build();
        bottomLeftBox = ScrollBoxWidget.builder(
                SCREEN_MARGIN + BUTTON_WIDTH + SCREEN_MARGIN,
                bottom_left_cursor,
                SCROLL_BOX_WIDTH,
                SharedConstants.GetScreenH() - SCREEN_MARGIN - bottom_left_cursor
        ).margin(SCROLL_BOX_MARGIN).build();
        bottomRightBox = ScrollBoxWidget.builder(
                SharedConstants.GetScreenW() - SCREEN_MARGIN - BUTTON_WIDTH - SCREEN_MARGIN - SCROLL_BOX_WIDTH,
                bottom_right_cursor,
                SCROLL_BOX_WIDTH,
                SharedConstants.GetScreenH() - SCREEN_MARGIN - bottom_right_cursor
        ).margin(SCROLL_BOX_MARGIN).build();

        ScrollBoxWidget[] scrollBoxWidgets = new ScrollBoxWidget[]{topLeftBox, topRightBox, bottomLeftBox, bottomRightBox};

        for (int i = 0; i < 4; i++)
        {
            ScrollBoxWidget box = scrollBoxWidgets[i];

            // options
            for (LabelType labelType : LabelType.values())
            {
                box.addChildRow(
                        ButtonWidget.builder(0, 0, labelType.getName())
                                .width(SCROLL_BOX_BUTTON_WIDTH)
                                .centeredText(true)
                                .onClick((mEv, pressed) -> onClickLabelInBox(labelType))
                                .build(), labelType.getId()
                );
            }
            // hidden by default
            box.visible = false;
        }

        // assign boxes
        for (int i = 0; i < LABELS_PER_CORNER; i++)
        {
            topLeftScreenLabels.get(i).setBox(topLeftBox);
            topRightScreenLabels.get(i).setBox(topRightBox);
            bottomLeftScreenLabels.get(i).setBox(bottomLeftBox);
            bottomRightScreenLabels.get(i).setBox(bottomRightBox);
        }
    }

    private void init_scrollboxes_callback()
    {
        List<ScreenLabel> all_labels = new ArrayList<>();
        all_labels.addAll(topLeftScreenLabels);
        all_labels.addAll(topRightScreenLabels);
        all_labels.addAll(bottomLeftScreenLabels);
        all_labels.addAll(bottomRightScreenLabels);

        for (ScreenLabel label : all_labels)
        {
            label.getButton().setOnClick((mEv, pressed) -> onClickLabel(label, pressed, all_labels));
        }
    }

    private void init_render()
    {
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
        int xPos = (SharedConstants.GetScreenW() - buttonWidth) / 2;
        final int topPadding = 10;

        // Creative Config button
        ButtonWidget creativeConfigButton = ButtonWidget.builder(xPos, topPadding, "Creative")
                .centeredText(true)
                .toggleButton(true)
                .build();

        creativeConfigButton.setWidth(buttonWidth);
        creativeConfigButton.setHeight(buttonHeight);
        creativeConfigButton.setOnClick((mouseButtonEvent, pressed) -> {
        });

        // Done button
        ButtonWidget doneButton = ButtonWidget.builder(xPos, topPadding + buttonHeight, "Done")
                .bgColor(Colors.GREEN.withAlpha(64))
                .hoverColor(Colors.GREEN.withAlpha(128))
                .clickColor(Colors.WHITE)
                .centeredText(true)
                .toggleButton(false)
                .build();
        doneButton.setWidth(buttonWidth);
        doneButton.setHeight(buttonHeight);
        doneButton.setOnClick((mouseButtonEvent, pressed) -> onClickDoneButton(doneButton));

        // Cancel button
        ButtonWidget cancelButton = ButtonWidget.builder(xPos, topPadding + 2 * (buttonHeight), "Cancel")
                .bgColor(Colors.RED.withAlpha(64))
                .hoverColor(Colors.RED.withAlpha(72))
                .clickColor(Colors.WHITE)
                .centeredText(true)
                .toggleButton(false)
                .build();
        cancelButton.setWidth(buttonWidth);
        cancelButton.setHeight(buttonHeight);
        cancelButton.setOnClick((mouseButtonEvent, pressed) -> onClickCancelButton());

        this.addRenderableWidget(creativeConfigButton);
        this.addRenderableWidget(doneButton);
        addRenderableWidget(cancelButton);
    }

    @Override
    public void onClose()
    {
        PVPLabels.refreshHotbarKeys();
        Minecraft.getInstance().setScreen(parent);
    }

    private void updateScreen()
    {
        // update pressedLabel on labels from config
        _updateTypeFromConfig(config.moveStateLabelConfig, LabelType.MOVE_STATE);
        _updateTypeFromConfig(config.pitchAngleLabelConfig, LabelType.PITCH_ANGLE);
        _updateTypeFromConfig(config.pressedKeyLabelConfig, LabelType.PRESSED_KEY);

        // update button text from clickedLabel types
        _updateTextFromTypes(topLeftScreenLabels);
        _updateTextFromTypes(topRightScreenLabels);
        _updateTextFromTypes(bottomLeftScreenLabels);
        _updateTextFromTypes(bottomRightScreenLabels);
    }

    private void _updateTypeFromConfig(ModConfig.LabelConfig label, LabelType type)
    {
        ModConfig.LabelCorner moveStateCorner = label.corner;
        int moveStateIndex = label.positionIndex;

        if (moveStateCorner == ModConfig.LabelCorner.NONE || moveStateIndex < 0 || moveStateIndex >= LABELS_PER_CORNER)
        {
            PvpTrainerClient.LOGGER.warn(
                    "Invalid clickedLabel config for type {}: corner={}, index={}",
                    type,
                    moveStateCorner,
                    moveStateIndex
            );
            return;
        }

        switch (moveStateCorner)
        {
            case TOP_LEFT -> topLeftScreenLabels.get(moveStateIndex).setLabelType(type);
            case TOP_RIGHT -> topRightScreenLabels.get(moveStateIndex).setLabelType(type);
            case BOTTOM_LEFT -> bottomLeftScreenLabels.get(moveStateIndex).setLabelType(type);
            case BOTTOM_RIGHT -> bottomRightScreenLabels.get(moveStateIndex).setLabelType(type);
        }
    }

    private void _updateTextFromTypes(List<ScreenLabel> labels)
    {
        for (final ScreenLabel label : labels)
        {
            label.getButton().setText(label.getLabelType().getName());
        }
    }

    /// Removes the given clickedLabel type from all screen labels
    private void clearLabelType(LabelType type)
    {
        _clearLabelTypeFromList(topLeftScreenLabels, type);
        _clearLabelTypeFromList(topRightScreenLabels, type);
        _clearLabelTypeFromList(bottomLeftScreenLabels, type);
        _clearLabelTypeFromList(bottomRightScreenLabels, type);

        switch (type)
        {
            case MOVE_STATE ->
            {
                config.moveStateLabelConfig.corner = ModConfig.LabelCorner.NONE;
                config.moveStateLabelConfig.positionIndex = -1;
            }
            case PITCH_ANGLE ->
            {
                config.pitchAngleLabelConfig.corner = ModConfig.LabelCorner.NONE;
                config.pitchAngleLabelConfig.positionIndex = -1;
            }
            case PRESSED_KEY ->
            {
                config.pressedKeyLabelConfig.corner = ModConfig.LabelCorner.NONE;
                config.pressedKeyLabelConfig.positionIndex = -1;
            }
        }
    }

    private void _clearLabelTypeFromList(List<ScreenLabel> labels, LabelType type)
    {
        for (ScreenLabel label : labels)
        {
            if (label.getLabelType() == type)
            {
                label.setLabelType(LabelType.NONE);
            }
        }
    }

    // MARK - Button Handlers ---------------------------------------------------------------------

    /// On click handler for Done button
    private void onClickDoneButton(ButtonWidget doneButton)
    {
        PVPLabels.refreshHotbarKeys();
        if (ModConfig.isValidConfig(config))
        {
            CONFIG = config;
            PvpTrainerClient.saveConfig();
            onClose();
        }
        else
        {
            doneButton.setText("Invalid Config!");
            doneButton.setOutlineColor(Colors.RED);
            doneButton.setBackgroundColor(Colors.RED.withAlpha(64));
            doneButton.setHoverColor(Colors.RED.withAlpha(128));
        }
    }

    /// On click handler for Cancel button
    private void onClickCancelButton()
    {
        config = CONFIG;
        PVPLabels.refreshHotbarKeys();
        onClose();
    }

    /// On click handler for clickedLabel selection buttons; Move State, Pitch Angle, Pressed Key
    private void onClickLabelInBox(LabelType pressedLabel)
    {
        // clear previous type
        LabelType previousType = selectedLabel.getLabelType();
        if (previousType != LabelType.NONE)
        {
            clearLabelType(previousType);
        }

        clearLabelType(pressedLabel);

        switch (pressedLabel)
        {
            case MOVE_STATE ->
            {
                config.moveStateLabelConfig.corner = selectedLabel.getCorner();
                config.moveStateLabelConfig.positionIndex = selectedLabel.getIndex();
            }
            case PITCH_ANGLE ->
            {
                config.pitchAngleLabelConfig.corner = selectedLabel.getCorner();
                config.pitchAngleLabelConfig.positionIndex = selectedLabel.getIndex();
            }
            case PRESSED_KEY ->
            {
                config.pressedKeyLabelConfig.corner = selectedLabel.getCorner();
                config.pressedKeyLabelConfig.positionIndex = selectedLabel.getIndex();
            }
        }

        // update selected clickedLabel
        selectedLabel.setLabelType(pressedLabel);
        selectedLabel.getButton().setPressed(false);
        selectedLabel.getBox().visible = false;

        this.updateScreen();
    }

    private void onClickLabel(ScreenLabel clickedLabel, Boolean isPressed, List<ScreenLabel> all_labels)
    {
        selectedLabel = clickedLabel;

        for (ScreenLabel otherLabel : all_labels)
        {
            if (otherLabel != selectedLabel)
            {
                otherLabel.getButton().setPressed(false);
                otherLabel.getBox().visible = false;
            }
        }

        // show current box
        selectedLabel.getBox().visible = isPressed;

        // unhighlight all buttons in box
        for (AbstractWidget widget : selectedLabel.getBox().getAllChildren())
        {
            if (widget instanceof ButtonWidget button)
            {
                button.setPressed(false);
            }
        }

        // highlight current selection in box
        ButtonWidget selectedButton = ((ButtonWidget) selectedLabel.getBox().getChild(clickedLabel.getLabelType().getId()));
        if (selectedButton != null)
        {
            selectedButton.setPressed(true);
        }
    }
}
