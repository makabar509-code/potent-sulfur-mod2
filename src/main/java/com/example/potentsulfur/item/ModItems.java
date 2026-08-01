package com.example.potentsulfur.item;

import com.example.potentsulfur.PotentSulfur;
import com.example.potentsulfur.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;

/**
 * Регистрация всех предметов мода.
 */
public final class ModItems {

    // Активная сера — предмет, которым "кормят" куб серы.
    public static final Item ACTIVE_SULFUR = registerItem(
            "active_sulfur",
            new Item(new Item.Properties())
    );

    // Обычный (не наполненный) предмет-блок Серного куба.
    public static final Item SULFUR_CUBE_ITEM = registerItem(
            "sulfur_cube",
            new BlockItem(ModBlocks.SULFUR_CUBE_CORE, new Item.Properties())
    );

    // Предмет "Наполненный Мощный Серный куб" — получается из ведра, хранит NBT постройки.
    public static final Item FILLED_SULFUR_CUBE_ITEM = registerItem(
            "filled_potent_sulfur_cube",
            new FilledSulfurCubeItem(new Item.Properties().stacksTo(1))
    );

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM,
                ResourceLocation.fromNamespaceAndPath(PotentSulfur.MOD_ID, name), item);
    }

    public static void register() {
        // Добавляем предметы во вкладку "Ингредиенты" творческого режима.
        // Если сборка ругается на этот API — просто удали блок ниже,
        // предметы всё равно будут работать через /give.
        try {
            net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
                    .modifyEntriesEvent(CreativeModeTabs.INGREDIENTS)
                    .register(entries -> {
                        entries.accept(ACTIVE_SULFUR);
                        entries.accept(SULFUR_CUBE_ITEM);
                    });
        } catch (Throwable ignored) {
            // API мог измениться в 26.2 — не критично для основной логики мода.
        }
        PotentSulfur.LOGGER.info("Зарегистрированы предметы Potent Sulfur");
    }
}
