package com.drypted.pvpTrainer.client.config;

import com.drypted.pvpTrainer.client.utils.Colors;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import org.jetbrains.annotations.Nullable;

import static com.drypted.pvpTrainer.client.PvpTrainerClient.CONFIG;
import static com.drypted.pvpTrainer.client.PvpTrainerClient.MOD_ID;

@Config(name = MOD_ID)
public class ModConfig implements ConfigData
{
    public boolean enableHud = true;
    public boolean showInCreative = true;
    public LabelConfig moveStateLabelConfig = LabelConfig.createMoveStateLabelDefaultConfig();
    public LabelConfig pressedKeyLabelConfig = LabelConfig.createDefaultConfig("Key Press");
    public LabelConfig pitchAngleLabelConfig = LabelConfig.createPitchAngleDefaultConfig();
    public boolean detectMouseButtons = true;
    public Hotbar hotbar = new Hotbar();

    public enum LabelCorner
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
        public String name;
        public boolean enabled = true;
        // corner defines the corner
        public LabelCorner corner;
        // position index defines the position in the stack (0 = first, 1 = second, etc)
        public int positionIndex;
        public int textColor = Colors.iWHITE;
        public int backgroundColor = Colors.iBLACK;
        public int backgroundColorOpacity;
        public int padding = 5;
        public int margin = 6;
        public int stackGap = 6;

        public int creativeMargin = 6;

        public static LabelConfig createDefaultConfig(String name)
        {
            LabelConfig cfg = new LabelConfig();
            cfg.name = name;
            cfg.corner = LabelCorner.TOP_LEFT;
            cfg.positionIndex = 0;
            cfg.backgroundColorOpacity = 128;
            cfg.padding = 5;
            cfg.margin = 6;
            cfg.creativeMargin = 6;
            return cfg;
        }

        public static LabelConfig createMoveStateLabelDefaultConfig()
        {
            LabelConfig cfg = new LabelConfig();
            cfg.name = "Move State";
            cfg.corner = LabelCorner.TOP_LEFT;
            cfg.positionIndex = 1;
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
            cfg.name = "Pitch Angle";
            cfg.corner = LabelCorner.TOP_LEFT;
            cfg.positionIndex = 2;
            cfg.backgroundColorOpacity = 0;
            cfg.padding = 0;
            cfg.margin = 24;
            cfg.creativeMargin = 0;
            return cfg;
        }

        @Nullable
        public static LabelConfig getLabelConfigAt(LabelCorner corner, int index)
        {
            LabelConfig moveStateLabelConfig = CONFIG.moveStateLabelConfig;
            if (moveStateLabelConfig.corner == corner && moveStateLabelConfig.positionIndex == index)
            {
                return moveStateLabelConfig;
            }
            LabelConfig pressedKeyLabelConfig = CONFIG.pressedKeyLabelConfig;
            if (pressedKeyLabelConfig.corner == corner && pressedKeyLabelConfig.positionIndex == index)
            {
                return pressedKeyLabelConfig;
            }
            LabelConfig pitchAngleLabelConfig = CONFIG.pitchAngleLabelConfig;
            if (pitchAngleLabelConfig.corner == corner && pitchAngleLabelConfig.positionIndex == index)
            {
                return pitchAngleLabelConfig;
            }
            return null;
        }
    }

    public static class Hotbar
    {
        public boolean showHotbarKeybinds = true;
        public int textColor = Colors.iWHITE;
        public int backgroundColor = Colors.iBLACK;
        public int backgroundColorOpacity = 128;
    }

    public static boolean isValidConfig(ModConfig modConfig)
    {
        // Ensure position indices are unique per corner
        for (LabelCorner corner : LabelCorner.values())
        {
            boolean[] usedIndices = new boolean[ModConfigScreen.LABELS_PER_CORNER];
            LabelConfig[] labels = {modConfig.moveStateLabelConfig, modConfig.pressedKeyLabelConfig, modConfig.pitchAngleLabelConfig};
            for (LabelConfig label : labels)
            {
                if (label.corner == corner && label.enabled)
                {
                    int index = label.positionIndex;
                    if (index < 0 || index >= usedIndices.length || usedIndices[index])
                    {
                        return false; // invalid index or duplicate
                    }
                    usedIndices[index] = true;
                }
            }
        }
        return true;
    }
}

