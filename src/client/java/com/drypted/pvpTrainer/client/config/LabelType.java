package com.drypted.pvpTrainer.client.config;

import static com.drypted.pvpTrainer.client.PvpTrainerClient.CONFIG;

public enum LabelType
{
    NONE(0, "-"),
    MOVE_STATE(1, CONFIG.moveStateLabelConfig.name),
    PITCH_ANGLE(2, CONFIG.pitchAngleLabelConfig.name),
    PRESSED_KEY(3, CONFIG.pressedKeyLabelConfig.name);

    private final int id;
    private final String name;

    LabelType(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public int getId()
    {
        return id;
    }
}