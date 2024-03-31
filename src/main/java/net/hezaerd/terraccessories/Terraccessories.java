package net.hezaerd.terraccessories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.hezaerd.terraccessories.registry.TerraItems;

public class Terraccessories implements ModInitializer {
	public static final String MOD_ID = "terraccessories";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final net.hezaerd.terraccessories.TerraccessoriesConfig CONFIG = net.hezaerd.terraccessories.TerraccessoriesConfig.createAndLoad();

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
		
		TerraItems.register();
	}


}