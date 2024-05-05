package teamHTBP.vidaReforged.client.screen.screens.wandCrafting;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.client.events.ClientTickHandler;
import teamHTBP.vidaReforged.client.events.ShadersHandler;
import teamHTBP.vidaReforged.client.screen.components.common.IconButton;
import teamHTBP.vidaReforged.client.screen.screens.common.VidaContainerScreen;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaScreenEventChannelViewModel;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaWandCraftingViewModel;
import teamHTBP.vidaReforged.core.common.item.Position;
import teamHTBP.vidaReforged.core.common.system.guidebook.VidaScreenEvent;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;
import teamHTBP.vidaReforged.core.utils.anim.SecondOrderDynamics;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;
import teamHTBP.vidaReforged.server.items.VidaWandEquipment;
import teamHTBP.vidaReforged.server.menu.VidaWandCraftingTableMenu;
import teamHTBP.vidaReforged.server.menu.slots.VidaWandEquipmentSlot;
import teamHTBP.vidaReforged.server.packets.MagicDatapackPacket;
import teamHTBP.vidaReforged.server.packets.MagicSelectionPacket;
import teamHTBP.vidaReforged.server.packets.VidaPacketManager;

import java.lang.reflect.Field;
import java.util.*;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;
import static teamHTBP.vidaReforged.helper.VidaGuiHelper.Style.vw;

public class VidaWandCraftingScreen extends VidaContainerScreen<VidaWandCraftingTableMenu> {
    private static final ResourceLocation CRAFTING_SCREEN_RESOURCE = new ResourceLocation(MOD_ID, "textures/gui/vida_wand_equipment.png");
    private final TextureSection SLOT = new TextureSection(CRAFTING_SCREEN_RESOURCE, 76, 12, 24, 24, 384, 384);
    List<VidaWandEquipmentSlot> equipmentSlots = new ArrayList<>();
    private final TextureSection WAND_SLOT = new TextureSection(CRAFTING_SCREEN_RESOURCE, 140, 12, 24, 24, 384, 384);
    /**反射SlotX和Y字段*/
    private final Field slotFieldY;
    private final Field slotFieldX;
    /**法杖显示区域*/
    VidaWandScreenModel modelViewWandSection;
    /**菜单区域*/
    VidaWandSelectSection selectMenuSection;
    /**饰品装备区域*/
    VidaWandScreenEquippingSection equippingSection;
    /**/
    VidaWandMagicSection magicSection;
    /***/
    IconButton iconButton;
    /**/
    private SecondOrderDynamics inventoryOffset = new SecondOrderDynamics(1, 1, 0, new Vector3f(0,0,0));

    private final TextureSection INVENTORY = new TextureSection(CRAFTING_SCREEN_RESOURCE, 0, 150, 176, 90, 384, 384);
    /**布局*/
    GridLayout gridLayout;
    /**ViewModel*/
    VidaWandCraftingViewModel viewModel;
    VidaScreenEventChannelViewModel channel;
    /**法杖位置*/
    private int wandModelX = 100;
    private int wandModelY = 80;

    public VidaWandCraftingScreen(VidaWandCraftingTableMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        // 特殊槽位需要被调整
        this.slotFieldY = ObfuscationReflectionHelper.findField(Slot.class,"f_40221_");;
        this.slotFieldY.setAccessible(true);
        this.slotFieldX = ObfuscationReflectionHelper.findField(Slot.class,"f_40220_");
        this.slotFieldX.setAccessible(true);
        this.isAllowThrow = false;
    }

    /**当第一次初始化时，先加载数据*/
    @Override
    public void added() {
        super.added();

        viewModel = new ViewModelProvider(this).get(VidaWandCraftingViewModel.class);
        channel = new ViewModelProvider(this).get(VidaScreenEventChannelViewModel.class);

        viewModel.setSlots(this.menu.getEquipmentSlots());
        viewModel.setMagics(this.menu.getMagics());

        viewModel.slots.observe(this, slots -> {equipmentSlots.clear(); equipmentSlots.addAll(slots.values());});
        channel.listenMessage(this, this::handleChannelEvent);
    }

    /**初始化消息队列处理*/
    public void handleChannelEvent(VidaScreenEvent event){
        if(event.getType().equals("open")){
            String place = event.getData().getAsJsonObject().get("place").getAsString();
            this.selectMenuSection.setVisible(false);
            this.modelViewWandSection.resize(16 * 4, 16 * 8);
            this.iconButton.setVisible(true);

            // 打开配饰栏
            if(place.equals("equipment")){
                this.inventoryOffset  = new SecondOrderDynamics(1, 1, 0, new Vector3f(0,16,0));
                this.equippingSection.setVisible(true);
                this.initSlots();
            }

            // 打开魔法栏
            if(place.equals("magic")){
                this.magicSection.setVisible(true);
            }

            //
            this.adjustComponentsPosition();
            return;
        }

        if(event.getType().equals("close")){
            this.modelViewWandSection.resize(16 * 6, 16 * 10);
            this.selectMenuSection.setVisible(true);
            this.equippingSection.setVisible(false);
            this.magicSection.setVisible(false);
            this.iconButton.setVisible(false);
            this.adjustComponentsPosition();
        }
    }

    /**界面构筑*/
    @Override
    protected void init() {
        super.init();

        try {
            // 计算背包槽开始渲染位置
            this.leftPos = (this.width - INVENTORY.w()) / 2;
            this.topPos = (this.height - INVENTORY.h());

            // 初始化模型
            this.initWandModel();
            // 初始化
            this.initSelectMenu();
            //
            this.initMagicSection();
            // 初始化
            this.initEquipmentSection();

            // 调整布局
            this.adjustComponentsPosition();
            // 初始化槽位
            this.initSlots();

        } catch (Exception ex){
            LOGGER.error(ex);
        }
    }

    /**构筑法杖模型*/
    protected void initWandModel(){
        if(this.modelViewWandSection != null){
            this.addRenderableOnly(this.modelViewWandSection);
            this.addRenderableOnly(this.iconButton);
            return;
        }
        this.modelViewWandSection = new VidaWandScreenModel(wandModelX, wandModelY, 16 * 6, 16 * 10, 6.0f , Component.empty());
        this.addRenderableOnly(modelViewWandSection);

        this.iconButton = new IconButton.Builder().width(64).isBackground(true).height(32).message(Component.literal("< 返回")).listener(() -> channel.pushMessage(new VidaScreenEvent("close", null))).build(0, 0);
        this.iconButton.setVisible(false);
        this.addComponentAndChild(this.iconButton);
    }

    /**构筑选择栏*/
    protected void initSelectMenu(){
        if(this.selectMenuSection != null) {
            this.addRenderableOnly(selectMenuSection);
            this.selectMenuSection.setWidth(width - modelViewWandSection.getWidth());
            this.selectMenuSection.setHeight(height);
            this.selectMenuSection.init();
            return;
        }
        this.selectMenuSection = new VidaWandSelectSection(0, 0, width - modelViewWandSection.getWidth(), this.height, Component.empty());
        this.selectMenuSection.init();
        this.addRenderableOnly(selectMenuSection);
    }

    /**构筑装配槽位*/
    protected void initSlots(){
        // 槽位
        Map<Position, VidaWandEquipmentSlot> equipments = this.menu.getEquipmentSlots();
        final int slotX = modelViewWandSection.getX() + modelViewWandSection.getWidth() + 10;
        // 防止初始化Y为0
        final int startY = this.equippingSection.getY() == 0 ? 12 : this.equippingSection.getY();
        final int spacingY = this.modelViewWandSection.getHeight() / 4 + 6 ;

        //改变slot的实际位置
        try {
            Map<Position, VidaWandEquipmentSlot> slots = this.menu.getEquipmentSlots();
            for(Position position : slots.keySet()){
                VidaWandEquipmentSlot slot = slots.get(position);
                this.slotFieldY.set(slot, -topPos + startY + spacingY * position.ordinal());
                this.slotFieldX.set(slot, -leftPos + slotX);
                equipments.put(position, slot);
            }
            viewModel.setSlots(equipments);
        } catch (IllegalAccessException e) {
            LOGGER.error(e);
        }
    }

    /**初始化饰品选择区域*/
    protected void initEquipmentSection(){
        final int equipmentSectionHeight = this.height - INVENTORY.h() - 32;
        final int equipmentSectionWidth = vw(80);

        if(this.equippingSection != null){
            this.equippingSection.setWidth(equipmentSectionWidth);
            this.equippingSection.setHeight(this.topPos - 32);
            this.addRenderableOnly(this.equippingSection);
            this.equippingSection.init();
            return;
        }

        this.equippingSection = new VidaWandScreenEquippingSection(0, 0, equipmentSectionWidth, equipmentSectionHeight, Component.empty());
        this.equippingSection.setVisible(false);
        this.equippingSection.init();

        this.addRenderableOnly(this.equippingSection);
    }

    /**构筑魔法选择区域*/
    protected void initMagicSection(){
        final int magicSectionHeight = this.height - 32;
        final int magicSectionWidth = vw(80);

        if(this.magicSection != null){
            this.magicSection.setWidth(magicSectionWidth);
            this.magicSection.setHeight(this.height - 32);
            this.addRenderableOnly(this.magicSection);
            this.magicSection.init();
            return;
        }

        this.magicSection = new VidaWandMagicSection(0, 0, magicSectionWidth, magicSectionHeight, Component.empty());
        this.magicSection.init();
        this.magicSection.setVisible(false);

        this.addRenderableOnly(this.magicSection);
    }

    /**渲染标题*/
    @Override
    protected void renderLabels(GuiGraphics graphics, int x, int y) {}

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {}

    /**调整布局*/
    public void adjustComponentsPosition(){
        int index = 2;

        this.gridLayout = new GridLayout(10, 10);
        gridLayout.spacing(0);

        gridLayout.addChild(modelViewWandSection, 0, 1);
        if(selectMenuSection.visible) gridLayout.addChild(selectMenuSection, 0, index++);
        if(magicSection.visible) gridLayout.addChild(magicSection, 0, index++);
        if(equippingSection.visible) gridLayout.addChild(equippingSection, 0, index++, this.gridLayout.defaultCellSetting().paddingLeft(64));


        gridLayout.arrangeElements();

        this.iconButton.setX(modelViewWandSection.getX() - 6);
        this.iconButton.setY(modelViewWandSection.getY() + modelViewWandSection.getHeight() + 39);
    }
    
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        try {
            //
            VidaGuiHelper.blitWithShader(graphics, ShadersHandler.stars, 0, 0, 0, width, height, (int) ClientTickHandler.ticks, partialTicks);

            // 渲染背景
            renderBackground(graphics);

            // 调整布局
            adjustComponentsPosition();

            // 渲染组件
            renderables.forEach(widget -> widget.render(graphics, mouseX, mouseY, partialTicks));
            // 渲染物品栏
            if (this.equippingSection.visible) {
                Vector3f offset = this.inventoryOffset.update(this.minecraft.getDeltaFrameTime()  * 0.4f, new Vector3f(0), null);

                graphics.pose().pushPose();
                graphics.pose().translate(0, offset.y, 0);
                renderSlots(graphics);
                // 渲染玩家背包
                super.renderSlots(graphics, mouseX, mouseY, partialTicks);
                super.renderDraggingItem(graphics, mouseX, mouseY, partialTicks);
                renderTooltip(graphics, mouseX, mouseY);
                graphics.pose().popPose();
            }
        }catch (Exception exception){
            LOGGER.error(exception);
        }
    }


    /**渲染槽位*/
    public void renderSlots(GuiGraphics graphics){
        // 玩家背包栏
        graphics.blit(
                INVENTORY.location(),
                this.leftPos, this.topPos - 8, 0,
                INVENTORY.minU(), INVENTORY.minV(),
                INVENTORY.w(), INVENTORY.h(),
                384, 384
        );


        // 高亮
        ItemStack hoveredStack = hoveredSlot == null ? ItemStack.EMPTY : hoveredSlot.getItem();
        hoveredStack = this.menu.getCarried().isEmpty() ? hoveredStack : this.menu.getCarried().copy();

        for(VidaWandEquipmentSlot slot : equipmentSlots){
            RenderSystem.enableBlend();

            if(hoveredStack.getItem() instanceof VidaWandEquipment equipment && equipment.getAttribute().getPosition() == slot.getPosition()){
                // 提示现在悬浮的物品应该放在那一栏
                RenderSystem.setShaderColor(1, 1, 1, 1);
            }else if(!slot.getItem().isEmpty()){
                // 如果槽位被放入
                RenderSystem.setShaderColor(0.99f, 0.99f, 0.8f, 1);
            }else{
                // 如果槽位为空
                RenderSystem.setShaderColor(0.5f, 0.5f, 0.5f, 1);
            }

            graphics.blit(
                    SLOT.location(),
                    this.leftPos + slot.x - 4, this.topPos + slot.y - 4, 0,
                    SLOT.minU(), SLOT.minV(),
                    SLOT.w(), SLOT.h(),
                    384, 384
            );

            RenderSystem.setShaderColor(1, 1, 1,1);
            RenderSystem.disableBlend();
        }

        // 法杖槽位
        graphics.blit(
                WAND_SLOT.location(),
                this.leftPos - 68 - 4, this.topPos - 4, 0,
                WAND_SLOT.minU(), WAND_SLOT.minV(),
                WAND_SLOT.w(), WAND_SLOT.h(),
                384, 384
        );
    }

    /**获取所有监听器*/
    @Override
    public List<? extends GuiEventListener> children() {
        List<GuiEventListener> listeners = new ArrayList<>();
        listeners.addAll(selectMenuSection.children());
        listeners.addAll(magicSection.children());
        listeners.addAll(modelViewWandSection.children());
        listeners.addAll(equippingSection.children());
        listeners.add(modelViewWandSection);
        if(iconButton.visible){
            listeners.add(iconButton);
        }
        return listeners;
    }

    /**饰品栏被打开时才能与背包栏目进行交互*/
    @Override
    public boolean mouseClickedSlot(double mouseX, double mouseY, int key) {
        if(!this.equippingSection.visible){
            return false;
        }
        return super.mouseClickedSlot(mouseX, mouseY, key);
    }

    /**饰品栏被打开时才能与背包栏目进行交互, 鼠标拖拽事件*/
    public boolean mouseDragged(double mouseX, double mouseY, int key, double dragX, double dragY) {
        if(!this.equippingSection.visible){
            if(children() != null && children().size() > 0){
                children().forEach(child -> child.mouseDragged(mouseX, mouseY, key, dragX, dragY));
            }
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, key, dragX, dragY);
    }

    @Override
    protected void close() {
        Map<Integer, ResourceLocation> magics = this.viewModel.getMagics();
        VidaPacketManager.sendToServer(new MagicSelectionPacket(magics));
    }
}
