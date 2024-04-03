package net.hezaerd.terraccessories.recipe;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModRecipe {
    public static void register() {
        Registry.register(Registries.RECIPE_TYPE, TinkererWorkshopRecipe.Type.ID, TinkererWorkshopRecipe.Type.INSTANCE);
        Registry.register(Registries.RECIPE_SERIALIZER, TinkererWorkshopRecipe.Serializer.ID, TinkererWorkshopRecipe.Serializer.INSTANCE);
    }
}
