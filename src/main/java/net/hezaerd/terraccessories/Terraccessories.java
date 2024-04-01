package net.hezaerd.terraccessories;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.hezaerd.terraccessories.registry.TerraItems;

public class Terraccessories implements ModInitializer {
	public static final String MOD_ID = "terraccessories";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final net.hezaerd.terraccessories.TerraccessoriesConfig CONFIG = net.hezaerd.terraccessories.TerraccessoriesConfig.createAndLoad();

	public static final Identifier MY_SOUND_ID = new Identifier("terraccessories:teleportation");
	public static SoundEvent MY_SOUND_EVENT = SoundEvent.of(MY_SOUND_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

		Registry.register(Registries.SOUND_EVENT, MY_SOUND_ID, MY_SOUND_EVENT);
		
		TerraItems.register();
	}


}