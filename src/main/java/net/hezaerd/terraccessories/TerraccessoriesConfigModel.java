package net.hezaerd.terraccessories;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.*;

@Modmenu(modId = Terraccessories.MOD_ID)
@Config(name = "terraccessories", wrapperName = "TerraccessoriesConfig")
public class TerraccessoriesConfigModel {

    /* Tools */
    @SectionHeader("tools")

    @Nest
    public final NestedMirror mirror = new NestedMirror();


    /* Mirrors */
    public static class NestedMirror {
        @Sync(Option.SyncMode.OVERRIDE_CLIENT)
        public boolean mirror_interdimensional = true;
        @Sync(Option.SyncMode.OVERRIDE_CLIENT)
        @RangeConstraint(min = 0, max = 10)
        public int mirror_cost = 1;
        @Sync(Option.SyncMode.OVERRIDE_CLIENT)
        public boolean mirror_debuff = true;
        @Nest
        public final NestedMirrorDebuff mirror_debuffs = new NestedMirrorDebuff();
    }

    public static class NestedMirrorDebuff {
        @Sync(Option.SyncMode.OVERRIDE_CLIENT)
        public boolean mirror_blindness = true;
        @Sync(Option.SyncMode.OVERRIDE_CLIENT)
        @RangeConstraint(min = 0, max = 20)
        public int mirror_blindness_duration = 2;
        @Sync(Option.SyncMode.OVERRIDE_CLIENT)
        public boolean mirror_slowness = true;
        @Sync(Option.SyncMode.OVERRIDE_CLIENT)
        @RangeConstraint(min = 0, max = 20)
        public int mirror_slowness_duration = 3;
    }

    /* Trinkets */
    @SectionHeader("trinkets")

    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @RangeConstraint(min = 0, max = 10)
    public double hermes_boots_speed_bonus = 40;
    public boolean hermes_boots_sprint_particles = true;
}
