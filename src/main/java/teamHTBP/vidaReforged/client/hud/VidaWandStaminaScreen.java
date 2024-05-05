package teamHTBP.vidaReforged.client.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import teamHTBP.vidaReforged.core.api.hud.IVidaScreen;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;
import teamHTBP.vidaReforged.server.items.VidaWand;

import static teamHTBP.vidaReforged.helper.VidaGuiHelper.renderHollowCircle;
import static teamHTBP.vidaReforged.helper.VidaInventoryHelper.getHandInItemByClient;
import static teamHTBP.vidaReforged.server.items.VidaWand.holdTime;

/**
 * 法杖蓄力槽
 * */
@OnlyIn(Dist.CLIENT)
public class VidaWandStaminaScreen extends GuiGraphics implements IVidaScreen {
    float lastDegree = 0;


    public VidaWandStaminaScreen(Minecraft mc, MultiBufferSource.BufferSource bufferSource) {
        super(mc, bufferSource);
    }


    public void render(GuiGraphics graphics, float partialTicks) {
        ItemStack handInItem = getHandInItemByClient(InteractionHand.MAIN_HAND);
        if(handInItem == null || !handInItem.is(VidaItemLoader.VIDA_WAND.get())){
            return;
        }

        // 最大蓄力时间
        final int maxHoldTime = VidaWand.getMaxUseDuration(handInItem);

        final float degrees = 360.0f * (holdTime * 1.0f / maxHoldTime);

        float lerpDegree = Mth.lerp(partialTicks, lastDegree, degrees);

        //
        PoseStack poseStack = graphics.pose();

        // 渲染圆环
        poseStack.pushPose();
        renderHollowCircle(
                this,
                this.pose(),
                this.guiWidth() / 2,
                this.guiHeight() / 2,
                5,
                10,
                lerpDegree,
                new ARGBColor(10, 247, 254, 100),
                new ARGBColor(10, 166, 255, 100)
        );
        poseStack.popPose();

        this.lastDegree = degrees;

    }
}
