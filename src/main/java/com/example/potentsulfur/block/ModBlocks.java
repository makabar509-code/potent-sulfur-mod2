package com.example.potentsulfur.block;

import com.example.potentsulfur.PotentSulfur;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.SoundType;

public final class ModBlocks {

    // Ядро куба: то, во что превращается предмет "Sulfur Cube" при установке в мире.
    // Именно у этого блока хранится счётчик кормления и логика роста.
    public static final Block SULFUR_CUBE_CORE = registerBlock(
            "sulfur_cube",
            new SulfurCubeCoreBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .strength(1.5f)
                    .sound(SoundType.AMETHYST)
                    .noOcclusion())
    );

    // Блоки "оболочки" — из них состоят стены выросшего куба. Сквозь них можно проходить.
    public static final Block SULFUR_CUBE_SHELL = registerBlock(
            "potent_sulfur_shell",
            new SulfurCubeShellBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .strength(-1.0f, 3600000.0f) // как бедрок — ломается только через инструменты мода
                    .sound(SoundType.AMETHYST)
                    .noOcclusion()
                    .noCollission()
                    .isViewBlocking((state, world, pos) -> false))
    );

    // Блок, в который превращается предмет "Наполненный куб" при установке.
    // По нему нужно ударить ножницами, чтобы освободить сохранённую постройку.
    public static final Block FILLED_SULFUR_CUBE = registerBlock(
            "filled_potent_sulfur_cube",
            new FilledSulfurCubeBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0f)
                    .sound(SoundType.AMETHYST_CLUSTER)
                    .noOcclusion())
    );

    private static Block registerBlock(String name, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK,
                ResourceLocation.fromNamespaceAndPath(PotentSulfur.MOD_ID, name), block);
    }

    public static void register() {
        PotentSulfur.LOGGER.info("Зарегистрированы блоки Potent Sulfur");
    }
}
