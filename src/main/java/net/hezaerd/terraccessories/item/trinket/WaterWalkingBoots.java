package net.hezaerd.terraccessories.item.trinket;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.hezaerd.terraccessories.Terraccessories;
import net.hezaerd.terraccessories.utils.BootsUtils;
import net.hezaerd.terraccessories.utils.Log;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class WaterWalkingBoots extends TrinketItem {

    public WaterWalkingBoots() {
        super(new OwoItemSettings().group(Terraccessories.TERRACCESSORIES_GROUP).maxCount(1));
        //Avoid crash on loading screen Ratio GEEK_Raphazel

    }



    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.tick(stack, slot, entity);
        BootsUtils.walkOnWater((PlayerEntity) entity);
        BootsUtils.walkOnLava((PlayerEntity) entity);
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.onUnequip(stack, slot, entity);

        entity.setNoGravity(false);
    }
}
