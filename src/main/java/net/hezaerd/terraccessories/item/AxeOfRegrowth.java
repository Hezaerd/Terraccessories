package net.hezaerd.terraccessories.item;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.hezaerd.terraccessories.Terraccessories;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class AxeOfRegrowth extends AxeItem {

    private final int boneMealValue = 10;

    public AxeOfRegrowth(ToolMaterial material, float attackDamage, float attackSpeed) {
        super(material, attackDamage, attackSpeed,
                new OwoItemSettings()
                        .group(Terraccessories.TERRACCESSORIES_GROUP)
                        .maxCount(1)
                        .maxDamage(640));
    }

    /* Tooltip */
    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.terraccessories.axe_of_regrowth.tooltip").formatted(Formatting.DARK_GREEN));
        tooltip.add(Text.translatable("item.terraccessories.axe_of_regrowth.tooltip2").formatted(Formatting.DARK_GREEN));
    }

    /* Use */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!user.isSneaking()) {
            return TypedActionResult.pass(itemStack);
        }

        if(isDamagedEnough(itemStack)) {
            playSoundAndSendMessage(world, user, SoundEvents.ENTITY_BAT_TAKEOFF, "item.terraccessories.axe_of_regrowth.repair.durability", Formatting.GOLD);
            setItemCooldown(user);
            return TypedActionResult.fail(itemStack);
        }

        if(!playerHasBoneMeal(user)) {
            playSoundAndSendMessage(world, user, SoundEvents.BLOCK_ANVIL_PLACE, "item.terraccessories.axe_of_regrowth.repair.bonemeal", Formatting.RED);
            setItemCooldown(user);
            return TypedActionResult.fail(itemStack);
        }

        repairItem(world, user, itemStack);
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    /* Use on block */
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        ItemStack itemStack = context.getStack();
        BlockPos blockPos = context.getBlockPos();
        PlayerEntity playerEntity = context.getPlayer();
        BlockState blockState = world.getBlockState(blockPos);

        boolean isDirt = blockState.isOf(Blocks.DIRT);

        if (isDirt) {
            if (playerEntity instanceof ServerPlayerEntity) {
                Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos, itemStack);
            }

            applyGrass(world, blockPos);

            itemStack.damage(1, playerEntity, (p) -> {
                playerEntity.sendToolBreakStatus(context.getHand());
            });

            return ActionResult.success(world.isClient);
        }

        super.useOnBlock(context);

        return ActionResult.PASS;
    }

    /* Post mine */
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        int damage = 1; // damage done to the axe

        if(!world.isClient && state.isIn(BlockTags.LOGS)) {
            final int durability = Math.abs(stack.getMaxDamage() - stack.getDamage());

            BlockPos.Mutable mutableUp = pos.mutableCopy();
            BlockPos.Mutable mutableDown = pos.mutableCopy();

            BlockState dirtState = null;

            while(world.getBlockState(mutableUp.move(Direction.UP)).isIn(BlockTags.LOGS)) {
                world.breakBlock(mutableUp, true, miner);
                damage++;
            }

            while(world.getBlockState(mutableDown.move(Direction.DOWN)).isIn(BlockTags.LOGS)) {
                world.breakBlock(mutableDown, true, miner);
                damage++;

                // Check if there is dirt below the log
                if(world.getBlockState(mutableDown.down()).isIn(BlockTags.DIRT)) {
                    dirtState = world.getBlockState(mutableDown);
                }
            }

            if(dirtState != null) {
                placeSapling(world, mutableDown.up(), getSapling(state));
            }

            for(BlockPos blockPos : BlockPos.iterateOutwards(mutableUp.offset(Direction.UP), 8, 8, 8)) {
                final BlockState blockState = world.getBlockState(blockPos);
                if(blockState.isIn(BlockTags.LEAVES)) {
                    ((LeavesBlock)blockState.getBlock()).scheduledTick(blockState, (ServerWorld)world, blockPos, world.random);
                }
            }

            if(state.getHardness(world, pos) != 0.0F) {
                stack.damage(damage, miner, (p) -> {
                    p.sendToolBreakStatus(p.getActiveHand());
                });
            }
        }
        return true;
    }

    private void playSoundAndSendMessage(World world, PlayerEntity user, SoundEvent soundEvent, String message, Formatting formatting) {
        world.playSound(null, user.getBlockPos(), soundEvent, SoundCategory.BLOCKS, 0.8F, 1.0F);
        user.sendMessage(Text.translatable(message).formatted(formatting), true);
    }

    private void setItemCooldown(PlayerEntity user) {
        user.getItemCooldownManager().set(this, 20);
    }

    private boolean isDamagedEnough(ItemStack itemStack) {
        return itemStack.getDamage() < boneMealValue;
    }

    private boolean playerHasBoneMeal(PlayerEntity user) {
        return user.getInventory().contains(new ItemStack(Items.BONE_MEAL));
    }

    private void repairItem(World world, PlayerEntity user, ItemStack item) {
        int maxToRepair = item.getDamage() / boneMealValue;
        int amount = user.getInventory().count(Items.BONE_MEAL);
        int slot = user.getInventory().getSlotWithStack(new ItemStack(Items.BONE_MEAL));
        int available = Math.min(maxToRepair, amount);

        item.damage(-available * boneMealValue, user, (p) -> {});
        user.getInventory().removeStack(slot, available);
        playSoundAndSendMessage(world, user, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, "item.terraccessories.axe_of_regrowth.repair.success", Formatting.GREEN);
    }

    private void applyGrass(World world, BlockPos pos) {
        world.setBlockState(pos, Blocks.GRASS_BLOCK.getDefaultState());
        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);

        Random random = world.random;
        for (int i = 0; i < 4; i++) {
            double x = pos.getX() + random.nextDouble();
            double y = pos.up().getY() + random.nextDouble();
            double z = pos.getZ() + random.nextDouble();

            world.addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }

    private void placeSapling(World world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, state);
        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    private BlockState getSapling(BlockState log) {
        Block block = log.getBlock();
        if (block.equals(Blocks.OAK_LOG)) {
            return Blocks.OAK_SAPLING.getDefaultState();
        } else if (block.equals(Blocks.SPRUCE_LOG)) {
            return Blocks.SPRUCE_SAPLING.getDefaultState();
        } else if (block.equals(Blocks.BIRCH_LOG)) {
            return Blocks.BIRCH_SAPLING.getDefaultState();
        } else if (block.equals(Blocks.JUNGLE_LOG)) {
            return Blocks.JUNGLE_SAPLING.getDefaultState();
        } else if (block.equals(Blocks.ACACIA_LOG)) {
            return Blocks.ACACIA_SAPLING.getDefaultState();
        } else if (block.equals(Blocks.DARK_OAK_LOG)) {
            return Blocks.DARK_OAK_SAPLING.getDefaultState();
        }
        return Blocks.OAK_SAPLING.getDefaultState();
    }
}
