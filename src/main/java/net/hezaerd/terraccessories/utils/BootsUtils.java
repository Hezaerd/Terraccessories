package net.hezaerd.terraccessories.utils;

import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.FluidTags;

public class BootsUtils {
    private static final double MAX_DISTANCE_SURFACE = 0.1;

    public  enum BootsState {
        GROUND,
        WATER,
        LAVA,
        AIR,
        NONE
    }

    public static void walkOnFluid(ItemStack stack, PlayerEntity player, Block fluid)
    {
        if (fluid == Blocks.WATER) {
            walkOnWater(stack, player);
        }
        else if (fluid == Blocks.LAVA) {
            walkOnLava(stack, player);
        }
    }

    public static void walkOnWater(ItemStack stack, PlayerEntity player)
    {
        if (player == null || stack == null) return;
        NbtCompound nbt = stack.getOrCreateNbt();
        if (player.isSneaking() || player.isOnGround()) {
            nbt.putInt("previous_water_state", BootsState.GROUND.ordinal());
            player.setNoGravity(false);
            return;
        }

        BlockState blockStateDown = player.getWorld().getBlockState(player.getBlockPos().down());
        BlockState blockStateFoot = player.getWorld().getBlockState(player.getBlockPos());

        Log.i("nbt Tag: " + nbt.getInt("previous_water_state"));
        boolean steppingOnFluid = (blockStateFoot.getBlock() == Blocks.WATER || blockStateDown.getBlock() == Blocks.WATER) && blockStateFoot.isAir();

        int airOrGround = blockStateDown.isAir() ? BootsState.AIR.ordinal() : BootsState.GROUND.ordinal();
        if (nbt.getInt("previous_water_state") == BootsState.GROUND.ordinal()
                && (blockStateFoot.getBlock() == Blocks.WATER || blockStateDown.getBlock() == Blocks.WATER)){
            player.setNoGravity(true);
            player.setVelocity(player.getVelocity().x, 0.4, player.getVelocity().z);
            player.setNoGravity(false);

            nbt.putInt("previous_water_state", BootsState.WATER.ordinal());
        }
        else if (nbt.getInt("previous_water_state") == BootsState.AIR.ordinal() && blockStateDown.getBlock() == Blocks.WATER){

            nbt.putInt("previous_water_state", steppingOnFluid ? BootsState.WATER.ordinal() : BootsState.GROUND.ordinal());
        }
        else if ((nbt.getInt("previous_state") == BootsState.WATER.ordinal()
                && blockStateDown.getBlock() != Blocks.WATER)){
            nbt.putInt("previous_water_state", airOrGround);
        }

        double distanceToSurface = (Math.floor(player.getY())) - player.getY();
        if (distanceToSurface > MAX_DISTANCE_SURFACE) {
            player.setNoGravity(false);
            return;
        }
        if (steppingOnFluid) {
            player.setOnGround(true);
            distanceToSurface = Math.min(0.04, distanceToSurface);
            player.setVelocity(player.getVelocity().x, Math.max(distanceToSurface, player.getVelocity().y), player.getVelocity().z);
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null && client.player != null  && client.player.input != null)
            {
                if (client.player.input.jumping){
                    player.jump();
                }
            }
        }
        else {
            player.setOnGround(false);
        }
    }

    public static void walkOnLava(ItemStack stack, PlayerEntity player)
    {
        if (player == null || stack == null) return;
        NbtCompound nbt = stack.getOrCreateNbt();
        if (player.isSneaking() || player.isOnGround()) {
            nbt.putInt("previous_lava_state", BootsState.GROUND.ordinal());
            player.setNoGravity(false);
            return;
        }
        BlockState blockStateDown = player.getWorld().getBlockState(player.getBlockPos().down());
        BlockState blockStateFoot = player.getWorld().getBlockState(player.getBlockPos());

        Log.i("nbt Tag: " + nbt.getInt("previous_lava_state"));
        boolean steppingOnFluid = (blockStateFoot.getBlock() == Blocks.LAVA || blockStateDown.getBlock() == Blocks.LAVA) && blockStateFoot.isAir();


        int airOrGround = blockStateDown.isAir() ? BootsState.AIR.ordinal() : BootsState.GROUND.ordinal();
        if (nbt.getInt("previous_lava_state") == BootsState.GROUND.ordinal() && (blockStateFoot.getBlock() == Blocks.LAVA
                || blockStateDown.getBlock() == Blocks.LAVA)){
            player.setNoGravity(true);
            player.setVelocity(player.getVelocity().x, 0.4, player.getVelocity().z);
            player.setNoGravity(false);

            nbt.putInt("previous_lava_state", BootsState.LAVA.ordinal());
        }
        else if (nbt.getInt("previous_lava_state") == BootsState.AIR.ordinal() && blockStateDown.getBlock() == Blocks.LAVA){
            nbt.putInt("previous_lava_state", steppingOnFluid ? BootsState.LAVA.ordinal() : BootsState.GROUND.ordinal());
        }
        else if ((nbt.getInt("previous_lava_state") == BootsState.LAVA.ordinal())
                && blockStateDown.getBlock() != Blocks.LAVA){
            nbt.putInt("previous_lava_state", airOrGround);
        }

        double distanceToSurface = (Math.floor(player.getY())) - player.getY();
        if (distanceToSurface > MAX_DISTANCE_SURFACE) {
            player.setNoGravity(false);
            return;
        }
        if (steppingOnFluid) {
            player.setOnGround(true);
            distanceToSurface = Math.min(0.04, distanceToSurface);
            player.setVelocity(player.getVelocity().x, Math.max(distanceToSurface, player.getVelocity().y), player.getVelocity().z);
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null && client.player != null  && client.player.input != null)
            {
                if (client.player.input.jumping){
                    player.jump();
                }
            }
        }
        else {
            player.setOnGround(false);
        }
    }



}
