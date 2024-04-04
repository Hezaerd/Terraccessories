package net.hezaerd.terraccessories.config;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.*;
import net.hezaerd.terraccessories.Terraccessories;
import net.hezaerd.terraccessories.utils.LibMod;

@Modmenu(modId = LibMod.MOD_ID)
@Config(name = "terraccessories", wrapperName = "TerraccessoriesConfig")
public class TerraccessoriesConfigModel {

    /* Tools */
    @SectionHeader("tools")

    @Nest
    public final NestedMirror mirror = new NestedMirror();

    @Nest
    public final NestedRodOfDiscord rod_of_discord = new NestedRodOfDiscord();


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

    public static class NestedRodOfDiscord {
        @Sync(Option.SyncMode.OVERRIDE_CLIENT)
        @RangeConstraint(min = 0, max = 32)
        public int range = 8;
        @Sync(Option.SyncMode.OVERRIDE_CLIENT)
        public boolean isUnbreakable = false;
        public boolean teleportParticle = true;
        public boolean teleportSound = true;
    }



    /* Trinkets */
    @SectionHeader("trinkets")

    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @RangeConstraint(min = 0, max = 10)
    public double hermes_boots_speed_bonus = 40;
    public boolean hermes_boots_sprint_particles = true;
}
