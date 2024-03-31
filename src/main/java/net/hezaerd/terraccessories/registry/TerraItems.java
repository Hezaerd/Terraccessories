package net.hezaerd.terraccessories.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.hezaerd.terraccessories.items.MagicMirror;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TerraItems {
    public static final MagicMirror MAGIC_MIRROR = new MagicMirror(new FabricItemSettings());

    public static void register() {
        Registry.register(Registries.ITEM, new Identifier("terraccessories", "magic_mirror"), MAGIC_MIRROR);
    }
}
