package net.hezaerd.terraccessories.items.trinkets;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.hezaerd.terraccessories.Terraccessories;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;

public class JumpBottle extends TrinketItem {
    private static final double jumpHeight = 0.5;
    private static final double particlesSpeed = 0.1;
    private final SoundEvent jumpSound;
    private final DefaultParticleType jumpParticle;

    private enum JumpState {
        GROUND,
        JUMPING,
        DOUBLE_JUMPING
    }

    public JumpBottle(SoundEvent sound, DefaultParticleType particle) {
        super(new OwoItemSettings().group(Terraccessories.TERRACCESSORIES_GROUP).maxCount(1));

        jumpSound = sound;
        jumpParticle = particle;

        getDefaultStack().getOrCreateNbt().putBoolean("doubleJump", true);
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client == null || client.player == null)
            return;

        NbtCompound nbt = stack.getOrCreateNbt();

        if (client.player.isOnGround())
        {
            nbt.putInt("jump_state", JumpState.GROUND.ordinal());
        }
        else if (!isJumping() && nbt.getInt("jump_state") != JumpState.DOUBLE_JUMPING.ordinal())
        {
            nbt.putInt("jump_state", JumpState.JUMPING.ordinal());
        }
        else if (nbt.getInt("jump_state") == JumpState.JUMPING.ordinal())
        {
            nbt.putInt("jump_state", JumpState.DOUBLE_JUMPING.ordinal());
            entity.setVelocity(new Vec3d(entity.getVelocity().getX(), jumpHeight, entity.getVelocity().getZ()));

            entity.getWorld().playSound(null, entity.getBlockPos(), jumpSound, SoundCategory.PLAYERS, 1.0F, 1.0F);

            for (int i = 0; i < 50; i++)
            {
                client.world.addParticle(jumpParticle, entity.getX(), entity.getY(), entity.getZ(), Math.cos(i) * particlesSpeed, 0.0D, Math.sin(i) * particlesSpeed);
            }
        }
    }

    private boolean isJumping() {
        MinecraftClient client = MinecraftClient.getInstance();
        return client != null && client.player != null && client.player.input != null && client.player.input.jumping;
    }
}
