package net.hezaerd.terraccessories.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;

import net.hezaerd.terraccessories.items.*;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;


public class TerraItems {
    public static final MagicMirror MAGIC_MIRROR = new MagicMirror(new FabricItemSettings());
    public static final IceMirror ICE_MIRROR = new IceMirror(new FabricItemSettings());
    public static final BottomlessWaterBucket BOTTOMLESS_WATER_BUCKET = new BottomlessWaterBucket(new FabricItemSettings());
    public static final BottomlessLavaBucket BOTTOMLESS_LAVA_BUCKET = new BottomlessLavaBucket(new FabricItemSettings());
    public static final Crate CRATE = new Crate(new FabricItemSettings());

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
                entries.add(DEMON_CONCH);
            }).build();

    public static void register() {
        Registry.register(Registries.ITEM_GROUP, new Identifier("terraccessories", "terraccessories"), ITEM_GROUP);

        Registry.register(Registries.ITEM, new Identifier("terraccessories", "magic_mirror"), MAGIC_MIRROR);
        Registry.register(Registries.ITEM, new Identifier("terraccessories", "ice_mirror"), ICE_MIRROR);
        Registry.register(Registries.ITEM, new Identifier("terraccessories", "bottomless_water_bucket"), BOTTOMLESS_WATER_BUCKET);
        Registry.register(Registries.ITEM, new Identifier("terraccessories", "bottomless_lava_bucket"), BOTTOMLESS_LAVA_BUCKET);
        Registry.register(Registries.ITEM, new Identifier("terraccessories", "crate"), CRATE);
        Registry.register(Registries.ITEM, new Identifier("terraccessories", "demon_conch"), DEMON_CONCH);
    }
}
