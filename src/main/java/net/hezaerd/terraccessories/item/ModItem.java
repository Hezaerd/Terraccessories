package net.hezaerd.terraccessories.item;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import net.hezaerd.terraccessories.Terraccessories;
import net.hezaerd.terraccessories.item.trinket.*;
import net.hezaerd.terraccessories.sound.ModSound;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;

public class ModItem implements ItemRegistryContainer {

    /* Tools */
    public static final Item MAGIC_MIRROR = new MagicMirror();
    public static final Item ICE_MIRROR = new MagicMirror();
    public static final Item DEMON_CONCH = new DemonConch();
    public  static final Item MAGIC_CONCH = new MagicConch();
    public static final Item BOTTOMLESS_WATER_BUCKET = new BottomlessBucket(Fluids.WATER);
    public static final Item BOTTOMLESS_LAVA_BUCKET = new BottomlessBucket(Fluids.LAVA);
    public static final Item ROD_OF_DISCORD = new RodOfDiscord();
    public static final Item STAFF_OF_REGROWTH = new StaffOfRegrowth();
    public static final Item AXE_OF_REGROWTH = new AxeOfRegrowth(ToolMaterials.NETHERITE, 9.0F, -3.1F);

    /* Potion */
    public static final Item RANDOM_TELEPORTATION_POTION = new RandomTeleportationPotion();

    /* Trinkets */
    public static final Item AGLET = new Aglet();
    public static final Item JUMP_BOTTLE = new JumpBottle(SoundEvents.BLOCK_FIRE_EXTINGUISH, ParticleTypes.CLOUD);
    public static final Item FART_JUMP_BOTTLE = new JumpBottle(ModSound.FART_JUMP, ParticleTypes.CLOUD);
    public static final Item AKLET_OF_THE_WIND = new AkletOfTheWind();
    public static final Item HERMES_BOOTS = new HermesBoots();
    public static final Item LUCKY_HORSESHOE = new LuckyHorseshoe();

    public static final Item WATER_WALKING_BOOTS = new WaterWalkingBoots();
    public static final Item ROCKET_BOOTS = new RocketBoots();
//    public static final Item BAND_OF_REGENERATION = new BandOfRegeneration(); // STAND-BY

    /* Resources */
    public static final Item SOUL_OF_FRIGHT = new Soul(64);
    public static final Item SOUL_OF_MIGHT = new Soul(64);
    public static final Item SOUL_OF_SIGHT = new Soul(64);
    public static final Item SOUL_OF_ELEMENTS = new Soul(16);
}