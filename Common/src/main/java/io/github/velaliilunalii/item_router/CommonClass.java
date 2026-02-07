package io.github.velaliilunalii.item_router;

import io.github.velaliilunalii.item_router.block.ItemRouterBlock;
import io.github.velaliilunalii.item_router.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.List;
import java.util.function.Supplier;

public class CommonClass {
    public static Block ITEM_ROUTER;
    public static Item AMETHYST_PEARL;
    public static Item OBSIDIAN_PEARL;

    public static Item SOCKETED_AMETHYST_PEARL;
    public static Item SOCKETED_OBSIDIAN_PEARL;
    public static Item SOCKETED_ENDER_PEARL;

    public static Supplier<Block> ITEM_ROUTER_SUPPLIER = () ->
            new ItemRouterBlock(BlockBehaviour.Properties.copy(Blocks.LODESTONE).requiresCorrectToolForDrops());

    public static Supplier<Item> AMETHYST_PEARL_SUPPLIER = () ->
            new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS).stacksTo(16));

    public static Supplier<Item> OBSIDIAN_PEARL_SUPPLIER = () ->
            new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS).stacksTo(16));

    public static Supplier<Item> SOCKETED_AMETHYST_PEARL_SUPPLIER = () ->
            new Item(new Item.Properties());
    public static Supplier<Item> SOCKETED_OBSIDIAN_PEARL_SUPPLIER = () ->
            new Item(new Item.Properties());
    public static Supplier<Item> SOCKETED_ENDER_PEARL_SUPPLIER = () ->
            new Item(new Item.Properties());

    // This method serves as an initialization hook for the mod. The vanilla
    // game has no mechanism to load tooltip listeners so this must be
    // invoked from a mod loader specific project like Forge or Fabric.
    public static void init() {
        Constants.LOG.info("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.isDevelopmentEnvironment() ? "development" : "production");
        Constants.LOG.info("Diamond Item >> {}", Registry.ITEM.getKey(Items.DIAMOND));
    }

    // This method serves as a hook to modify item tooltips. The vanilla game
    // has no mechanism to load tooltip listeners so this must be registered
    // by a mod loader like Forge or Fabric.
    public static void onItemTooltip(ItemStack stack, TooltipFlag context, List<Component> tooltip) {

        if (!stack.isEmpty()) {

            final FoodProperties food = stack.getItem().getFoodProperties();

            if (food != null) {

                tooltip.add(Component.literal("Nutrition: " + food.getNutrition()));
                tooltip.add(Component.literal("Saturation: " + food.getSaturationModifier()));
            }
        }
    }
}