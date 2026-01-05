package com.drypted.pvpTrainer.client.renderer;

import com.drypted.pvpTrainer.client.config.ModConfig.LabelConfig;
import com.drypted.pvpTrainer.client.config.ModConfig.LabelPosition;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;

import java.util.EnumMap;

import static com.drypted.pvpTrainer.client.PvpTrainerClient.CONFIG;
import static com.drypted.pvpTrainer.client.renderer.Constants.*;

public final class PVPHudScreen
{
    // font
    public static final Minecraft CLIENT = Minecraft.getInstance();
    public static final Font FONT = CLIENT.font;
    // cache (for performance)
    // hotbar keybinds
    private static final String[] hotbarKeybinds = new String[9];
    private static final EnumMap<LabelPosition, Integer> labelsStackOffset = new EnumMap<>(LabelPosition.class);
    // screen size
    public static int ScreenW;
    public static int ScreenH;
    // pitch angle
    private static float lastPitch = Float.NaN;
    private static String cachedPitch = "";


    public static void clientStartInit()
    {
        refreshHotbarKeys();
    }

    public static void refreshHotbarKeys()
    {
        for (int i = 0; i < 9; i++)
        {
            hotbarKeybinds[i] = CLIENT.options.keyHotbarSlots[i].getTranslatedKeyMessage().getString();
        }
    }

    public static void render(GuiGraphics context, DeltaTracker tickCounter, String lastKey)
    {
        // if disabled; RETURN
        if (!CONFIG.enableHud) return;

        // if F1 pressed; RETURN
        Minecraft client = Minecraft.getInstance();
        if (client.options.hideGui) return;

        // player null check
        Player player = client.player;
        if (player == null) return;

        // if creative mode and creative gui disabled; RETURN
        if (!CONFIG.showInCreative && player.isCreative()) return;

        // update screen size
        ScreenW = client.getWindow().getGuiScaledWidth();
        ScreenH = client.getWindow().getGuiScaledHeight();

        labelsStackOffset.clear();

        // movement state
        String moveState = //
                player.isCrouching() ? "Sneaking ..." : // else
                        player.isSprinting() ? "Sprinting ..." : "";

        if (CONFIG.moveStateLabelConfig.enabled)
        {
            drawLabel(context, moveState, CONFIG.moveStateLabelConfig);
        }

        if (CONFIG.pressedKeyLabelConfig.enabled)
        {
            drawLabel(context, lastKey, CONFIG.pressedKeyLabelConfig);
        }

        if (CONFIG.pitchAngleLabelConfig.enabled)
        {
            drawLabel(context, getPitchText(player.getXRot()), CONFIG.pitchAngleLabelConfig);
        }

        // don't draw hotbar in spectator mode
        if (CONFIG.hotbar.showHotbarKeybinds && !player.isSpectator())
        {
            drawHotbar(context);
        }
    }

    private static String getPitchText(float pitch)
    {
        // round to 1 decimal place
        float rounded = Math.round(pitch * 10.0f) / 10.0f;
        // cache result string
        if (rounded != lastPitch)
        {
            lastPitch = rounded;
            cachedPitch = Float.toString(rounded);
        }
        return cachedPitch;
    }

    private static void drawLabel(GuiGraphics context, String text, LabelConfig labelConfig)
    {
        int offset = labelsStackOffset.getOrDefault(labelConfig.position, 0);

        int boxWidth = FONT.width(text) + (labelConfig.padding * 2);
        int boxHeight = FONT.lineHeight + (labelConfig.padding * 2);

        int xPos;
        int yPos;

        switch (labelConfig.position)
        {
            case TOP_LEFT ->
            {
                xPos = labelConfig.margin;
                yPos = labelConfig.margin + offset;
            }
            case TOP_RIGHT ->
            {
                xPos = ScreenW - boxWidth - labelConfig.margin;
                yPos = labelConfig.margin + offset;
            }
            case BOTTOM_LEFT ->
            {
                // - offset to stack upwards
                xPos = labelConfig.margin;
                yPos = ScreenH - boxHeight - labelConfig.margin - offset;
            }
            case BOTTOM_RIGHT ->
            {
                // - offset to stack upwards
                xPos = ScreenW - boxWidth - labelConfig.margin;
                yPos = ScreenH - boxHeight - labelConfig.margin - offset;
            }
            case ABOVE_HOTBAR ->
            {
                xPos = (ScreenW - boxWidth) / 2;
                yPos = ScreenH - HOTBAR_HEIGHT - boxHeight - labelConfig.margin - offset;
            }
            default -> throw new IllegalStateException();
        }

        PVPRendererUtils.drawTextAbsolute(
                context,
                text,
                xPos,
                yPos,
                labelConfig.backgroundColor,
                labelConfig.textColor,
                labelConfig.backgroundColorOpacity,
                labelConfig.padding,
                1.0f
        );

        labelsStackOffset.put(labelConfig.position, offset + boxHeight + labelConfig.stackGap);
    }

    private static void drawHotbar(GuiGraphics context)
    {
        int hotbarX = (ScreenW - HOTBAR_WIDTH) / 2;
        int hotbarY = ScreenH - HOTBAR_HEIGHT;

        for (int i = 0; i < 9; i++)
        {
            int baseX = hotbarX + (i * HOTBAR_SLOT_WIDTH) + HOTBAR_TEXT_PADDING;
            int baseY = hotbarY + HOTBAR_TEXT_PADDING;

            PVPRendererUtils.drawTextAbsolute(
                    context,
                    hotbarKeybinds[i],
                    baseX,
                    baseY,
                    CONFIG.hotbar.backgroundColor,
                    CONFIG.hotbar.textColor,
                    CONFIG.hotbar.backgroundColorOpacity,
                    2,
                    0.7f
            );
        }
    }
}
