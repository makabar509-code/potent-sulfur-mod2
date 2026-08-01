package com.example.potentsulfur;

import com.example.potentsulfur.block.ModBlocks;
import com.example.potentsulfur.block.entity.ModBlockEntities;
import com.example.potentsulfur.item.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PotentSulfur implements ModInitializer {

    public static final String MOD_ID = "potentsulfur";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Инициализация Potent Sulfur Cubes...");
        ModBlocks.register();
        ModItems.register();
        ModBlockEntities.register();
        LOGGER.info("Potent Sulfur Cubes загружен.");
    }
}
