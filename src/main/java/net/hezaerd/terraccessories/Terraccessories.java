package net.hezaerd.terraccessories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import net.hezaerd.terraccessories.items.MagicMirror;


public class Terraccessories implements ModInitializer {
	public static final String MOD_ID = "terraccessories";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final MagicMirror MAGIC_MIRROR = new MagicMirror(new FabricItemSettings()
			.maxCount(1));

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

		Registry.register(Registries.ITEM, new Identifier("terraccessories", "magic_mirror"), MAGIC_MIRROR);
	}


}