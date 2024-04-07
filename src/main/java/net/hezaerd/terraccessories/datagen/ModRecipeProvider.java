package net.hezaerd.terraccessories.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.hezaerd.terraccessories.block.ModBlock;
import net.hezaerd.terraccessories.item.ModItem;
import net.hezaerd.terraccessories.utils.LibMod;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlock.TINKERER_WORKSHOP, 1)
                .pattern("WWW")
                .pattern("PPP")
                .pattern("P P")
                .input('W', Items.RED_WOOL)
                .input('P', ItemTags.PLANKS)
                .criterion(hasItem(Items.RED_WOOL), conditionsFromItem(Items.RED_WOOL))
                .offerTo(exporter, LibMod.id("tinkerer_workshop"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItem.SOUL_OF_ELEMENTS, 1)
                .input(ModItem.SOUL_OF_FRIGHT)
                .input(ModItem.SOUL_OF_MIGHT)
                .input(ModItem.SOUL_OF_SIGHT)
                .criterion(hasItem(ModItem.SOUL_OF_FRIGHT), conditionsFromItem(ModItem.SOUL_OF_FRIGHT))
                .criterion(hasItem(ModItem.SOUL_OF_MIGHT), conditionsFromItem(ModItem.SOUL_OF_MIGHT))
                .criterion(hasItem(ModItem.SOUL_OF_SIGHT), conditionsFromItem(ModItem.SOUL_OF_SIGHT))
                .offerTo(exporter, LibMod.id("soul_of_elements"));
    }
}
