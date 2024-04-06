package net.hezaerd.terraccessories.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class TerraEnchantment extends Enchantment {

    private boolean isRegistered;

    public TerraEnchantment onRegister() {
        this.isRegistered = true;
        return this;
    }

    public TerraEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }
}
