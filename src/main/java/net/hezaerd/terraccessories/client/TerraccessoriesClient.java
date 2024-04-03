package net.hezaerd.terraccessories.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.hezaerd.terraccessories.block.ModBlock;
import net.minecraft.client.render.RenderLayer;

public class TerraccessoriesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlock.TINKERER_WORKSHOP, RenderLayer.getTranslucent());
    }
}
