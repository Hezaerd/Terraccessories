package net.hezaerd.terraccessories.sound;


import net.hezaerd.terraccessories.Terraccessories;
import net.hezaerd.terraccessories.utils.LibMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSound {

    public static final SoundEvent TELEPORTATION = register("teleportation");
    public static final SoundEvent FART_JUMP = register("fart");

    private static SoundEvent register(String name) {
        Identifier id = new Identifier(LibMod.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
}
