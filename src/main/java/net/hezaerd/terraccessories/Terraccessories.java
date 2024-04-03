package net.hezaerd.terraccessories;

import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import net.hezaerd.terraccessories.registry.BlockInit;
import net.hezaerd.terraccessories.registry.RecipesInit;
import net.hezaerd.terraccessories.registry.ScreenHandlerInit;
import net.hezaerd.terraccessories.statuseffect.ModStatusEffect;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.hezaerd.terraccessories.items.ModItems;

public class Terraccessories implements ModInitializer {
	public static final String MOD_ID = "terraccessories";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final net.hezaerd.terraccessories.config.TerraccessoriesConfig CONFIG = net.hezaerd.terraccessories.config.TerraccessoriesConfig.createAndLoad();
	public static final OwoItemGroup TERRACCESSORIES_GROUP = OwoItemGroup.builder(id("main"), () -> Icon.of(ModItems.MAGIC_MIRROR))
			.initializer(group -> {
				group.addTab(Icon.of(ModItems.MAGIC_MIRROR), "Terraccessories", null, true);
			}).build();

	@Override
	public void onInitialize() {
		ScreenHandlerInit.init();
		RecipesInit.register();

		FieldRegistrationHandler.register(ModItems.class, MOD_ID, false);
		FieldRegistrationHandler.register(BlockInit.class, MOD_ID, true);

		ModStatusEffect.init();

		TERRACCESSORIES_GROUP.initialize();
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}