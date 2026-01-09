package com.drypted.pvpTrainer.client.renderer;

import com.drypted.pvpTrainer.client.utils.PVPAttackType;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class PVPAttack
{
    public static final int SHOW_ATTACK_TIMER_MAX = 20;
    public static PVPAttackType AttackType = PVPAttackType.NONE;
    public static int ShowAttackTimer = SHOW_ATTACK_TIMER_MAX;

    public static void init()
    {
    }

    public static void tick(Minecraft client)
    {
        ShowAttackTimer--;
    }

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker)
    {
        // draw attack type
        if (PVPAttack.ShowAttackTimer > 0)
        {
            // show icons for each attack type, left of the crosshair
            // NONE             -> No icon
            // CRITICAL_HIT     -> Diamond Sword
            // SWEEPING_ATTACK  -> Wooden Sword
            // WEAK_ATTACK      -> No icon
            // NORMAL_ATTACK    -> Iron Sword
            // SPRINT_ATTACK    -> Gold Sword
            // STUN_ATTACK      -> Shield
            // MACE_ATTACK      -> Mace
            // BOW_SHOT         -> Bow
            // CROSSBOW_SHOT    -> Crossbow
            // PIERCING_SHOT    -> 3 Arrows

            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null)
                return;

            ItemStack stack = switch (PVPAttack.AttackType)
            {
                case CRITICAL_HIT -> new ItemStack(Items.DIAMOND_SWORD);
                case SWEEPING_ATTACK -> new ItemStack(Items.WOODEN_SWORD);
                case NORMAL_ATTACK -> new ItemStack(Items.IRON_SWORD);
                case SPRINT_ATTACK -> new ItemStack(Items.GOLDEN_SWORD);
                case STUN_ATTACK -> new ItemStack(Items.SHIELD);
                case MACE_ATTACK -> new ItemStack(Items.MACE);
                case BOW_SHOT -> new ItemStack(Items.BOW);
                case CROSSBOW_SHOT -> new ItemStack(Items.CROSSBOW);
                case PIERCING_SHOT -> new ItemStack(Items.ARROW);
                case WEAK_ATTACK, NONE -> ItemStack.EMPTY;
            };

            if (stack.isEmpty())
                return;

            int screenWidth = mc.getWindow().getGuiScaledWidth();
            int screenHeight = mc.getWindow().getGuiScaledHeight();

            // Crosshair center
            int centerX = screenWidth / 2;
            int centerY = screenHeight / 2;

            // Render left of crosshair
            int x = centerX - 24;
            int y = centerY - 8;

            guiGraphics.renderItem(stack, x, y);
        }
    }
}
