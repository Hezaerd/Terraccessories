package net.hezaerd.terraccessories.items.trinkets;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.hezaerd.terraccessories.Terraccessories;
import net.hezaerd.terraccessories.registry.StatusEffectsInit;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class BandOfRegeneration extends TrinketItem {

    public BandOfRegeneration() {
        super(new OwoItemSettings().group(Terraccessories.TERRACCESSORIES_GROUP).maxCount(1));
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.terraccessories.band_of_regeneration.tooltip").formatted(Formatting.BLUE));
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (entity.getWorld().isClient()) {
            return;
        }

        if (entity instanceof PlayerEntity player) {
            StatusEffectInstance effect = player.getStatusEffect(StatusEffectsInit.ACCESSORIES_REGENERATION);

            if (effect != null) {
                int amplifier = effect.getAmplifier();
                player.addStatusEffect(new StatusEffectInstance(StatusEffectsInit.ACCESSORIES_REGENERATION, -1, amplifier + 1, false, false, false));
            } else {
                player.addStatusEffect(new StatusEffectInstance(StatusEffectsInit.ACCESSORIES_REGENERATION, -1, 0, false, false, false));
            }
        }
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (entity.getWorld().isClient()) {
            return;
        }

        if (entity instanceof PlayerEntity player) {
            StatusEffectInstance effect = player.getStatusEffect(StatusEffectsInit.ACCESSORIES_REGENERATION);

            if (effect != null) {
                int amplifier = effect.getAmplifier();

                player.removeStatusEffect(StatusEffectsInit.ACCESSORIES_REGENERATION);

                if (amplifier != 0) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffectsInit.ACCESSORIES_REGENERATION, -1, amplifier - 1, false, false, false));
                }
            }
        }
    }
}
