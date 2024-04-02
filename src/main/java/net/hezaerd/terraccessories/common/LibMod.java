package net.hezaerd.terraccessories.common;

import net.minecraft.util.Identifier;

public class LibMod {
    public static final String MOD_NAME = "Terraccessories";
    public static final String MOD_ID = "terraccessories";

    public static Identifier id(String path) {
        return new Identifier(MOD_ID, path);
    }
}
