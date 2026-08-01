package com.example.potentsulfur.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Хранит, сколько раз куб покормили Активной серой (0-4).
 * 0 = обычный маленький куб.
 * 1..4 = "Мощный Серный куб" размером (2*feedCount+1)^3.
 */
public class SulfurCubeCoreBlockEntity extends BlockEntity {

    public static final int MAX_FEED = 4;

    private int feedCount = 0;

    public SulfurCubeCoreBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SULFUR_CUBE_CORE_BE, pos, state);
    }

    public int getFeedCount() {
        return feedCount;
    }

    public void setFeedCount(int feedCount) {
        this.feedCount = Math.max(0, Math.min(MAX_FEED, feedCount));
        setChanged();
    }

    /** Текущий размер грани куба (3, 5, 7 или 9). Для feedCount = 0 куб ещё "спит" — размер 1. */
    public int getSize() {
        return feedCount == 0 ? 1 : feedCount * 2 + 1;
    }

    public boolean isFullyGrown() {
        return feedCount >= MAX_FEED;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("FeedCount", feedCount);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, net.minecraft.core.HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.feedCount = tag.getInt("FeedCount");
    }
}
