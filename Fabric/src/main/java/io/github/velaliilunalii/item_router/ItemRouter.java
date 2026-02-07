package io.github.velaliilunalii.item_router;

import io.github.velaliilunalii.item_router.block.entity.ItemRouterBlockEntity;
import io.github.velaliilunalii.item_router.block.entity.ModBlockEntities;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class ItemRouter implements ModInitializer {


    @Override
    public void onInitialize() {
        CommonClass.ITEM_ROUTER = Registry.register(
                Registry.BLOCK,
                new ResourceLocation(Constants.MOD_ID, "item_router"),
                CommonClass.ITEM_ROUTER_SUPPLIER.get()
        );

        CommonClass.AMETHYST_PEARL = Registry.register(
                Registry.ITEM,
                new ResourceLocation(Constants.MOD_ID, "amethyst_pearl"),
                CommonClass.AMETHYST_PEARL_SUPPLIER.get()
        );

        CommonClass.OBSIDIAN_PEARL = Registry.register(
                Registry.ITEM,
                new ResourceLocation(Constants.MOD_ID, "obsidian_pearl"),
                CommonClass.OBSIDIAN_PEARL_SUPPLIER.get()
        );

        CommonClass.SOCKETED_AMETHYST_PEARL = Registry.register(
                Registry.ITEM,
                new ResourceLocation(Constants.MOD_ID, "socketed_amethyst_pearl"),
                CommonClass.SOCKETED_AMETHYST_PEARL_SUPPLIER.get()
        );

        CommonClass.SOCKETED_OBSIDIAN_PEARL = Registry.register(
                Registry.ITEM,
                new ResourceLocation(Constants.MOD_ID, "socketed_obsidian_pearl"),
                CommonClass.SOCKETED_OBSIDIAN_PEARL_SUPPLIER.get()
        );

        CommonClass.SOCKETED_ENDER_PEARL = Registry.register(
                Registry.ITEM,
                new ResourceLocation(Constants.MOD_ID, "socketed_ender_pearl"),
                CommonClass.SOCKETED_ENDER_PEARL_SUPPLIER.get()
        );

        Registry.register(
                Registry.ITEM,
                new ResourceLocation(Constants.MOD_ID, "item_router"),
                new BlockItem(CommonClass.ITEM_ROUTER, new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS))
        );

        ModBlockEntities.ITEM_ROUTER_BLOCK_ENTITY = Registry.register(
                Registry.BLOCK_ENTITY_TYPE,
                new ResourceLocation(Constants.MOD_ID, "item_router_block_entity"),
                FabricBlockEntityTypeBuilder.create(
                        ItemRouterBlockEntity::new,
                        CommonClass.ITEM_ROUTER
                ).build()
        );

        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        CommonClass.init();

        // Some code like events require special initialization from the
        // loader specific code.
        ItemTooltipCallback.EVENT.register(CommonClass::onItemTooltip);
    }
}
