package teamHTBP.vidaReforged.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.MatrixUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ForgeHooksClient;
import org.joml.AxisAngle4d;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL30;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.color.VidaColor;
import teamHTBP.vidaReforged.server.menu.TimeElementCraftingTableMenuContainer;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;
import static teamHTBP.vidaReforged.server.menu.TimeElementCraftingTableMenuContainer.MENU_NAME;

public class TimeElementCraftTableScreen extends AbstractContainerScreen<TimeElementCraftingTableMenuContainer> {
    /**材质管理*/
    private final TextureManager textureManager = Minecraft.getInstance().textureManager;
    /**是否被悬浮*/
    private boolean isHovered = false;
    /**是否能被旋转*/
    private boolean isFocused = false;

    Inventory inventory;
    private float prevRotateX;
    private float prevRotateY;
    private float rotateX;
    private float rotateY;

    public TimeElementCraftTableScreen(TimeElementCraftingTableMenuContainer pMenu, Inventory pInventory, Component pTitle) {
        super(pMenu, pInventory, pTitle);
        inventory = pInventory;
    }




    /**渲染背景*/
    @Override
    public void renderBackground(GuiGraphics graphics) {
        super.renderBackground(graphics);
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
        renderBlocks(graphics, pPartialTick);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
        this.renderBackground(graphics);
    }


    @Override
    protected void renderLabels(GuiGraphics graphics, int pMouseX, int pMouseY) {

    }

    public void renderBlocks(GuiGraphics graphics,float partialTicks){
        //
        BlockState state = this.inventory.player.getCommandSenderWorld().getBlockState(this.menu.getBlockPos());
        ItemRenderer itemRenderer = this.minecraft.getItemRenderer();
        PoseStack pPoseStack = graphics.pose();


        ItemStack renderStack = new ItemStack(state.getBlock());
        //pPoseStack.;
        pPoseStack.pushPose();
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
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0f);
        //设置渲染位移
        //设置渲染的位置
        //pPoseStack.translate((float)x + componentSection.cw() - 20.0F, (float)y + componentSection.ch() - 32.0F, 100.0F);
        pPoseStack.translate(32.0F, 32.0F, 0.0F);
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
        pPoseStack.translate(0.5,0.5,0.5); //将模型调整到正中央
        final float mul =  ((float)Math.PI / 180F);
        pPoseStack.mulPose(new Quaternionf(new AxisAngle4d(rotateX * mul,0,1,0)));
        pPoseStack.mulPose(new Quaternionf(new AxisAngle4d(rotateY * mul,1,0,0)));
        pPoseStack.translate(-0.5,-0.5,-0.5f);// 将模型调整到旋转后的正中央
        // 再次将模型比例缩小50%
        MatrixUtil.mulComponentWise(pPoseStack.last().pose(), 0.5F);
        //渲染设定是否发光
        boolean flag = !model.usesBlockLight();
        if (flag) {
            Lighting.setupForFlatItems();
        }
        //获取渲染buffer
        MultiBufferSource.BufferSource source = mc.renderBuffers().bufferSource();
        //真正绑定模型
        VertexConsumer buffer = ItemRenderer.getFoilBuffer(source, rendertype, true, renderStack.hasFoil());
        //开始渲染
        itemRenderer.renderModelLists(model, renderStack, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, pPoseStack, buffer);
        //结束渲染
        source.endBatch();
        //恢复渲染初始值
        if (flag) {
            Lighting.setupFor3DItems();
        }
        // RenderSystem.disableAlphaTest();
        // RenderSystem.disableRescaleNormal();
        pPoseStack.popPose();
        pPoseStack.popPose();
    }

    /**当鼠标拖拽过程中*/
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if(!isFocused) {
            return false;
        }
        //保存上一次的值,用于线性插值
        prevRotateX = rotateX;
        prevRotateY = rotateY;
        //针对拖动进行旋转
        rotateX += dragX;
        rotateY += dragY;
        rotateX = rotateX % 361;
        rotateY = rotateY % 361;
        return true;
    }

    /**如果点击组件,标记组件能被旋转*/
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(isActive()) {
            this.isFocused = true;
        }
        return false;
    }

    /**放开组件,标记组件不能被旋转*/
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if(isFocused){
            this.isFocused = false;
            return true;
        }
        return false;
    }

    public boolean isActive(){
        return true;
    }
}
