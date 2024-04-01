package net.hezaerd.terraccessories.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;

import net.hezaerd.terraccessories.items.*;
import net.hezaerd.terraccessories.items.bucket.BottomlessLavaBucket;
import net.hezaerd.terraccessories.items.bucket.BottomlessWaterBucket;
import net.hezaerd.terraccessories.items.mirrors.IceMirror;
import net.hezaerd.terraccessories.items.mirrors.MagicMirror;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public class TerraItems {
    public static final MagicMirror MAGIC_MIRROR = new MagicMirror(new FabricItemSettings());
    public static final IceMirror ICE_MIRROR = new IceMirror(new FabricItemSettings());
    public static final BottomlessWaterBucket BOTTOMLESS_WATER_BUCKET = new BottomlessWaterBucket(Fluids.WATER, new FabricItemSettings());
    public static final BottomlessLavaBucket BOTTOMLESS_LAVA_BUCKET = new BottomlessLavaBucket(Fluids.LAVA, new FabricItemSettings());
    public static final Crate CRATE = new Crate(new FabricItemSettings());
    public static final RandomTeleportationPotion RANDOM_TELEPORTATION_POTION = new RandomTeleportationPotion(new FabricItemSettings());
    public  static final DemonConch DEMON_CONCH = new DemonConch(new FabricItemSettings());

    private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(MAGIC_MIRROR))
            .displayName(Text.translatable("itemGroup.terraccessories"))
            .entries((context, entries) -> {
                entries.add(MAGIC_MIRROR);
                entries.add(ICE_MIRROR);
                entries.add(BOTTOMLESS_WATER_BUCKET);
                entries.add(BOTTOMLESS_LAVA_BUCKET);
                entries.add(CRATE);
                entries.add(RANDOM_TELEPORTATION_POTION);
                entries.add(DEMON_CONCH);
            }).build();

    public static void register() {
        Registry.register(Registries.ITEM_GROUP, new Identifier("terraccessories", "terraccessories"), ITEM_GROUP);

        Registry.register(Registries.ITEM, new Identifier("terraccessories", "mirrors.magic_mirror"), MAGIC_MIRROR);
        Registry.register(Registries.ITEM, new Identifier("terraccessories", "mirrors.ice_mirror"), ICE_MIRROR);
        Registry.register(Registries.ITEM, new Identifier("terraccessories", "bucket/bottomless_water_bucket"), BOTTOMLESS_WATER_BUCKET);
        Registry.register(Registries.ITEM, new Identifier("terraccessories", "bucket/bottomless_lava_bucket"), BOTTOMLESS_LAVA_BUCKET);
        Registry.register(Registries.ITEM, new Identifier("terraccessories", "crate"), CRATE);
        Registry.register(Registries.ITEM, new Identifier("terraccessories", "random_teleportation_potion"), RANDOM_TELEPORTATION_POTION);
        Registry.register(Registries.ITEM, new Identifier("terraccessories", "demon_conch"), DEMON_CONCH);
    }
}
