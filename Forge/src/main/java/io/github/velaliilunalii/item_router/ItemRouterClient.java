package io.github.velaliilunalii.item_router;

import io.github.velaliilunalii.item_router.client.renderer.ItemRouterBlockEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ItemRouterClient {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                ItemRouter.ITEM_ROUTER_BLOCK_ENTITY.get(),
                ItemRouterBlockEntityRenderer::new
        );
    }
}
