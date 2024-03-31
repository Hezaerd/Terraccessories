package net.hezaerd.terraccessories.items;

import net.hezaerd.terraccessories.common.Suitable;
import net.hezaerd.terraccessories.common.Teleport;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class RandomTeleportationPotion extends Item {
    public RandomTeleportationPotion(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        if (world.isClient) {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }

        ServerPlayerEntity player = (ServerPlayerEntity)user;
        ServerWorld serverWorld = (ServerWorld)world;

        BlockPos pos = new BlockPos(player.getBlockPos().add(Random.create().nextBetween(-100, 100), 0, Random.create().nextBetween(-100, 100)));

        Teleport.teleportToPos(user, Suitable.findSuitablePos(serverWorld, pos, true));

        return super.use(world, user, hand);
    }
}
