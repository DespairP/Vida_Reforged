package teamHTBP.vidaReforged.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.model.data.ModelData;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.client.screen.components.guidebooks.VidaPageAnimationProgressBar;
import teamHTBP.vidaReforged.core.common.mutiblock.VidaMultiblock;
import teamHTBP.vidaReforged.core.common.system.guidebook.*;
import teamHTBP.vidaReforged.core.common.vertex.LiquidBlockVertexConsumer;
import teamHTBP.vidaReforged.core.utils.anim.SecondOrderDynamics;
import teamHTBP.vidaReforged.core.utils.animation.Animator;
import teamHTBP.vidaReforged.core.utils.animation.DestinationAnimator;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;
import teamHTBP.vidaReforged.server.blocks.VidaBlockLoader;
import teamHTBP.vidaReforged.server.menu.TimeElementCraftingTableMenuContainer;

import java.util.HashMap;
import java.util.List;

public class TimeElementCraftTableScreenTest extends AbstractContainerScreen<TimeElementCraftingTableMenuContainer> {
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
    VidaPageAnimation animation = new VidaPageAnimation.Builder()
            .setup()  // 构造场景
                .size(3,3)  // 3D场景大小，比如这里是3X3的场景
                .enableRotate() // 是否允许玩家自行旋转
                .defineObject("id_1", Blocks.GRASS_BLOCK.defaultBlockState()) // 定义场景物品
                .defineObject("id_2", VidaBlockLoader.VIDA_LOG.get().defaultBlockState())
                .done()
            .step(1)  // 开始第一步
                .subStep(1) // 1.1步
                    .getObjectById("id_1").show(0, 0, 0) // 在（0，0）位置10帧弹出方块
                    .getObjectById("id_2").show(0, 1, 0)
                    .end()
                .subStep(2)
                    .getObjectById("id_1").moveTo(0, 0, 2)
                    .end()
            .end()
            .build(); // 第一步终止
    private int step = 1;
    private int substep = 1;
    private int ticks = 0;
    private HashMap<String, AnimatedObject> definedAnimatedBlocks;
    private BackgroundSetup background;

    private int x;

    private int y;

    public VidaGuiHelper.TickHelper ticker;

    private static int WAIT_SECOND = 5;

    private int currentSubStepCostTime = 0;

    private int maxCostTime = 0;

    private VidaPageAnimationStep currentStep;

    private VidaPageAnimationProgressBar bar;


    public TimeElementCraftTableScreenTest(TimeElementCraftingTableMenuContainer pMenu, Inventory pInventory, Component pTitle) {
        super(pMenu, pInventory, pTitle);
        inventory = pInventory;
        definedAnimatedBlocks = new HashMap<>();
    }

    @Override
    public void added() {
        super.added();

        // 获取配置
        VidaPageAnimationSetup setup = animation.getSetup();
        for(VidaPageAnimationObject object : setup.getDefinedObjects()){
            AnimatedObject animObject = new AnimatedObject(new BlockPos(0, 0, 0), object, 0, this::renderBlock);
            animObject.block = object.block != null ? object.block : Blocks.AIR.defaultBlockState();
            definedAnimatedBlocks.put(object.id, animObject);
        }

        this.background = new BackgroundSetup(setup.getSize().x, setup.getSize().z);
        this.background.render = this::renderBackgroundSetup;
        this.step = 1;
        this.substep = 1;
        this.ticker = new VidaGuiHelper.TickHelper();
        this.unpackAndLoadSteps(1);

        this.y = 200;
    }


    public void unpackAndLoadSteps(int step){
        this.ticker.reset();
        this.step = step;

        VidaPageAnimationStep animStep = animation.getSteps().get(Integer.valueOf(step));
        VidaPageAnimationSubStep animSubStep = animStep.getSubStep(this.substep);

        List<VidaPageAnimationObjectHistory> histories = animSubStep.getChanges();

        int costLength = 0;

        for(VidaPageAnimationObjectHistory history : histories){
            String id = history.getId();
            AnimatedObject animatedObject = definedAnimatedBlocks.get(id);
            switch (history.getType()){
                case MOVE -> {
                    //TODO
                }
                case SHOW -> {
                    animatedObject.show(history.getAttribute().getPos());
                    animatedObject.isVisible = true;
                }
            }
            costLength = Math.max(costLength, history.getTime());
        }

        this.maxCostTime = costLength;

        // TODO:还原至上一个step状态
    }

    @Override
    protected void init() {
        super.init();
        multiblock.setWorld(inventory.player.level());
        alphaAnimator.start();
        this.bar = new VidaPageAnimationProgressBar((width - 300) / 2, y + 100, 300, 4, WAIT_SECOND, animation);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
        this.renderBackground(graphics);
        this.background.render(graphics, pMouseX, pMouseY, pPartialTick);
    }

    /** 渲染背景 */
    @Override
    public void renderBackground(GuiGraphics graphics) {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1F, 1F, 1F, alphaAnimator.getValue());
        super.renderBackground(graphics);
        RenderSystem.disableBlend();
    }

    /** 渲染方块背景 */
    public void renderBackgroundSetup(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks, Vector3f size, int x, int z){
        // 获取blockRenderer、bufferSource与PoseStack
        final BlockRenderDispatcher blockRenderer = this.minecraft.getBlockRenderer();
        final MultiBufferSource.BufferSource buffers = graphics.bufferSource();
        final PoseStack poseStack = graphics.pose();

        // 入栈1：设置结构偏移
        poseStack.pushPose();
        poseStack.translate((width - MAX_STRUCTURE_WIDTH) / 2, y, 1000.0F);
        poseStack.scale(size.x, size.y, size.z);
        // 中心方块是-1,-1偏移后的方块
        poseStack.translate(-1.0F, -1.0F, 0.0F);


        // 将模型调整到旋转正中央
        poseStack.translate(0.5 ,0.5,0.5);
        // 因为renderBatched整个结构是倒着渲染的，先要将整个结构按X轴正转180度，然后再按20度，让方块的顶部面向玩家
        poseStack.mulPose(Axis.XP.rotationDegrees(180 - 30 + rotateY));
        // 再按Y轴转45度，让方块左边和右边面向玩家，呈现三视图的状态
        poseStack.mulPose(Axis.YP.rotationDegrees(45 + rotateX));
        // 将模型调整到旋转前正中央
        poseStack.translate(-0.5 ,-0.5,-0.5);



        // 入栈1-1：方块渲染
        RenderSystem.enableBlend();
        // 取消面剪切
        RenderSystem.disableCull();
        // 方块Shader颜色
        RenderSystem.setShaderColor(1F, 1F, 1F, alphaAnimator.getValue());

        Iterable<BlockPos> blockPoses = BlockPos.betweenClosed(new BlockPos(- x / 2, -1, - z /2), new BlockPos(x / 2, -1, z / 2));
        for(BlockPos pos : blockPoses){
            BlockState state = (pos.getX() + pos.getZ()) % 2 == 0 ? Blocks.WHITE_CONCRETE.defaultBlockState() : Blocks.GRAY_CONCRETE.defaultBlockState();

            // 入栈2：设置单个方块
            poseStack.pushPose();
            poseStack.translate(pos.getX(), pos.getY(), pos.getZ());


            //渲染实心方块
            // 获取blockstate对应烘焙模型
            final BakedModel model = blockRenderer.getBlockModel(state);
            for (RenderType layer : model.getRenderTypes(state, RAND, ModelData.EMPTY)) {
                final VertexConsumer buffer = buffers.getBuffer(RenderType.translucent());
                // 通知此batch加入相应的render
                blockRenderer.renderBatched(state, pos, multiblock, poseStack, buffer,false, RAND, ModelData.EMPTY, layer);
            }


            // 出栈2
            poseStack.popPose();
        }

        // 通知batch加入完毕
        buffers.endBatch();
        // 恢复初始值
        RenderSystem.enableCull();
        // 出栈1-1
        RenderSystem.disableBlend();
        // 出栈1
        poseStack.popPose();
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        // 渲染物品栏
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
        //
        long tickLength = ticker.getTicksLength();
        if(tickLength > WAIT_SECOND){
            renderBlocks(graphics, pMouseX, pMouseY, pPartialTick);
        }

        renderProgressBar(graphics, pMouseX, pMouseY, pPartialTick);

        alphaAnimator.tick(pPartialTick);
    }

    public void renderProgressBar(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick){
        this.bar.render(graphics, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int pMouseX, int pMouseY) {

    }

    public void renderBlocks(GuiGraphics graphics, int mouseX, int mouseY,float partialTicks){
        PoseStack pPoseStack = graphics.pose();

        pPoseStack.pushPose();
        //renderBlockWithState(graphics, this.menu.getBlockPos() , partialTicks); //1
        renderBlockWithState(graphics, mouseX, mouseY, partialTicks);

        pPoseStack.popPose();
    }


    /*  public void renderBlocksWithRangeInGui(GuiGraphics graphics, float partialTicks){
        PoseStack pPoseStack = graphics.pose();

        pPoseStack.pushPose();
        //renderBlockWithState(graphics, this.menu.getBlockPos() , partialTicks); //1

        pPoseStack.popPose();
    }*/


    public void renderBlock(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks, BlockState block, Vector3f size, Vector3f pos){
        PoseStack poseStack = graphics.pose();
        BlockRenderDispatcher blockRenderer = this.minecraft.getBlockRenderer();
        final MultiBufferSource.BufferSource buffers = graphics.bufferSource();

        BlockState blockState = block;
        BlockPos blockPos = new BlockPos((int)pos.x, (int)pos.y, (int)pos.z);
        // 入栈2：设置单个方块
        poseStack.pushPose();
        poseStack.translate(pos.x,pos.y,pos.z);
        final FluidState fluidState = blockState.getFluidState();

        poseStack.pushPose();
        poseStack.translate(0.5f, 0.5f, 0.5f);
        poseStack.scale(size.x / 16.0f, size.y / 16.0f, size.z / 16.0f);
        poseStack.translate(-0.5f, -0.5f, -0.5f);


        // TODO:渲染液体方块
        if (!fluidState.isEmpty()) {
            final RenderType layer = ItemBlockRenderTypes.getRenderLayer(fluidState);
            final VertexConsumer buffer = buffers.getBuffer(layer);
            blockRenderer.renderLiquid(blockPos, multiblock, new LiquidBlockVertexConsumer(buffer, poseStack, blockPos), blockState, fluidState);
        }

        //渲染实心方块
        if(blockState.getRenderShape() != RenderShape.INVISIBLE){
            // 获取blockstate对应烘焙模型
            final BakedModel model = blockRenderer.getBlockModel(blockState);
            for (RenderType layer : model.getRenderTypes(blockState, RAND, ModelData.EMPTY)) {
                final VertexConsumer buffer = buffers.getBuffer(RenderType.translucent());
                // 通知此batch加入相应的render
                blockRenderer.renderBatched(blockState, blockPos, multiblock, poseStack, buffer,false, RAND, ModelData.EMPTY, layer);
            }
        }

        poseStack.popPose();

        // 出栈2
        poseStack.popPose();
    }

    public void renderBlockWithState(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        // 获取blockRenderer、bufferSource与PoseStack
        final MultiBufferSource.BufferSource buffers = graphics.bufferSource();
        final PoseStack pPoseStack = graphics.pose();

        // 入栈1：设置结构偏移
        pPoseStack.pushPose();
        pPoseStack.translate((width - MAX_STRUCTURE_WIDTH) / 2, y, 1003.0F);
        pPoseStack.scale(32,32,32);
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


        // 从中心开始 (-5,-5,-5) 到 (5,5,5) 渲染方块
        for(AnimatedObject animatedObject : definedAnimatedBlocks.values()){
            animatedObject.render(graphics, mouseX, mouseY, partialTicks);
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


    @Override
    protected void containerTick() {
        super.containerTick();
        ticker.tick(minecraft.getPartialTick());

        long ticks = ticker.getTicksLength() > WAIT_SECOND + maxCostTime ? WAIT_SECOND + maxCostTime : ticker.getTicksLength();
        bar.setTick((int)ticks);
    }

    public static class AnimatedObject {
        public SecondOrderDynamics position;

        public final SecondOrderDynamics size;
        public boolean isVisible;

        public final IAnimatedObjectRender render;

        public BlockState block;

        public VidaPageAnimationObject.Type type;
        private Vector3f targetSize = new Vector3f(0, 0, 0);
        private Vector3f targetPos = new Vector3f(0, 0, 0);

        private boolean isNeedUpdate = false;

        private int time;

        /**
         *
         * @param position 初始位置
         * @param size 初始大小
         * @param render
         */
        public AnimatedObject(BlockPos position, VidaPageAnimationObject object , int size, IAnimatedObjectRender render) {
            this.position = new SecondOrderDynamics(1, 1, 0, position.getCenter().toVector3f());
            this.size = new SecondOrderDynamics(1, 1, 0, new Vector3f(size, size, size));
            this.type = object.type;
            this.render = render;
            this.isVisible = false;

            switch (type){
                case BLOCK -> {
                    block = object.block;
                }
            }
        }

        public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
            if(! isVisible ){
                return;
            }

            Vector3f curSize = size.currentPos();
            Vector3f curPos = position.currentPos();

            if(isNeedUpdate) {
                curSize = size.update(Minecraft.getInstance().getDeltaFrameTime()  / time, targetSize, null);
                curPos = position.update(Minecraft.getInstance().getDeltaFrameTime() / time, targetPos, null);
            }

            this.render.render(graphics, mouseX, mouseY, partialTicks, block, curSize, curPos);
        }

        public void show(Vector3f targetPos){
            this.isNeedUpdate = true;
            this.targetSize = new Vector3f(16, 16, 16);
            this.targetPos = targetPos;
            this.position = new SecondOrderDynamics(1, 1, 0, new Vector3f(targetPos));
            this.time = 20;
        }
    }


    public interface IAnimatedObjectRender{
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks, BlockState block, Vector3f size, Vector3f pos);
    }

    public static class BackgroundSetup{
        int x;
        int z;
        private SecondOrderDynamics size = new SecondOrderDynamics(1, 1, 0,  new Vector3f(0,0,0));
        private final Vector3f sizeTarget = new Vector3f(32, 32, 32);
        private IBackgroundObjectRender render;


        public BackgroundSetup(int x, int z){
            this.x = x;
            this.z = z;
        }

        public void setRender(IBackgroundObjectRender render) {
            this.render = render;
        }

        public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            Vector3f curSize = size.update(Minecraft.getInstance().getDeltaFrameTime() * 0.1f, sizeTarget, null);
            render.render(graphics, mouseX, mouseY, partialTicks, curSize, x, z);
        }
    }

    public interface IBackgroundObjectRender{
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks, Vector3f size, int x, int z);
    }
}
