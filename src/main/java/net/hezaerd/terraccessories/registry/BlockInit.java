package net.hezaerd.terraccessories.registry;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.BlockEntityRegistryContainer;
import io.wispforest.owo.registration.reflect.BlockRegistryContainer;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.hezaerd.terraccessories.Terraccessories;
import net.hezaerd.terraccessories.block.custom.TinkererWorkshop;
import net.hezaerd.terraccessories.block.entity.TinkererWorkshopEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;

public class BlockInit implements BlockRegistryContainer {

    public static final Block TINKERER_WORKSHOP = new TinkererWorkshop();

    @Override
    public BlockItem createBlockItem(Block block, String identifier) {
        return new BlockItem(block, new OwoItemSettings().group(Terraccessories.TERRACCESSORIES_GROUP));
    }

    public static final class Entities implements BlockEntityRegistryContainer {

        public static final BlockEntityType<TinkererWorkshopEntity> TINKERER_WORKSHOP = FabricBlockEntityTypeBuilder.create(TinkererWorkshopEntity::new, BlockInit.TINKERER_WORKSHOP).build();
    }
}

