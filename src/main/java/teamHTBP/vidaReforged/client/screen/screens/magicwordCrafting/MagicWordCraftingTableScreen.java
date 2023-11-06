package teamHTBP.vidaReforged.client.screen.screens.magicwordCrafting;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;
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
    /**显示所有持有的词条容器*/
    MagicWordListWidget magicWordWidget;
    /**过滤器*/
    MagicWordFilterList magicWordFilterLists;
    /**显示选择的词条*/
    MagicSelectedWordListWidget magicSelectedWordListWidget;
    /**合成按钮*/
    MagicWordCraftingButton magicWordCraftingButton;
    /**放入物品的槽位*/
    List<MagicSlotComponent> magicSlots;
    final Inventory inventory;
    private final static ResourceLocation INVENTORY_LOCATION = new ResourceLocation(MOD_ID, "textures/gui/magic_word_inventory.png");
    private VidaMagicWordViewModel viewModel;


    public MagicWordCraftingTableScreen(MagicWordCraftingTableMenu menu, Inventory inventory, Component p_97743_) {
        super(menu, inventory, Component.literal("magic_word_crafting_table"));
        this.inventory = inventory;
    }

    @Override
    public void added() {
        // init view model
        this.viewModel = new ViewModelProvider(this).get(VidaMagicWordViewModel.class);
    }

    @Override
    protected void init() {
        super.init();

        viewModel.playerMagicWords.setValue(menu.getPlayerMagicWords());
        viewModel.blockPos.setValue(menu.getBlockPos());

        // 200 is right panel, 20 is button list
        this.leftPos = (Minecraft.getInstance().getWindow().getGuiScaledWidth() - 200 - 20 - 196) / 2;
        this.topPos = (Minecraft.getInstance().getWindow().getGuiScaledHeight() - 84);


        double factor = minecraft.getWindow().getGuiScale();
        int screenWidth = this.width;
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        int componentHeight = (int) (screenHeight - 28);
        int x = screenWidth - 200;
        int y = (int)((screenHeight - componentHeight) / 2);

        magicWordWidget = new MagicWordListWidget(x, y, MagicWordListWidget.WIDTH, componentHeight, factor);
        magicWordFilterLists = new MagicWordFilterList(x - MagicWordFilter.PIXEL, y + componentHeight - MagicWordFilter.PIXEL * MagicWordFilterList.BUTTON_AMOUNT);
        magicSelectedWordListWidget = new MagicSelectedWordListWidget(this.leftPos + 46,this.topPos - 140);
        magicWordCraftingButton = new MagicWordCraftingButton( this.leftPos + 64, this.topPos - 50);
        magicSlots = new ArrayList<>();


        this.addMagicSlots();
    }

    private void addMagicSlots(){

        List<Slot> slots = this.menu.getAllElementSlot();
        for(Slot slot : slots){
            if(slot == null) {
                continue;
            }

            this.magicSlots.add(new MagicSlotComponent(slot, this.leftPos + slot.x, this.topPos + slot.y));
        }
        //Slot slot = this.menu.getResultSlot();
        //this.magicSlots.add(new MagicSlotComponent(slot,this.leftPos + slot.x, this.topPos + slot.y));
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {

    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        if(magicWordCraftingButton != null && magicWordCraftingButton.isHovered()){
            graphics.renderTooltip(this.font, Component.translatable("tootip.vida_reforged.word_crafting_notice"),  mouseX,  mouseY);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        this.renderInventory(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics,mouseX,mouseY);
        this.compareServerAndClientSideAndUpdate();
        renderMagicWords(graphics, mouseX, mouseY, partialTicks);
        this.renderLabels(graphics, mouseX, mouseY);
    }

    public void renderInventory(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        int startX = this.getGuiLeft();
        int startY = this.getGuiTop();


        TextureSection section = new TextureSection(INVENTORY_LOCATION,0,0,176,90);

        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate(0, -8f,0);
        graphics.blit(
                section.location(),
                startX, startY, 0,
                section.minU(), section.minV(),
                section.w(), section.h(),
                section.w(), section.h()
        );
        poseStack.popPose();
    }

    /**对比服务端数据和客户端数据，如果不一样，立即更换*/
    public void compareServerAndClientSideAndUpdate(){
        //更新按钮
        boolean isCrafting = false;
        //更新词条
        Map<VidaElement,String> clientMap = this.viewModel.selectedMagicWord.getValue();
        Map<VidaElement,String> serverMap = new LinkedHashMap<>();
        BlockPos pos = getMenu().getBlockPos();
        if(inventory.player.getCommandSenderWorld().getBlockEntity(pos) instanceof MagicWordCraftingTableBlockEntity entity){
            isCrafting = entity.isCrafting;
            serverMap = entity.getMagicWordMap();
        }
        boolean flag = false;
        // client -> server
        for(VidaElement element : clientMap.keySet()){
            String serverWord = serverMap.get(element);
            String clientWord = clientMap.get(element);
            if(!compareString(serverWord,clientWord)){
                flag = true;
                break;
            }
        }

        // server -> client
        for(VidaElement element : serverMap.keySet()){
            String serverWord = serverMap.get(element);
            String clientWord = clientMap.get(element);
            if(!compareString(serverWord,clientWord)){
                flag = true;
                break;
            }
        }

        //
        this.viewModel.isCrafting.setValue(isCrafting);
        if(flag){
            // 更新但是不发送数据包
            HashMap<VidaElement,String> newMagicWordMap = new HashMap<>(serverMap);
            this.viewModel.selectedMagicWord.setValue(newMagicWordMap);
        }
    }

    public void renderMagicWords(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        if(magicWordWidget != null){
            RenderSystem.enableBlend();
            magicWordWidget.render(graphics, mouseX, mouseY, partialTicks);
            magicWordFilterLists.render(graphics, mouseX, mouseY, partialTicks);
            magicSelectedWordListWidget.render(graphics, mouseX, mouseY, partialTicks);
            magicSlots.forEach(slot -> slot.render(graphics, mouseX, mouseY, partialTicks));
            graphics.setColor(1, 1, 1,1);
            magicWordCraftingButton.render(graphics, mouseX, mouseY, partialTicks);
            graphics.setColor(1, 1, 1,1);
            renderBlock(graphics, mouseX, mouseY, partialTicks);
            graphics.setColor(1, 1, 1,1);
            RenderSystem.disableBlend();
        }
    }


    public void renderBlock(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        PoseStack pPoseStack = graphics.pose();
        final BlockRenderDispatcher blockRenderer = this.minecraft.getBlockRenderer();

        // 入栈1：设置结构偏移
        pPoseStack.pushPose();
        pPoseStack.translate(magicSelectedWordListWidget.getX() + 48, magicSelectedWordListWidget.getY() + 46, 100.0F);
        pPoseStack.scale(12.0F, 12.0F, 12.0F);
        // 中心方块是-1,-1偏移后的方块
        pPoseStack.translate(-1.0F, -1.0F, 0.0F);


        // 将模型调整到旋转正中央
        pPoseStack.translate(0.5 ,0.5,0.5);
        // 因为renderBatched整个结构是倒着渲染的，先要将整个结构按X轴正转180度，然后再按20度，让方块的顶部面向玩家
        pPoseStack.mulPose(Axis.XP.rotationDegrees(160));
        // 再按Y轴转45度，让方块左边和右边面向玩家，呈现三视图的状态
        pPoseStack.mulPose(Axis.YP.rotationDegrees(45));
        // 将模型调整到旋转前正中央
        pPoseStack.translate(-0.5 ,-0.5,-0.5);



        // 入栈1-1：方块渲染
        RenderSystem.enableBlend();
        // 取消面剪切
        RenderSystem.disableCull();
        // 方块Shader颜色
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

        //
        BlockState state = VidaBlockLoader.MAGIC_WORD_CRAFTING.get().defaultBlockState();

        // 通知此batch加入相应的render
        blockRenderer.renderSingleBlock(state, pPoseStack, graphics.bufferSource(),0, 15728880, ModelData.EMPTY, RenderType.translucent());

        // 出栈1-1
        RenderSystem.disableBlend();
        // 出栈1
        pPoseStack.popPose();

    }

    @Override
    public List<? extends GuiEventListener> children() {
        List<GuiEventListener> listeners = new ArrayList<>();
        listeners.addAll(this.magicWordFilterLists.getChildren());
        listeners.addAll(this.magicWordWidget.getChildren());
        listeners.add(this.magicWordWidget);
        listeners.addAll(this.magicSelectedWordListWidget.getChildren());
        listeners.add(this.magicWordCraftingButton);
        return listeners;
    }

    @Override
    protected void clearWidgets() {
        this.viewModel.selectedMagicWord.clearObservers();
        this.viewModel.selectedFilterElement.clearObservers();
        super.clearWidgets();
    }


    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double dragX, double dragY) {
        boolean isItemStackDragged = super.mouseDragged(mouseX, mouseY, mouseButton, dragX, dragY);
        return this.getFocused() != null && this.isDragging() && mouseButton == 0 ? this.getFocused().mouseDragged(mouseX, mouseY, mouseButton, dragX, dragY) : false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int key) {
        if(this.getFocused() != null){
            this.getFocused().mouseReleased(mouseX, mouseY, key);
        }
        return super.mouseReleased(mouseX, mouseY, key);
    }
}
