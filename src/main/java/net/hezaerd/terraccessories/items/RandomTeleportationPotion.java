package net.hezaerd.terraccessories.items;

import net.hezaerd.terraccessories.common.Suitable;
import net.hezaerd.terraccessories.common.Teleport;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class RandomTeleportationPotion extends Item {
    private static int range = 10000;
    private  static int cooldown = 10;

    public RandomTeleportationPotion(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return super.use(world, user, hand);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {

        if (world.isClient) {
            return super.finishUsing(stack, world, user);
        }

        ServerPlayerEntity player = (ServerPlayerEntity)user;
        ServerWorld serverWorld = (ServerWorld)world;

        for (int radius = range; radius > 1; radius/=2)
        {
            BlockPos pos = new BlockPos(player.getBlockPos().add(Random.create().nextBetween(-radius, radius), 0, Random.create().nextBetween(-radius, radius)));
            BlockPos truc = Suitable.findSuitablePos(serverWorld, pos, true);

            if (truc == null)
                continue;

            player.getItemCooldownManager().set(this, cooldown * 10);
            Teleport.teleportToPos(player, truc);
            stack.decrement(1);

            return super.finishUsing(stack, world, user);
        }

        return super.finishUsing(stack, world, user);
    }
}
