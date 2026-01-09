package com.drypted.pvpTrainer.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;

public class Constants
{
    // font
    public static final Minecraft CLIENT = Minecraft.getInstance();
    public static final Font FONT = CLIENT.font;    // screen size
    // hotbar constants
    public static final int HOTBAR_WIDTH = 182;
    public static final int HOTBAR_HEIGHT = 22;
    public static final int HOTBAR_SLOT_WIDTH = HOTBAR_WIDTH / 9;
    public static final int HOTBAR_TEXT_PADDING = 3;
}
