package net.hezaerd.terraccessories.item;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.hezaerd.terraccessories.Terraccessories;
import net.hezaerd.terraccessories.utils.Log;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

public class StaffOfRegrowth extends Item {
    public StaffOfRegrowth() {
        super(new OwoItemSettings().group(Terraccessories.TERRACCESSORIES_GROUP).maxCount(1).maxDamage(640));
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
            PlayerEntity player = context.getPlayer();
            BlockPos pos = context.getBlockPos();
            MinecraftClient client = MinecraftClient.getInstance();

            // Set the block to grass
            context.getWorld().setBlockState(context.getBlockPos(), Blocks.GRASS_BLOCK.getDefaultState());
            
            // Play the sound
            context.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);

            // Create particles
            Random random = context.getWorld().random;
            for (int i = 0; i < 4; i++) {
                double x = pos.getX() + random.nextDouble();
                double y = pos.up().getY() + random.nextDouble();
                double z = pos.getZ() + random.nextDouble();

                client.particleManager.addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0.0D, 0.0D, 0.0D);
            }

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}
