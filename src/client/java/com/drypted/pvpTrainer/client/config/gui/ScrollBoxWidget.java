package com.drypted.pvpTrainer.client.config.gui;

import com.drypted.pvpTrainer.client.utils.Color;
import com.drypted.pvpTrainer.client.utils.Colors;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ScrollBoxWidget extends AbstractWidget
{
    private static final int SCROLLBAR_WIDTH = 6;

    private final List<WidgetEntry> children = new ArrayList<>();
    private final int margin;
    private final int spacing;
    private final Color bgColor;
    private final Color outlineColor;
    private final Color scrollbarColor;
    private final Color scrollerColor;

    private double scrollAmount;
    private boolean scrolling;

    public ScrollBoxWidget(int x, int y, int width, int height, int margin, int spacing, Color bgColor, Color outlineColor, Color scrollbarColor, Color scrollerColor)
    {
        super(x, y, width, height, Component.empty());
        this.margin = margin;
        this.bgColor = bgColor;
        this.spacing = spacing;
        this.outlineColor = outlineColor;
        this.scrollbarColor = scrollbarColor;
        this.scrollerColor = scrollerColor;
    }

    /* Children */

    public void addChildRow(AbstractWidget widget, int id)
    {
        int contentY = margin;
        if (!children.isEmpty())
        {
            WidgetEntry lastEntry = children.getLast();
            contentY = lastEntry.contentY + lastEntry.widget.getHeight() + spacing;
        }
        this.children.add(new WidgetEntry(widget, margin, contentY, id));
    }

    public void addChildAt(AbstractWidget widget, int contentX, int contentY, int id)
    {
        this.children.add(new WidgetEntry(widget, contentX, contentY, id));
    }

    @Nullable
    public AbstractWidget getChild(int id)
    {
        for (WidgetEntry entry : children)
        {
            if (entry.id == id)
            {
                return entry.widget;
            }
        }
        return null;
    }

    public List<AbstractWidget> getAllChildren()
    {
        List<AbstractWidget> widgets = new ArrayList<>();
        for (WidgetEntry entry : children)
        {
            widgets.add(entry.widget);
        }
        return widgets;
    }

    public void removeChild(AbstractWidget widget)
    {
        children.removeIf(entry -> entry.widget == widget);
    }

    /* Scroll Logic */

    private int contentHeight()
    {
        int max = 0;
        for (WidgetEntry e : children)
        {
            max = Math.max(max, e.contentY + e.widget.getHeight());
        }
        return max + margin;
    }

    private double scrollRate()
    {
        return 10.0;
    }

    private int maxScrollAmount()
    {
        return Math.max(0, contentHeight() - height);
    }

    private boolean scrollbarVisible()
    {
        return maxScrollAmount() > 0;
    }

    private int scrollerHeight()
    {
        return Mth.clamp((int) ((float) (height * height) / (float) contentHeight()), 32, height - 8);
    }

    private int scrollBarX()
    {
        return getRight() - SCROLLBAR_WIDTH;
    }

    private int scrollBarY()
    {
        return Math.max(getY(), (int) scrollAmount * (height - scrollerHeight()) / maxScrollAmount() + getY());
    }

    private void setScrollAmount(double amount)
    {
        this.scrollAmount = Mth.clamp(amount, 0.0, (double) maxScrollAmount());
    }

    private boolean isOverScrollbar(double mouseX, double mouseY)
    {
        return mouseX >= scrollBarX() && mouseX <= scrollBarX() + SCROLLBAR_WIDTH && mouseY >= getY() && mouseY < getBottom();
    }

    /* Render */

    @Override
    protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float delta)
    {
        layoutChildren();

        final int x1 = getX();
        final int y1 = getY();
        final int x2 = x1 + width;
        final int y2 = y1 + height;

        drawBackground(g, x1, y1, x2, y2);
        drawChildren(g, mouseX, mouseY, delta, x1, y1, x2, y2);
        renderCustomScrollbar(g, mouseX, mouseY);
    }

    private void layoutChildren()
    {
        int scroll = (int) scrollAmount;

        for (WidgetEntry e : children)
        {
            e.widget.setPosition(getX() + e.contentX, getY() + e.contentY - scroll);
        }
    }

    private void drawBackground(GuiGraphics g, int x1, int y1, int x2, int y2)
    {
        // Background
        g.fill(x1 + 1, y1 + 1, x2 - 1, y2 - 1, bgColor.asInt());

        // Border
        g.fill(x1, y1, x2, y1 + 1, outlineColor.asInt());
        g.fill(x1, y2 - 1, x2, y2, outlineColor.asInt());
        g.fill(x1, y1, x1 + 1, y2, outlineColor.asInt());
        g.fill(x2 - 1, y1, x2, y2, outlineColor.asInt());
    }

    private void drawChildren(GuiGraphics g, int mouseX, int mouseY, float delta, int x1, int y1, int x2, int y2)
    {
        // Clip
        g.enableScissor(x1 + 1, y1 + 1, x2 - 1, y2 - 1);

        for (WidgetEntry e : children)
        {
            AbstractWidget child = e.widget;
            if (!child.visible) continue;
            child.render(g, mouseX, mouseY, delta);
        }

        g.disableScissor();
    }

    private void renderCustomScrollbar(GuiGraphics g, int mouseX, int mouseY)
    {
        if (!scrollbarVisible())
        {
            return;
        }

        int x = scrollBarX();
        int h = scrollerHeight();
        int y = scrollBarY();

        // scrollbar background
        g.fill(x, getY(), x + SCROLLBAR_WIDTH, getBottom(), scrollbarColor.asInt());
        // scroller
        g.fill(x, y, x + SCROLLBAR_WIDTH, y + h, scrollerColor.asInt());
        // line
        g.fill(x, getY(), x + 1, getBottom(), outlineColor.asInt());

        if (isOverScrollbar(mouseX, mouseY))
        {
            g.requestCursor(this.scrolling ? CursorTypes.RESIZE_NS : CursorTypes.POINTING_HAND);
        }
    }

    /* Input */

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY)
    {
        if (!visible)
        {
            return false;
        }
        setScrollAmount(scrollAmount - scrollY * scrollRate());
        return true;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent e, boolean doubleClick)
    {
        // Check scrollbar first
        if (scrollbarVisible() && isValidClickButton(e.buttonInfo()) && isOverScrollbar(e.x(), e.y()))
        {
            this.scrolling = true;
            return true;
        }

        // Check children (in reverse order for proper z-order)
        for (int i = children.size() - 1; i >= 0; i--)
        {
            AbstractWidget w = children.get(i).widget;
            if (w.mouseClicked(e, doubleClick)) return true;
        }

        return false;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent mouseButtonEvent, double deltaX, double deltaY)
    {
        if (scrolling)
        {
            if (mouseButtonEvent.y() < getY())
            {
                setScrollAmount(0.0);
            }
            else if (mouseButtonEvent.y() > getBottom())
            {
                setScrollAmount(maxScrollAmount());
            }
            else
            {
                double f = Math.max(1, maxScrollAmount());
                int i = scrollerHeight();
                double g = Math.max(1.0, f / (height - i));
                setScrollAmount(scrollAmount + deltaY * g);
            }
            return true;
        }

        return super.mouseDragged(mouseButtonEvent, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent e)
    {
        this.scrolling = false;

        for (WidgetEntry entry : children)
        {
            entry.widget.mouseReleased(e);
        }
        return false;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narration)
    {
    }

    /* Builder */

    public static Builder builder(int x, int y, int width, int height)
    {
        return new Builder(x, y, width, height);
    }

    public static final class Builder
    {
        private final int x;
        private final int y;
        private final int width;
        private final int height;
        private int margin = 4;
        private int spacing = 0;

        private Color bgColor = Colors.BLACK.withHalfAlpha();
        private Color outlineColor = Colors.WHITE;
        private Color scrollbarColor = Colors.CLEAR;
        private Color scrollerColor = Colors.WHITE;

        private Builder(int x, int y, int width, int height)
        {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public Builder margin(int margin)
        {
            this.margin = margin;
            return this;
        }

        public Builder spacing(int spacing)
        {
            this.spacing = spacing;
            return this;
        }

        public Builder bgColor(Color color)
        {
            this.bgColor = color;
            return this;
        }

        public Builder outlineColor(Color color)
        {
            this.outlineColor = color;
            return this;
        }

        public Builder scrollbarColor(Color color)
        {
            this.scrollbarColor = color;
            return this;
        }

        public Builder scrollerColor(Color color)
        {
            this.scrollerColor = color;
            return this;
        }

        public ScrollBoxWidget build()
        {
            return new ScrollBoxWidget(
                    x,
                    y,
                    width,
                    height,
                    margin,
                    spacing,
                    bgColor,
                    outlineColor,
                    scrollbarColor,
                    scrollerColor
            );
        }
    }

    private record WidgetEntry(AbstractWidget widget, int contentX, int contentY, int id)
    {
        @Override
        public boolean equals(Object obj)
        {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            WidgetEntry other = (WidgetEntry) obj;
            return this.id == other.id;
        }
    }
}