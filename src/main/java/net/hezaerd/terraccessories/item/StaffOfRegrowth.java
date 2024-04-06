package net.hezaerd.terraccessories.item;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.hezaerd.terraccessories.Terraccessories;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

import java.util.List;

public class StaffOfRegrowth extends Item {
    public StaffOfRegrowth() {
        super(new OwoItemSettings().group(Terraccessories.TERRACCESSORIES_GROUP).maxCount(1));
    }

    /* Tooltip */
    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.terraccessories.staff_of_regrowth.tooltip").formatted(Formatting.DARK_GREEN));
    }


    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().isClient) {
            return ActionResult.PASS;
        }

        BlockState blockState = context.getWorld().getBlockState(context.getBlockPos());

        // Check if the block is dirt
        if (blockState.isOf(net.minecraft.block.Blocks.DIRT)) {
            // Set the block to grass
            context.getWorld().setBlockState(context.getBlockPos(), net.minecraft.block.Blocks.GRASS_BLOCK.getDefaultState());
        }

        return ActionResult.SUCCESS;
    }
}
