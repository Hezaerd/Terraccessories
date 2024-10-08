package net.hezaerd.terraccessories.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.hezaerd.terraccessories.item.ModItem;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.Models;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput fabricDataOutput) {
        super(fabricDataOutput);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

        /* Tools */
        itemModelGenerator.register(ModItem.DEMON_CONCH, Models.GENERATED);
        itemModelGenerator.register(ModItem.MAGIC_CONCH, Models.GENERATED);
        itemModelGenerator.register(ModItem.BOTTOMLESS_WATER_BUCKET, Models.GENERATED);
        itemModelGenerator.register(ModItem.BOTTOMLESS_LAVA_BUCKET, Models.GENERATED);
        itemModelGenerator.register(ModItem.STAFF_OF_REGROWTH, Models.HANDHELD);

        /* Potions */
        itemModelGenerator.register(ModItem.RANDOM_TELEPORTATION_POTION, Models.GENERATED);

        /* Trinkets */
        itemModelGenerator.register(ModItem.AGLET, Models.GENERATED);
        itemModelGenerator.register(ModItem.JUMP_BOTTLE, Models.GENERATED);
        itemModelGenerator.register(ModItem.FART_JUMP_BOTTLE, Models.GENERATED);
        itemModelGenerator.register(ModItem.AKLET_OF_THE_WIND, Models.GENERATED);
        itemModelGenerator.register(ModItem.HERMES_BOOTS, Models.GENERATED);
        itemModelGenerator.register(ModItem.LUCKY_HORSESHOE, Models.GENERATED);
        itemModelGenerator.register(ModItem.WATER_WALKING_BOOTS, Models.GENERATED);
        itemModelGenerator.register(ModItem.ROCKET_BOOTS, Models.GENERATED);

        /* Resources */
        itemModelGenerator.register(ModItem.SOUL_OF_FRIGHT, Models.GENERATED);
        itemModelGenerator.register(ModItem.SOUL_OF_MIGHT, Models.GENERATED);
        itemModelGenerator.register(ModItem.SOUL_OF_SIGHT, Models.GENERATED);
        itemModelGenerator.register(ModItem.SOUL_OF_ELEMENTS, Models.GENERATED);

    }
}
