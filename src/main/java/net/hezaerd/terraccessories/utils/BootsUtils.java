package net.hezaerd.terraccessories.utils;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

public class BootsUtils {
    private static final double MAX_DISTANCE_SURFACE = 0.5;

    public static void walkOnWater(PlayerEntity player) {
        if (player.isSneaking()) {
            return;
        }

        if (player.isTouchingWater()) {
            BlockState blockStateFoot = player.getWorld().getBlockState(player.getBlockPos());
            BlockState blockStateHead = player.getWorld().getBlockState(player.getBlockPos().up());

            //Get the distance to the surface of the water
            double distanceToSurface = (Math.floor(player.getY()) + 0.85) - player.getY();

            if (distanceToSurface > MAX_DISTANCE_SURFACE) {
                player.setNoGravity(false);
                return;
            }

            if (blockStateFoot.getBlock() == Blocks.WATER && blockStateHead.getBlock() == Blocks.AIR) {
                player.setNoGravity(true);

                //Clamp the distance to the surface to a maximum of 0.04
                distanceToSurface = Math.min(0.04, distanceToSurface);

                //Set the player's velocity to the distance to the surface if it is greater than the player's current velocity
                player.setVelocity(player.getVelocity().x, Math.max(distanceToSurface, player.getVelocity().y), player.getVelocity().z);
            }
            MinecraftClient client = MinecraftClient.getInstance();

            //Avoid crash on loading screen
            if (client != null && client.player != null  && client.player.input != null)
            {
                client.player.setSprinting(!player.isSprinting() && client.player.input.pressingForward);

                if (!player.isOnGround() && client.player.input.jumping && !player.isSubmergedInWater()){
                    player.jump();
                }
            }
        } else {
            player.setNoGravity(false);

        }
    }
}
