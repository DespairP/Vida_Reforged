package teamHTBP.vidaReforged.client.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import teamHTBP.vidaReforged.core.api.hud.IVidaScreen;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.server.blockEntities.BasePurificationCauldronBlockEntity;
import teamHTBP.vidaReforged.server.blockEntities.CollectorBlockEntity;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class VidaCollectorScreen extends AbstractVidaHUDScreen implements IVidaScreen {

    public final ResourceLocation LOCATION = new ResourceLocation(MOD_ID, "textures/gui/collector_hud.png");

    private final int RESOLUTION = 48;

    public final TextureSection BLOCK = new TextureSection(LOCATION, 1, 35,13,13);

    public final TextureSection ITEM_SLOT = new TextureSection(LOCATION, 1, 1,24,24);

    public final TextureSection BAR = new TextureSection(LOCATION, 19, 29,22,6);

    public final TextureSection PROGRESS = new TextureSection(LOCATION, 22, 38,16,2);


    public VidaCollectorScreen(Minecraft minecraft, MultiBufferSource.BufferSource bufferSource) {
        super(minecraft, bufferSource);
    }

    public void render(PoseStack poseStack, BlockEntity entity, float alpha) {
        CollectorBlockEntity collectorBlockEntity = null;
        if (entity instanceof CollectorBlockEntity blockEntity) {
            collectorBlockEntity = blockEntity;
        }
        if (collectorBlockEntity == null) {
            return;
        }

        poseStack.pushPose();

        // 渲染方块小图标
        final int blockX = centerX(BLOCK);
        final int blockY = centerY(BLOCK) - 30;
        blit(
                LOCATION,
                blockX, blockY, 0,
                BLOCK.minU(), BLOCK.minV(),
                BLOCK.w(), BLOCK.h(),
                RESOLUTION, RESOLUTION
        );

        // 渲染槽位
        final int slotX = centerX(ITEM_SLOT);
        final int slotY = blockY - 30;
        blit(
                LOCATION,
                slotX, slotY, 0,
                ITEM_SLOT.minU(), ITEM_SLOT.minV(),
                ITEM_SLOT.w(), ITEM_SLOT.h(),
                RESOLUTION, RESOLUTION
        );

        // 渲染能量条
        final int barX = centerX(BAR);
        final int barY = blockY + ITEM_SLOT.h();
        blit(
                LOCATION,
                barX, barY, 0,
                BAR.minU(), BAR.minV(),
                BAR.w(), BAR.h(),
                RESOLUTION, RESOLUTION
        );


        if(!collectorBlockEntity.collectItem.isEmpty()){
            // 渲染物品
            renderItem(collectorBlockEntity.collectItem.copy(), slotX + 4, slotY + 4);
            // 渲染进度
            final int progressX = barX + 3;
            final int progressY = barY + 2;
            blit(
                    LOCATION,
                    progressX, progressY, 0,
                    PROGRESS.minU(), PROGRESS.minV(),
                    (int)(PROGRESS.w()  * collectorBlockEntity.getProgress() / collectorBlockEntity.getMaxProgress()), PROGRESS.h(),
                    RESOLUTION, RESOLUTION
            );
        }

        poseStack.popPose();

    }



    @Override
    public void render(PoseStack poseStack, float partialTicks) {

    }
}
