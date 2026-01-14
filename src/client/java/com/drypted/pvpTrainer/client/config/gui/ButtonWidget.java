package com.drypted.pvpTrainer.client.config.gui;

import com.drypted.pvpTrainer.client.PvpTrainerClient;
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

public class ButtonWidget extends AbstractWidget
{
    private static final Font FONT = Minecraft.getInstance().font;

    private final String text;
    private final Color bgColor;
    private final Color fgColor;
    private final int padding;
    private final int hoverColor = Colors.WHITE.asInt();
    private final int clickColor = Colors.YELLOW.asInt();
    private boolean isToggleButton = true;
    private boolean isTextCentered = false;
    private boolean pressed = false;

    // callback
    private Consumer<MouseButtonEvent> onClickCallback;

    public ButtonWidget(int x, int y, String text, Color bgColor, Color fgColor, int padding)
    {
        super(x, y, 0, 0, Component.empty());
        this.text = text;
        this.bgColor = bgColor;
        this.fgColor = fgColor;
        this.padding = padding;

        int textW = FONT.width(text);
        int textH = FONT.lineHeight;

        this.width = textW + (padding * 2);
        this.height = textH + (padding * 2);
    }

    @Override
    protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float delta)
    {
        fgColor.makeOpaque();

        // getX/Y = x/y top left corner pos
        final int startPosX = getX();
        final int startPosY = getY();
        final int endPosX = startPosX + width;
        final int endPosY = startPosY + height;

        // Main body; inset by 1 pixel to allow for outline
        g.fill(startPosX + 1, startPosY + 1, endPosX - 1, endPosY - 1, bgColor.asInt());

        int outlineColor = this.pressed ? clickColor : this.isHovered ? hoverColor : bgColor.asInt();

        // stripes
        g.fill(startPosX, startPosY + 1, startPosX + 1, endPosY - 1, outlineColor); // left
        g.fill(endPosX - 1, startPosY + 1, endPosX, endPosY - 1, outlineColor); // right
        g.fill(startPosX + 1, startPosY, endPosX - 1, startPosY + 1, outlineColor); // top
        g.fill(startPosX + 1, endPosY - 1, endPosX - 1, endPosY, outlineColor); // top

        // corner pixels
        g.fill(startPosX + 1, startPosY + 1, startPosX + 2, startPosY + 2, outlineColor); // top-left
        g.fill(endPosX - 2, startPosY + 1, endPosX - 1, startPosY + 2, outlineColor); // top-right
        g.fill(startPosX + 1, endPosY - 2, startPosX + 2, endPosY - 1, outlineColor); // bottom-left
        g.fill(endPosX - 2, endPosY - 2, endPosX - 1, endPosY - 1, outlineColor); // bottom-right

        int textX;
        int textY;

        if (isTextCentered)
        {
            final int textWidth = FONT.width(text);
            final int textHeight = FONT.lineHeight;

            textX = this.getX() + (this.getWidth() / 2) - (textWidth / 2);
            textY = this.getY() + (this.getHeight() / 2) - (textHeight / 2);
        }
        else
        {
            textX = getX() + padding;
            textY = getY() + padding;
        }

        // Scaled text
        g.drawString(FONT, text, textX, textY, this.pressed ? clickColor : fgColor.asInt(), false);
    }

    @Override
    public void onClick(MouseButtonEvent mouseButtonEvent, boolean doubleClick)
    {
        if (isToggleButton) this.pressed = !this.pressed;
        else this.pressed = true;
        PvpTrainerClient.LOGGER.info("Clicked Button: " + this.text);
    }

    @Override
    public void onRelease(MouseButtonEvent mouseButtonEvent)
    {
        if (!isToggleButton) this.pressed = false;
        if (isMouseInButton(mouseButtonEvent)) onClickCallback.accept(mouseButtonEvent);
    }

    private boolean isMouseInButton(MouseButtonEvent mouse)
    {
        // mouseX = mouse.x();
        // mouseY = mouse.y();

        // startX = this.getX();
        // startY = this.getY();
        // endX   = this.getRight();
        // endY   = this.getBottom();

        // return (mouseX >= startX && mouseX <= endX) // x axis check
        //         && (mouseY >= startY && mouse.y() <= endY); // y axis check

        return (mouse.x() >= this.getX() && mouse.x() <= this.getRight()) // x axis check
                && (mouse.y() >= this.getY() && mouse.y() <= this.getBottom()); // y axis check
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput)
    {
    }

    // GETTERS & SETTERS --------------------------------------------------------------------------

    public void setOnClick(Consumer<MouseButtonEvent> onClickCallback)
    {
        this.onClickCallback = onClickCallback;
    }

    public boolean isTextCentered()
    {
        return isTextCentered;
    }

    public void setTextCentered(boolean textCentered)
    {
        this.isTextCentered = textCentered;
    }

    public void setToggleButton(boolean toggleButton)
    {
        isToggleButton = toggleButton;
    }

    public boolean isPressed()
    {
        return pressed;
    }

    public void setPressed(boolean pressed)
    {
        this.pressed = pressed;
    }

    // BUILDER ------------------------------------------------------------------------------------

    public static Builder builder(int x, int y, String text)
    {
        return new Builder(x, y, text);
    }

    public static final class Builder
    {
        private final int x;
        private final int y;
        private final String text;

        private Color bgColor = Colors.BLACK.withAlpha(128);
        private Color fgColor = Colors.WHITE;
        private int padding = 5;
        private boolean textCentered = false;
        private boolean toggleButton = true;
        private boolean pressed = false;
        private Consumer<MouseButtonEvent> onClick = (e) -> {
        };

        private Builder(int x, int y, String text)
        {
            this.x = x;
            this.y = y;
            this.text = text;
        }

        public Builder bgColor(Color bgColor)
        {
            this.bgColor = bgColor;
            return this;
        }

        public Builder fgColor(Color fgColor)
        {
            this.fgColor = fgColor;
            return this;
        }

        public Builder padding(int padding)
        {
            this.padding = padding;
            return this;
        }

        public Builder centeredText(boolean centered)
        {
            this.textCentered = centered;
            return this;
        }

        public Builder toggleButton(boolean toggle)
        {
            this.toggleButton = toggle;
            return this;
        }

        public Builder pressed(boolean pressed)
        {
            this.pressed = pressed;
            return this;
        }

        public Builder onClick(Consumer<MouseButtonEvent> onClick)
        {
            this.onClick = onClick;
            return this;
        }

        public ButtonWidget build()
        {
            ButtonWidget button = new ButtonWidget(x, y, text, bgColor, fgColor, padding);

            button.setTextCentered(textCentered);
            button.setToggleButton(toggleButton);
            button.setOnClick(onClick);
            button.pressed = this.pressed;

            return button;
        }
    }
}
