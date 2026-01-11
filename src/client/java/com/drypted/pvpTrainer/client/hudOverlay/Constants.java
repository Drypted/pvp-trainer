package com.drypted.pvpTrainer.client.hudOverlay;

import static com.drypted.pvpTrainer.client.PvpTrainerClient.CLIENT;

public class Constants
{
    // hotbar constants
    public static final int HOTBAR_WIDTH = 182;
    public static final int HOTBAR_HEIGHT = 22;
    public static final int HOTBAR_SLOT_WIDTH = HOTBAR_WIDTH / 9;
    public static final int HOTBAR_TEXT_PADDING = 3;

    public static int GetScreenW()
    {
        return CLIENT.getWindow().getGuiScaledWidth();
    }
    
    public static int GetScreenH()
    {
        return CLIENT.getWindow().getGuiScaledHeight();
    }
}
