package net.hezaerd.terraccessories.utils;

import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.FluidTags;

public class BootsUtils {
    private static final double MAX_DISTANCE_SURFACE = 0.1;

    //nbt tag to check if previous block was solid then switch to not fall on lava when moving from ground to above lava

    public static void walkOnFluid(PlayerEntity player, Blocks fluid)
    {}

    public static  void  walkOnLava(PlayerEntity player) {
        if (player == null) return;
        if (player.isSneaking() || player.isOnGround()) {
            player.setNoGravity(false);
            return;
        }

        if (player.isTouchingWater()) {}
        BlockState blockStateDown = player.getWorld().getBlockState(player.getBlockPos().down());
        BlockState blockStateFoot = player.getWorld().getBlockState(player.getBlockPos());

        boolean steppingOnLava = blockStateDown.getBlock() == Blocks.LAVA && blockStateFoot.getBlock() == Blocks.AIR;
        Log.i("steppingOnLava: " + steppingOnLava);
        //Get the distance to the surface of the lava
        double distanceToSurface = (Math.floor(player.getY())) - player.getY();

//        if (distanceToSurface > MAX_DISTANCE_SURFACE) {
//            player.setNoGravity(false);
//            return;
//        }

        if (steppingOnLava) {
            player.setOnGround(true);

            //Clamp the distance to the surface to a maximum of 0.04
            distanceToSurface = Math.min(0.04, distanceToSurface);


            //Set the player's velocity to the distance to the surface if it is greater than the player's current velocity
            player.setVelocity(player.getVelocity().x, Math.max(distanceToSurface, player.getVelocity().y), player.getVelocity().z);
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null && client.player != null  && client.player.input != null)
            {
                client.player.setSprinting(!player.isSprinting() && client.player.input.pressingForward);

                if (client.player.input.jumping){
                    player.jump();
                }
            }
        }
        else {
            player.setOnGround(false);
        }

    }

    public static void walkOnWater(PlayerEntity player) {
        if (player == null) return;
        if (player.isSneaking() || player.isOnGround() || player.isSubmergedInWater()) {
            player.setNoGravity(false);
            return;
        }

        if (player.isTouchingWater()) {}
            BlockState blockStateDown = player.getWorld().getBlockState(player.getBlockPos().down());
            BlockState blockStateFoot = player.getWorld().getBlockState(player.getBlockPos());

            //Get the distance to the surface of the water
            double distanceToSurface = (Math.floor(player.getY())) - player.getY();

            if (distanceToSurface > MAX_DISTANCE_SURFACE) {
                player.setNoGravity(false);
                return;
            }

            if (blockStateDown.getBlock() == Blocks.WATER && blockStateFoot.getBlock() == Blocks.AIR) {
                player.setNoGravity(true);

                //Clamp the distance to the surface to a maximum of 0.04
                distanceToSurface = Math.min(0.04, distanceToSurface);

                //Set the player's velocity to the distance to the surface if it is greater than the player's current velocity
                player.setVelocity(player.getVelocity().x, Math.max(distanceToSurface, player.getVelocity().y), player.getVelocity().z);
                MinecraftClient client = MinecraftClient.getInstance();
                if (client != null && client.player != null  && client.player.input != null)
                {
                    client.player.setSprinting(!player.isSprinting() && client.player.input.pressingForward);

                    if (client.player.input.jumping){
                        player.jump();
                    }
                }
            }
            else {
                player.setNoGravity(false);
            }



    }
}
