package net.hezaerd.terraccessories.fluid;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.hezaerd.terraccessories.fluid.fluid.Shimmer;
import net.hezaerd.terraccessories.utils.LibMod;
import net.hezaerd.terraccessories.utils.Log;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;

public class ModFluid {
/*
    public static final TerraFluid SHIMMER = new Shimmer();
*/

    public static FlowableFluid STILL_SHIMMER;
    public static FlowableFluid FLOWING_SHIMMER;
    public static Block SHIMMER_BLOCK;

    public static void init() {
        STILL_SHIMMER = Registry.register(Registries.FLUID, LibMod.id("shimmer"), new Shimmer.Still());
        FLOWING_SHIMMER = Registry.register(Registries.FLUID, LibMod.id("flowing_shimmer"), new Shimmer.Flowing());
        SHIMMER_BLOCK = Registry.register(Registries.BLOCK, LibMod.id("shimmer"), new FluidBlock(STILL_SHIMMER, FabricBlockSettings.copy(Blocks.WATER)){});

/*        try {
            int registered = 0;

            for (Field field : ModFluid.class.getDeclaredFields()) {
                if (TerraFluid.class.isAssignableFrom(field.getType())) {
                    Identifier id = LibMod.id(field.getName().toLowerCase());
                    Registry.register(Registries.FLUID, id, (TerraFluid) field.get(null)).onRegister();

                    Log.d("Registered status effect: " + id);
                    registered++;
                }
            }

            Log.i("Registered " + registered + " status effects.");
        }
        catch (IllegalAccessException e) {
            Log.e("Failed to register status effects: " + e.getMessage());
        }*/
    }
}
