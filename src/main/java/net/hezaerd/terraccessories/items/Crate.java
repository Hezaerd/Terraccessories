package net.hezaerd.terraccessories.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Crate extends Item {
    private static final int cooldown = 10;

    public Crate(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.playSound(SoundEvents.BLOCK_WOOD_BREAK, 1.0F, 1.0F);
        user.getStackInHand(hand).decrement(1);
        user.getItemCooldownManager().set(this, cooldown);

        return super.use(world, user, hand);
    }
}
