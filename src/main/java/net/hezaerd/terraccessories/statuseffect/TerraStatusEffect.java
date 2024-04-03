package net.hezaerd.terraccessories.statuseffect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class TerraStatusEffect extends StatusEffect {

    private boolean instant;
    private boolean isRegistered;

    public TerraStatusEffect(StatusEffectCategory type, int color, boolean isInstant) {
        super(type, color);
        this.instant = isInstant;
    }

    @Override
    public boolean isInstant() {
        return instant;
    }

    @Override
    public boolean canApplyUpdateEffect(int remainingTicks, int level) {
        if(isInstant()) {
            return true;
        }
        return remainingTicks % 20 == 0;
    }

    protected boolean canApplyEffect(int remainingTicks, int level) {
        if(!isInstant()) {
            Thread.dumpStack();
        }
        return false;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (isInstant()) {
            applyInstantEffect(null, null, entity, amplifier, 1.0d);
        }
    }

    public TerraStatusEffect onRegister() {
        this.isRegistered = true;
        return this;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void onEffectRemoved(LivingEntity entity) {
    }

}