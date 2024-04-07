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
        AIR
    }



    public static void walkOnFluid(ItemStack stack, PlayerEntity player, Block fluid)
    {
        if (player == null || stack == null) return;
        NbtCompound nbt = stack.getOrCreateNbt();
        if (player.isSneaking() || player.isOnGround()) {
            nbt.putInt("previous_state", BootsState.GROUND.ordinal());
            player.setNoGravity(false);
            return;
        }
        ShapeContext shapeContext = ShapeContext.of(player);
        

        /*    if (shapeContext.isAbove(FluidBlock.COLLISION_SHAPE, this.getBlockPos(), true) && !this.getWorld().getFluidState(this.getBlockPos().up()).isIn(FluidTags.LAVA))*/
        BlockState blockStateDown = player.getWorld().getBlockState(player.getBlockPos().down());
        BlockState blockStateFoot = player.getWorld().getBlockState(player.getBlockPos());
        //if player is previously on ground add y velocity to avoid falling on lava
        boolean steppingOnFluid = blockStateDown.getBlock() == fluid && blockStateFoot.isAir();
        int lavaOrWater = fluid == Blocks.LAVA ? BootsState.LAVA.ordinal() : BootsState.WATER.ordinal();
        int airOrGround = blockStateDown.isAir() ? BootsState.AIR.ordinal() : BootsState.GROUND.ordinal();
        if (nbt.getInt("previous_state") == BootsState.GROUND.ordinal() && (blockStateFoot.getBlock() == fluid || blockStateDown.getBlock() == fluid)){
            player.setNoGravity(true);
            player.setVelocity(player.getVelocity().x, 0.2, player.getVelocity().z);
            Log.i("Setting no gravity");
            player.setNoGravity(false);
            nbt.putInt("previous_state", lavaOrWater);
        }
        else if (nbt.getInt("previous_state") == lavaOrWater && blockStateDown.getBlock() != fluid){
            nbt.putInt("previous_state", steppingOnFluid ? lavaOrWater : airOrGround);
        }
        else if (nbt.getInt("previous_state") == BootsState.AIR.ordinal() && blockStateDown.getBlock() == fluid){
            nbt.putInt("previous_state", steppingOnFluid ? lavaOrWater : BootsState.GROUND.ordinal());
        }

        Log.i("Previous state: " + nbt.getInt("previous_state"));
        Log.i("Stepping on fluid: " + steppingOnFluid);

        //Get the distance to the surface of the lava
        double distanceToSurface = (Math.floor(player.getY())) - player.getY();
        if (distanceToSurface > MAX_DISTANCE_SURFACE) {
            player.setNoGravity(false);
            return;
        }
        if (steppingOnFluid) {
            player.setOnGround(true);
            //Clamp the distance to the surface to a maximum of 0.04
            distanceToSurface = Math.min(0.04, distanceToSurface);
            //Set the player's velocity to the distance to the surface if it is greater than the player's current velocity
            player.setVelocity(player.getVelocity().x, Math.max(distanceToSurface, player.getVelocity().y), player.getVelocity().z);
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null && client.player != null  && client.player.input != null)
            {
//                client.player.setSprinting(!player.isSprinting() && client.player.input.pressingForward);

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
