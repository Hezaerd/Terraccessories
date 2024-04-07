package net.hezaerd.terraccessories.enchantment;


import com.google.gson.JsonObject;
import net.hezaerd.terraccessories.enchantment.enchants.HarmonyEnchant;
import net.hezaerd.terraccessories.utils.LibMod;
import net.hezaerd.terraccessories.utils.Log;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModEnchantment {

    // Enchantments
    public static TerraEnchantment HARMONY = new HarmonyEnchant();

    public static void init() {
        try {
            int registered = 0;

            for (Field field : ModEnchantment.class.getDeclaredFields()) {
                if (TerraEnchantment.class.isAssignableFrom(field.getType())) {
                    Identifier id = LibMod.id(field.getName().toLowerCase());
                    Registry.register(Registries.ENCHANTMENT, id, (TerraEnchantment) field.get(null)).onRegister();

                    Log.d("Registered enchantment: " + id);
                    registered++;
                }
            }

            Log.i("Registered " + registered + " enchantments.");
        } catch (IllegalAccessException e) {
            Log.e("Failed to register enchantments: " + e.getMessage());
        }
    }

    public static Map<Enchantment, Integer> ParseEnchantment(JsonObject enchantmentName) {
        Map<Enchantment, Integer> enchant = new HashMap<>();
        enchantmentName.entrySet().forEach(entry -> {
            int level = entry.getValue() == null ? 1 : entry.getValue().getAsInt();
            enchant.put(Registries.ENCHANTMENT.get(new Identifier(entry.getKey())), level);
        });

        return enchant;
    }
}
