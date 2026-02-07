package io.github.velaliilunalii.item_router;

import io.github.velaliilunalii.item_router.block.entity.ItemRouterBlockEntity;
import io.github.velaliilunalii.item_router.block.entity.ModBlockEntities;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(Constants.MOD_ID)
public class ItemRouter {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Constants.MOD_ID);

    public static final RegistryObject<Block> ITEM_ROUTER = BLOCKS.register("item_router", CommonClass.ITEM_ROUTER_SUPPLIER);
    public static final RegistryObject<Item> AMETHYST_PEARL = ITEMS.register("amethyst_pearl", CommonClass.AMETHYST_PEARL_SUPPLIER);
    public static final RegistryObject<Item> OBSIDIAN_PEARL = ITEMS.register("obsidian_pearl", CommonClass.OBSIDIAN_PEARL_SUPPLIER);
    public static final RegistryObject<Item> SOCKETED_AMETHYST_PEARL = ITEMS.register("socketed_amethyst_pearl", CommonClass.SOCKETED_AMETHYST_PEARL_SUPPLIER);
    public static final RegistryObject<Item> SOCKETED_OBSIDIAN_PEARL = ITEMS.register("socketed_obsidian_pearl", CommonClass.SOCKETED_OBSIDIAN_PEARL_SUPPLIER);
    public static final RegistryObject<Item> SOCKETED_ENDER_PEARL = ITEMS.register("socketed_ender_pearl", CommonClass.SOCKETED_ENDER_PEARL_SUPPLIER);

    public static final RegistryObject<Item> ITEM_ROUTER_ITEM = ITEMS.register("item_router",
            () -> new BlockItem(ITEM_ROUTER.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS))
    );

    public static final RegistryObject<BlockEntityType<ItemRouterBlockEntity>> ITEM_ROUTER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("item_router_block_entity", () ->
                    BlockEntityType.Builder.of(
                            ItemRouterBlockEntity::new,
                            ITEM_ROUTER.get()
                    ).build(null)
            );

    public ItemRouter() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);

        modEventBus.addListener(this::onCommonSetup);

        MinecraftForge.EVENT_BUS.addListener(this::onItemTooltip);

        Constants.LOG.info("Hello Forge world!");
    }

    private void onCommonSetup(final net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) {
        CommonClass.ITEM_ROUTER = ITEM_ROUTER.get();
        CommonClass.AMETHYST_PEARL = AMETHYST_PEARL.get();
        CommonClass.OBSIDIAN_PEARL = OBSIDIAN_PEARL.get();
        CommonClass.SOCKETED_AMETHYST_PEARL = SOCKETED_AMETHYST_PEARL.get();
        CommonClass.SOCKETED_OBSIDIAN_PEARL = SOCKETED_OBSIDIAN_PEARL.get();
        CommonClass.SOCKETED_ENDER_PEARL = SOCKETED_ENDER_PEARL.get();
        ModBlockEntities.ITEM_ROUTER_BLOCK_ENTITY = ITEM_ROUTER_BLOCK_ENTITY.get();

        CommonClass.init();
    }

    private void onItemTooltip(ItemTooltipEvent event) {
        CommonClass.onItemTooltip(event.getItemStack(), event.getFlags(), event.getToolTip());
    }
}