package com.drypted.pvpTrainer.client.hudOverlay;

import com.drypted.pvpTrainer.client.utils.PVPAttackType;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import static com.drypted.pvpTrainer.client.hudOverlay.Constants.GetScreenH;
import static com.drypted.pvpTrainer.client.hudOverlay.Constants.GetScreenW;

public final class PVPAttackDetector
{
    private static final int SHOW_ATTACK_TIMER_MAX = 20;
    private static PVPAttackType attackType = PVPAttackType.NONE;
    private static int showAttackTimer = SHOW_ATTACK_TIMER_MAX;

    public static void init()
    {
    }

    public static void tick(Minecraft client)
    {
        if (showAttackTimer >= 0) showAttackTimer--;
    }

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker)
    {
        if (showAttackTimer > 0)
        {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;

            ItemStack stack = attackType.getIconStack();

            if (stack.isEmpty()) return;

            // Crosshair center
            int centerX = GetScreenW() / 2;
            int centerY = GetScreenH() / 2;

            // Render left of crosshair
            int x = centerX - 24;
            int y = centerY - 8;

            guiGraphics.renderItem(stack, x, y);
        }
    }

    public static void detectAttackType(@NotNull Player player, Entity target)
    {
        // reset timer
        showAttackTimer = SHOW_ATTACK_TIMER_MAX;

        // RECONSTRUCTING VANILLA LOGIC TO DETECT TYPE
        ItemStack heldItem = player.getMainHandItem();
        boolean isSprinting = player.isSprinting();
        boolean isFalling = isFalling(player);

        float cooldown = player.getAttackStrengthScale(0.5F);
        // WEAK_ATTACK
        if (cooldown < 0.9F)
        {
            attackType = PVPAttackType.WEAK_ATTACK;
        }
        // NORMAL_ATTACK
        else if (cooldown > 0.9F)
        {
            attackType = PVPAttackType.NORMAL_ATTACK;
        }

        // MACE_ATTACK
        if (isMaceAttack(player, heldItem, isFalling))
        {
            attackType = PVPAttackType.MACE_ATTACK;
        }
        // CRITICAL_HIT
        else if (isCrit(isFalling, isSprinting))
        {
            attackType = PVPAttackType.CRITICAL_HIT;
        }
        // SPRINT_ATTACK
        else if (isKnockbackAttack(isSprinting, isFalling))
        {
            attackType = PVPAttackType.SPRINT_ATTACK;
        }
        // SWEEP
        else if (isSweep(player, isSprinting, isFalling))
        {
            double approximateSpeedSq = player.getKnownMovement().horizontalDistanceSqr();
            double maxSpeedForSweepAttack = (double) player.getSpeed() * (double) 2.5F;
            if (approximateSpeedSq < Mth.square(maxSpeedForSweepAttack))
            {
                attackType = PVPAttackType.SWEEPING_ATTACK;
            }
        }
        // AXE STUN
        else if (heldItem.getItem() instanceof AxeItem && target instanceof LivingEntity livingTarget)
        {
            if (livingTarget.isBlocking())
            {
                attackType = PVPAttackType.STUN_ATTACK;
            }
        }
    }

    private static boolean isFalling(@NotNull Player player)
    {
        return player.fallDistance > 0.0F && !player.onGround() && !player.onClimbable() && !player.isInWater() &&
                !player.hasEffect(MobEffects.BLINDNESS) && !player.isPassenger();
    }

    private static boolean isSweep(@NotNull Player player, boolean isSprinting, boolean isFalling)
    {
        return !isSprinting && !isFalling && player.onGround() && player.getItemInHand(InteractionHand.MAIN_HAND)
                .is(ItemTags.SWORDS);
    }

    private static boolean isKnockbackAttack(boolean isSprinting, boolean isFalling)
    {
        return isSprinting && !isFalling;
    }

    private static boolean isCrit(boolean isFalling, boolean isSprinting)
    {
        return isFalling && !isSprinting;
    }

    private static boolean isMaceAttack(@NotNull Player player, ItemStack heldItem, boolean isFalling)
    {
        // make sure player has not equipped elytra
        if (player.isFallFlying())
        {
            return false;
        }
        return heldItem.getItem() == Items.MACE && isFalling && player.fallDistance > 1.5;
    }
}
