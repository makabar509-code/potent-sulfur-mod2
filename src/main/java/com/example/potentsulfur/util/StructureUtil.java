package com.example.potentsulfur.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

/**
 * Обёртка над ванильным StructureTemplate (тем же, что использует Structure Block),
 * чтобы сохранить объём мира (стены куба + всё, что игрок построил внутри)
 * в NBT, а затем вставить его обратно в мир при "распечатывании" ножницами.
 */
public final class StructureUtil {

    private StructureUtil() {}

    /** Сохраняет прямоугольную область [min..max] в NBT-тег (аналог "Save" у Structure Block). */
    public static CompoundTag saveRegion(Level world, BlockPos min, BlockPos max) {
        if (!(world instanceof ServerLevel serverLevel)) return new CompoundTag();

        StructureTemplateManager manager = serverLevel.getStructureManager();
        StructureTemplate template = new StructureTemplate();

        BlockPos size = new BlockPos(
                max.getX() - min.getX() + 1,
                max.getY() - min.getY() + 1,
                max.getZ() - min.getZ() + 1
        );

        template.fillFromWorld(world, min, size, true, Blocks.STRUCTURE_VOID);

        HolderLookup.Provider registries = serverLevel.registryAccess();
        return template.save(new CompoundTag());
    }

    /** Убирает всё содержимое региона [min..max], превращая его в воздух. */
    public static void clearRegion(Level world, BlockPos min, BlockPos max) {
        for (int x = min.getX(); x <= max.getX(); x++) {
            for (int y = min.getY(); y <= max.getY(); y++) {
                for (int z = min.getZ(); z <= max.getZ(); z++) {
                    world.setBlockAndUpdate(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState());
                }
            }
        }
    }

    /** Вставляет ранее сохранённую структуру обратно в мир, "нижним центром" на anchor. */
    public static void placeRegion(Level world, BlockPos anchor, CompoundTag structureNbt) {
        if (!(world instanceof ServerLevel serverLevel)) return;
        if (structureNbt == null || structureNbt.isEmpty()) return;

        StructureTemplate template = new StructureTemplate();
        HolderLookup.Provider registries = serverLevel.registryAccess();
        template.load(registries, structureNbt);

        StructurePlaceSettings settings = new StructurePlaceSettings()
                .setRotation(Rotation.NONE)
                .setMirror(Mirror.NONE)
                .setIgnoreEntities(false);

        // Структура сохранялась от min-угла куба, размещаем от той же логики,
        // anchor здесь — это позиция размещённого "наполненного" блока (нижний центр).
        BlockPos size = template.getSize();
        BlockPos placePos = anchor.offset(-(size.getX() - 1) / 2, 0, -(size.getZ() - 1) / 2);

        template.placeInWorld(serverLevel, placePos, placePos, settings, world.getRandom(), 3);
    }
}
