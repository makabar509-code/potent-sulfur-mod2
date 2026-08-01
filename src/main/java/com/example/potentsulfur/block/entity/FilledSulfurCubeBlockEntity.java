package com.example.potentsulfur.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Хранит сохранённую структуру (постройку игрока) до момента,
 * пока её не "освободят" ножницами.
 */
public class FilledSulfurCubeBlockEntity extends BlockEntity {

    private CompoundTag structureNbt = new CompoundTag();
    private int size = 3;

    public FilledSulfurCubeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FILLED_SULFUR_CUBE_BE, pos, state);
    }

    public CompoundTag getStructureNbt() {
        return structureNbt;
    }

    public void setStructureNbt(CompoundTag structureNbt, int size) {
        this.structureNbt = structureNbt;
        this.size = size;
        setChanged();
    }

    public int getSize() {
        return size;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Structure", structureNbt);
        tag.putInt("Size", size);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.structureNbt = tag.getCompound("Structure");
        this.size = tag.getInt("Size");
    }
}
