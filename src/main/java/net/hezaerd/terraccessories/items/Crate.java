package net.hezaerd.terraccessories.items;

import net.hezaerd.terraccessories.registry.TerraItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class Crate extends Item {
    private static final int cooldown = 2;

    public Crate(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.playSound(SoundEvents.BLOCK_WOOD_BREAK, 1.0F, 1.0F);
        user.getStackInHand(hand).decrement(1);
        user.getItemCooldownManager().set(this, cooldown);

        if (Random.create().nextBetween(0, 100) == 0)
            user.getInventory().insertStack(new ItemStack(TerraItems.MAGIC_MIRROR, 1));
        if (Random.create().nextBetween(0, 200) == 0)
            user.getInventory().insertStack(new ItemStack(TerraItems.INFINITE_LAVA_BUCKET, 1));
        if (Random.create().nextBetween(0, 100) == 0)
            user.getInventory().insertStack(new ItemStack(TerraItems.DEMON_CONCH, 1));
        if (Random.create().nextBetween(0, 100) == 0)
            user.getInventory().insertStack(new ItemStack(TerraItems.INFINITE_WATER_BUCKET, 1));

        return super.use(world, user, hand);
    }
}
