package net.hezaerd.terraccessories.registry;

import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import net.hezaerd.terraccessories.items.DemonConch;
import net.hezaerd.terraccessories.items.MagicConch;
import net.hezaerd.terraccessories.items.MagicMirror;
import net.hezaerd.terraccessories.items.RandomTeleportationPotion;
import net.hezaerd.terraccessories.items.BottomlessBucket;
import net.hezaerd.terraccessories.items.trinkets.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;

public class ItemsInit implements ItemRegistryContainer {

    public static final Item MAGIC_MIRROR = new MagicMirror();
    public static final Item ICE_MIRROR = new MagicMirror();
    public static final Item DEMON_CONCH = new DemonConch();
    public static final Item BOTTOMLESS_WATER_BUCKET = new BottomlessBucket(Fluids.WATER);
    public static final Item BOTTOMLESS_LAVA_BUCKET = new BottomlessBucket(Fluids.LAVA);
    public static final Item RANDOM_TELEPORTATION_POTION = new RandomTeleportationPotion();
    public static final Item AGLET = new Aglet();
    public static final Item JUMP_BOTTLE = new JumpBottle(SoundEvents.BLOCK_FIRE_EXTINGUISH, ParticleTypes.CLOUD);
    public static final Item FART_JUMP_BOTTLE = new JumpBottle(SoundInit.FART_JUMP, ParticleTypes.CLOUD);
    public static final Item AKLET_OF_THE_WIND = new AkletOfTheWind();

    public  static final Item MAGIC_CONCH = new MagicConch();
    public static final Item HERMES_BOOTS = new HermesBoots();
    public static final Item LUCKY_HORSESHOE = new LuckyHorseshoe();
}
