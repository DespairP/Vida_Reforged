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

        final int blockX = centerX(BLOCK);
        final int blockY = centerY(BLOCK) - 30;

        blit(
                LOCATION,
                blockX, blockY, 0,
                BLOCK.minU(), BLOCK.minV(),
                BLOCK.w(), BLOCK.h(),
                RESOLUTION, RESOLUTION
        );

        final int slotX = centerX(ITEM_SLOT);
        final int slotY = blockY - 30;

        blit(
                LOCATION,
                slotX, slotY, 0,
                ITEM_SLOT.minU(), ITEM_SLOT.minV(),
                ITEM_SLOT.w(), ITEM_SLOT.h(),
                RESOLUTION, RESOLUTION
        );

        if(!collectorBlockEntity.collectItem.isEmpty()){

        }

        poseStack.popPose();

    }

    @Override
    public void render(PoseStack poseStack) {

    }
}
