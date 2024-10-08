package net.hezaerd.terraccessories;

import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import net.hezaerd.terraccessories.block.ModBlock;
import net.hezaerd.terraccessories.fluid.ModFluid;
import net.hezaerd.terraccessories.enchantment.ModEnchantment;
import net.hezaerd.terraccessories.loottable.ModLootTableModifier;
import net.hezaerd.terraccessories.recipe.ModRecipe;
import net.hezaerd.terraccessories.screen.ModScreenHandler;
import net.hezaerd.terraccessories.statuseffect.ModStatusEffect;
import net.hezaerd.terraccessories.utils.LibMod;
import net.hezaerd.terraccessories.utils.Wisdom;

import net.fabricmc.api.ModInitializer;
import net.hezaerd.terraccessories.item.ModItem;

public class Terraccessories implements ModInitializer {
	public static final net.hezaerd.terraccessories.config.TerraccessoriesConfig CONFIG =
			net.hezaerd.terraccessories.config.TerraccessoriesConfig.createAndLoad();

	public static final OwoItemGroup TERRACCESSORIES_GROUP = OwoItemGroup.builder(LibMod.id("main"), () -> Icon.of(ModItem.MAGIC_MIRROR))
			.initializer(group -> {
				group.addTab(Icon.of(ModItem.MAGIC_MIRROR), "Terraccessories", null, true);
			}).build();

	@Override
	public void onInitialize() {
		ModLootTableModifier.ModifyLootTables();

		ModScreenHandler.init();
		ModRecipe.register();
		FieldRegistrationHandler.register(ModItem.class, LibMod.MOD_ID, false);
		FieldRegistrationHandler.register(ModBlock.class, LibMod.MOD_ID, true);
		ModFluid.init();
		ModStatusEffect.init();
		ModEnchantment.init();

		TERRACCESSORIES_GROUP.initialize();

		Wisdom.spread();
	}
}