package teamHTBP.vidaReforged.client.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;
import teamHTBP.vidaReforged.server.blockEntities.VaseBlockEntity;

public class VaseScreen extends VidaHUDScreen {
    VaseBlockEntity entity = null;
    final TextureSection HUD = new TextureSection(new ResourceLocation(VidaReforged.MOD_ID, "textures/block/blockmodel/vase.png"), 4, 3, 9, 13, 32, 32);

    public VaseScreen(Minecraft minecraft, MultiBufferSource.BufferSource bufferSource) {
        super(minecraft, bufferSource);
    }

    public void setNewEntity(VaseBlockEntity entity){
        this.entity = entity;
    }

    public void render(GuiGraphics graphics, float partialTicks){
        if(entity == null || entity.isRemoved()){
            return;
        }
        // 如果已经完成插花就不需要渲染了
        if(entity.isCompleted()){
            return;
        }
        SimpleContainer container = entity.getFlowers();
        VidaGuiHelper.blitWithTexture(graphics, centerX(HUD), centerY(HUD) - 32, 0, HUD);
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        int offsetX = -16;
        for(int i = 0; i < 3; i++){
            graphics.renderItem(container.getItem(i), centerX(HUD) + offsetX + i * 16 - 3,centerY(HUD) - 62);
        }
        poseStack.popPose();
        if(container.isEmpty()){
            return;
        }
        Component key = Component.translatable("gui.vida_reforged.vase_complete");
        poseStack.pushPose();
        graphics.drawCenteredString(Minecraft.getInstance().font, key, centerX(HUD), centerY(HUD) - 42, 0xFFFFFF);
        poseStack.popPose();

        poseStack.pushPose();
        int keyLength = Minecraft.getInstance().font.width(key);
        graphics.renderItem(new ItemStack(Items.CLAY_BALL),  centerX(HUD) - keyLength / 2 + 17,  centerY(HUD) - 46);
        poseStack.popPose();
    }
}
