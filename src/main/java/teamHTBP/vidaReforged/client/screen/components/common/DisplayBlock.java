package teamHTBP.vidaReforged.client.screen.components.common;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
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
import net.minecraftforge.client.ForgeHooksClient;
import org.joml.AxisAngle4d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;

import java.util.List;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

@Deprecated
public class DisplayBlock extends VidaWidget {
    FloatRange alphaRange = new FloatRange(0,0,1f);
    FloatRange hoverRange = new FloatRange(0,0,0.4f);
    final ItemRenderer itemRenderer;
    final TextureManager textureManager;
    final Font font;
    float rotateX = 0;
    float rotateY = 0;
    private final ItemStack itemStack;
    protected boolean isDragging = false;
    public static final ResourceLocation GUIDEBOOK_RESOURCE = new ResourceLocation(MOD_ID, "textures/gui/book.png");
    public static final TextureSection BACKGROUND_SEC = new TextureSection(GUIDEBOOK_RESOURCE, 80, 336, 128, 64, 512, 512);

    public int size = 16;

    public DisplayBlock(int x, int y, int width, int height, ItemStack renderItem, Component component) {
        super(x, y, width, height, component);
        this.itemRenderer = mc.getItemRenderer();
        this.textureManager = mc.getTextureManager();
        this.itemStack = renderItem;
        this.font = Minecraft.getInstance().font;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int modelX = (int) (getX() + (getWidth() - 16.0F) / 2.0F);
        int modelY = getY() + 10;
        int textX = (int) (getX() + (getWidth() - 80) / 2.0F);
        int textY = getY() + 20;
        rotateX += Minecraft.getInstance().getDeltaFrameTime() * 3;
        renderBg(graphics, getX(), getY(), getWidth(), getHeight(), partialTicks);
        renderWithState(graphics, itemStack, modelX, modelY,partialTicks);
        renderBlockText(graphics, getMessage(), textX, textY, partialTicks);
    }

    /**渲染文字*/
    protected void renderBlockText(GuiGraphics graphics,Component component, int x, int y, float partialTicks) {
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        graphics.drawCenteredString(font, component, x + 40, y + 30, ARGBColor.of(alphaRange.get(),1,1, 1).argb());
        poseStack.popPose();
    }

    /**渲染*/
    protected void renderBg(GuiGraphics graphics, int x, int y, int width, int height, float partialTicks){}

    /***/
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if(!visible){
            this.alphaRange.set(0.0f);
            this.hoverRange.set(0.0f);
        }
    }

    /**
     * @param graphics
     * @param partialTicks
     */
    public void renderWithState(GuiGraphics graphics, ItemStack renderStack, int startX, int startY, float partialTicks){
        //透明度
        this.alphaRange.increase(0.02f);
        //this.hoverRange.change(isHovered || isDragging,0.02f);
        this.isDragging = false;

        BakedModel model = itemRenderer.getModel(renderStack, mc.level, mc.player, 0);

        //渲染
        PoseStack pPoseStack = graphics.pose();

        pPoseStack.pushPose();
        pPoseStack.translate(model.isGui3d()? size + 8.0F + startX : startX, model.isGui3d() ? size + 8.0F + startY : startY + 32.0F, size);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        pPoseStack.pushPose();
        //绑定方块模型,用于模型材质绑定
        ResourceLocation atlas = InventoryMenu.BLOCK_ATLAS;
        RenderSystem.setShaderTexture(0, atlas);
        textureManager.getTexture(atlas).setFilter(false, false);
        //GL11方法, @see:ItemRenderer#renderItemModelIntoGUI
        RenderSystem.enableBlend();
        //渲染值全颜色
        RenderSystem.setShaderColor(0.8F, 0.8F, 0.8F, 1.0F);
        //设置渲染位移
        //设置渲染的位置
        //设置渲染大小
        pPoseStack.mulPoseMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
        pPoseStack.scale(32.0F, 32.0F, 32.0F);
        //获取要渲染的模型
        Minecraft mc = Minecraft.getInstance();
        //将模型调整成适合GUI渲染的scale模型
        model = ForgeHooksClient.handleCameraTransforms(pPoseStack, model, ItemDisplayContext.GUI, false);

        //模型旋转
        pPoseStack.translate(0.5 ,0.5 ,0.5); //将模型调整到正中央

        final float mul =  ((float)Math.PI / 180F);

        pPoseStack.mulPose(new Quaternionf(new AxisAngle4d(rotateX * mul, 0,1,0)));
        pPoseStack.mulPose(new Quaternionf(new AxisAngle4d(rotateY * mul, 1,0,0)));

        pPoseStack.translate(-0.5,-0.5,-0.5f);// 将模型调整到旋转后的正中央



        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1,1,1,alphaRange.get() + hoverRange.get());


        // 再次将模型比例缩小50%
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
        return true;
    }

    @Override
    public void playDownSound(SoundManager p_93665_) {
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

    }

    @Override
    public List<GuiEventListener> children() {
        return List.of(this);
    }
}
