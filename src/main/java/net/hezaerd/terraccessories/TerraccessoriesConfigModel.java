package net.hezaerd.terraccessories;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.*;

@Modmenu(modId = Terraccessories.MOD_ID)
@Config(name = "terraccessories", wrapperName = "TerraccessoriesConfig")
public class TerraccessoriesConfigModel {
    /* General */
    @SectionHeader("general")
    public boolean enable_accessories = true;

    /* Tools */
    @SectionHeader("tools")

    @Nest
    public final NestedMirror mirror = new NestedMirror();


    /* Mirrors */
    public static class NestedMirror {
        @Sync(Option.SyncMode.OVERRIDE_CLIENT)
        public boolean mirror_interdimensional = true;
        @Sync(Option.SyncMode.OVERRIDE_CLIENT)
        public boolean mirror_debuff = true;
        @Sync(Option.SyncMode.OVERRIDE_CLIENT)
        @RangeConstraint(min = 0, max = 60)
        public int mirror_cooldown = 20;
        @Sync(Option.SyncMode.OVERRIDE_CLIENT)
        @RangeConstraint(min = 0, max = 10)
        public int mirror_cost = 1;

    }
}
