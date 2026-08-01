package com.example.potentsulfur.item;

import com.example.potentsulfur.block.ModBlocks;
import com.example.potentsulfur.block.entity.FilledSulfurCubeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * Предмет, который выдаётся, когда игрок собирает Мощный Серный куб пустым ведром.
 * Вся постройка, что была внутри, хранится в NBT этого предмета.
 * При установке в мир создаёт FilledSulfurCubeBlockEntity с этой структурой.
 */
public class FilledSulfurCubeItem extends BlockItem {

    private static final String STRUCTURE_KEY = "PotentSulfurStructure";
    private static final String SIZE_KEY = "PotentSulfurSize";

    public FilledSulfurCubeItem(Properties properties) {
        super(ModBlocks.FILLED_SULFUR_CUBE, properties);
    }

    public static void writeStructure(ItemStack stack, CompoundTag structureNbt, int size) {
        CompoundTag customData = stack.getOrCreateTag();
        customData.put(STRUCTURE_KEY, structureNbt);
        customData.putInt(SIZE_KEY, size);
    }

    public static CompoundTag readStructure(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(STRUCTURE_KEY)) return new CompoundTag();
        return tag.getCompound(STRUCTURE_KEY);
    }

    public static int readSize(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return 3;
        return tag.getInt(SIZE_KEY);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, net.minecraft.world.entity.player.Player player,
                                                   ItemStack stack, BlockState state) {
        boolean result = super.updateCustomBlockEntityTag(pos, level, player, stack, state);
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof FilledSulfurCubeBlockEntity filledBe) {
            filledBe.setStructureNbt(readStructure(stack), readSize(stack));
        }
        return result;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        int size = readSize(stack);
        tooltip.add(Component.translatable("tooltip.potentsulfur.contains_build", size, size, size));
    }
}
