package com.drypted.pvpTrainer.client.mixin;

import com.drypted.pvpTrainer.client.renderer.PVPAttack;
import com.drypted.pvpTrainer.client.utils.PVPAttackType;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin
{
    @Shadow
    public abstract float getSpeed();

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci)
    {
        PVPAttack.ShowAttackTimer--;
    }

    @Inject(method = "attack", at = @At("HEAD"))
    private void onAttackDetection(Entity target, CallbackInfo ci)
    {
        // reset timer
        PVPAttack.ShowAttackTimer = PVPAttack.SHOW_ATTACK_TIMER_MAX;

        // RECONSTRUCTING VANILLA LOGIC TO DETECT TYPE
        Player player = (Player) (Object) this;
        ItemStack heldItem = player.getMainHandItem();

        boolean isSprinting = player.isSprinting();
        boolean isFalling = player.fallDistance > 0.0F && !player.onGround() && !player.onClimbable() && !player.isInWater() && !player.hasEffect(
                MobEffects.BLINDNESS) && !player.isPassenger();

        float cooldown = player.getAttackStrengthScale(0.5F);
        // WEAK_ATTACK
        if (cooldown < 0.9F)
        {
            PVPAttack.AttackType = PVPAttackType.WEAK_ATTACK;
        }
        // NORMAL_ATTACK
        else if (cooldown > 0.9F)
        {
            PVPAttack.AttackType = PVPAttackType.NORMAL_ATTACK;
        }

        // MACE_ATTACK
        if (heldItem.getItem() == Items.MACE && isFalling && player.fallDistance > 1.5)
        {
            PVPAttack.AttackType = PVPAttackType.MACE_ATTACK;
        }
        // CRITICAL_HIT
        else if (isFalling && !isSprinting)
        {
            PVPAttack.AttackType = PVPAttackType.CRITICAL_HIT;
        }
        // SPRINT_ATTACK
        else if (isSprinting && !isFalling)
        {
            PVPAttack.AttackType = PVPAttackType.SPRINT_ATTACK;
        }
        // SWEEP
        else if (!isSprinting && !isFalling && player.onGround() && player.getItemInHand(InteractionHand.MAIN_HAND)
                .is(ItemTags.SWORDS))
        {
            double approximateSpeedSq = player.getKnownMovement().horizontalDistanceSqr();
            double maxSpeedForSweepAttack = (double) this.getSpeed() * (double) 2.5F;
            if (approximateSpeedSq < Mth.square(maxSpeedForSweepAttack))
            {
                PVPAttack.AttackType = PVPAttackType.SWEEPING_ATTACK;
            }
        }
        // AXE STUN
        else if (heldItem.getItem() instanceof AxeItem && target instanceof LivingEntity livingTarget)
        {
            if (livingTarget.isBlocking())
            {
                PVPAttack.AttackType = PVPAttackType.STUN_ATTACK;
            }
        }
    }
}
