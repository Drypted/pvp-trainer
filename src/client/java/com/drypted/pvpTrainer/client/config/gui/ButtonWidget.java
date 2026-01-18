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

    private String text;
    private final int padding;
    private final boolean isRounded;
    private final Color bgColor;
    private final Color fgColor;
    private final Color hoverColor;
    private final Color clickColor;
    private boolean isToggleButton;
    private boolean isTextCentered;
    private boolean pressed;

    // callback
    private Consumer<MouseButtonEvent> onClickCallback = (e) -> {
    };

    public ButtonWidget(int x, int y, int padding, boolean isRounded, String text, Color bgColor, Color fgColor, Color hoverColor, Color clickColor)
    {
        super(x, y, 0, 0, Component.empty());
        this.text = text;
        this.padding = padding;
        this.isRounded = isRounded;
        this.bgColor = bgColor;
        this.fgColor = fgColor;
        this.hoverColor = hoverColor;
        this.clickColor = clickColor;

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

        int outlineColor = this.pressed ? clickColor.asInt() : this.isHovered ? hoverColor.asInt() : bgColor.asInt();

        if (isRounded)
        {
            // Main body; inset by 1 pixel to allow for outline
            g.fill(startPosX + 1, startPosY + 1, endPosX - 1, endPosY - 1, bgColor.asInt());

            // stripes
            g.fill(startPosX, startPosY + 1, startPosX + 1, endPosY - 1, outlineColor); // left
            g.fill(endPosX - 1, startPosY + 1, endPosX, endPosY - 1, outlineColor); // right
            g.fill(startPosX + 1, startPosY, endPosX - 1, startPosY + 1, outlineColor); // top
            g.fill(startPosX + 1, endPosY - 1, endPosX - 1, endPosY, outlineColor); // top

            // corner pixels
            if (this.isHovered() || this.pressed)
            {
                g.fill(startPosX + 1, startPosY + 1, startPosX + 2, startPosY + 2, outlineColor); // top-left
                g.fill(endPosX - 2, startPosY + 1, endPosX - 1, startPosY + 2, outlineColor); // top-right
                g.fill(startPosX + 1, endPosY - 2, startPosX + 2, endPosY - 1, outlineColor); // bottom-left
                g.fill(endPosX - 2, endPosY - 2, endPosX - 1, endPosY - 1, outlineColor); // bottom-right
            }
        }
        else
        {
            // Main body
            g.fill(startPosX, startPosY, endPosX, endPosY, bgColor.asInt());

            // outline
            if (this.isHovered() || this.pressed)
            {
                g.fill(startPosX, startPosY + 1, startPosX + 1, endPosY, outlineColor); // left; see y only
                g.fill(startPosX, startPosY, endPosX - 1, startPosY + 1, outlineColor); // top; see x only
                g.fill(endPosX - 1, startPosY, endPosX, endPosY - 1, outlineColor); // right; see y only
                g.fill(startPosX + 1, endPosY - 1, endPosX, endPosY, outlineColor); // bottom; see x only
            }
        }

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
        g.drawString(FONT, text, textX, textY, fgColor.asInt(), false);
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

    public String getText()
    {
        return this.text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    // HOVER & CLICK TYPE
    // can be outline or fill

    public enum HoverType
    {
        OUTLINE,
        FILL
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
        private int width = 0;
        private int height = 0;
        private final String text;

        private int padding = 5;
        private boolean isRounded = false;
        private Color bgColor = Colors.BLACK.withHalfAlpha();
        private Color fgColor = Colors.WHITE;
        private Color hoverColor = Colors.WHITE;
        private Color clickColor = Colors.YELLOW;
        private boolean textCentered = false;
        private boolean toggleButton = false;
        private boolean pressed = false;
        private Consumer<MouseButtonEvent> onClick = (e) -> {
        };

        private Builder(int x, int y, String text)
        {
            this.x = x;
            this.y = y;
            this.text = text;
        }

        public Builder width(int width)
        {
            this.width = width;
            return this;
        }

        public Builder height(int height)
        {
            this.height = height;
            return this;
        }

        public Builder padding(int padding)
        {
            this.padding = padding;
            return this;
        }

        public Builder isRounded(boolean isRounded)
        {
            this.isRounded = isRounded;
            return this;
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

        public Builder hoverColor(Color hoverColor)
        {
            this.hoverColor = hoverColor;
            return this;
        }

        public Builder clickColor(Color clickColor)
        {
            this.clickColor = clickColor;
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
            ButtonWidget button = new ButtonWidget(x, y, padding, isRounded, text, bgColor, fgColor, hoverColor, clickColor);

            button.setTextCentered(textCentered);
            button.setToggleButton(toggleButton);
            button.setOnClick(onClick);
            button.pressed = this.pressed;

            if (this.width > 0) button.setWidth(this.width);
            if (this.height > 0) button.setHeight(this.height);

            return button;
        }
    }
}
