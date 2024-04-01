package net.hezaerd.terraccessories.items;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.hezaerd.terraccessories.Terraccessories;
import net.hezaerd.terraccessories.common.Suitable;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.List;
import net.hezaerd.terraccessories.common.Teleport;

public class DemonConch extends Item {
    /* Properties */
    private static final int cooldown = 200;

    public DemonConch() {
        super(new OwoItemSettings().group(Terraccessories.TERRACCESSORIES_GROUP).maxCount(1));
    }

    /* Tooltip */
    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.terraccessories.demon_conch.tooltip").formatted(Formatting.DARK_PURPLE));
        tooltip.add(Text.translatable("item.terraccessories.demon_conch.tooltip2").formatted(Formatting.DARK_PURPLE, Formatting.ITALIC));
    }

    /* Usage animation */
    @Override
    public UseAction getUseAction(ItemStack stack) { return UseAction.BOW; }

    /* Maximum usage time */
    @Override
    public int getMaxUseTime(ItemStack stack) { return 16; }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (world.isClient()) { return super.finishUsing(stack, world, user); }

        ServerPlayerEntity player = (ServerPlayerEntity)user;
        ServerWorld serverWorld = (ServerWorld)world;
        ServerWorld serverNetherWorld = serverWorld.getServer().getWorld(World.NETHER);

        if(!player.isCreative())
            player.getItemCooldownManager().set(this, cooldown);

        // Check if the player is already in the nether
        if (serverWorld.getDimension().respawnAnchorWorks()) {
            player.sendMessage(Text.translatable("item.terraccessories.demon_conch.already_nether").formatted(Formatting.DARK_RED), true);
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_GHAST_HURT, SoundCategory.PLAYERS, 1.0F, 1.0F);
            return super.finishUsing(stack, world, user);
        }

        // Check if the nether world is available
        if (serverNetherWorld == null) {
            player.sendMessage(Text.translatable("item.terraccessories.demon_conch.fail").formatted(Formatting.DARK_RED), true);
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_BAT_TAKEOFF, SoundCategory.PLAYERS, 1.0F, 1.0F);
            return super.finishUsing(stack, world, user);
        }

        BlockPos netherPos = new BlockPos(player.getBlockPos().getX() / 8, player.getBlockPos().getY(), player.getBlockPos().getZ() / 8);
        BlockPos netherSuitablePos = Suitable.findSuitablePos(serverNetherWorld, netherPos, true);

        if (netherSuitablePos == null) {
            player.sendMessage(Text.translatable("item.terraccessories.demon_conch.non_suitable").formatted(Formatting.DARK_RED), true);
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_BAT_TAKEOFF, SoundCategory.PLAYERS, 1.0F, 1.0F);
            return super.finishUsing(stack, world, user);
        }

        player.sendMessage(Text.translatable("item.terraccessories.demon_conch.success").formatted(Formatting.RED), true);
        Teleport.changeDimensionAndTeleport(player, serverNetherWorld, netherSuitablePos);
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, SoundCategory.PLAYERS, 1.0F, 1.0F);

        return super.finishUsing(stack, world, user);
    }
}
