package teamHTBP.vidaReforged.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Vector2i;
import teamHTBP.vidaReforged.core.api.hud.IVidaScreen;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.helper.GuiHelper;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.server.blockEntities.BasePurificationCauldronBlockEntity;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

/**
 * 纯净锅HUD
 */
public class VidaCauldronScreen extends GuiGraphics implements IVidaScreen {

    ResourceLocation location = new ResourceLocation(MOD_ID, "textures/gui/cauldron_hud.png");

    TextureSection plateSection = new TextureSection(location, 2, 23, 22, 7);

    TextureSection cauldronSection = new TextureSection(location, 2, 38, 22, 15);

    TextureSection bubbleSection = new TextureSection(location, 9, 7, 10, 8);

    TextureSection barSection = new TextureSection(location, 30, 14, 6, 22);

    TextureSection barFillSection = new TextureSection(location, 40, 32, 2, 1);

    public VidaCauldronScreen(Minecraft minecraft, MultiBufferSource.BufferSource bufferSource) {
        super(minecraft, bufferSource);
    }

    public void render(PoseStack poseStack, BlockEntity entity, float alpha) {
        BasePurificationCauldronBlockEntity cauldronBlockEntity = null;
        if (entity instanceof BasePurificationCauldronBlockEntity blockEntity) {
            cauldronBlockEntity = blockEntity;
        }
        if (cauldronBlockEntity == null) {
            return;
        }
        poseStack.pushPose();

        int plateX = (guiWidth() - plateSection.w()) / 2;
        int plateY = guiHeight() / 2 - 50;

        int cauldronX = (guiWidth() - plateSection.w()) / 2;
        int cauldronY = plateY + 20;

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);

        blit(
                plateSection.location(),
                plateX, plateY, 0,
                plateSection.minU(), plateSection.minV(),
                plateSection.w(), plateSection.h(),
                64, 64);

        blit(
                cauldronSection.location(),
                cauldronX, cauldronY, 0,
                cauldronSection.minU(), cauldronSection.minV(),
                cauldronSection.w(), cauldronSection.h(),
                64, 64
        );

        if (cauldronBlockEntity.isInProgress) {
            blit(
                    bubbleSection.location(),
                    cauldronX + 7, cauldronY - 8, 0,
                    bubbleSection.minU(), bubbleSection.minV(),
                    bubbleSection.w(), bubbleSection.h(),
                    64, 64
            );
        }

        this.renderItem(cauldronBlockEntity, new Vector2i(plateX, plateY));

        blit(
                barSection.location(),
                cauldronX + 30, cauldronY - 12, 0,
                barSection.minU(), barSection.minV(),
                barSection.w(), barSection.h(),
                64, 64
        );

        float progress = 17.0F * cauldronBlockEntity.totalProgress / cauldronBlockEntity.getMaxMainTaskProgress();
        int progressOffsetX = 2;
        int progressOffsetY = (int)progress;


        blit(
                barFillSection.location(),
                cauldronX + 30 + progressOffsetX, cauldronY + 7 - progressOffsetY, 0,
                barFillSection.minU(), barFillSection.minV() + 1 - (int)progress,
                barFillSection.w(), barFillSection.h() - 1 + (int)progress,
                64, 64
        );

        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
        poseStack.popPose();
    }

    public void renderItem(BasePurificationCauldronBlockEntity blockEntity, Vector2i plate) {
        if (!blockEntity.isInProgress) {
            return;
        }
        super.renderItem(blockEntity.purificationItems.get(0), plate.x + 4, plate.y - 20);

        Font font = Minecraft.getInstance().font;
        super.drawCenteredString( font, ""+ blockEntity.purificationItems.get(0).getCount(), plate.x + 20, plate.y - 6, 0xFFFFFF);


        if (blockEntity.getMaxSubTaskProgress() <= 0){
            return;
        }

        float degree = 360.0f * blockEntity.progress / blockEntity.getMaxSubTaskProgress();

        GuiHelper.renderCircle(
                this,
                this.pose(),
                plate.x + 12,
                plate.y - 12,
                10,
                degree,
                new ARGBColor(100,255,255, 255));
    }

    @Override
    public void render(PoseStack poseStack, float partialTicks) {

    }
}
