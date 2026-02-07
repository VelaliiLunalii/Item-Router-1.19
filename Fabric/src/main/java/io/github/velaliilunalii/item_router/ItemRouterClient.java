package io.github.velaliilunalii.item_router;

import io.github.velaliilunalii.item_router.block.entity.ModBlockEntities;
import io.github.velaliilunalii.item_router.client.renderer.ItemRouterBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;

public class ItemRouterClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(
                ModBlockEntities.ITEM_ROUTER_BLOCK_ENTITY,
                ItemRouterBlockEntityRenderer::new
        );
    }
}
