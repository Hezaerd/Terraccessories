package net.hezaerd.terraccessories.registry;

import net.hezaerd.terraccessories.Terraccessories;
import net.hezaerd.terraccessories.block.entity.TinkererScreenHandler;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;

public class ScreenHandlerInit {
    public static final ScreenHandlerType<TinkererScreenHandler> TINKERER_WORKSHOP_SCREEN_HANDLER_TYPE = new ScreenHandlerType<>(TinkererScreenHandler::new, FeatureFlags.DEFAULT_ENABLED_FEATURES);

    public static void init() {
        Registry.register(Registries.SCREEN_HANDLER, Terraccessories.id("tinkerer_workshop"), TINKERER_WORKSHOP_SCREEN_HANDLER_TYPE);
    }
}
