package net.hezaerd.terraccessories.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.hezaerd.terraccessories.items.InfiniteWaterBucket;
import net.hezaerd.terraccessories.items.MagicMirror;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TerraItems {
    public static final MagicMirror MAGIC_MIRROR = new MagicMirror(new FabricItemSettings());
    public static final InfiniteWaterBucket INFINITE_WATER_BUCKET = new InfiniteWaterBucket(new FabricItemSettings());

    private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(INFINITE_WATER_BUCKET))
            .displayName(Text.translatable("itemGroup.terraccessories"))
            .entries((context, entries) -> {
                entries.add(INFINITE_WATER_BUCKET);
                entries.add(MAGIC_MIRROR);
            }).build();

    public static void register() {
        Registry.register(Registries.ITEM_GROUP, new Identifier("terraccessories", "terraccessories"), ITEM_GROUP);

        Registry.register(Registries.ITEM, new Identifier("terraccessories", "magic_mirror"), MAGIC_MIRROR);
        Registry.register(Registries.ITEM, new Identifier("terraccessories", "infinite_water_bucket"), INFINITE_WATER_BUCKET);
    }
}
