package net.hezaerd.terraccessories.items;

import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InfiniteWaterBucket extends Item {
    public InfiniteWaterBucket(Settings settings) { super(settings); }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();

        world.playSound(null, blockPos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
        world.setBlockState(blockPos.offset(context.getSide()), Blocks.WATER.getDefaultState());

        return ActionResult.PASS;
    }
}
