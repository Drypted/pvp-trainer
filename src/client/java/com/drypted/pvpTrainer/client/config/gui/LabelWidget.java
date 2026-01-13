package com.drypted.pvpTrainer.client.config.gui;

import com.drypted.pvpTrainer.client.utils.Color;
import com.drypted.pvpTrainer.client.utils.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class LabelWidget extends AbstractWidget
{
    private static final Font FONT = Minecraft.getInstance().font;

    private final String text;
    private final Color bgColor;
    private final Color fgColor;
    private final int padding;
    private final float scale;
    private final int highlightColor = Colors.WHITE.asInt();


    private boolean pressed = false;

    // callback
    private Consumer<MouseButtonEvent> onClickCallback = (mouseButtonEvent) -> {
    };

    public LabelWidget(int x, int y, String text, Color bgColor, Color fgColor, int padding, float scale)
    {
        super(x, y, 0, 0, Component.empty());
        this.text = text;
        this.bgColor = bgColor;
        this.fgColor = fgColor;
        this.padding = padding;
        this.scale = scale;

        int textW = FONT.width(text);
        int textH = FONT.lineHeight;

        this.width = (int) ((textW * scale) + padding * 2);
        this.height = (int) ((textH * scale) + padding * 2);
    }

    // create
    public static LabelWidget create(int x, int y, String text)
    {
        return new LabelWidget(x, y, text, Colors.BLACK.withAlpha(128), Colors.WHITE, 5, 1.0f);
    }

    public static LabelWidget create(int x, int y, String text, Color bgColor, Color fgColor, int padding, float scale)
    {
        return new LabelWidget(x, y, text, bgColor, fgColor, padding, scale);
    }

    @Override
    protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float delta)
    {
        fgColor.makeOpaque();

        // getX/Y = x/y top left corner pos
        final int startPosX = getX();
        final int startPosY = getY();
        final int __endPosX = startPosX + width;
        final int __endPosY = startPosY + height;

        // Main body; inset by 1 pixel to allow for outline
        g.fill(startPosX + 1, startPosY + 1, __endPosX - 1, __endPosY - 1, bgColor.asInt());


        // stripes
        g.fill(startPosX, startPosY + 1, startPosX + 1, __endPosY - 1, bgColor.asInt()); // left
        g.fill(__endPosX - 1, startPosY + 1, __endPosX, __endPosY - 1, bgColor.asInt()); // right
        g.fill(startPosX + 1, startPosY, __endPosX - 1, startPosY + 1, bgColor.asInt()); // top
        g.fill(startPosX + 1, __endPosY - 1, __endPosX - 1, __endPosY, bgColor.asInt()); // top

        // Hover effect: highlight and outline
        if (this.isHovered)
        {
            // stripes
            g.fill(startPosX, startPosY + 1, startPosX + 1, __endPosY - 1, highlightColor); // left
            g.fill(__endPosX - 1, startPosY + 1, __endPosX, __endPosY - 1, highlightColor); // right
            g.fill(startPosX + 1, startPosY, __endPosX - 1, startPosY + 1, highlightColor); // top
            g.fill(startPosX + 1, __endPosY - 1, __endPosX - 1, __endPosY, highlightColor); // top

            // corner pixels
            g.fill(startPosX + 1, startPosY + 1, startPosX + 2, startPosY + 2, highlightColor); // top-left
            g.fill(__endPosX - 2, startPosY + 1, __endPosX - 1, startPosY + 2, highlightColor); // top-right
            g.fill(startPosX + 1, __endPosY - 2, startPosX + 2, __endPosY - 1, highlightColor); // bottom-left
            g.fill(__endPosX - 2, __endPosY - 2, __endPosX - 1, __endPosY - 1, highlightColor); // bottom-right
        }

        int textX = (int) ((getX() + padding) / scale);
        int textY = (int) ((getY() + padding) / scale);

        // Clicked effect: move text down/right by 1 pixel
        if (pressed)
        {
            textY += 1;
        }

        // Scaled text
        g.pose().pushMatrix();
        g.pose().scale(scale, scale, g.pose());

        g.drawString(FONT, text, textX, textY, fgColor.asInt(), false);

        g.pose().popMatrix();
    }

    @Override
    public void onClick(MouseButtonEvent mouseButtonEvent, boolean clicked)
    {
        super.onClick(mouseButtonEvent, clicked);
        onClickCallback.accept(mouseButtonEvent);
        this.pressed = true;
    }

    @Override
    public void onRelease(MouseButtonEvent mouseButtonEvent)
    {
        super.onRelease(mouseButtonEvent);
        this.pressed = false;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput)
    {
    }

    public void setOnClickCallback(Consumer<MouseButtonEvent> onClickCallback)
    {
        this.onClickCallback = onClickCallback;
    }
}
