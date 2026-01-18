package com.drypted.pvpTrainer.client.hudOverlay;

import com.drypted.pvpTrainer.client.config.ModConfig;
import com.drypted.pvpTrainer.client.utils.Color;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;

import java.util.EnumMap;

import static com.drypted.pvpTrainer.client.PvpTrainerClient.CLIENT;
import static com.drypted.pvpTrainer.client.PvpTrainerClient.CONFIG;
import static com.drypted.pvpTrainer.client.hudOverlay.SharedConstants.*;

public final class PVPLabels
{
    // cache (for performance)
    private static final EnumMap<ModConfig.LabelCorner, Integer> labelsStackOffset = new EnumMap<>(ModConfig.LabelCorner.class);
    // hotbar keybinds
    private static final String[] hotbarKeybinds = new String[9];
    private static String lastPressedKey;
    private static String moveState;
    // pitch angle
    private static float lastPitch = Float.NaN;
    private static String cachedPitch = "";


    public static void init()
    {
        refreshHotbarKeys();
    }

    public static void tick(Minecraft client)
    {
        // poll current key once every tick
        long window = client.getWindow().handle();
        refreshLastKey(window);

        LocalPlayer player = client.player;
        if (player == null) return;

        moveState = //
                player.isCrouching() ? "Sneaking" : // else
                        player.isSprinting() ? "Sprinting" : "";
    }

    public static void render(GuiGraphics context, DeltaTracker tickCounter)
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

        labelsStackOffset.clear();

        if (CONFIG.moveStateLabelConfig.enabled)
        {
            drawLabel(context, moveState, CONFIG.moveStateLabelConfig);
        }

        if (CONFIG.pressedKeyLabelConfig.enabled)
        {
            drawLabel(context, lastPressedKey, CONFIG.pressedKeyLabelConfig);
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

    private static void refreshLastKey(long window)
    {
        // Keyboard Buttons
        for (int key = GLFW.GLFW_KEY_SPACE; key <= GLFW.GLFW_KEY_LAST; key++)
        {
            if (GLFW.glfwGetKey(window, key) == GLFW.GLFW_PRESS)
            {
                InputConstants.Key mcKey = InputConstants.Type.KEYSYM.getOrCreate(key);
                lastPressedKey = mcKey.getDisplayName().getString();
                return;
            }
        }

        // Check mouse buttons
        if (CONFIG.detectMouseButtons)
        {
            for (int button = GLFW.GLFW_MOUSE_BUTTON_1; button <= GLFW.GLFW_MOUSE_BUTTON_LAST; button++)
            {
                if (GLFW.glfwGetMouseButton(window, button) == GLFW.GLFW_PRESS)
                {
                    lastPressedKey = "Mouse " + button;
                    return;
                }
            }
        }

        lastPressedKey = "";
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

    public static void refreshHotbarKeys()
    {
        for (int i = 0; i < 9; i++)
        {
            hotbarKeybinds[i] = CLIENT.options.keyHotbarSlots[i].getTranslatedKeyMessage().getString();
        }
    }

    private static void drawLabel(GuiGraphics context, String text, ModConfig.LabelConfig labelConfig)
    {
        int offset = labelsStackOffset.getOrDefault(labelConfig.corner, 0);

        int margin = labelConfig.margin;
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || player.isCreative()) margin = labelConfig.creativeMargin;


        int boxWidth = CLIENT.font.width(text) + (labelConfig.padding * 2);
        int boxHeight = CLIENT.font.lineHeight + (labelConfig.padding * 2);

        int xPos;
        int yPos;

        switch (labelConfig.corner)
        {
            case TOP_LEFT ->
            {
                xPos = margin;
                yPos = margin + offset;
            }
            case TOP_RIGHT ->
            {
                xPos = GetScreenW() - boxWidth - margin;
                yPos = margin + offset;
            }
            case BOTTOM_LEFT ->
            {
                // - offset to stack upwards
                xPos = margin;
                yPos = GetScreenH() - boxHeight - margin - offset;
            }
            case BOTTOM_RIGHT ->
            {
                // - offset to stack upwards
                xPos = GetScreenW() - boxWidth - margin;
                yPos = GetScreenH() - boxHeight - margin - offset;
            }
            case ABOVE_HOTBAR ->
            {
                xPos = (GetScreenW() - boxWidth) / 2;
                yPos = GetScreenH() - HOTBAR_HEIGHT - boxHeight - margin - offset;
            }
            case CROSSHAIR ->
            {
                xPos = (GetScreenW() - boxWidth) / 2;
                yPos = (GetScreenH() + boxHeight) / 2 + margin + offset;
            }
            default -> throw new IllegalStateException();
        }

        Color backgroundColor = new Color(labelConfig.backgroundColor);
        backgroundColor.applyAlpha(labelConfig.backgroundColorOpacity);

        Color textColor = new Color(labelConfig.textColor);

        PVPRendererUtils.drawTextAbsolute(context, text, xPos, yPos, backgroundColor, textColor, labelConfig.padding, 1.0f);

        labelsStackOffset.put(labelConfig.corner, offset + boxHeight + labelConfig.stackGap);
    }

    private static void drawHotbar(GuiGraphics context)
    {
        int hotbarX = (GetScreenW() - HOTBAR_WIDTH) / 2;
        int hotbarY = GetScreenH() - HOTBAR_HEIGHT;

        Color backgroundColor = new Color(CONFIG.hotbar.backgroundColor);
        backgroundColor.applyAlpha(CONFIG.hotbar.backgroundColorOpacity);

        Color textColor = new Color(CONFIG.hotbar.textColor);

        for (int i = 0; i < 9; i++)
        {
            int baseX = hotbarX + (i * HOTBAR_SLOT_WIDTH) + HOTBAR_TEXT_PADDING;
            int baseY = hotbarY + HOTBAR_TEXT_PADDING;

            PVPRendererUtils.drawTextAbsolute(
                    context,
                    PVPLabels.hotbarKeybinds[i],
                    baseX,
                    baseY,
                    backgroundColor,
                    textColor,
                    2,
                    0.7f
            );
        }
    }
}
