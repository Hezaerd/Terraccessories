package net.hezaerd.terraccessories.items.trinkets;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.hezaerd.terraccessories.Terraccessories;
import net.hezaerd.terraccessories.statuseffect.ModStatusEffect;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
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
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (isUsable(user)) {
            return super.use(world, user, hand);
        }

        return TypedActionResult.fail(user.getStackInHand(hand));
    }

    private boolean isUsable(PlayerEntity player) {
        StatusEffectInstance effect = player.getStatusEffect(ModStatusEffect.ACCESSORIES_REGENERATION);

        if (effect != null) {
            int amplifier = effect.getAmplifier();

            if (amplifier >= 0) {
                player.sendMessage(Text.translatable("item.terraccessories.accessory.unusable").formatted(Formatting.RED), true);
                return false;
            }
        }

        return true;
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (entity.getWorld().isClient()) {
            return;
        }

        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;

            player.addStatusEffect(new StatusEffectInstance(ModStatusEffect.ACCESSORIES_REGENERATION, -1, 0));
        }
    }

    @Override
    public void onUnequip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (entity.getWorld().isClient()) {
            return;
        }

        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;

            player.removeStatusEffect(ModStatusEffect.ACCESSORIES_REGENERATION);
        }
    }
}
