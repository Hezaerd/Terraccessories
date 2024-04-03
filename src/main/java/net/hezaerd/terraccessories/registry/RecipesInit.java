package net.hezaerd.terraccessories.registry;

import net.hezaerd.terraccessories.recipe.TinkererWorkshopRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class RecipesInit {
    public static void register() {
        Registry.register(Registries.RECIPE_TYPE, TinkererWorkshopRecipe.Type.ID, TinkererWorkshopRecipe.Type.INSTANCE);
        Registry.register(Registries.RECIPE_SERIALIZER, TinkererWorkshopRecipe.Serializer.ID, TinkererWorkshopRecipe.Serializer.INSTANCE);
    }
}
