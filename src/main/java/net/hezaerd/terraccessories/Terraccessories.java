package net.hezaerd.terraccessories;

import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.hezaerd.terraccessories.registry.ItemsInit;

public class Terraccessories implements ModInitializer {
	public static final String MOD_ID = "terraccessories";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final net.hezaerd.terraccessories.TerraccessoriesConfig CONFIG = net.hezaerd.terraccessories.TerraccessoriesConfig.createAndLoad();
	public static final OwoItemGroup TERRACCESSORIES_GROUP = OwoItemGroup.builder(id("main"), () -> Icon.of(ItemsInit.MAGIC_MIRROR))
			.initializer(group -> {
				group.addTab(Icon.of(ItemsInit.MAGIC_MIRROR), "Terraccessories", null, true);
			}).build();

	@Override
	public void onInitialize() {

		FieldRegistrationHandler.register(ItemsInit.class, MOD_ID, false);

		TERRACCESSORIES_GROUP.initialize();
	}

	public static Identifier id(String path) {
		return new Identifier(MOD_ID, path);
	}
}