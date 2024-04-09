package net.hezaerd.terraccessories.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.hezaerd.terraccessories.block.ModBlock;
import net.hezaerd.terraccessories.block.entity.renderer.TinkererWorkshopEntityRenderer;
import net.hezaerd.terraccessories.enchantment.ModEnchantment;
import net.hezaerd.terraccessories.fluid.ModFluid;
import net.hezaerd.terraccessories.item.ModItem;
import net.hezaerd.terraccessories.screen.ModScreenHandler;
import net.hezaerd.terraccessories.screen.TinkererWorkshopScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.enchantment.EnchantmentHelper;
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

        ModelPredicateProviderRegistry.register(ModItem.ROD_OF_DISCORD, new Identifier("harmony"), (itemStack, clientWorld, livingEntity, feur) -> {
             if (EnchantmentHelper.getLevel(ModEnchantment.HARMONY, itemStack) > 0) {
                 return 1.0F;
             } else {
                 return 0.0F;
             }
        });

        BlockEntityRendererFactories.register(ModBlock.Entities.TINKERER_WORKSHOP, TinkererWorkshopEntityRenderer::new);
    }
}
