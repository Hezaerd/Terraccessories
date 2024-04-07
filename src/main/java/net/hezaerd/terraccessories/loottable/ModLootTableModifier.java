package net.hezaerd.terraccessories.loottable;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.hezaerd.terraccessories.item.ModItem;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

public class ModLootTableModifier {

    public static final Identifier JUNGLE_TEMPLE_ID =
        new Identifier("minecraft", "chests/jungle_temple");

    public static final Identifier WITHER_ID =
        new Identifier("minecraft", "entities/wither");

    public static final Identifier WARDEN_ID =
        new Identifier("minecraft", "entities/warden");

    public static final Identifier ENDER_DRAGON =
        new Identifier("minecraft", "entities/ender_dragon");

    public static void ModifyLootTables() {
        LootTableEvents.MODIFY.register(((resourceManager, lootManager, id, tableBuilder, source) -> {

            if(JUNGLE_TEMPLE_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1)) // 1 item
                        .conditionally(RandomChanceLootCondition.builder(0.33f)) // 33% chance
                        .with(ItemEntry.builder(ModItem.STAFF_OF_REGROWTH))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());

                tableBuilder.pool(poolBuilder);
            }

            if(WITHER_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1)) // 1 item
                        .conditionally(RandomChanceLootCondition.builder(1.0f)) // 100% chance
                        .with(ItemEntry.builder(ModItem.SOUL_OF_FRIGHT))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());

                tableBuilder.pool(poolBuilder);
            }

            if(WARDEN_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1)) // 1 item
                        .conditionally(RandomChanceLootCondition.builder(1.0f)) // 100% chance
                        .with(ItemEntry.builder(ModItem.SOUL_OF_SIGHT))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());

                tableBuilder.pool(poolBuilder);
            }

            if(ENDER_DRAGON.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1)) // 1 item
                        .conditionally(RandomChanceLootCondition.builder(1.0f)) // 100% chance
                        .with(ItemEntry.builder(ModItem.SOUL_OF_MIGHT))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 1.0f)).build());

                tableBuilder.pool(poolBuilder);
            }

        }));
    }
}
