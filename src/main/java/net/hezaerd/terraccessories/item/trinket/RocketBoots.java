package net.hezaerd.terraccessories.item.trinket;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.hezaerd.terraccessories.Terraccessories;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.random.Random;

public class RocketBoots extends TrinketItem {
    private int currentTicks = 0;
    private int maxTicks = 20;

    private static double force = 0.15;

    private enum JumpState {
        GROUND,
        AIR,
        PRE_CAHRGING,
        CHARGING,
        EMPTY
    }

    public RocketBoots() {
        super(new OwoItemSettings().group(Terraccessories.TERRACCESSORIES_GROUP).maxCount(1));
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client == null || client.player == null)
            return;

        NbtCompound nbt = stack.getOrCreateNbt();

        if (client.player.isOnGround())
            nbt.putInt("jump_state", RocketBoots.JumpState.GROUND.ordinal());

        switch (JumpState.values()[nbt.getInt("jump_state")])
        {
            case GROUND -> {
                currentTicks = 0;

                if (isJumping() || !client.player.isOnGround())
                    nbt.putInt("jump_state", JumpState.AIR.ordinal());
            }
            case AIR -> {
                if (!isJumping())
                    nbt.putInt("jump_state", JumpState.PRE_CAHRGING.ordinal());
            }
            case PRE_CAHRGING -> {
                if (isJumping())
                    nbt.putInt("jump_state", JumpState.CHARGING.ordinal());
            }
            case CHARGING -> {
                currentTicks++;

                if (!isJumping())
                {
                    nbt.putInt("jump_state", JumpState.EMPTY.ordinal());
                    return;
                }

                entity.addVelocity(0, force, 0);

                entity.getWorld().addParticle(ParticleTypes.CLOUD, entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
                entity.getWorld().addParticle(ParticleTypes.FLAME, entity.getX(), entity.getY(), entity.getZ(), Random.create().nextBetween(-1, 1) * 0.1, 0, Random.create().nextBetween(-1, 1) * 0.1);

                if (currentTicks >= maxTicks)
                    nbt.putInt("jump_state", JumpState.EMPTY.ordinal());
            }
            case EMPTY -> {

            }
        }
    }

    private boolean isJumping() {
        MinecraftClient client = MinecraftClient.getInstance();
        return client != null && client.player != null && client.player.input != null && client.player.input.jumping;
    }
}
