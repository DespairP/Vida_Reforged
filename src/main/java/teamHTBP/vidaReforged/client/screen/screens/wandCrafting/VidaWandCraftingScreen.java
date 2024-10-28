package teamHTBP.vidaReforged.client.screen.screens.wandCrafting;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
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
import teamHTBP.vidaReforged.core.common.ui.VidaLifecycleSection;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;
import teamHTBP.vidaReforged.core.utils.anim.SecondOrderDynamics;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;
import teamHTBP.vidaReforged.server.items.VidaWandEquipment;
import teamHTBP.vidaReforged.server.menu.VidaWandCraftingTableMenu;
import teamHTBP.vidaReforged.server.menu.slots.VidaWandEquipmentSlot;
import teamHTBP.vidaReforged.server.packets.MagicSelectionPacket;
import teamHTBP.vidaReforged.server.packets.VidaPacketManager;

import java.lang.reflect.Field;
import java.util.*;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;
import static teamHTBP.vidaReforged.helper.VidaGuiHelper.Style.vw;

/**
 * 法杖操作台
 */
public class VidaWandCraftingScreen extends VidaContainerScreen<VidaWandCraftingTableMenu> {
    private static final ResourceLocation CRAFTING_SCREEN_RESOURCE = new ResourceLocation(MOD_ID, "textures/gui/vida_wand_equipment.png");
    private final TextureSection SLOT = new TextureSection(CRAFTING_SCREEN_RESOURCE, 76, 12, 24, 24, 384, 384);
    private final TextureSection WAND_SLOT = new TextureSection(CRAFTING_SCREEN_RESOURCE, 140, 12, 24, 24, 384, 384);
    private final TextureSection INVENTORY = new TextureSection(CRAFTING_SCREEN_RESOURCE, 0, 150, 176, 90, 384, 384);
    /**
     * 反射SlotX和Y字段
     */
    private final Field slotFieldY;
    private final Field slotFieldX;
    /**
     * 饰品slot，用于饰品装备
     */
    List<VidaWandEquipmentSlot> equipmentSlots = new ArrayList<>();
    /**
     * 区域显示/不显示控制器
     */
    VidaFragmentController sectionManager;
    /**
     * 法杖显示区域
     */
    VidaWandScreenModel modelSection;
    /**
     * 菜单区域
     */
    VidaWandSelectSection selectSection;
    /**
     * 饰品装备区域
     */
    VidaWandScreenEquippingSection equippingSection;
    /**
     * 法杖魔法区域
     */
    VidaWandMagicSection magicSection;
    /**
     * 返回按钮
     */
    IconButton backIconButton;
    /**
     * 物品栏动画，因为饰品装备区域会显示物品栏，物品栏一般在screen主类中处理，所以会有这个变量在主类里
     */
    private SecondOrderDynamics inventoryOffset = new SecondOrderDynamics(1, 1, 0, new Vector3f(0, 0, 0));
    /**
     * 布局
     */
    GridLayout gridLayout;
    /**
     * ViewModel
     */
    VidaWandCraftingViewModel viewModel;
    VidaScreenEventChannelViewModel channel;
    /**
     * 法杖位置
     */
    private final int wandModelX = 100;
    private final int wandModelY = 80;

    public VidaWandCraftingScreen(VidaWandCraftingTableMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        // 特殊槽位需要被调整位置
        this.slotFieldY = ObfuscationReflectionHelper.findField(Slot.class, "f_40221_");
        this.slotFieldY.setAccessible(true);
        this.slotFieldX = ObfuscationReflectionHelper.findField(Slot.class, "f_40220_");
        this.slotFieldX.setAccessible(true);
        this.isAllowThrow = false;
    }

    // TODO
    private void prepare() {
    }

    /**
     * 当第一次初始化时，先加载数据
     */
    @Override
    public void added() {
        super.added();

        viewModel = new ViewModelProvider(this).get(VidaWandCraftingViewModel.class);
        channel = new ViewModelProvider(this).get(VidaScreenEventChannelViewModel.class);

        viewModel.setSlots(this.menu.getEquipmentSlots());
        viewModel.setMagics(this.menu.getMagics());

        viewModel.slots.observe(this, slots -> {
            equipmentSlots.clear();
            equipmentSlots.addAll(slots.values());
        });
        channel.listenMessage(this, this::handleChannelEvent);
    }

    /**
     * 初始化消息队列处理
     */
    public void handleChannelEvent(VidaScreenEvent event) {
        // 打开
        if (event.getType().equals("open")) {
            String place = event.getData().getAsJsonObject().get("place").getAsString();
            this.selectSection.hide();
            this.modelSection.resize(16 * 4, 16 * 8);
            // 打开配饰栏
            if (place.equals("equipment")) {
                this.inventoryOffset = new SecondOrderDynamics(1, 1, 0, new Vector3f(0, 16, 0));
                this.sectionManager.push(equippingSection);
                this.initSlots();
            }
            // 打开魔法栏
            if (place.equals("magic")) {
                this.sectionManager.push(magicSection);
            }
            this.adjustComponentsPosition();
        }
        // 关闭
        if (event.getType().equals("close")) {
            this.modelSection.resize(16 * 6, 16 * 10);
            this.sectionManager.popSection();
            this.adjustComponentsPosition();
        }
        // 设置返回按钮
        if (sectionManager != null) {
            backIconButton.setVisible(sectionManager.getSections().size() > 1);
        }
    }

    /**
     * 界面构筑
     */
    @Override
    protected void init() {
        super.init();

        try {
            // 计算背包槽开始渲染位置
            this.leftPos = (this.width - INVENTORY.w()) / 2;
            this.topPos = (this.height - INVENTORY.h());

            // 初始化模型
            this.initWandModel();
            List<VidaLifecycleSection> sections = List.of(
                    this.initSelectMenu(),// 初始化选择栏目
                    this.initMagicSection(),// 初始化魔法选择栏目
                    this.initEquipmentSection()// 初始化饰品栏目
            );

            // 初始化槽位
            this.initSlots();

            if (this.sectionManager == null) {
                this.sectionManager = new VidaFragmentController(new LinkedList<>(sections));
            }
            sections.forEach(this::addRenderableOnly);
            // 调整布局
            this.adjustComponentsPosition();

        } catch (Exception ex) {
            LOGGER.error(ex);
        }
    }

    /**
     * 构筑法杖模型
     */
    protected void initWandModel() {
        if (this.modelSection != null) {
            this.addRenderableOnly(this.modelSection);
            this.addRenderableOnly(this.backIconButton);
            return;
        }
        this.modelSection = new VidaWandScreenModel(wandModelX, wandModelY, 16 * 6, 16 * 10, 6.0f, Component.empty());
        this.addRenderableOnly(modelSection);

        this.backIconButton = new IconButton.Builder().width(64).isBackground(true).height(32).message(Component.literal("< 返回")).listener(() -> channel.pushMessage(new VidaScreenEvent("close", null))).build(0, 0);
        this.backIconButton.setVisible(false);
        this.addComponentAndChild(this.backIconButton);
    }

    /**
     * 构筑选择栏
     */
    protected VidaWandSelectSection initSelectMenu() {
        if (this.selectSection != null) {
            this.selectSection.setWidth(width - modelSection.getWidth());
            this.selectSection.setHeight(height);
            return this.selectSection;
        }
        this.selectSection = new VidaWandSelectSection(0, 0, width - modelSection.getWidth(), this.height, Component.empty());
        return this.selectSection;
    }

    /**
     * 构筑装配槽位
     */
    protected void initSlots() {
        // 槽位
        Map<Position, VidaWandEquipmentSlot> equipments = this.menu.getEquipmentSlots();
        final int slotX = modelSection.getX() + modelSection.getWidth() + 10;
        // 防止初始化Y为0
        final int startY = this.equippingSection.getY() == 0 ? 12 : this.equippingSection.getY();
        final int spacingY = this.modelSection.getHeight() / 4 + 6;

        //改变slot的实际位置
        try {
            Map<Position, VidaWandEquipmentSlot> slots = this.menu.getEquipmentSlots();
            for (Position position : slots.keySet()) {
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

    /**
     * 初始化饰品选择区域
     */
    protected VidaWandScreenEquippingSection initEquipmentSection() {
        final int equipmentSectionHeight = this.height - INVENTORY.h() - 32;
        final int equipmentSectionWidth = vw(80);

        if (this.equippingSection != null) {
            this.equippingSection.setWidth(equipmentSectionWidth);
            this.equippingSection.setHeight(this.topPos - 32);
            return this.equippingSection;
        }
        this.equippingSection = new VidaWandScreenEquippingSection(0, 0, equipmentSectionWidth, equipmentSectionHeight, Component.empty());
        return this.equippingSection;
    }

    /**
     * 构筑魔法选择区域
     */
    protected VidaWandMagicSection initMagicSection() {
        final int magicSectionHeight = this.height - 32;
        final int magicSectionWidth = vw(80);

        if (this.magicSection != null) {
            this.magicSection.setWidth(magicSectionWidth);
            this.magicSection.setHeight(this.height - 32);
            return this.magicSection;
        }
        this.magicSection = new VidaWandMagicSection(0, 0, magicSectionWidth, magicSectionHeight, Component.empty());
        return magicSection;
    }

    /**
     * 渲染标题
     */
    @Override
    protected void renderLabels(GuiGraphics graphics, int x, int y) {
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
    }

    /**
     * 调整布局
     */
    public void adjustComponentsPosition() {
        int index = 2;

        this.gridLayout = new GridLayout(10, 10);
        gridLayout.spacing(0);

        gridLayout.addChild(modelSection, 0, 1);
        if (sectionManager != null) {
            gridLayout.addChild(sectionManager.getCurrent(), 0, index, gridLayout.defaultCellSetting().paddingLeft(
                    sectionManager.getCurrent() == equippingSection ? 64 : 0    // 特别逻辑
            ));
        }

        gridLayout.arrangeElements();

        this.backIconButton.setX(modelSection.getX() - 6);
        this.backIconButton.setY(modelSection.getY() + modelSection.getHeight() + 39);
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
            for(Renderable renderable : renderables){
                renderable.render(graphics, mouseX, mouseY, partialTicks);
            }
            // 渲染物品栏
            if (this.equippingSection.visible) {
                Vector3f offset = this.inventoryOffset.update(this.minecraft.getDeltaFrameTime() * 0.4f, new Vector3f(0), null);

                graphics.pose().pushPose();
                graphics.pose().translate(0, offset.y, 0);
                renderSlots(graphics);
                // 渲染玩家背包
                super.renderSlots(graphics, mouseX, mouseY, partialTicks);
                super.renderDraggingItem(graphics, mouseX, mouseY, partialTicks);
                renderTooltip(graphics, mouseX, mouseY);
                graphics.pose().popPose();
            }
        } catch (Exception exception) {
            LOGGER.error(exception);
        }
    }


    /**
     * 渲染槽位
     */
    public void renderSlots(GuiGraphics graphics) {
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

        for (VidaWandEquipmentSlot slot : equipmentSlots) {
            RenderSystem.enableBlend();

            if (hoveredStack.getItem() instanceof VidaWandEquipment equipment && equipment.getAttribute().getPosition() == slot.getPosition()) {
                // 提示现在悬浮的物品应该放在那一栏
                RenderSystem.setShaderColor(1, 1, 1, 1);
            } else if (!slot.getItem().isEmpty()) {
                // 如果槽位被放入
                RenderSystem.setShaderColor(0.99f, 0.99f, 0.8f, 1);
            } else {
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

            RenderSystem.setShaderColor(1, 1, 1, 1);
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

    /**
     * 获取所有监听器
     */
    @Override
    public List<? extends GuiEventListener> children() {
        List<GuiEventListener> listeners = new ArrayList<>();
        listeners.add(modelSection);
        if(sectionManager != null){
            listeners.addAll(sectionManager.getCurrent().children());
        }
        if (backIconButton.visible) {
            listeners.add(backIconButton);
        }
        return listeners;
    }

    /**
     * 饰品栏被打开时才能与背包栏目进行交互
     */
    @Override
    public boolean mouseClickedSlot(double mouseX, double mouseY, int key) {
        if (!this.equippingSection.visible) {
            return false;
        }
        return super.mouseClickedSlot(mouseX, mouseY, key);
    }

    /**
     * 饰品栏被打开时才能与背包栏目进行交互, 鼠标拖拽事件
     */
    public boolean mouseDragged(double mouseX, double mouseY, int key, double dragX, double dragY) {
        if (!this.equippingSection.visible) {
            if (children() != null && children().size() > 0) {
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
