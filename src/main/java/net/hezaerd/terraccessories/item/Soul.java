package net.hezaerd.terraccessories.item;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.hezaerd.terraccessories.Terraccessories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;

public class Soul extends Item {
    public Soul(int count) {
        super(new OwoItemSettings()
                .group(Terraccessories.TERRACCESSORIES_GROUP)
                .maxCount(count)
                .rarity(Rarity.EPIC)
                .fireproof());
    }

    /* Glint */
    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}
