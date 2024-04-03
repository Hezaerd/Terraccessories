package net.hezaerd.terraccessories.workers;

import net.hezaerd.terraccessories.Terraccessories;
import net.hezaerd.terraccessories.utils.BiomesUtils;
import net.hezaerd.terraccessories.items.MagicConch;
import net.hezaerd.terraccessories.items.ModItems;
import net.hezaerd.terraccessories.utils.Log;
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
        yValues = MathHelper.stream(startPos.getY(), world.getBottomY() + 1, world.getTopY(), 64).toArray();
        sampleSpace = BiomesUtils.SAMPLE_SIZE_MODIFIER * BiomesUtils.BIOME_SIZE;
        maxSamples = BiomesUtils.MAX_SAMPLES_SIZE;
        maxRadius = BiomesUtils.RADIUS_MODIFIER * BiomesUtils.BIOME_SIZE;
        x = startPos.getX() + maxRadius;
        z = startPos.getZ() + maxRadius;
        nextLength = sampleSpace;
        length = 0;
        samples = 0;
        direction = Direction.DOWN;
        finished = false;
        biomeID = BiomesUtils.getIdentifierForBiome(world, biome);
        lastRadiusThreshold = 0;
    }

    public void start() {
        if (!stack.isEmpty() && stack.getItem() == ModItems.MAGIC_CONCH) {
            if (maxRadius > 0 && sampleSpace > 0) {
                Log.i("Starting search: " + sampleSpace + " sample space, " + maxSamples + " max samples, " + maxRadius + " max radius");
                WorkerManager.addWorker(this);
            } else {
                fail();
            }
        }
    }

    @Override
    public boolean hasWork() {
        return !finished && getRadius() >= 0 && samples <= maxSamples;
    }

    @Override
    public boolean doWork() {
        if (hasWork()) {
            if (direction == Direction.NORTH) {
                z += sampleSpace; // Change "-" to "+" to move from near to far in the North direction
            } else if (direction == Direction.EAST) {
                x -= sampleSpace; // Change "+" to "-" to move from near to far in the East direction
            } else if (direction == Direction.SOUTH) {
                z -= sampleSpace; // Change "+" to "-" to move from near to far in the South direction
            } else if (direction == Direction.WEST) {
                x += sampleSpace; // Change "-" to "+" to move from near to far in the West direction
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
                if (direction != Direction.DOWN) {
                    nextLength += sampleSpace;
                    direction = direction.rotateYCounterclockwise(); // Change to counterclockwise rotation
                } else {
                    direction = Direction.NORTH; // Change to initial direction
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
        Log.i("Search succeeded: " + getRadius() + " radius, " + samples + " samples");
        if (!stack.isEmpty() && stack.getItem() == ModItems.MAGIC_CONCH) {
            ((MagicConch) stack.getItem()).succeed(world, stack, player, x, z, samples);
        } else {
            Log.e("Invalid compass after search");
        }
        finished = true;
    }

    private void fail() {
        Log.i("Search failed: " + getRadius() + " radius, " + samples + " samples");
        if (!stack.isEmpty() && stack.getItem() == ModItems.MAGIC_CONCH) {
            ((MagicConch) stack.getItem()).fail();
        } else {
            Log.e("Invalid compass after search");
        }
        finished = true;
    }

    public void stop() {
        Log.i("Search stopped: " + getRadius() + " radius, " + samples + " samples");
        finished = true;
    }

    private int getRadius() {
        return BiomesUtils.getDistanceToBiome(startPos, x, z);
    }

    private int roundRadius(int radius, int roundTo) {
        return ((int) radius / roundTo) * roundTo;
    }
}
