package com.example.potentsulfur.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Декоративный блок оболочки. Игроки и мобы проходят сквозь него насквозь
 * (collision shape пустая), но он остаётся видимым и разрушаемым только
 * логикой мода (обычным ломанием отключено высокой твёрдостью в ModBlocks).
 */
public class SulfurCubeShellBlock extends Block {
    public SulfurCubeShellBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }
}
