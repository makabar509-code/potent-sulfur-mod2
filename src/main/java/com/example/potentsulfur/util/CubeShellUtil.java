package com.example.potentsulfur.util;

import com.example.potentsulfur.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;

/**
 * Отвечает за физическую перестройку куба в мире, когда игрок кормит
 * ядро Активной серой (куб растёт) или собирает его ведром (куб исчезает).
 *
 * Геометрия: ядро (core) — это нижний центральный блок куба.
 * Куб растёт вверх и в стороны от него. Стены/пол/потолок — блок-оболочка
 * (сквозь неё можно проходить), внутри — пустота, которую освобождает игрок.
 */
public final class CubeShellUtil {

    private CubeShellUtil() {}

    /** Половина стороны без центра, т.е. для size=3 -> radius=1, size=9 -> radius=4 */
    public static int radiusForSize(int size) {
        return (size - 1) / 2;
    }

    /**
     * Перестраивает оболочку с oldSize на newSize вокруг ядра corePos.
     * Ядро всегда остаётся нижним центральным блоком.
     */
    public static void regrow(Level world, BlockPos corePos, int oldSize, int newSize) {
        if (newSize > oldSize) {
            buildShell(world, corePos, newSize);
            if (oldSize > 1) {
                openUpOldShell(world, corePos, oldSize);
            }
        }
    }

    /** Строит внешнюю оболочку куба размера size вокруг corePos (нижний центр). */
    private static void buildShell(Level world, BlockPos corePos, int size) {
        int r = radiusForSize(size);
        int minX = corePos.getX() - r;
        int maxX = corePos.getX() + r;
        int minY = corePos.getY();
        int maxY = corePos.getY() + size - 1;
        int minZ = corePos.getZ() - r;
        int maxZ = corePos.getZ() + r;

        BlockState shell = ModBlocks.SULFUR_CUBE_SHELL.defaultBlockState();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    boolean onShell = x == minX || x == maxX || y == minY || y == maxY || z == minZ || z == maxZ;
                    if (!onShell) continue;

                    BlockPos pos = new BlockPos(x, y, z);
                    if (pos.equals(corePos)) continue; // здесь стоит блок ядра, не трогаем

                    BlockState current = world.getBlockState(pos);
                    // Не затираем блоки, которые игрок уже успел построить внутри между кормлениями.
                    if (current.isAir() || current.is(ModBlocks.SULFUR_CUBE_SHELL) || current.is(ModBlocks.SULFUR_CUBE_CORE)) {
                        world.setBlockAndUpdate(pos, shell);
                    }
                }
            }
        }
    }

    /** Превращает старую (теперь внутреннюю) оболочку в воздух, открывая пространство. */
    private static void openUpOldShell(Level world, BlockPos corePos, int oldSize) {
        int r = radiusForSize(oldSize);
        int minX = corePos.getX() - r;
        int maxX = corePos.getX() + r;
        int minY = corePos.getY();
        int maxY = corePos.getY() + oldSize - 1;
        int minZ = corePos.getZ() - r;
        int maxZ = corePos.getZ() + r;

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    boolean wasOnOldShell = x == minX || x == maxX || y == minY || y == maxY || z == minZ || z == maxZ;
                    // Не открываем нижний слой (y == minY) — это пол, он должен остаться,
                    // просто станет частью нового, большего пола на следующем шаге buildShell.
                    if (!wasOnOldShell || y == minY) continue;

                    BlockPos pos = new BlockPos(x, y, z);
                    if (pos.equals(corePos)) continue;

                    BlockState current = world.getBlockState(pos);
                    if (current.is(ModBlocks.SULFUR_CUBE_SHELL)) {
                        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
    }

    /** Полностью убирает куб (оболочку) при сборе ведром — не трогая ядро, его удаляют отдельно. */
    public static void clearShell(Level world, BlockPos corePos, int size) {
        int r = radiusForSize(size);
        int minX = corePos.getX() - r;
        int maxX = corePos.getX() + r;
        int minY = corePos.getY();
        int maxY = corePos.getY() + size - 1;
        int minZ = corePos.getZ() - r;
        int maxZ = corePos.getZ() + r;

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (pos.equals(corePos)) continue;
                    BlockState current = world.getBlockState(pos);
                    if (current.is(ModBlocks.SULFUR_CUBE_SHELL)) {
                        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
    }

    public static BlockPos getMin(BlockPos corePos, int size) {
        int r = radiusForSize(size);
        return new BlockPos(corePos.getX() - r, corePos.getY(), corePos.getZ() - r);
    }

    public static BlockPos getMax(BlockPos corePos, int size) {
        int r = radiusForSize(size);
        return new BlockPos(corePos.getX() + r, corePos.getY() + size - 1, corePos.getZ() + r);
    }
}
