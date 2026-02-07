package io.github.velaliilunalii.item_router.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.velaliilunalii.item_router.block.ItemRouterBlock;
import io.github.velaliilunalii.item_router.block.entity.ItemRouterBlockEntity;
import io.github.velaliilunalii.item_router.block.properties.Socket;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;

import static io.github.velaliilunalii.item_router.CommonClass.*;
import static io.github.velaliilunalii.item_router.block.ItemRouterBlock.getSocketForFace;
import static net.minecraft.world.item.Items.AIR;

public class ItemRouterBlockEntityRenderer implements BlockEntityRenderer<ItemRouterBlockEntity> {
    private static final float SCALE = 0.25F;
    private static final float OFFSET = 1F;
    private final ItemRenderer itemRenderer;

    public ItemRouterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(ItemRouterBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState blockState = blockEntity.getBlockState();
        BlockPos blockPos = blockEntity.getBlockPos();
        Level level = blockEntity.getLevel();
        int pos = (int)blockPos.asLong();

        for (Direction direction : Direction.values()) {
            Socket socket = getSocketForFace(blockState, direction);
            ItemStack itemStack = new ItemStack(socketToSocketedItem(socket));

            if (!itemStack.isEmpty()) {
                poseStack.pushPose();

                positionAndRotateForFace(poseStack, direction);

                poseStack.scale(SCALE, SCALE, SCALE);

                int lightLevel = getLightAtFace(level, blockPos, direction);

                this.itemRenderer.renderStatic(itemStack, ItemTransforms.TransformType.FIXED,
                        lightLevel, packedOverlay, poseStack, bufferSource, pos + direction.ordinal());

                poseStack.popPose();
            }
        }
    }

    public static Item socketToSocketedItem(Socket socket){
        if (socket.equals(Socket.AMETHYST)) return SOCKETED_AMETHYST_PEARL;
        if (socket.equals(Socket.OBSIDIAN)) return SOCKETED_OBSIDIAN_PEARL;
        if (socket.equals(Socket.ENDER)) return SOCKETED_ENDER_PEARL;
        return AIR;
    }

    private int getLightAtFace(Level level, BlockPos blockPos, Direction direction) {
        if (level == null) {
            return LightTexture.FULL_BRIGHT;
        }

        BlockPos adjacentPos = blockPos.relative(direction);

        return LightTexture.pack(
                level.getBrightness(LightLayer.BLOCK, adjacentPos),
                level.getBrightness(LightLayer.SKY, adjacentPos)
        );
    }

    private void positionAndRotateForFace(PoseStack poseStack, Direction direction) {
        switch (direction) {
            case UP -> {
                poseStack.translate(0.5, OFFSET, 0.5);
                poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            }
            case DOWN -> {
                poseStack.translate(0.5, 1.0 - OFFSET, 0.5);
                poseStack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
            }
            case NORTH -> {
                poseStack.translate(0.5, 0.5, 1.0 - OFFSET);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
            }
            case SOUTH -> {
                poseStack.translate(0.5, 0.5, OFFSET);
            }
            case EAST -> {
                poseStack.translate(OFFSET, 0.5, 0.5);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
            }
            case WEST -> {
                poseStack.translate(1.0 - OFFSET, 0.5, 0.5);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
            }
        }
    }
}