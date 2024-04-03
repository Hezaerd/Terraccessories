package net.hezaerd.terraccessories.utils;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public class Suitable {
    public static boolean isBlockSuitable(ServerWorld world, BlockPos pos) {
        return world.isAir(pos) && world.isAir(pos.up()) && world.getBlockState(pos.down()).isSolidBlock(world, pos.down());
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
