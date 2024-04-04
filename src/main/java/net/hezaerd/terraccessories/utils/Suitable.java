package net.hezaerd.terraccessories.utils;

import net.hezaerd.terraccessories.Terraccessories;
import net.hezaerd.terraccessories.config.TerraccessoriesConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;

public class Suitable {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static boolean isBlockSuitable(ServerWorld world, BlockPos pos) {
        return world.isAir(pos) && world.isAir(pos.up()) && world.getBlockState(pos.down()).isSolidBlock(world, pos.down());
    }

    public static boolean canCollide(BlockPos pos) {
        BlockState state = client.world.getBlockState(pos);
        VoxelShape collider = state.getCollisionShape(client.world, pos);

        return !collider.isEmpty();
    }

    public static boolean isLava(BlockPos pos) {
        BlockState state = client.world.getBlockState(pos);
        return state.isOf(Blocks.LAVA);
    }

    public static BlockPos findOpenSpotForwards(HitResult hit, double range) {
        return findSuitableInLineForward(hit, range, 1);
    }

    public static BlockPos findOpenSpotBackwards(HitResult hit, double range) {
        return findSuitableInLineForward(hit, range, -1);
    }

    private static BlockPos findSuitableInLineForward(HitResult hit, double range, int direction) {
        Vec3d vector = client.cameraEntity.getRotationVector();

        for (int i = 1; Math.max(0, direction) < range * 8; i++) {
            BlockPos pos = BlockPos.ofFloored(hit.getPos().add(vector.multiply(direction * 0.125 * i)));
            boolean foundObstacle = canCollide(pos);
            boolean isLoaded = client.world.getChunkManager().isChunkLoaded(pos.getX() >> 4, pos.getZ() >> 4);

            if (isLoaded && !foundObstacle) {
                boolean isBottomBlockFree = !canCollide(pos.down(1));

                if (isBottomBlockFree) {
                    return pos.down(1);
                }
            }
        }
        return null;
    }

    public static BlockPos findSuitablePos(ServerWorld world, BlockPos pos) {
        /* Check if the block is suitable */
        if (isBlockSuitable(world, pos))
            return pos;

        /* Check at other Y levels (under) */
        for (int y = pos.getY() - 1; y >= world.getBottomY(); y--) {
            BlockPos blockToTest = new BlockPos(pos.getX(), y, pos.getZ());
            if (isBlockSuitable(world, blockToTest))
                return blockToTest;
        }

        /* Check at other Y levels (above) */
        for (int y = pos.getY() + 1; y < 125; y++) {
            BlockPos blockToTest = new BlockPos(pos.getX(), y, pos.getZ());
            if (isBlockSuitable(world, blockToTest))
                return blockToTest;
        }

        return null;
    }

    public static BlockPos findSuitablePos(ServerWorld world, BlockPos pos, boolean checkChunk) {
        if (!checkChunk)
            return findSuitablePos(world, pos);

        /* Check if the block is suitable */
        if (isBlockSuitable(world, pos))
            return pos;

        /* Check at other Y levels (under) */
        for (int y = pos.getY() - 1; y >= world.getBottomY(); y--) {
            BlockPos blockToTest = new BlockPos(pos.getX(), y, pos.getZ());
            if (isBlockSuitable(world, blockToTest))
                return blockToTest;
        }

        /* Check at other Y levels (above) */
        for (int y = pos.getY() + 1; y < 125; y++) {
            BlockPos blockToTest = new BlockPos(pos.getX(), y, pos.getZ());
            if (isBlockSuitable(world, blockToTest))
                return blockToTest;
        }

        /* Get the chunk position */
        ChunkPos chunkPos = new ChunkPos(pos);

        /* Check in the chunk at the same Y level */
        for (int x = chunkPos.getStartX(); x < chunkPos.getEndX(); x++) {
            for (int z = chunkPos.getStartZ(); z < chunkPos.getEndZ(); z++) {
                BlockPos blockToTest = new BlockPos(x, pos.getY(), z);
                if (isBlockSuitable(world, blockToTest))
                    return blockToTest;
            }
        }

        /* Check in the chunk at other Y levels (under) */
        for (int y = pos.getY() - 1; y >= world.getBottomY(); y--) {
            for (int x = chunkPos.getStartX(); x < chunkPos.getEndX(); x++) {
                for (int z = chunkPos.getStartZ(); z < chunkPos.getEndZ(); z++) {
                    BlockPos blockToTest = new BlockPos(x, y, z);
                    if (isBlockSuitable(world, blockToTest))
                        return blockToTest;
                }
            }
        }

        /* Check in the chunk at other Y levels (above) */
        for (int y = pos.getY() + 1; y < 125; y++) {
            for (int x = chunkPos.getStartX(); x < chunkPos.getEndX(); x++) {
                for (int z = chunkPos.getStartZ(); z < chunkPos.getEndZ(); z++) {
                    BlockPos blockToTest = new BlockPos(x, y, z);
                    if (isBlockSuitable(world, blockToTest))
                        return blockToTest;
                }
            }
        }

        return null;
    }
}
