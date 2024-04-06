package net.hezaerd.terraccessories.enchantment.enchants;

import net.hezaerd.terraccessories.enchantment.TerraEnchantment;
import net.hezaerd.terraccessories.item.ModItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class HarmonyEnchant extends TerraEnchantment {
    public HarmonyEnchant() {
        super(Enchantment.Rarity.RARE, EnchantmentTarget.BOW, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinPower(int level) {
        return 50; // impossible to get vanilla
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        Item item = stack.getItem();
        return super.isAcceptableItem(stack) || item == ModItem.ROD_OF_DISCORD;
    }
}
