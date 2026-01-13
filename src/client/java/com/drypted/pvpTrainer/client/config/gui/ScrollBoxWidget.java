package com.drypted.pvpTrainer.client.config.gui;

import com.drypted.pvpTrainer.client.utils.Color;
import com.drypted.pvpTrainer.client.utils.Colors;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractScrollArea;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class ScrollBoxWidget extends AbstractScrollArea
{
    private final List<AbstractWidget> children = new ArrayList<>();
    private final Color bgColor;
    private final int padding;

    public ScrollBoxWidget(int x, int y, int width, int height, Color bgColor, int padding)
    {
        super(x, y, width, height, Component.empty());
        this.bgColor = bgColor;
        this.padding = padding;
    }

    /* ---------------- Children ---------------- */

    public void addChild(AbstractWidget widget)
    {
        children.add(widget);
    }

    public void removeChild(AbstractWidget widget)
    {
        children.remove(widget);
    }

    @Override
    protected int contentHeight()
    {
        int max = 0;
        for (AbstractWidget w: children)
        {
            max = Math.max(max, w.getY() + w.getHeight() - getY());
        }
        return max + padding;
    }

    @Override
    protected double scrollRate()
    {
        return 10.0; // pixels per mouse wheel tick
    }

    /* ---------------- Render ---------------- */

    @Override
    protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float delta)
    {
        final int x1 = getX();
        final int y1 = getY();
        final int x2 = x1 + width;
        final int y2 = y1 + height;

        // Background
        g.fill(x1 + 1, y1 + 1, x2 - 1, y2 - 1, bgColor.asInt());

        // Border
        g.fill(x1, y1, x2, y1 + 1, Colors.WHITE.asInt());
        g.fill(x1, y2 - 1, x2, y2, Colors.WHITE.asInt());
        g.fill(x1, y1, x1 + 1, y2, Colors.WHITE.asInt());
        g.fill(x2 - 1, y1, x2, y2, Colors.WHITE.asInt());

        // Enable scissor / clip for scrolling
        g.enableScissor(x1 + 1, y1 + 1, x2 - 1, y2 - 1);

        // Render children with scroll offset
        for (AbstractWidget child: children)
        {
            int oldY = child.getY();
            child.setY(oldY - (int) scrollAmount());
            child.render(g, mouseX, mouseY, delta);
            child.setY(oldY);
        }

        g.disableScissor();

        // Render custom scrollbar
    }

    /* ---------------- Mouse / Scroll ---------------- */

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput)
    {

    }

    @Override
    public void onClick(MouseButtonEvent mouseButtonEvent, boolean bl)
    {
        AbstractWidget widget = getWidgetAt(mouseButtonEvent);
        if (widget == null)
        {
            return;
        }

        widget.mouseClicked(mouseButtonEvent, bl);
    }

    @Override
    public void onRelease(MouseButtonEvent mouseButtonEvent)
    {
        AbstractWidget widget = getWidgetAt(mouseButtonEvent);
        if (widget == null)
        {
            return;
        }

        widget.mouseReleased(mouseButtonEvent);
    }

    private AbstractWidget getWidgetAt(MouseButtonEvent mEv)
    {
        int clipTop = getY() + 1;
        int clipBottom = getY() + height - 1;
        int scroll = (int) scrollAmount();

        for (AbstractWidget child: children)
        {
            int childX = child.getX();
            int childY = child.getY() - scroll;
            int childW = child.getWidth();
            int childH = child.getHeight();

            // Outside visible scroll area
            if (childY + childH < clipTop || childY > clipBottom)
            {
                continue;
            }

            // check if element inside box
            if (mEv.x() >= childX && mEv.x() < childX + childW && mEv.y() >= childY && mEv.y() < childY + childH)
            {
                return child;
            }
        }

        return null;
    }
}
