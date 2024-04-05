package net.hezaerd.terraccessories.statuseffect.effects;

import net.hezaerd.terraccessories.statuseffect.TerraStatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class ChaosEffect extends TerraStatusEffect {
    public ChaosEffect(StatusEffectCategory type, int color, boolean isInstant) {
        super(type, color, isInstant);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(net.minecraft.entity.LivingEntity entity, int amplifier) {
    }
    
}