package teamHTBP.vidaReforged.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
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
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.data.ModelData;
import org.joml.*;
import org.lwjgl.opengl.GL30;
import teamHTBP.vidaReforged.client.screen.components.common.IconButton;
import teamHTBP.vidaReforged.core.common.mutiblock.VidaMultiblock;
import teamHTBP.vidaReforged.core.common.vertex.LiquidBlockVertexConsumer;
import teamHTBP.vidaReforged.core.utils.anim.SecondOrderDynamics;
import teamHTBP.vidaReforged.core.utils.animation.Animator;
import teamHTBP.vidaReforged.core.utils.animation.DestinationAnimator;
import teamHTBP.vidaReforged.core.utils.animation.TimeInterpolator;
import teamHTBP.vidaReforged.core.utils.animation.calculator.IValueProvider;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.color.VidaColor;
import teamHTBP.vidaReforged.server.blocks.VidaBlockLoader;
import teamHTBP.vidaReforged.server.menu.TimeElementCraftingTableMenuContainer;

import java.lang.Math;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11C.*;
import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;
import static teamHTBP.vidaReforged.server.menu.TimeElementCraftingTableMenuContainer.MENU_NAME;

public class TimeElementCraftTableScreen extends AbstractContainerScreen<TimeElementCraftingTableMenuContainer> {
    /**材质管理*/
    private final TextureManager textureManager = Minecraft.getInstance().textureManager;
    /**RandomSource*/
    private final RandomSource RAND = RandomSource.createNewThreadLocalInstance();
    /**是否被悬浮*/
    private boolean isHovered = false;
    /**是否能被旋转*/
    private boolean isFocused = false;

    private Animator<Float> alphaAnimator = DestinationAnimator.of(6.0f,0.0f,1.0f);

    Inventory inventory;
    private float prevRotateX;
    private float prevRotateY;
    private float rotateX;
    private float rotateY;
    private SecondOrderDynamics size = new SecondOrderDynamics(1, 1, 0,  new Vector3f(1,1,1));
    private final Vector3f sizeTarget = new Vector3f(16, 16, 16);
    private static final double MAX_STRUCTURE_WIDTH = Math.sqrt( 16 ^ 2 * 2) * 10f;
    VidaMultiblock multiblock = new VidaMultiblock();



    public TimeElementCraftTableScreen(TimeElementCraftingTableMenuContainer pMenu, Inventory pInventory, Component pTitle) {
        super(pMenu, pInventory, pTitle);
        inventory = pInventory;
    }

    @Override
    protected void init() {
        super.init();
        multiblock.setWorld(inventory.player.level());
        alphaAnimator.start();
    }

    /**渲染背景*/
    @Override
    public void renderBackground(GuiGraphics graphics) {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1F, 1F, 1F, alphaAnimator.getValue());
        super.renderBackground(graphics);
        RenderSystem.disableBlend();
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
        renderBlocks(graphics, pPartialTick);
        alphaAnimator.tick(pPartialTick);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
        this.renderBackground(graphics);
    }


    @Override
    protected void renderLabels(GuiGraphics graphics, int pMouseX, int pMouseY) {

    }

    public void renderBlocks(GuiGraphics graphics,float partialTicks){
        renderBlocksWithRangeInGui(graphics,partialTicks);
    }


    /**
     * @param graphics poseStack
     * @param partialTicks partialTicks
     */
    public void renderBlocksWithRangeInGui(GuiGraphics graphics, float partialTicks){
        PoseStack pPoseStack = graphics.pose();

        pPoseStack.pushPose();
        renderBlockWithState(graphics, this.menu.getBlockPos() , partialTicks); //1

        pPoseStack.popPose();
    }


    public void renderBlockWithState(GuiGraphics graphics, BlockPos startPos, float partialTicks){
        Vector3f scaleSize = size.update(Minecraft.getInstance().getDeltaFrameTime() * 0.2F, sizeTarget, null);
        // 获取blockRenderer、bufferSource与PoseStack
        final BlockRenderDispatcher blockRenderer = this.minecraft.getBlockRenderer();
        final MultiBufferSource.BufferSource buffers = graphics.bufferSource();
        final PoseStack pPoseStack = graphics.pose();

        // 入栈1：设置结构偏移
        pPoseStack.pushPose();
        pPoseStack.translate((width - MAX_STRUCTURE_WIDTH) / 2, 132.0F, 100.0F);
        pPoseStack.scale(scaleSize.x, scaleSize.y, scaleSize.z);
        // 中心方块是-1,-1偏移后的方块
        pPoseStack.translate(-1.0F, -1.0F, 0.0F);


        // 将模型调整到旋转正中央
        pPoseStack.translate(0.5 ,0.5,0.5);
        // 因为renderBatched整个结构是倒着渲染的，先要将整个结构按X轴正转180度，然后再按20度，让方块的顶部面向玩家
        pPoseStack.mulPose(Axis.XP.rotationDegrees(180 - 20 + rotateY));
        // 再按Y轴转45度，让方块左边和右边面向玩家，呈现三视图的状态
        pPoseStack.mulPose(Axis.YP.rotationDegrees(45 + rotateX));
        // 将模型调整到旋转前正中央
        pPoseStack.translate(-0.5 ,-0.5,-0.5);



        // 入栈1-1：方块渲染
        RenderSystem.enableBlend();
        // 取消面剪切
        RenderSystem.disableCull();
        // 方块Shader颜色
        RenderSystem.setShaderColor(1F, 1F, 1F, alphaAnimator.getValue());


        final Map<BlockPos, BlockState> testMap = Map.of(
                new BlockPos(0,0,0), VidaBlockLoader.VIDA_LOG.get().defaultBlockState(),
                new BlockPos(0,1,0), VidaBlockLoader.VIDA_LOG.get().defaultBlockState()
        );

        // 从中心开始 (-5,-5,-5) 到 (5,5,5) 渲染方块
        for(BlockPos pos : testMap.keySet()){
            BlockState state = testMap.get(pos);
            // 入栈2：设置单个方块
            pPoseStack.pushPose();
            pPoseStack.translate(pos.getX(), pos.getY(), pos.getZ());
            final FluidState fluidState = state.getFluidState();

            if(pos.getY() > 0){
                pPoseStack.translate(0, 1, 0);

                pPoseStack.translate(.5f, .5f, .5f);
                pPoseStack.scale(0.5f,0.5f,0.5f);
                pPoseStack.translate(-.5f, -.5f, -.5f);


            }

            // TODO:渲染液体方块
            if (!fluidState.isEmpty()) {
                final RenderType layer = ItemBlockRenderTypes.getRenderLayer(fluidState);
                final VertexConsumer buffer = buffers.getBuffer(layer);
                blockRenderer.renderLiquid(pos, multiblock, new LiquidBlockVertexConsumer(buffer, pPoseStack, pos), state, fluidState);
            }

            //渲染实心方块
            if(state.getRenderShape() != RenderShape.INVISIBLE){
                // 获取blockstate对应烘焙模型
                final BakedModel model = blockRenderer.getBlockModel(state);
                for (RenderType layer : model.getRenderTypes(state, RAND, ModelData.EMPTY)) {
                    final VertexConsumer buffer = buffers.getBuffer(RenderType.translucent());
                    // 通知此batch加入相应的render
                    blockRenderer.renderBatched(state, pos, multiblock, pPoseStack, buffer,false, RAND, ModelData.EMPTY, layer);
                }
            }
            // 出栈2
            pPoseStack.popPose();
        }



        // 通知batch加入完毕
        buffers.endBatch();
        // 恢复初始值
        RenderSystem.enableCull();
        // 出栈1-1
        RenderSystem.disableBlend();
        // 出栈1
        pPoseStack.popPose();
    }


    /**
     * @param graphics
     * @param state
     * @param partialTicks
     */
    /*
    public void renderBlockWithState(GuiGraphics graphics, BlockState state, int startX, int startY, int offsetStepX, int offsetStepY, int offsetStepZ, float partialTicks){
        ItemRenderer itemRenderer = this.minecraft.getItemRenderer();
        PoseStack pPoseStack = graphics.pose();

        ItemStack renderStack = new ItemStack(state.getBlock());
        //pPoseStack.;
        pPoseStack.pushPose();
        pPoseStack.translate(16.0F * offsetStepX + startX, 16.0F * offsetStepY + startY, 16.0F * offsetStepZ);
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
        pPoseStack.translate(32.0F, 32.0F, 32.0F);
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
        pPoseStack.translate(0.5 + offsetStepX,0.5 + offsetStepY,0.5 + offsetStepZ); //将模型调整到正中央
        //final float mul =  ((float)Math.PI / 180F);
        final float mul =  ((float)Math.PI / 180F);

        pPoseStack.mulPose(new Quaternionf(new AxisAngle4d(rotateX * mul, 0,1,0)));
        pPoseStack.mulPose(new Quaternionf(new AxisAngle4d(rotateY * mul, 1,0,0)));

        pPoseStack.translate(-(0.5 + offsetStepX),-(0.5 + offsetStepY),-(0.5f + offsetStepZ));// 将模型调整到旋转后的正中央

        // 再次将模型比例缩小50%
        //MatrixUtil.mulComponentWise(pPoseStack.last().pose(), 0.5F);
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
        RenderSystem.disableDepthTest();
        pPoseStack.popPose();
    }*/

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
