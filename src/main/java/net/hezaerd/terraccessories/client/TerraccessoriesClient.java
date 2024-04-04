package net.hezaerd.terraccessories.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.hezaerd.terraccessories.block.ModBlock;
import net.hezaerd.terraccessories.screen.ModScreenHandler;
import net.hezaerd.terraccessories.screen.TinkererWorkshopScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;

public class TerraccessoriesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandler.TINKERER_WORKSHOP_SCREEN_HANDLER_TYPE, TinkererWorkshopScreen::new);

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlock.TINKERER_WORKSHOP, RenderLayer.getTranslucent());
    }
}
