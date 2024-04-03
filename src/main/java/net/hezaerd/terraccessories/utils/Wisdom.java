package net.hezaerd.terraccessories.utils;

import com.google.common.collect.ImmutableList;
import net.hezaerd.terraccessories.Terraccessories;
import net.minecraft.util.Util;
import net.minecraft.util.math.random.Random;
import oshi.annotation.concurrent.Immutable;

import java.util.List;

public final class Wisdom {

    private Wisdom() {
    }

    private static final Random CRYSTAL_BALL = Random.create();
    public static List<String> ALL_THE_WISDOM = ImmutableList.of(
            "The early bird gets the worm, but the second mouse gets the cheese.",
            "The only thing we have to fear is fear itself.",
            "Did you know, by pressing alt+f4, you can make you game boot up 18x faster?",
            "The cake is a lie.",
            "The mitochondria is the powerhouse of the cell.",
            "baguette.",
            "Je suis monté!",
            "Death kills you.",
            "C'est pas gentil d'être méchant, mais c'est pas méchant d'être gentil.",
            "There is a 1% chance that instead of Frog, you get Froge!",
            "it seems to react to redstone",
            "I'm sorry, Dave. I'm afraid I can't do that.",
            "I am Error.",
            "I am Groot.",
            "Gentlemen, you can't fight in here! This is the War Room!",
            "I'm not a psychopath, I'm a high-functioning sociopath. Do your research.",
            "Luke, I am your father.",
            "ZA WORLD TOKI WO TOMARE"
    );

    public static void spread() {
        Terraccessories.LOGGER.info(Util.getRandom(ALL_THE_WISDOM, CRYSTAL_BALL));
    }
}
