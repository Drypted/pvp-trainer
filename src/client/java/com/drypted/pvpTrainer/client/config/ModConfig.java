package com.drypted.pvpTrainer.client.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import static com.drypted.pvpTrainer.client.PvpTrainerClient.MOD_ID;

@Config(name = MOD_ID)
public class ModConfig implements ConfigData
{
    public boolean enableHud = true;
    public boolean showInCreative = true;
    public LabelConfig pitchAngleLabelConfig = new LabelConfig(LabelPosition.ABOVE_HOTBAR, 0, 0, 16);
    public LabelConfig moveStateLabelConfig = new LabelConfig(LabelPosition.TOP_LEFT, 128);
    public LabelConfig pressedKeyLabelConfig = new LabelConfig(LabelPosition.TOP_LEFT, 128);
    public boolean detectMouseButtons = true;
    public Hotbar hotbar = new Hotbar();

    public enum LabelPosition
    {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, ABOVE_HOTBAR
    }

    public static class LabelConfig
    {
        public boolean enabled = true;
        public LabelPosition position;
        public int textColor = 0xFFFFFF;
        public int backgroundColor = 0x000000;
        public int backgroundColorOpacity;
        public int padding = 5;
        public int margin = 6;
        public int stackGap = 6;

        LabelConfig(LabelPosition position, int backgroundColorOpacity)
        {
            this.position = position;
            this.backgroundColorOpacity = backgroundColorOpacity;
        }

        LabelConfig(LabelPosition position, int backgroundColorOpacity, int padding, int margin)
        {
            this.position = position;
            this.backgroundColorOpacity = backgroundColorOpacity;
            this.padding = padding;
            this.margin = margin;
        }
    }

    public static class Hotbar
    {
        public boolean showHotbarKeybinds = true;
        public int textColor = 0xFFFFFF;
        public int backgroundColor = 0x000000;
        public int backgroundColorOpacity = 128;
    }

    public static final ModConfig DEFAULT = new ModConfig();
}

