package com.drypted.pvpTrainer.client.utils;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

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
// PIERCING_SHOT    -> Arrow
// MULTISHOT_SHOT   -> 3 Arrows
public enum PVPAttackType
{
    NONE(Items.AIR),
    // sword
    CRITICAL_HIT(Items.DIAMOND_SWORD),
    SWEEPING_ATTACK(Items.WOODEN_SWORD),
    WEAK_ATTACK(Items.AIR),
    NORMAL_ATTACK(Items.IRON_SWORD),
    SPRINT_ATTACK(Items.GOLDEN_SWORD),
    // axe
    STUN_ATTACK(Items.SHIELD),
    // mace
    MACE_ATTACK(Items.MACE);

    private final Item icon;

    PVPAttackType(Item icon)
    {
        this.icon = icon;
    }


    public ItemStack getIconStack()
    {
        if (this.icon == Items.AIR)
        {
            return ItemStack.EMPTY;
        }
        return new ItemStack(this.icon);
    }
}