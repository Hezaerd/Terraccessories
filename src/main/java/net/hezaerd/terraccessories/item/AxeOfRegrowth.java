package net.hezaerd.terraccessories.item;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.hezaerd.terraccessories.Terraccessories;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

public class AxeOfRegrowth extends MiningToolItem {
    public AxeOfRegrowth() {
        super(7.0F, -3.2F, ToolMaterials.NETHERITE, BlockTags.LOGS,
                new OwoItemSettings()
                .group(Terraccessories.TERRACCESSORIES_GROUP)
                .maxCount(1)
                .maxDamage(640)
                .rarity(Rarity.RARE));
    }

    /* Tooltip */
    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.terraccessories.axe_of_regrowth.tooltip").formatted(Formatting.DARK_GREEN));
        tooltip.add(Text.translatable("item.terraccessories.axe_of_regrowth.tooltip2").formatted(Formatting.DARK_GREEN));
    }

    /* Can Mine */
    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return true;
    }


    /* Use */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!user.isSneaking()) {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }

        if(user.getStackInHand(hand).getDamage() < 10) {
            world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_BAT_TAKEOFF, SoundCategory.BLOCKS, 0.8F, 1.0F);
            user.sendMessage(Text.translatable("item.terraccessories.axe_of_regrowth.fail_damage").formatted(Formatting.GOLD), true);
            user.getItemCooldownManager().set(this, 20);
            return TypedActionResult.fail(user.getStackInHand(hand));
        }

        if(!user.getInventory().contains(new ItemStack(Items.BONE_MEAL))) {
            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            user.sendMessage(Text.translatable("item.terraccessories.axe_of_regrowth.fail_bonemeal").formatted(Formatting.RED), true);
            user.getItemCooldownManager().set(this, 20);
            return TypedActionResult.fail(user.getStackInHand(hand));
        }

        int maxToRepair = user.getStackInHand(hand).getDamage() / 10;
        int amount = user.getInventory().count(Items.BONE_MEAL);
        int slot = user.getInventory().getSlotWithStack(new ItemStack(Items.BONE_MEAL));
        int available = Math.min(maxToRepair, amount);

        user.getStackInHand(hand).damage(-available * 10, user, (p) -> {});
        user.getInventory().removeStack(slot, available);
        world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
        user.sendMessage(Text.translatable("item.terraccessories.axe_of_regrowth.success").formatted(Formatting.GREEN), true);

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    /* Use on Block */
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

            // Damage the item
            context.getStack().damage(1, player, (p) -> {
                p.sendToolBreakStatus(context.getHand());
            });

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
