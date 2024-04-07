package net.hezaerd.terraccessories.item;

import com.google.common.collect.BiMap;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.hezaerd.terraccessories.Terraccessories;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

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
        Optional<BlockState> strippedState = this.getStrippedState(blockState);
        Optional<BlockState> oxidizedState = Oxidizable.getDecreasedOxidationState(blockState);
        Optional<BlockState> waxedState = Optional.ofNullable((Block)((BiMap<?, ?>) HoneycombItem.WAXED_TO_UNWAXED_BLOCKS.get()).get(blockState.getBlock())).map((block) -> block.getStateWithProperties(blockState));
        Optional<BlockState> currentState = Optional.empty();

        if (strippedState.isPresent()) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            currentState = strippedState;
        } else if (oxidizedState.isPresent()) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_SCRAPE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.syncWorldEvent(playerEntity, WorldEvents.BLOCK_SCRAPED, blockPos, 0);
            currentState = oxidizedState;
        } else if (waxedState.isPresent()) {
            world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_WAX_OFF, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.syncWorldEvent(playerEntity, WorldEvents.WAX_REMOVED, blockPos, 0);
            currentState = waxedState;
        }

        if (currentState.isPresent()) {
            if (playerEntity instanceof ServerPlayerEntity) {
                Criteria.ITEM_USED_ON_BLOCK.trigger((ServerPlayerEntity)playerEntity, blockPos, itemStack);
            }

            world.setBlockState(blockPos, currentState.get(), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Emitter.of(playerEntity, currentState.get()));

            if (playerEntity != null) {
                itemStack.damage(1, (LivingEntity)playerEntity, (Consumer)((p) -> {
                    playerEntity.sendToolBreakStatus(context.getHand());
                }));
            }

            return ActionResult.success(world.isClient);
        }

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

        else {
            return ActionResult.PASS;
        }
    }

    /* Get stripped state */
    private Optional<BlockState> getStrippedState(BlockState state) {
        return Optional.ofNullable(STRIPPED_BLOCKS.get(state.getBlock())).map((block) ->
                block.getDefaultState().with(PillarBlock.AXIS, state.get(PillarBlock.AXIS)));
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
}
