package io.github.velaliilunalii.item_router.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ItemRouterBlockEntity extends BlockEntity {

    public ItemRouterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ITEM_ROUTER_BLOCK_ENTITY, pos, state);
    }
}
