package com.example.potentsulfur.block;

import com.example.potentsulfur.block.entity.ModBlockEntities;
import com.example.potentsulfur.block.entity.SulfurCubeCoreBlockEntity;
import com.example.potentsulfur.item.FilledSulfurCubeItem;
import com.example.potentsulfur.item.ModItems;
import com.example.potentsulfur.util.CubeShellUtil;
import com.example.potentsulfur.util.StructureUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;

public class SulfurCubeCoreBlock extends Block implements EntityBlock {

    public SulfurCubeCoreBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SulfurCubeCoreBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player,
                                  InteractionHand hand, BlockHitResult hit) {
        if (!(world.getBlockEntity(pos) instanceof SulfurCubeCoreBlockEntity core)) {
            return InteractionResult.PASS;
        }

        ItemStack heldStack = player.getItemInHand(hand);
        Item held = heldStack.getItem();

        // 1) Кормление Активной серой -> рост куба.
        if (held == ModItems.ACTIVE_SULFUR) {
            if (core.isFullyGrown()) {
                if (!world.isClientSide) {
                    player.displayClientMessage(net.minecraft.network.chat.Component
                            .translatable("message.potentsulfur.fully_grown"), true);
                }
                return InteractionResult.SUCCESS;
            }
            if (!world.isClientSide) {
                int oldSize = core.getSize();
                core.setFeedCount(core.getFeedCount() + 1);
                int newSize = core.getSize();
                CubeShellUtil.regrow(world, pos, oldSize, newSize);
                if (!player.getAbilities().instabuild) {
                    heldStack.shrink(1);
                }
                world.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            return InteractionResult.SUCCESS;
        }

        // 2) Пустое ведро -> собрать куб и всё, что построено внутри, в предмет.
        if (held.getDescriptionId().equals(Items.BUCKET.getDescriptionId()) && core.getFeedCount() > 0) {
            if (!world.isClientSide) {
                int size = core.getSize();
                BlockPos min = CubeShellUtil.getMin(pos, size);
                BlockPos max = CubeShellUtil.getMax(pos, size);

                var structureNbt = StructureUtil.saveRegion(world, min, max);

                ItemStack filled = new ItemStack(ModItems.FILLED_SULFUR_CUBE_ITEM);
                FilledSulfurCubeItem.writeStructure(filled, structureNbt, size);

                // Очищаем весь объём (оболочку + содержимое) и само ядро.
                StructureUtil.clearRegion(world, min, max);
                world.removeBlock(pos, false);

                if (!heldStack.isEmpty()) {
                    heldStack.shrink(1);
                }
                if (!player.getInventory().add(filled)) {
                    player.drop(filled, false);
                }
                world.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        // Ядро тоже "прозрачно" для ходьбы, как и стены — целиком в духе фичи.
        return Shapes.empty();
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return null;
    }
}
