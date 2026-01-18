package com.drypted.pvpTrainer.client.config;

import com.drypted.pvpTrainer.client.config.gui.ButtonWidget;
import com.drypted.pvpTrainer.client.config.gui.ScrollBoxWidget;

public class ScreenLabel
{
    private LabelType label = LabelType.NONE;

    private ScrollBoxWidget box;
    private final ButtonWidget button = ButtonWidget.builder(0, 0, "")
            .width(ModConfigScreen.BUTTON_WIDTH)
            .height(ModConfigScreen.BUTTON_HEIGHT)
            .centeredText(true)
            .toggleButton(true)
            .build();
    private final ModConfig.LabelCorner corner;
    private final int index;

    public ScreenLabel(ModConfig.LabelCorner corner, int index)
    {
        this.corner = corner;
        this.index = index;
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

    public ScrollBoxWidget getBox()
    {
        return box;
    }

    public void setBox(ScrollBoxWidget box)
    {
        this.box = box;
    }

    public ModConfig.LabelCorner getCorner()
    {
        return corner;
    }

    public int getIndex()
    {
        return index;
    }
}