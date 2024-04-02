package net.hezaerd.terraccessories.registry;

import net.hezaerd.terraccessories.Terraccessories;
import net.hezaerd.terraccessories.common.LibMod;
import net.hezaerd.terraccessories.statusEffect.ModStatusEffect;
import net.hezaerd.terraccessories.statusEffect.effects.AccessoriesRegenerationEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;

public class StatusEffectsInit {

    // Non-instantaneous status effects
    public static ModStatusEffect ACCESSORIES_REGENERATION = new AccessoriesRegenerationEffect(StatusEffectCategory.BENEFICIAL, 0xc7106e, false);

    public static void init() {
        try {
            int registered = 0;

            for (Field field : StatusEffectsInit.class.getDeclaredFields()) {
                if (ModStatusEffect.class.isAssignableFrom(field.getType())) {
                    Identifier id = LibMod.id(field.getName().toLowerCase());
                    Registry.register(Registries.STATUS_EFFECT, id, (ModStatusEffect) field.get(null)).onRegister();

                    Terraccessories.LOGGER.debug("Registered status effect: " + id);
                    registered++;
                }
            }

            Terraccessories.LOGGER.info("Registered " + registered + " status effects.");
        }
        catch (IllegalAccessException e) {
            Terraccessories.LOGGER.error("Failed to register status effects: " + e.getMessage());
        }
    }
}
