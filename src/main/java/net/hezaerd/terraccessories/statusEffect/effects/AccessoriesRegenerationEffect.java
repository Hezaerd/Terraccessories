package net.hezaerd.terraccessories.statusEffect.effects;

import net.hezaerd.terraccessories.statusEffect.ModStatusEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class AccessoriesRegenerationEffect extends ModStatusEffect {

    public AccessoriesRegenerationEffect(StatusEffectCategory type, int color, boolean isInstant) {
        super(type, color, isInstant);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity player) {
            if (player.getHealth() < player.getMaxHealth()) {
                player.heal(1);
            }
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        int i = 50 >> amplifier;
        if (i > 0) {
            return duration % i == 0;
        } else {
            return true;
        }
    }
}
