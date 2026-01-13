package com.drypted.pvpTrainer.client.config;

import com.drypted.pvpTrainer.client.utils.Colors;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import static com.drypted.pvpTrainer.client.PvpTrainerClient.MOD_ID;

@Config(name = MOD_ID)
public class ModConfig implements ConfigData
{
    public static final ModConfig DEFAULT = new ModConfig();
    public boolean enableHud = true;
    public boolean showInCreative = true;
    public LabelConfig moveStateLabelConfig = LabelConfig.createMoveStateLabelDefaultConfig();
    public LabelConfig pressedKeyLabelConfig = LabelConfig.createDefaultConfig();
    public LabelConfig pitchAngleLabelConfig = LabelConfig.createPitchAngleDefaultConfig();
    public boolean detectMouseButtons = true;
    public Hotbar hotbar = new Hotbar();

    public enum LabelPosition
    {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        ABOVE_HOTBAR,
        CROSSHAIR
    }

    public static class LabelConfig
    {
        public boolean enabled = true;
        public LabelPosition position;
        public int textColor = Colors.iWHITE; // white
        public int backgroundColor = Colors.iBLACK; // black
        public int backgroundColorOpacity;
        public int padding = 5;
        public int margin = 6;
        public int stackGap = 6;

        public int creativeMargin = 6;

        public static LabelConfig createDefaultConfig()
        {
            LabelConfig cfg = new LabelConfig();
            cfg.position = LabelPosition.TOP_LEFT;
            cfg.backgroundColorOpacity = 128;
            cfg.padding = 5;
            cfg.margin = 6;
            cfg.creativeMargin = 6;
            return cfg;
        }

        public static LabelConfig createMoveStateLabelDefaultConfig()
        {
            LabelConfig cfg = new LabelConfig();
            cfg.position = LabelPosition.ABOVE_HOTBAR;
            cfg.textColor = Colors.iYELLOW; // yellow
            cfg.backgroundColorOpacity = 64;
            cfg.padding = 4;
            cfg.margin = 42;
            cfg.creativeMargin = 28;
            return cfg;
        }

        public static LabelConfig createPitchAngleDefaultConfig()
        {
            LabelConfig cfg = new LabelConfig();
            cfg.position = LabelPosition.CROSSHAIR;
            cfg.backgroundColorOpacity = 0;
            cfg.padding = 0;
            cfg.margin = 24;
            cfg.creativeMargin = 0;
            return cfg;
        }
    }

    public static class Hotbar
    {
        public boolean showHotbarKeybinds = true;
        public int textColor = Colors.iWHITE;
        public int backgroundColor = Colors.iBLACK;
        public int backgroundColorOpacity = 128;
    }
}

