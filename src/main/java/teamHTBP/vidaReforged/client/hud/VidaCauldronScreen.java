package teamHTBP.vidaReforged.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Vector2i;
import teamHTBP.vidaReforged.core.api.hud.IVidaScreen;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.server.blockEntities.BasePurificationCauldronBlockEntity;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

/**
 * 纯净锅HUD
 * */
public class VidaCauldronScreen extends GuiGraphics implements IVidaScreen {

    ResourceLocation location = new ResourceLocation(MOD_ID, "textures/gui/cauldron_hud.png");

    TextureSection plateSection = new TextureSection(location,2,23,22, 7);

    public VidaCauldronScreen(Minecraft minecraft, MultiBufferSource.BufferSource bufferSource) {
        super(minecraft, bufferSource);
    }

    public void render(PoseStack poseStack, BlockEntity entity,float alpha){
        BasePurificationCauldronBlockEntity cauldronBlockEntity = null;
        if(entity instanceof BasePurificationCauldronBlockEntity blockEntity){
            cauldronBlockEntity = blockEntity;
        }
        if(cauldronBlockEntity == null){
            return;
        }
        poseStack.pushPose();

        int plateX = (guiWidth() - plateSection.w()) / 2;
        int plateY = guiHeight() / 2 - 30;

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1, 1, 1, alpha);

        blit(
                plateSection.location(),
                plateX, plateY, 0,
                plateSection.minU(), plateSection.minV(),
                plateSection.w(), plateSection.h(),
                64, 64);

        this.renderItem(cauldronBlockEntity, new Vector2i(plateX, plateY));

        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
        poseStack.popPose();
    }

    public void renderItem(BasePurificationCauldronBlockEntity blockEntity,Vector2i plate){
        if(!blockEntity.isInProgress){
            return;
        }
        super.renderItem(blockEntity.purificationItems.get(0), plate.x + 4, plate.y - 16);
    }

    public void renderCauldron(){

    }

    @Override
    public void render(PoseStack poseStack) {

    }
}
