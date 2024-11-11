package teamHTBP.vidaReforged.client.screen.screens.magicwordCrafting;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import teamHTBP.vidaReforged.client.screen.components.magicWords.*;
import teamHTBP.vidaReforged.client.screen.screens.common.VidaContainerScreen;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaMagicWordViewModel;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.server.blockEntities.MagicWordCraftingTableBlockEntity;
import teamHTBP.vidaReforged.server.blocks.VidaBlockLoader;
import teamHTBP.vidaReforged.server.menu.MagicWordCraftingTableMenu;

import java.util.*;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;
import static teamHTBP.vidaReforged.core.utils.math.StringUtils.compareString;

public class MagicWordCraftingTableScreen extends VidaContainerScreen<MagicWordCraftingTableMenu> {
    /**
     * 显示所有持有的词条容器
     */
    protected MagicWordDisplayContainer magicWordWidget;
    /**
     * 过滤器
     */
    protected MagicWordFilterList<VidaElement> magicWordFilterLists;
    /**
     * 显示选择的词条
     */
    protected MagicSelectedWordContainer selectedWords;
    /**
     * 合成按钮
     */
    protected MagicWordCraftingButton magicWordCraftingButton;
    /**
     * 放入物品的槽位边框
     */
    List<MagicSlotComponent> magicSlots;
    /**
     * 材质管理
     */
    private final static ResourceLocation INVENTORY_LOCATION_RESOURCE = new ResourceLocation(MOD_ID, "textures/gui/magic_word_inventory.png");
    /**
     * 数据容器
     */
    private VidaMagicWordViewModel viewModel;
    /**Level*/
    private Level level;
    private static final int BORDER_Y = 28;
    private static final int BORDER_X = 200;


    public MagicWordCraftingTableScreen(MagicWordCraftingTableMenu menu, Inventory playerInventory, Component component) {
        super(menu, playerInventory, component);
        this.level = playerInventory.player.level();
    }

    @Override
    public void added() {
        super.added();
        // 初始化数据容器
        this.viewModel = new ViewModelProvider(this).get(VidaMagicWordViewModel.class);
        this.viewModel.playerMagicWords.setValue(menu.getPlayerMagicWords());
        this.viewModel.blockPos.setValue(menu.getBlockPos());
        this.viewModel.selectedFilterElement.setValue(VidaElement.GOLD);
    }

    @Override
    protected void init() {
        super.init();

        // 计算物品栏位置
        this.leftPos = (Minecraft.getInstance().getWindow().getGuiScaledWidth() - 200 - 20 - 196) / 2;
        this.topPos = (Minecraft.getInstance().getWindow().getGuiScaledHeight() - 84);

        // 计算组件位置
        int componentWidth = MagicWordDisplayContainer.WIDTH;
        int componentHeight = this.height - BORDER_Y;
        int x = this.width - BORDER_X;
        int y = (this.height - componentHeight) / 2;

        // 初始化组件
        this.magicWordWidget = new MagicWordDisplayContainer(x, y, componentWidth, componentHeight);
        this.magicWordFilterLists = new MagicWordFilterList<VidaElement>(
                x - MagicWordFilterButton.BTN_SIZE,
                y + componentHeight - MagicWordFilterButton.BTN_SIZE * MagicWordFilterList.DEFAULT_BUTTON_AMOUNT,
                MagicWordFilterButton.BTN_SIZE,
                MagicWordFilterList.DEFAULT_BUTTON_AMOUNT,
                new ResourceLocation(MOD_ID, "filter_container")
        )
                .addOption(VidaElement.GOLD, this::onFilterButtonClick)
                .addOption(VidaElement.WOOD, this::onFilterButtonClick)
                .addOption(VidaElement.AQUA, this::onFilterButtonClick)
                .addOption(VidaElement.FIRE, this::onFilterButtonClick)
                .addOption(VidaElement.EARTH, this::onFilterButtonClick);
        this.selectedWords = new MagicSelectedWordContainer(this, this.leftPos + 46, this.topPos - 140);
        this.magicWordCraftingButton = new MagicWordCraftingButton(this.leftPos + 64, this.topPos - 50);
        this.magicSlots = new ArrayList<>();
        this.addMagicSlots();
        this.addNodes(
                magicWordWidget,
                magicWordFilterLists,
                selectedWords,
                magicWordCraftingButton
        );
        VidaContainerScreen.composeStyles(this, new ResourceLocation(MOD_ID, "vida_magic_word_table"));

        // 添加监听器
        this.viewModel.selectedFilterElement.observe(this, newValue -> this.magicWordFilterLists.setSelected(newValue));
    }

    private void addMagicSlots() {
        List<Slot> slots = this.menu.getAllElementSlot();
        for (Slot slot : slots) {
            if (slot == null) {
                continue;
            }
            this.magicSlots.add(new MagicSlotComponent(slot, this.leftPos + slot.x, this.topPos + slot.y));
        }
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        // 渲染黑色背景
        this.renderBackground(graphics);
        // 渲染物品栏
        this.renderInventory(graphics, mouseX, mouseY, partialTicks);
        // 渲染悬浮提示
        this.renderTooltip(graphics, mouseX, mouseY);
        // 渲染组件
        this.renderMagicWords(graphics, mouseX, mouseY, partialTicks);
        // 渲染槽位
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void containerTick() {
        this.compareServerAndClientSideAndUpdate();
    }

    public void renderInventory(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int startX = this.getGuiLeft();
        int startY = this.getGuiTop();


        TextureSection section = new TextureSection(INVENTORY_LOCATION_RESOURCE, 0, 0, 176, 90, 176, 90);

        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate(0, -8f, 0);
        graphics.blit(
                section.location(),
                startX, startY, 0,
                section.minU(), section.minV(),
                section.w(), section.h(),
                section.w(), section.h()
        );
        poseStack.popPose();
    }

    /**
     * 对比服务端数据和客户端数据，如果不一样，立即更换
     */
    public void compareServerAndClientSideAndUpdate() {
        //更新按钮
        boolean isCrafting = false;
        //更新词条
        Map<VidaElement, String> clientMap = this.viewModel.selectedMagicWord.getValue();
        Map<VidaElement, String> serverMap = new LinkedHashMap<>();
        BlockPos pos = getMenu().getBlockPos();
        if (level.getBlockEntity(pos) instanceof MagicWordCraftingTableBlockEntity entity) {
            isCrafting = entity.isCrafting;
            serverMap = entity.getMagicWordMap();
        }
        boolean flag = false;
        // client -> server
        for (VidaElement element : clientMap.keySet()) {
            String serverWord = serverMap.get(element);
            String clientWord = clientMap.get(element);
            if (!compareString(serverWord, clientWord)) {
                flag = true;
                break;
            }
        }

        // server -> client
        for (VidaElement element : serverMap.keySet()) {
            String serverWord = serverMap.get(element);
            String clientWord = clientMap.get(element);
            if (!compareString(serverWord, clientWord)) {
                flag = true;
                break;
            }
        }

        //
        this.viewModel.isCrafting.setValue(isCrafting);
        if (flag) {
            // 更新但是不发送数据包
            HashMap<VidaElement, String> newMagicWordMap = new HashMap<>(serverMap);
            this.viewModel.selectedMagicWord.setValue(newMagicWordMap);
        }
    }

    public void renderMagicWords(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.magicWordWidget.render(graphics, mouseX, mouseY, partialTicks);
        this.magicWordFilterLists.render(graphics, mouseX, mouseY, partialTicks);
        this.selectedWords.render(graphics, mouseX, mouseY, partialTicks);
        this.magicSlots.forEach(slot -> slot.render(graphics, mouseX, mouseY, partialTicks));
        this.magicWordCraftingButton.render(graphics, mouseX, mouseY, partialTicks);
        this.renderBlock(graphics, mouseX, mouseY, partialTicks);
    }


    public void renderBlock(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack poseStack = graphics.pose();
        final BlockRenderDispatcher blockRenderer = this.minecraft.getBlockRenderer();

        // 入栈1：设置结构偏移
        poseStack.pushPose();
        poseStack.translate(selectedWords.getX() + 48, selectedWords.getY() + 46, 100.0F);
        poseStack.scale(12.0F, 12.0F, 12.0F);
        // 中心方块是-1,-1偏移后的方块
        poseStack.translate(-1.0F, -1.0F, 0.0F);


        // 将模型调整到旋转正中央
        poseStack.translate(0.5, 0.5, 0.5);
        // 因为renderBatched整个结构是倒着渲染的，先要将整个结构按X轴正转180度，然后再按20度，让方块的顶部面向玩家
        poseStack.mulPose(Axis.XP.rotationDegrees(160));
        // 再按Y轴转45度，让方块左边和右边面向玩家，呈现三视图的状态
        poseStack.mulPose(Axis.YP.rotationDegrees(45));
        // 将模型调整到旋转前正中央
        poseStack.translate(-0.5, -0.5, -0.5);


        // 入栈1-1：方块渲染
        RenderSystem.enableBlend();
        // 取消面剪切
        RenderSystem.disableCull();
        // 方块Shader颜色
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

        //
        BlockState state = VidaBlockLoader.MAGIC_WORD_CRAFTING.get().defaultBlockState();

        // 通知此batch加入相应的render
        blockRenderer.renderSingleBlock(state, poseStack, graphics.bufferSource(), 0, 15728880, ModelData.EMPTY, RenderType.translucent());

        // 出栈1-1
        RenderSystem.disableBlend();
        // 出栈1
        poseStack.popPose();
    }

    public void onFilterButtonClick(VidaElement id) {
        this.viewModel.selectedFilterElement.setValue(id);
    }

    @Override
    public List<? extends GuiEventListener> children() {
        List<GuiEventListener> listeners = new ArrayList<>();
        listeners.addAll(this.magicWordFilterLists.children());
        listeners.addAll(this.magicWordWidget.children());
        listeners.add(this.magicWordWidget);
        listeners.addAll(this.selectedWords.children());
        listeners.add(this.magicWordCraftingButton);
        return listeners;
    }

    @Override
    protected void clearWidgets() {
        this.viewModel.selectedMagicWord.clearObservers(this);
        this.viewModel.selectedFilterElement.clearObservers(this);
        super.clearWidgets();
    }


    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double dragX, double dragY) {
        boolean isItemStackDragged = super.mouseDragged(mouseX, mouseY, mouseButton, dragX, dragY);
        return this.getFocused() != null && this.isDragging() && mouseButton == 0 ? this.getFocused().mouseDragged(mouseX, mouseY, mouseButton, dragX, dragY) : false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int key) {
        if (this.getFocused() != null) {
            this.getFocused().mouseReleased(mouseX, mouseY, key);
        }
        return super.mouseReleased(mouseX, mouseY, key);
    }
}
