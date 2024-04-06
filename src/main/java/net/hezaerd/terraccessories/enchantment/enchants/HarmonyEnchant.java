package net.hezaerd.terraccessories.enchantment.enchants;

import net.hezaerd.terraccessories.enchantment.TerraEnchantment;
import net.hezaerd.terraccessories.item.ModItem;
import net.hezaerd.terraccessories.item.RodOfDiscord;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class HarmonyEnchant extends TerraEnchantment {
    public HarmonyEnchant() {
        super(Enchantment.Rarity.RARE, EnchantmentTarget.BREAKABLE, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinPower(int level) {
        return 50; // impossible to get vanilla
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return false;
    }

    @Override
    public boolean isAvailableForRandomSelection() {
        return false;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof RodOfDiscord;
    }
}
