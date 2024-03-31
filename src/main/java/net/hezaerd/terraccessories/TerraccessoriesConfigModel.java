package net.hezaerd.terraccessories;

import io.wispforest.owo.config.annotation.Config;

@Config(name = "terraccessories-config", wrapperName = "TerraccessoriesConfig")
public class TerraccessoriesConfigModel {
    /* Magic Mirror */
    public boolean magicMirrorInterdimensional = true;
    public boolean magicMirrorDoDebuff = true;
    public int magicMirrorCooldown = 200;
    public int magicMirrorLevelCost = 1;

    /* Ice Mirror */
    public boolean iceMirrorInterdimensional = true;
    public boolean iceMirrorDoDebuff = true;
    public int iceMirrorCooldown = 200;
    public int iceMirrorLevelCost = 1;
}
