package net.hezaerd.terraccessories.items;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.sound.SoundEvents;

public class MagicMirror extends Item {

    public static final String MOD_ID = "terraccessories";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public MagicMirror(Settings settings) {
        super(settings);
    }

    // print out "Magic Mirror" when the item is right-clicked
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        LOGGER.info("User " + player.getName().getString() + " right-clicked with Magic Mirror");

        player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);


        return TypedActionResult.success(player.getStackInHand(hand));
    }
}
