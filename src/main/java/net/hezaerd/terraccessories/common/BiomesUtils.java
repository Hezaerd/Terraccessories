package net.hezaerd.terraccessories.common;

import net.minecraft.registry.Registry;

import net.minecraft.registry.RegistryKeys;

import net.minecraft.util.Identifier;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Optional;


public class BiomesUtils {

    public static int BIOME_SIZE = 4;
    public static int SAMPLE_SIZE_MODIFIER = 16;
    public  static int MAX_SAMPLES_SIZE = 50000;
    public static  int RADIUS_MODIFIER = 2500;

    public static Registry<Biome> getBiomeRegistry(World world) {
        return world.getRegistryManager().get(RegistryKeys.BIOME);
    }
    public static Optional<Biome> getBiomeForIdentifier(World world, Identifier id) {
        return getBiomeRegistry(world).getOrEmpty(id);
    }
    public static Identifier getIdentifierForBiome(World world, Biome biome) {
        return getBiomeRegistry(world).getId(biome);
    }

    public static int getDistanceToBiome(BlockPos startPos, int biomeX, int biomeZ) {
        return (int) MathHelper.sqrt((float) startPos.getSquaredDistance(new BlockPos(biomeX, startPos.getY(), biomeZ)));
    }


}
