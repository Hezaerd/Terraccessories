package net.hezaerd.terraccessories.utils;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtElement;
import org.lwjgl.glfw.GLFW;

public class Jesus {


    public static void walkOnWater(PlayerEntity player) {

        if (player.isSneaking()) {
            return;
        }

        if (player.isTouchingWater()) {
            BlockState blockState = player.getWorld().getBlockState(player.getBlockPos().down());
            if (blockState.getBlock() == Blocks.WATER) {
                player.setNoGravity(true);
                player.setVelocity(player.getVelocity().x, 0.001, player.getVelocity().z);
            }
            MinecraftClient client = MinecraftClient.getInstance();
            if (!player.isSprinting())
            {
                client.player.setSprinting(true);
            }
            if (!player.isOnGround() && client.player.input.jumping){
                player.jump();
            }
        } else {
            player.setNoGravity(false);

        }
    }
}
