package com.drypted.pvpTrainer.client.config;

import static com.drypted.pvpTrainer.client.PvpTrainerClient.CONFIG;

public enum LabelType
{
    NONE("-"),
    MOVE_STATE(CONFIG.moveStateLabelConfig.name),
    PITCH_ANGLE(CONFIG.pitchAngleLabelConfig.name),
    PRESSED_KEY(CONFIG.pressedKeyLabelConfig.name);

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