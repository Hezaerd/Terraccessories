package net.hezaerd.terraccessories.workers;

import net.hezaerd.terraccessories.Terraccessories;
import net.hezaerd.terraccessories.common.BiomesUtils;
import net.hezaerd.terraccessories.items.MagicConch;
import net.hezaerd.terraccessories.registry.ItemsInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeCoords;

public class BiomeSearchWorker implements WorkerManager.IWorker {
    private final int sampleSpace;
    private final int maxSamples;
    public final int maxRadius;
    private ServerWorld world;
    private Identifier biomeID;
    private BlockPos startPos;
    private int samples;
    private int nextLength;
    private Direction direction;
    private ItemStack stack;
    private PlayerEntity player;
    private int x;
    private int z;
    private int[] yValues;
    private int length;
    private boolean finished;
    private int lastRadiusThreshold;

    public BiomeSearchWorker(ServerWorld world, PlayerEntity player, ItemStack stack, Biome biome, BlockPos startPos) {
        this.world = world;
        this.player = player;
        this.stack = stack;
        this.startPos = startPos;
        x = startPos.getX();
        z = startPos.getZ();
        yValues = MathHelper.stream(startPos.getY(), world.getBottomY() + 1, world.getTopY(), 64).toArray();
        sampleSpace = BiomesUtils.SAMPLE_SIZE_MODIFIER * BiomesUtils.BIOME_SIZE;
        maxSamples = BiomesUtils.MAX_SAMPLES_SIZE;
        maxRadius = BiomesUtils.RADIUS_MODIFIER * BiomesUtils.BIOME_SIZE;
        nextLength = sampleSpace;
        length = 0;
        samples = 0;
        direction = Direction.UP;
        finished = false;
        biomeID = BiomesUtils.getIdentifierForBiome(world, biome);
        lastRadiusThreshold = 0;
    }

    public void start() {
        if (!stack.isEmpty() && stack.getItem() == ItemsInit.MAGIC_CONCH) {
            if (maxRadius > 0 && sampleSpace > 0) {
                Terraccessories.LOGGER.info("Starting search: " + sampleSpace + " sample space, " + maxSamples + " max samples, " + maxRadius + " max radius");
                WorkerManager.addWorker(this);
            } else {
                fail();
            }
        }
    }

    @Override
    public boolean hasWork() {
        return !finished && getRadius() <= maxRadius && samples <= maxSamples;
    }

    @Override
    public boolean doWork() {
        if (hasWork()) {
            if (direction == Direction.NORTH) {
                z -= sampleSpace;
            } else if (direction == Direction.EAST) {
                x += sampleSpace;
            } else if (direction == Direction.SOUTH) {
                z += sampleSpace;
            } else if (direction == Direction.WEST) {
                x -= sampleSpace;
            }

            int sampleX = BiomeCoords.fromBlock(x);
            int sampleZ = BiomeCoords.fromBlock(z);

            for (int y : yValues) {
                int sampleY = BiomeCoords.fromBlock(y);
                final Biome biomeAtPos = world.getChunkManager().getChunkGenerator().getBiomeSource().getBiome(sampleX, sampleY, sampleZ, world.getChunkManager().getNoiseConfig().getMultiNoiseSampler()).value();
                final Identifier biomeAtPosID = BiomesUtils.getIdentifierForBiome(world, biomeAtPos);
                if (biomeAtPosID != null && biomeAtPosID.equals(biomeID)) {
                    succeed();
                    return false;
                }
            }

            samples++;
            length += sampleSpace;
            if (length >= nextLength) {
                if (direction != Direction.UP) {
                    nextLength += sampleSpace;
                    direction = direction.rotateYClockwise();
                } else {
                    direction = Direction.NORTH;
                }
                length = 0;
            }
            int radius = getRadius();
            if (radius > 500 && radius / 500 > lastRadiusThreshold) {
                
                lastRadiusThreshold = radius / 500;
            }
        }
        if (hasWork()) {
            return true;
        }
        if (!finished) {
            fail();
        }
        return false;
    }

    private void succeed() {
        Terraccessories.LOGGER.info("Search succeeded: " + getRadius() + " radius, " + samples + " samples");
        if (!stack.isEmpty() && stack.getItem() == ItemsInit.MAGIC_CONCH) {
            ((MagicConch) stack.getItem()).succeed(stack, player, x, z, samples);
        } else {
            Terraccessories.LOGGER.error("Invalid compass after search");
        }
        finished = true;
    }

    private void fail() {
        Terraccessories.LOGGER.info("Search failed: " + getRadius() + " radius, " + samples + " samples");
        if (!stack.isEmpty() && stack.getItem() == ItemsInit.MAGIC_CONCH) {
            ((MagicConch) stack.getItem()).fail();
        } else {
            Terraccessories.LOGGER.error("Invalid compass after search");
        }
        finished = true;
    }

    public void stop() {
        Terraccessories.LOGGER.info("Search stopped: " + getRadius() + " radius, " + samples + " samples");
        finished = true;
    }

    private int getRadius() {
        return BiomesUtils.getDistanceToBiome(startPos, x, z);
    }

    private int roundRadius(int radius, int roundTo) {
        return ((int) radius / roundTo) * roundTo;
    }
}
