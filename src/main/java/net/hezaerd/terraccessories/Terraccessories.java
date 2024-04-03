package net.hezaerd.terraccessories;

import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import net.hezaerd.terraccessories.block.ModBlock;
import net.hezaerd.terraccessories.recipe.ModRecipe;
import net.hezaerd.terraccessories.screen.ModScreenHandler;
import net.hezaerd.terraccessories.statuseffect.ModStatusEffect;
import net.hezaerd.terraccessories.utils.LibMod;
import net.hezaerd.terraccessories.utils.Wisdom;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.hezaerd.terraccessories.items.ModItems;

public class Terraccessories implements ModInitializer {
	public static final net.hezaerd.terraccessories.config.TerraccessoriesConfig CONFIG =
			net.hezaerd.terraccessories.config.TerraccessoriesConfig.createAndLoad();

	public static final OwoItemGroup TERRACCESSORIES_GROUP = OwoItemGroup.builder(LibMod.id("main"), () -> Icon.of(ModItems.MAGIC_MIRROR))
			.initializer(group -> {
				group.addTab(Icon.of(ModItems.MAGIC_MIRROR), "Terraccessories", null, true);
			}).build();

	@Override
	public void onInitialize() {
		ModScreenHandler.init();
		ModRecipe.register();
		FieldRegistrationHandler.register(ModItems.class, LibMod.MOD_ID, false);
		FieldRegistrationHandler.register(ModBlock.class, LibMod.MOD_ID, true);
		ModStatusEffect.init();

		TERRACCESSORIES_GROUP.initialize();

		Wisdom.spread();
	}
}