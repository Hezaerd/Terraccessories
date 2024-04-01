package net.hezaerd.terraccessories.registry;

import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import net.hezaerd.terraccessories.items.DemonConch;
import net.hezaerd.terraccessories.items.MagicMirror;
import net.hezaerd.terraccessories.items.RandomTeleportationPotion;
import net.hezaerd.terraccessories.items.bucket.BottomlessLavaBucket;
import net.hezaerd.terraccessories.items.bucket.BottomlessWaterBucket;
import net.hezaerd.terraccessories.items.trinkets.Aglet;
import net.hezaerd.terraccessories.items.trinkets.AkletOfTheWind;
import net.hezaerd.terraccessories.items.trinkets.HermesBoots;
import net.minecraft.item.Item;

public class ItemsInit implements ItemRegistryContainer {

    public static final Item MAGIC_MIRROR = new MagicMirror();
    public static final Item ICE_MIRROR = new MagicMirror();
    public static final Item DEMON_CONCH = new DemonConch();
    public static final Item BOTTOMLESS_WATER_BUCKET = new BottomlessWaterBucket();
    public static final Item BOTTOMLESS_LAVA_BUCKET = new BottomlessLavaBucket();
    public static final Item RANDOM_TELEPORTATION_POTION = new RandomTeleportationPotion();
    public static final Item AGLET = new Aglet();
    public static final Item AKLET_OF_THE_WIND = new AkletOfTheWind();
    public static final Item HERMES_BOOTS = new HermesBoots();
}
