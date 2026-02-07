package io.github.velaliilunalii.item_router.block;

import io.github.velaliilunalii.item_router.block.properties.Socket;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import io.github.velaliilunalii.item_router.block.entity.ItemRouterBlockEntity;
import net.minecraft.world.level.block.EntityBlock;

import java.util.List;
import java.util.stream.IntStream;

import static io.github.velaliilunalii.item_router.CommonClass.AMETHYST_PEARL;
import static io.github.velaliilunalii.item_router.CommonClass.OBSIDIAN_PEARL;
import static net.minecraft.world.item.Items.AIR;
import static net.minecraft.world.item.Items.ENDER_PEARL;

public class ItemRouterBlock extends Block implements EntityBlock{
    public static final EnumProperty<Socket> UP_VISION = EnumProperty.create("up_vision", Socket.class);
    public static final EnumProperty<Socket> DOWN_VISION = EnumProperty.create("down_vision", Socket.class);
    public static final EnumProperty<Socket> NORTH_VISION = EnumProperty.create("north_vision", Socket.class);
    public static final EnumProperty<Socket> SOUTH_VISION = EnumProperty.create("south_vision", Socket.class);
    public static final EnumProperty<Socket> EAST_VISION = EnumProperty.create("east_vision", Socket.class);
    public static final EnumProperty<Socket> WEST_VISION = EnumProperty.create("west_vision", Socket.class);

    public ItemRouterBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any())
                .setValue(UP_VISION, Socket.EMPTY)
                .setValue(DOWN_VISION, Socket.EMPTY)
                .setValue(NORTH_VISION, Socket.EMPTY)
                .setValue(SOUTH_VISION, Socket.EMPTY)
                .setValue(EAST_VISION, Socket.EMPTY)
                .setValue(WEST_VISION, Socket.EMPTY));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ItemRouterBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{UP_VISION, DOWN_VISION, NORTH_VISION, SOUTH_VISION, EAST_VISION, WEST_VISION});
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return defaultBlockState();
    }

    public static Socket getSocketForFace(BlockState state, Direction direction) {
        return switch (direction) {
            case UP -> state.getValue(ItemRouterBlock.UP_VISION);
            case DOWN -> state.getValue(ItemRouterBlock.DOWN_VISION);
            case NORTH -> state.getValue(ItemRouterBlock.NORTH_VISION);
            case SOUTH -> state.getValue(ItemRouterBlock.SOUTH_VISION);
            case EAST -> state.getValue(ItemRouterBlock.EAST_VISION);
            case WEST -> state.getValue(ItemRouterBlock.WEST_VISION);
        };
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player.getMainHandItem().isEmpty()) {
            if (level instanceof ServerLevel serverLevel) {
                if (player.isCrouching()){
                    socketItem(blockState, serverLevel, blockPos, player, blockHitResult, Socket.EMPTY);
                }else {
                    int range = getRangeModifierBlock(level, blockPos);
                    BlockPos[] box = getBox(blockState, blockPos, range);
                    depositInventory(null, box, level, blockPos, player);
                }
            }else {
                return InteractionResult.SUCCESS;
            }
        }else if(player.getMainHandItem().is(socketToItem(Socket.AMETHYST))){
            if (level instanceof ServerLevel serverLevel) {
                socketItem(blockState, serverLevel, blockPos, player, blockHitResult, Socket.AMETHYST);
            }else {
                return InteractionResult.SUCCESS;
            }
        }else if(player.getMainHandItem().is(socketToItem(Socket.OBSIDIAN))){
            if (level instanceof ServerLevel serverLevel) {
                socketItem(blockState, serverLevel, blockPos, player, blockHitResult, Socket.OBSIDIAN);
            }else {
                return InteractionResult.SUCCESS ;
            }
        }else if(player.getMainHandItem().is(socketToItem(Socket.ENDER))){
            if (level instanceof ServerLevel serverLevel) {
                socketItem(blockState, serverLevel, blockPos, player, blockHitResult, Socket.ENDER);
            }else {
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    public static void socketItem(BlockState blockState, ServerLevel level, BlockPos blockPos, Player player, BlockHitResult blockHitResult, Socket newSocket){
        Direction direction = blockHitResult.getDirection();
        EnumProperty<Socket> socketProperty = directionToProperty(direction);
        Socket socket = blockState.getValue(socketProperty);
        socket = emptySocket(socket, level, blockPos, direction);
        ItemStack stack = player.getMainHandItem().split(1);
        socket = stack.isEmpty() ? socket : newSocket;
        level.setBlock(blockPos, blockState.setValue(socketProperty, socket), 2);
        level.playSound((Player)null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.SMALL_AMETHYST_BUD_PLACE, SoundSource.NEUTRAL, 1F, 0.7F + (RandomSource.create().nextFloat() * 0.6F));
    }

    public static Socket emptySocket(Socket socket, Level level, BlockPos blockPos, Direction direction){
        if (socket.isEmpty()) return socket;
        Vec3 vec3 = Vec3.atCenterOf(blockPos.relative(direction));
        ItemEntity $$11 = new ItemEntity(level, vec3.x, vec3.y, vec3.z, new ItemStack(socketToItem(socket)));
        $$11.setDeltaMovement(
                level.random.triangle(0.0, 0.11485000171139836), level.random.triangle(0.2, 0.11485000171139836), level.random.triangle(0.0, 0.11485000171139836)
        );
        level.addFreshEntity($$11);
        return Socket.EMPTY;
    }

    public static Item socketToItem(Socket socket){
        if (socket.equals(Socket.AMETHYST)) return AMETHYST_PEARL;
        if (socket.equals(Socket.OBSIDIAN)) return OBSIDIAN_PEARL;
        if (socket.equals(Socket.ENDER)) return ENDER_PEARL;
        return AIR;
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState1, boolean b) {
        if (!blockState.is(blockState1.getBlock())) {

            for (Direction direction : Direction.values()){
                dropItemStack(level, blockPos, socketToItem(getSocketForFace(blockState, direction)).getDefaultInstance());
            }

            super.onRemove(blockState, level, blockPos, blockState1, b);
        }
    }

    public static void dropItemStack(Level level, BlockPos blockPos, ItemStack itemStack) {
        double $$5 = (double) EntityType.ITEM.getWidth();
        double $$6 = 1.0 - $$5;
        double $$7 = $$5 / 2.0;
        double $$8 = Math.floor((double)blockPos.getX()) + level.random.nextDouble() * $$6 + $$7;
        double $$9 = Math.floor((double)blockPos.getY()) + level.random.nextDouble() * $$6;
        double $$10 = Math.floor((double)blockPos.getZ()) + level.random.nextDouble() * $$6 + $$7;

        while (!itemStack.isEmpty()) {
            ItemEntity $$11 = new ItemEntity(level, $$8, $$9, $$10, itemStack.split(level.random.nextInt(21) + 10));
            $$11.setDeltaMovement(
                    level.random.triangle(0.0, 0.11485000171139836), level.random.triangle(0.2, 0.11485000171139836), level.random.triangle(0.0, 0.11485000171139836)
            );
            level.addFreshEntity($$11);
        }
    }

    public static EnumProperty<Socket> directionToProperty(Direction direction){
        if (direction.equals(Direction.UP)) return UP_VISION;
        if (direction.equals(Direction.DOWN)) return DOWN_VISION;
        if (direction.equals(Direction.NORTH)) return NORTH_VISION;
        if (direction.equals(Direction.SOUTH)) return SOUTH_VISION;
        if (direction.equals(Direction.EAST)) return EAST_VISION;
        if (direction.equals(Direction.WEST)) return WEST_VISION;
        return null;
    }

    private static void depositInventory(@Nullable BlockPos[] originalBox, BlockPos[] box, Level level, BlockPos blockPos, Player player){
        boolean firstIteration = originalBox == null;
        boolean oneOrMoreSuccess = false;
        for (BlockPos pos : BlockPos.betweenClosed(box[0], box[1])) {
            if (firstIteration || !isIn(pos, originalBox)) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                boolean success = false;
                if (blockEntity instanceof Container container) {
                    Direction direction = Direction.fromNormal(pos.subtract(blockPos));
                    success = sendItem(player.getInventory(), container, direction);

                    if (firstIteration) {
                        BlockPos[] recursiveBox = getBox(null, pos, 3);
                        depositInventory(box, recursiveBox, level, blockPos, player);
                    }
                }
                if (success) {
                    oneOrMoreSuccess = true;
                    Vec3 origin = Vec3.atCenterOf(blockPos);
                    Vec3 destination = Vec3.atCenterOf(pos);
                    for (int i = 0; i < 10; i++) {
                        Vec3 particlePos = destination.lerp(origin, (double) i / 10);
                        if (level instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(ParticleTypes.DRAGON_BREATH, particlePos.x, particlePos.y, particlePos.z, 1, (double) 0.0F, (double) 0.0F, (double) 0.0F, 0);
                        }
                    }
                }
            }
        }
        if(oneOrMoreSuccess) level.playSound((Player)null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), SoundEvents.SHULKER_TELEPORT, SoundSource.NEUTRAL, 0.6F, 1F);
    }

    private static BlockPos[] getBox(@Nullable BlockState blockState, BlockPos blockPos, int range){
        if (blockState == null) return new BlockPos[]{blockPos.offset(-range, -range, -range), blockPos.offset(range, range, range)};

        int posX = blockState.getValue(EAST_VISION).range;
        int posY = blockState.getValue(UP_VISION).range;
        int posZ = blockState.getValue(SOUTH_VISION).range;
        int negX = -blockState.getValue(WEST_VISION).range;
        int negY = -blockState.getValue(DOWN_VISION).range;
        int negZ = -blockState.getValue(NORTH_VISION).range;
        return new BlockPos[]{blockPos.offset(negX, negY, negZ), blockPos.offset(posX, posY, posZ)};
    }

    private static boolean isIn(BlockPos blockPos, BlockPos[] box){
        return blockPos.getX() >= box[0].getX() && blockPos.getY() >= box[0].getY() && blockPos.getZ() >= box[0].getZ() &&
                blockPos.getX() <= box[1].getX() && blockPos.getY() <= box[1].getY() && blockPos.getZ() <= box[1].getZ();
    }

    //inventory -> container
    private static boolean sendItem(Inventory inventory, Container container, Direction direction) {
        boolean success = false;
        if (container != null && !isFullContainer(container, direction)) {
            for (int i = 0; i < inventory.getContainerSize(); ++i) {
                ItemStack item = inventory.getItem(i);
                if (!item.isEmpty() && container.hasAnyMatching(itemStack -> itemStack.getItem().equals(item.getItem()))) {
                    ItemStack itemStack = inventory.removeItem(i, item.copy().getCount());
                    ItemStack itemStack2 = addItem(container, itemStack, direction);
                    if (itemStack2.isEmpty()) {
                        container.setChanged();
                        success = true;
                    }else inventory.setItem(i, itemStack);
                }
                if (!item.equals(inventory.getItem(i))) success = true;
            }
        }
        return success;
    }

    private static boolean isFullContainer(Container container, Direction direction) {
        return getSlots(container, direction).allMatch((i) -> {
            ItemStack itemStack = container.getItem(i);
            return itemStack.getCount() >= itemStack.getMaxStackSize();
        });
    }

    private static IntStream getSlots(Container container, Direction direction) {
        return container instanceof WorldlyContainer ? IntStream.of(((WorldlyContainer)container).getSlotsForFace(direction)) : IntStream.range(0, container.getContainerSize());
    }

    public static ItemStack addItem(Container container, ItemStack itemStack, @Nullable Direction direction) {
        if (container instanceof WorldlyContainer worldlyContainer && direction != null) {
            int[] is = worldlyContainer.getSlotsForFace(direction);

            for(int i = 0; i < is.length && !itemStack.isEmpty(); ++i) {
                if (container.getItem(is[i]).is(itemStack.getItem())) itemStack = tryMoveInItem(container, itemStack, is[i], direction);
            }
            if (!itemStack.isEmpty()){
                for(int i = 0; i < is.length && !itemStack.isEmpty(); ++i) {
                    itemStack = tryMoveInItem(container, itemStack, is[i], direction);
                }
            }
        } else {
            int j = container.getContainerSize();

            for(int k = 0; k < j && !itemStack.isEmpty(); ++k) {
                if (container.getItem(k).is(itemStack.getItem())) itemStack = tryMoveInItem(container, itemStack, k, direction);
            }
            if (!itemStack.isEmpty()){
                for(int k = 0; k < j && !itemStack.isEmpty(); ++k) {
                    itemStack = tryMoveInItem(container, itemStack, k, direction);
                }
            }
        }

        return itemStack;
    }

    private static ItemStack tryMoveInItem(Container container2, ItemStack itemStack, int i, @Nullable Direction direction) {
        ItemStack itemStack2 = container2.getItem(i);
        if (canPlaceItemInContainer(container2, itemStack, i, direction)) {
            boolean bl = false;
            if (itemStack2.isEmpty()) {
                container2.setItem(i, itemStack);
                itemStack = ItemStack.EMPTY;
                bl = true;
            } else if (canMergeItems(itemStack2, itemStack)) {
                int j = itemStack.getMaxStackSize() - itemStack2.getCount();
                int k = Math.min(itemStack.getCount(), j);
                itemStack.shrink(k);
                itemStack2.grow(k);
                bl = k > 0;
            }

            if (bl) container2.setChanged();
        }

        return itemStack;
    }

    private static boolean canPlaceItemInContainer(Container container, ItemStack itemStack, int i, @Nullable Direction direction) {
        if (!container.canPlaceItem(i, itemStack)) {
            return false;
        } else {
            return !(container instanceof WorldlyContainer) || ((WorldlyContainer)container).canPlaceItemThroughFace(i, itemStack, direction);
        }
    }

    private static boolean canMergeItems(ItemStack itemStack, ItemStack itemStack2) {
        if (!itemStack.is(itemStack2.getItem())) {
            return false;
        } else if (itemStack.getDamageValue() != itemStack2.getDamageValue()) {
            return false;
        } else if (itemStack.getCount() > itemStack.getMaxStackSize()) {
            return false;
        } else {
            return ItemStack.tagMatches(itemStack, itemStack2);
        }
    }

    private static int getRangeModifierBlock(Level level, BlockPos blockPos){
        Block block = level.getBlockState(blockPos.below()).getBlock();
        if (block.equals(Blocks.AMETHYST_BLOCK)) return 16;
        if (block.equals(Blocks.OBSIDIAN) || block.equals(Blocks.CRYING_OBSIDIAN)) return 4;
        return 8;
    }

    @Override
    public void appendHoverText(ItemStack $$0, @Nullable BlockGetter $$1, List<Component> tooltip, TooltipFlag $$3) {
        tooltip.add(Component.literal("Teleports player's items to nearby containers").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        tooltip.add(Component.literal("Socket pearls on faces").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        super.appendHoverText($$0, $$1, tooltip, $$3);
    }
}