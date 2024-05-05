package teamHTBP.vidaReforged.client.screen.components.guidebooks;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.AxisAngle4d;
import org.joml.Quaternionf;
import teamHTBP.vidaReforged.core.api.screen.IGuideBookSection;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;

@Deprecated
public class GuideBookBlockModel extends AbstractWidget implements IGuidebookComponent {
    FloatRange alphaRange = new FloatRange(0,0,0.6f);
    FloatRange hoverRange = new FloatRange(0,0,0.4f);
    final ItemRenderer itemRenderer;
    final TextureManager textureManager;
    final Font font;
    float rotateX = 0;
    float rotateY = 0;
    private final ResourceLocation blockId;

    protected boolean isDragging = false;

    public GuideBookBlockModel(int x, int y, int width, int height,ResourceLocation blockId) {
        super(x, y, width, height, Component.translatable("block model"));
        this.itemRenderer = Minecraft.getInstance().getItemRenderer();
        this.textureManager = Minecraft.getInstance().getTextureManager();
        this.blockId = blockId;
        this.font = Minecraft.getInstance().font;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int modelX = (int) (getX() + (getWidth() - 16.0F) / 2.0F);
        int modelY = (int) ((getY() - 16.0F + getHeight()) * 1.3F / 3.0F);
        int textX = (int) (getX() + (getWidth() - 80) / 2.0F);
        int textY = (int) ((getY() - 16.0F + getHeight()) * 2.7F / 3.0F);

        renderBg(graphics, getX(), getY(), getWidth(), getHeight(), partialTicks);
        Block block = ForgeRegistries.BLOCKS.getValue(blockId);
        renderBlockWithState(graphics, new ItemStack(block), modelX, modelY,partialTicks);
        renderBlockText(graphics, block.getName().withStyle(style -> style.withBold(true)), textX, textY, partialTicks);
    }

    /**渲染文字*/
    protected void renderBlockText(GuiGraphics graphics,Component component, int x, int y, float partialTicks) {
        renderBg(graphics, x, y,80,15, partialTicks);

        PoseStack poseStack = graphics.pose();

        poseStack.pushPose();
        graphics.drawCenteredString(font, component, x + 40, y + 3, 0xFFFFFF);
        poseStack.popPose();
    }

    /**渲染*/
    protected void renderBg(GuiGraphics graphics, int x, int y, int width, int height, float partialTicks){
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        graphics.fillGradient(
                x,
                y,
                x + width,
                y + height,
                0x50000000,
                0x20000000
        );
        poseStack.popPose();
    }

    /**
     * @param graphics
     * @param partialTicks
     */
    public void renderBlockWithState(GuiGraphics graphics, ItemStack renderStack, int startX, int startY, float partialTicks){
        //透明度
        this.alphaRange.increase(0.02f);
        this.hoverRange.change(isHovered || isDragging,0.02f);
        this.isDragging = false;

        //渲染
        PoseStack pPoseStack = graphics.pose();

        pPoseStack.pushPose();
        pPoseStack.translate(16.0F + 8.0F + startX, 16.0F + 8.0F + startY, 16.0F);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        pPoseStack.pushPose();
        //绑定方块模型,用于模型材质绑定
        ResourceLocation atlas = InventoryMenu.BLOCK_ATLAS;
        RenderSystem.setShaderTexture(0, atlas);
        textureManager.getTexture(atlas).setFilter(false, false);
        //GL11方法, @see:ItemRenderer#renderItemModelIntoGUI
        //RenderSystem.enableRescaleNormal();
        //RenderSystem.enableAlphaTest();
        //RenderSystem.defaultAlphaFunc();
        RenderSystem.enableBlend();
        //RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        //渲染值全颜色
        RenderSystem.setShaderColor(0.8F, 0.8F, 0.8F, 1.0F);
        //设置渲染位移
        //设置渲染的位置
        //pPoseStack.translate((float)x + componentSection.cw() - 20.0F, (float)y + componentSection.ch() - 32.0F, 100.0F);
        //设置渲染大小
        pPoseStack.scale(1.0F, -1.0F, 1.0F);
        pPoseStack.scale(32.0F, 32.0F, 32.0F);
        //获取要渲染的模型
        Minecraft mc = Minecraft.getInstance();
        BakedModel model = itemRenderer.getModel(renderStack, mc.level, mc.player, 0);
        //获取渲染的方式
        RenderType rendertype = ItemBlockRenderTypes.getRenderType(renderStack, true);
        //将模型调整成适合GUI渲染的scale模型
        model = ForgeHooksClient.handleCameraTransforms(pPoseStack, model, ItemDisplayContext.GUI, false);
        //模型旋转
        pPoseStack.translate(0.5 ,0.5 ,0.5); //将模型调整到正中央
        //final float mul =  ((float)Math.PI / 180F);
        final float mul =  ((float)Math.PI / 180F);

        pPoseStack.mulPose(new Quaternionf(new AxisAngle4d(rotateX * mul, 0,1,0)));
        pPoseStack.mulPose(new Quaternionf(new AxisAngle4d(rotateY * mul, 1,0,0)));

        pPoseStack.translate(-0.5,-0.5,-0.5f);// 将模型调整到旋转后的正中央

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1,1,1,alphaRange.get() + hoverRange.get());

        // 再次将模型比例缩小50%
        //MatrixUtil.mulComponentWise(pPoseStack.last().pose(), 0.5F);
        //渲染设定是否发光
        boolean flag = !model.usesBlockLight();
        if (flag) {
            Lighting.setupForFlatItems();
        }
        //获取渲染buffer
        MultiBufferSource.BufferSource source = graphics.bufferSource();
        //真正绑定模型
        VertexConsumer buffer = source.getBuffer(RenderType.translucent());
        //开始渲染
        itemRenderer.renderModelLists(model, renderStack, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, pPoseStack, buffer);
        //结束渲染
        source.endBatch();
        //恢复渲染初始值
        if (flag) {
            Lighting.setupFor3DItems();
        }

        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1,1,1,1);
        pPoseStack.popPose();
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
        pPoseStack.popPose();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double dragX, double dragY) {
        this.rotateX = (float) ((this.rotateX + dragX) % 360);
        this.rotateY = (float) ((this.rotateY + dragY) % 360);
        this.isDragging = true;
        return true;
    }

    @Override
    public boolean mouseReleased(double p_93684_, double p_93685_, int p_93686_) {
        this.onRelease(p_93684_, p_93685_);
        return true;
    }

    @Override
    public void onRelease(double p_93669_, double p_93670_) {
        this.isDragging = false;
    }

    @Override
    public void playDownSound(SoundManager p_93665_) {
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

    }
}
