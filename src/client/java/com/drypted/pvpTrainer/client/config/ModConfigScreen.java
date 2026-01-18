package com.drypted.pvpTrainer.client.config;

import com.drypted.pvpTrainer.client.config.gui.ButtonWidget;
import com.drypted.pvpTrainer.client.config.gui.ScrollBoxWidget;
import com.drypted.pvpTrainer.client.hudOverlay.PVPLabels;
import com.drypted.pvpTrainer.client.hudOverlay.SharedConstants;
import com.drypted.pvpTrainer.client.utils.Colors;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

import static com.drypted.pvpTrainer.client.PvpTrainerClient.CONFIG;

class ModConfigScreen extends Screen
{
    private final Screen parent;
    private final ModConfig config;

    public static final int LABELS_PER_CORNER = 4;
    public static final int BUTTON_WIDTH = 80;
    public static final int BUTTON_HEIGHT = 20;
    public static final int SCREEN_MARGIN = 6;

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
            ScreenLabel top_left = new ScreenLabel(LabelType.MOVE_STATE);
            ScreenLabel top_right = new ScreenLabel(LabelType.MOVE_STATE);
            ScreenLabel bottom_left = new ScreenLabel(LabelType.MOVE_STATE);
            ScreenLabel bottom_right = new ScreenLabel(LabelType.MOVE_STATE);

            String top_left_button_text = __getLabelTextOf(ModConfig.LabelCorner.TOP_LEFT, i);
            top_left.getButton().setX(SCREEN_MARGIN);
            top_left.getButton().setY(top_left_cursor);
            top_left.getButton().setText(top_left_button_text);

            String top_right_button_text = __getLabelTextOf(ModConfig.LabelCorner.TOP_RIGHT, i);
            top_right.getButton().setX(SharedConstants.GetScreenW() - BUTTON_WIDTH - SCREEN_MARGIN);
            top_right.getButton().setY(top_right_cursor);
            top_right.getButton().setText(top_right_button_text);

            String bottom_left_button_text = __getLabelTextOf(ModConfig.LabelCorner.BOTTOM_LEFT, i);
            bottom_left.getButton().setX(SCREEN_MARGIN);
            bottom_left.getButton().setY(bottom_left_cursor - BUTTON_HEIGHT);
            bottom_left.getButton().setText(bottom_left_button_text);

            String bottom_right_button_text = __getLabelTextOf(ModConfig.LabelCorner.BOTTOM_RIGHT, i);
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

    private String __getLabelTextOf(ModConfig.LabelCorner corner, int index)
    {
        ModConfig.LabelConfig cfg = ModConfig.LabelConfig.getLabelConfigAt(corner, index);
        if (cfg == null)
        {
            return "None";
        }
        else
        {
            return cfg.name;
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

        final int scrollBoxWidth = 100;

        topLeftBox = ScrollBoxWidget.builder(
                        SCREEN_MARGIN + BUTTON_WIDTH + SCREEN_MARGIN,
                        SCREEN_MARGIN,
                        scrollBoxWidth,
                        top_left_cursor - SCREEN_MARGIN
                )
                .build();
        topRightBox = ScrollBoxWidget.builder(
                SharedConstants.GetScreenW() - SCREEN_MARGIN - BUTTON_WIDTH - SCREEN_MARGIN - scrollBoxWidth,
                SCREEN_MARGIN,
                scrollBoxWidth,
                top_right_cursor - SCREEN_MARGIN
        ).build();
        bottomLeftBox = ScrollBoxWidget.builder(
                SCREEN_MARGIN + BUTTON_WIDTH + SCREEN_MARGIN,
                bottom_left_cursor,
                scrollBoxWidth,
                SharedConstants.GetScreenH() - SCREEN_MARGIN - bottom_left_cursor
        ).build();
        bottomRightBox = ScrollBoxWidget.builder(
                SharedConstants.GetScreenW() - SCREEN_MARGIN - BUTTON_WIDTH - SCREEN_MARGIN - scrollBoxWidth,
                bottom_right_cursor,
                scrollBoxWidth,
                SharedConstants.GetScreenH() - SCREEN_MARGIN - bottom_right_cursor
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
            label.getButton().setOnClick((mEv, pressed) -> {
                selectedLabel = label;

                for (ScreenLabel otherLabel : all_labels)
                {
                    if (otherLabel != selectedLabel)
                    {
                        otherLabel.getButton().setPressed(false);
                        otherLabel.getBox().visible = false;
                    }
                }

                // show current box
                selectedLabel.getBox().visible = true;
            });
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
        doneButton.setOnClick((mouseButtonEvent, pressed) -> {
            PVPLabels.refreshHotbarKeys();
            // if (ModConfig.isValidConfig(config))
            if (true)
            {
                CONFIG = this.config;
                AutoConfig.getConfigHolder(ModConfig.class).setConfig(CONFIG);
                AutoConfig.getConfigHolder(ModConfig.class).save();
                onClose();
            }
            else
            {
                doneButton.setText("Invalid Config!");
                doneButton.setOutlineColor(Colors.RED);
                doneButton.setBackgroundColor(Colors.RED.withAlpha(64));
                doneButton.setHoverColor(Colors.RED.withAlpha(128));
            }

        });

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
        cancelButton.setOnClick((mouseButtonEvent, pressed) -> {
            // reload config from disk
            CONFIG = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
            PVPLabels.refreshHotbarKeys();
            onClose();
        });

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
}
