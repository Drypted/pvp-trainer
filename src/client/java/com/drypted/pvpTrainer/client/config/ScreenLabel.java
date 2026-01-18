package com.drypted.pvpTrainer.client.config;

import com.drypted.pvpTrainer.client.config.gui.ButtonWidget;
import com.drypted.pvpTrainer.client.config.gui.ScrollBoxWidget;

public class ScreenLabel
{
    private LabelType label;

    private ScrollBoxWidget box;
    private final ButtonWidget button = ButtonWidget.builder(0, 0, "")
            .width(ModConfigScreen.BUTTON_WIDTH)
            .height(ModConfigScreen.BUTTON_HEIGHT)
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

    public void setBox(ScrollBoxWidget box)
    {
        this.box = box;
    }

    public ScrollBoxWidget getBox()
    {
        return box;
    }
}