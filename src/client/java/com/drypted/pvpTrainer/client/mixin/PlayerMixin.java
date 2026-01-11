package com.drypted.pvpTrainer.client.mixin;

import com.drypted.pvpTrainer.client.hudOverlay.PVPAttackDetector;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin
{
    @Inject(method = "attack", at = @At("HEAD"))
    private void onAttack(Entity target, CallbackInfo ci)
    {
        Player player = (Player) (Object) this;
        PVPAttackDetector.detectAttackType(player, target);
    }
}
