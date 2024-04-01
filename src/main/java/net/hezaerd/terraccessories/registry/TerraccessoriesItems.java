package net.hezaerd.terraccessories.registry;


import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import net.hezaerd.terraccessories.items.DemonConch;
import net.hezaerd.terraccessories.items.RandomTeleportationPotion;
import net.hezaerd.terraccessories.items.bucket.BottomlessLavaBucket;
import net.hezaerd.terraccessories.items.bucket.BottomlessWaterBucket;
import net.hezaerd.terraccessories.items.mirrors.IceMirror;
import net.hezaerd.terraccessories.items.mirrors.MagicMirror;
import net.minecraft.item.Item;

public class TerraccessoriesItems implements ItemRegistryContainer {

    public static final Item MAGIC_MIRROR = new MagicMirror();
    public static final Item ICE_MIRROR = new IceMirror();
    public static final Item DEMON_CONCH = new DemonConch();
    public static final Item BOTTOMLESS_WATER_BUCKET = new BottomlessWaterBucket();
    public static final Item BOTTOMLESS_LAVA_BUCKET = new BottomlessLavaBucket();
    public static final Item RANDOM_TELEPORTATION_POTION = new RandomTeleportationPotion();

}
