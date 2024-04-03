package net.hezaerd.terraccessories.statuseffect;

import net.hezaerd.terraccessories.Terraccessories;
import net.hezaerd.terraccessories.utils.LibMod;
import net.hezaerd.terraccessories.statuseffect.effects.AccessoriesRegenerationEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;

public class ModStatusEffect {

    // Non-instantaneous status effects
    public static TerraStatusEffect ACCESSORIES_REGENERATION = new AccessoriesRegenerationEffect(StatusEffectCategory.BENEFICIAL, 0xc7106e, false);

    public static void init() {
        try {
            int registered = 0;

            for (Field field : ModStatusEffect.class.getDeclaredFields()) {
                if (TerraStatusEffect.class.isAssignableFrom(field.getType())) {
                    Identifier id = LibMod.id(field.getName().toLowerCase());
                    Registry.register(Registries.STATUS_EFFECT, id, (TerraStatusEffect) field.get(null)).onRegister();

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
