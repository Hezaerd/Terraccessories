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

        if(!isDamagedEnough(itemStack)) {
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

        super.useOnBlock(context);

        boolean isDirt = blockState.isOf(Blocks.DIRT);
        boolean isSapling = blockState.isOf(Blocks.OAK_SAPLING) || blockState.isOf(Blocks.SPRUCE_SAPLING) || blockState.isOf(Blocks.BIRCH_SAPLING) || blockState.isOf(Blocks.JUNGLE_SAPLING) || blockState.isOf(Blocks.ACACIA_SAPLING) || blockState.isOf(Blocks.DARK_OAK_SAPLING);

        if (isDirt) {
            if (playerEntity instanceof ServerPlayerEntity) {
                Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity) playerEntity, blockPos, itemStack);
            }

            applyGrass(world, blockPos);
        }

        if (isSapling) {
            if (playerEntity instanceof ServerPlayerEntity) {
                Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity) playerEntity, blockPos, itemStack);
            }

            // apply bone meal to sapling
            if (playerEntity.getServer() != null) {
                ServerWorld serverWorld = playerEntity.getServer().getWorld(world.getRegistryKey());
                ((SaplingBlock) blockState.getBlock()).grow(serverWorld, serverWorld.random, blockPos, blockState);
            }
        }

        itemStack.damage(1, playerEntity, (p) -> {
            playerEntity.sendToolBreakStatus(context.getHand());
        });

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

            do {
                world.breakBlock(mutableDown, true, miner);
                damage++;

                if(world.getBlockState(mutableDown.down()).isIn(BlockTags.DIRT)) {
                    dirtState = world.getBlockState(mutableDown);
                }
            }
            while(world.getBlockState(mutableDown.move(Direction.DOWN)).isIn(BlockTags.LOGS));

            if(dirtState != null) {
                placeSapling(world, mutableDown.up(), getSapling(state), (PlayerEntity)miner);
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
        return !(itemStack.getDamage() < boneMealValue);
    }

    private boolean playerHasBoneMeal(PlayerEntity user) {
        return user.getInventory().contains(new ItemStack(Items.BONE_MEAL));
    }

    private boolean playerHasSapling(PlayerEntity user, BlockState log) {
        return user.getInventory().contains(getSaplingItemStack(log));
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

    private void placeSapling(World world, BlockPos pos, BlockState state, PlayerEntity miner) {

        // Check if player has sapling
        if(!playerHasSapling(miner, state)) {
            miner.sendMessage(Text.translatable("item.terraccessories.axe_of_regrowth.sapling").formatted(Formatting.RED), true);
            return;
        }

        ItemStack sapling = getSaplingItemStack(state);

        // Remove sapling from player inventory
        int slot = miner.getInventory().getSlotWithStack(sapling);
        miner.getInventory().removeStack(slot, 1);

        world.setBlockState(pos, state);
        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    private BlockState getSapling(BlockState log) {
        Block block = log.getBlock();
        if (block.equals(Blocks.OAK_LOG) || block.equals(Blocks.STRIPPED_OAK_LOG)) {
            return Blocks.OAK_SAPLING.getDefaultState();
        } else if (block.equals(Blocks.SPRUCE_LOG) || block.equals(Blocks.STRIPPED_SPRUCE_LOG)) {
            return Blocks.SPRUCE_SAPLING.getDefaultState();
        } else if (block.equals(Blocks.BIRCH_LOG) || block.equals(Blocks.STRIPPED_BIRCH_LOG)) {
            return Blocks.BIRCH_SAPLING.getDefaultState();
        } else if (block.equals(Blocks.JUNGLE_LOG) || block.equals(Blocks.STRIPPED_JUNGLE_LOG)) {
            return Blocks.JUNGLE_SAPLING.getDefaultState();
        } else if (block.equals(Blocks.ACACIA_LOG) || block.equals(Blocks.STRIPPED_ACACIA_LOG)) {
            return Blocks.ACACIA_SAPLING.getDefaultState();
        } else if (block.equals(Blocks.DARK_OAK_LOG) || block.equals(Blocks.STRIPPED_DARK_OAK_LOG)) {
            return Blocks.DARK_OAK_SAPLING.getDefaultState();
        }
        return Blocks.OAK_SAPLING.getDefaultState();
    }

    private ItemStack getSaplingItemStack(BlockState sapling) {
        Block block = sapling.getBlock();
        if (block.equals(Blocks.OAK_SAPLING)) {
            return new ItemStack(Items.OAK_SAPLING);
        } else if (block.equals(Blocks.SPRUCE_SAPLING)) {
            return new ItemStack(Items.SPRUCE_SAPLING);
        } else if (block.equals(Blocks.BIRCH_SAPLING)) {
            return new ItemStack(Items.BIRCH_SAPLING);
        } else if (block.equals(Blocks.JUNGLE_SAPLING)) {
            return new ItemStack(Items.JUNGLE_SAPLING);
        } else if (block.equals(Blocks.ACACIA_SAPLING)) {
            return new ItemStack(Items.ACACIA_SAPLING);
        } else if (block.equals(Blocks.DARK_OAK_SAPLING)) {
            return new ItemStack(Items.DARK_OAK_SAPLING);
        }
        return new ItemStack(Items.OAK_SAPLING);
    }
}
