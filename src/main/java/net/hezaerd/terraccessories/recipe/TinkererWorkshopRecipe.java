package net.hezaerd.terraccessories.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.hezaerd.terraccessories.enchantment.ModEnchantment;
import net.hezaerd.terraccessories.utils.LibMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class TinkererWorkshopRecipe implements Recipe<SimpleInventory> {
    private final Identifier id;
    private final List<Ingredient> recipeItems;
    private final ItemStack output;
    private final Map<Enchantment, Integer> enchantmentLevels;

    public TinkererWorkshopRecipe(Identifier id, List<Ingredient> ingredients, ItemStack itemStack, Map<Enchantment, Integer> enchantments) {
        this.id = id;
        this.recipeItems = ingredients;
        this.output = itemStack;
        this.enchantmentLevels = enchantments;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if(world.isClient()) {
            return false;
        }

        if (recipeItems.get(0).test(inventory.getStack(0)))
            return recipeItems.get(1).test(inventory.getStack(1));

        return false;
    }

    @Override
    public ItemStack craft(SimpleInventory inventory, DynamicRegistryManager registryManager) { return output; }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) { return output.copy(); }

    public ItemStack getResult(DynamicRegistryManager registryManager) {
        ItemStack result = this.output.copy();

        for (Map.Entry<Enchantment, Integer> entry : enchantmentLevels.entrySet()) {
            result.addEnchantment(entry.getKey(), entry.getValue());
        }

        return result;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.ofSize(this.recipeItems.size());
        list.addAll(recipeItems);
        return list;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<TinkererWorkshopRecipe> {
        public static final Type INSTANCE = new Type();
        public static final Identifier ID = LibMod.id("tinkerer_workshop_recipe");
    }

    public static class Serializer implements RecipeSerializer<TinkererWorkshopRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final Identifier ID = LibMod.id("tinkerer_workshop_recipe");

        @Override
        public TinkererWorkshopRecipe read(Identifier id, JsonObject json) {
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));

            JsonArray ingredients = JsonHelper.getArray(json, "ingredients");
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(2, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new TinkererWorkshopRecipe(id, inputs, output, ModEnchantment.ParseEnchantment(JsonHelper.getObject(json, "ench")));
        }

        @Override
        public TinkererWorkshopRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromPacket(buf));
            }

            ItemStack output = buf.readItemStack();

            return new TinkererWorkshopRecipe(id, inputs, output, null);
        }

        @Override
        public void write(PacketByteBuf buf, TinkererWorkshopRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.write(buf);
            }
            buf.writeItemStack(recipe.getOutput(null));
        }
    }
}
