package com.drypted.pvpTrainer.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

import static com.drypted.pvpTrainer.client.renderer.Constants.*;
import static com.drypted.pvpTrainer.client.renderer.PVPHudScreen.ScreenH;
import static com.drypted.pvpTrainer.client.renderer.PVPHudScreen.ScreenW;


public class PVPRendererUtils
{
    public static final int DEFAULT_PADDING = 5;
    private static final Font FONT = Minecraft.getInstance().font;

    public static void drawTextRelative(GuiGraphics g, String text, int posXPercent, int posYPercent, int bgColor, int fgColor)
    {
        drawTextRelative(g, text, posXPercent, posYPercent, bgColor, fgColor, 100, DEFAULT_PADDING, 1.0f);
    }

    public static void drawTextRelative(GuiGraphics g, String text, int posXPercent, int posYPercent, int bgColor, int fgColor, int bgOpacity)
    {
        drawTextRelative(g, text, posXPercent, posYPercent, bgColor, fgColor, bgOpacity, DEFAULT_PADDING, 1.0f);
    }

    public static void drawTextRelative(GuiGraphics g, String text, int posXPercent, int posYPercent, int bgColor, int fgColor, int bgOpacity, int padding, float scale)
    {
        int screenW = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenH = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        int textW = FONT.width(text);
        int textH = FONT.lineHeight;

        int boxW = (int) ((textW * scale) + padding * 2);
        int boxH = (int) ((textH * scale) + padding * 2);

        // convert relative to absolute
        float posX = (posXPercent * 0.01f) * (screenW - boxW);
        float posY = (posYPercent * 0.01f) * (screenH - boxH);

        drawTextAbsolute(g, text, (int) posX, (int) posY, bgColor, fgColor, bgOpacity, padding, scale);
    }

    public static void drawTextAbsolute(GuiGraphics g, String text, int posX, int posY, int bgColor, int fgColor, int bgOpacity, int padding, float scale)
    {
        // Background 50% opaque
        bgColor = (bgOpacity << 24) | (bgColor & 0xFFFFFF);

        // ignore alpha value
        fgColor = (fgColor & 0xFFFFFF);
        // make fully opaque
        fgColor = (0xFF << 24) | fgColor;

        if (text.isEmpty()) return;

        int textW = FONT.width(text);
        int textH = FONT.lineHeight;

        int boxW = (int) ((textW * scale) + padding * 2);
        int boxH = (int) ((textH * scale) + padding * 2);

        int boxX1 = (int) (posX);
        int boxY1 = (int) (posY);
        int boxX2 = (int) (posX + boxW);
        int boxY2 = (int) (posY + boxH);

        // Fill main rectangle body
        g.fill(boxX1 + 1, boxY1 + 1, boxX2 - 1, boxY2 - 1, bgColor);

        // Fill pixel “rounded” corners
        g.fill(boxX1, boxY1 + 1, boxX1 + 1, boxY2 - 1, bgColor); // left strip
        g.fill(boxX2 - 1, boxY1 + 1, boxX2, boxY2 - 1, bgColor); // right strip
        g.fill(boxX1 + 1, boxY1, boxX2 - 1, boxY1 + 1, bgColor); // top strip
        g.fill(boxX1 + 1, boxY2 - 1, boxX2 - 1, boxY2, bgColor); // bottom strip

        // Optionally cut 1 pixel at corners for “rounded” look
        g.fill(boxX1, boxY1, boxX1 + 1, boxY1 + 1, 0x00000000); // top-left
        g.fill(boxX2 - 1, boxY1, boxX2, boxY1 + 1, 0x00000000); // top-right
        g.fill(boxX1, boxY2 - 1, boxX1 + 1, boxY2, 0x00000000); // bottom-left
        g.fill(boxX2 - 1, boxY2 - 1, boxX2, boxY2, 0x00000000); // bottom-right

        // Draw scaled text
        g.pose().pushMatrix();
        g.pose().scale(scale, scale, g.pose());

        int scaledX = (int) ((posX + padding) / scale);
        int scaledY = (int) ((posY + padding) / scale);

        g.drawString(FONT, text, scaledX, scaledY, fgColor, false);

        g.pose().popMatrix();
    }

    public static void drawHollowRect(GuiGraphics g, int x1, int y1, int x2, int y2, int innerThickness, int color)
    {
        // Top
        g.fill(x1, y1, x2, y1 + innerThickness, color);
        // Bottom
        g.fill(x1, y2 - innerThickness, x2, y2, color);
        // Left
        g.fill(x1, y1 + innerThickness, x1 + innerThickness, y2 - innerThickness, color);
        // Right
        g.fill(x2 - innerThickness, y1 + innerThickness, x2, y2 - innerThickness, color);
    }

    public static void drawHotbarOutlineRect(GuiGraphics g, int slot, int color)
    {
        int slotX1 = (ScreenW / 2) - (HOTBAR_WIDTH / 2) + (slot * HOTBAR_SLOT_WIDTH);
        int slotY1 = ScreenH - HOTBAR_HEIGHT;
        int slotX2 = slotX1 + HOTBAR_SLOT_WIDTH + 2;
        int slotY2 = slotY1 + HOTBAR_HEIGHT;

        drawHollowRect(g, slotX1, slotY1, slotX2, slotY2, 3, color);
    }
}
