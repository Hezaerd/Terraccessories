package net.hezaerd.terraccessories.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.hezaerd.terraccessories.block.ModBlock;
import net.hezaerd.terraccessories.fluid.ModFluid;
import net.hezaerd.terraccessories.screen.ModScreenHandler;
import net.hezaerd.terraccessories.screen.TinkererWorkshopScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class TerraccessoriesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandler.TINKERER_WORKSHOP_SCREEN_HANDLER_TYPE, TinkererWorkshopScreen::new);

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlock.TINKERER_WORKSHOP, RenderLayer.getTranslucent());

        FluidRenderHandlerRegistry.INSTANCE.register(ModFluid.STILL_SHIMMER, ModFluid.FLOWING_SHIMMER, new SimpleFluidRenderHandler(
                new Identifier("minecraft:block/water_still"),
                new Identifier("minecraft:block/water_flow"),
                0x800080
        ));

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), ModFluid.STILL_SHIMMER, ModFluid.FLOWING_SHIMMER);
    }
}
