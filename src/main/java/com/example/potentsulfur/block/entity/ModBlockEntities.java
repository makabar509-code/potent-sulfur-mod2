package com.example.potentsulfur.block.entity;

import com.example.potentsulfur.PotentSulfur;
import com.example.potentsulfur.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class ModBlockEntities {

    public static final BlockEntityType<SulfurCubeCoreBlockEntity> SULFUR_CUBE_CORE_BE =
            Registry.register(
                    BuiltInRegistries.BLOCK_ENTITY_TYPE,
                    ResourceLocation.fromNamespaceAndPath(PotentSulfur.MOD_ID, "sulfur_cube_core"),
                    BlockEntityType.Builder.of(SulfurCubeCoreBlockEntity::new, ModBlocks.SULFUR_CUBE_CORE).build(null)
            );

    public static final BlockEntityType<FilledSulfurCubeBlockEntity> FILLED_SULFUR_CUBE_BE =
            Registry.register(
                    BuiltInRegistries.BLOCK_ENTITY_TYPE,
                    ResourceLocation.fromNamespaceAndPath(PotentSulfur.MOD_ID, "filled_potent_sulfur_cube"),
                    BlockEntityType.Builder.of(FilledSulfurCubeBlockEntity::new, ModBlocks.FILLED_SULFUR_CUBE).build(null)
            );

    public static void register() {
        PotentSulfur.LOGGER.info("Зарегистрированы block entity Potent Sulfur");
    }
}
