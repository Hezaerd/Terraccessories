package net.hezaerd.terraccessories.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.hezaerd.terraccessories.block.entity.TinkererWorkshopEntity;
import net.hezaerd.terraccessories.registry.BlockInit;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TinkererWorkshop extends BlockWithEntity {
    public TinkererWorkshop() {
        super(FabricBlockSettings.copyOf(Blocks.CRAFTING_TABLE).nonOpaque());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        if (!world.isClient) {
            if (world.getBlockEntity(pos) instanceof NamedScreenHandlerFactory factory) {
                player.openHandledScreen(factory);
            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TinkererWorkshopEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? null : checkType(type, BlockInit.Entities.TINKERER_WORKSHOP, (world1, pos, state1, blockEntity) -> blockEntity.tick(world));
    }
}
