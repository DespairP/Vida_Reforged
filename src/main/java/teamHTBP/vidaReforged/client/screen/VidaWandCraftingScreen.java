package teamHTBP.vidaReforged.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import teamHTBP.vidaReforged.client.events.LayerRegistryHandler;
import teamHTBP.vidaReforged.client.model.itemModel.VidaWandModel;
import teamHTBP.vidaReforged.core.common.item.Position;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;
import teamHTBP.vidaReforged.server.items.VidaWandEquipment;
import teamHTBP.vidaReforged.server.menu.VidaWandCraftingTableMenu;
import teamHTBP.vidaReforged.server.menu.slots.VidaWandEquipmentSlot;

import java.lang.reflect.Field;
import java.util.*;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class VidaWandCraftingScreen extends AbstractContainerScreen<VidaWandCraftingTableMenu> {
    private static final ResourceLocation CRAFTING_SCREEN = new ResourceLocation(MOD_ID, "textures/gui/vida_wand_equipment.png");
    private static final ResourceLocation VIDA_WAND_MODEL = new ResourceLocation(MOD_ID, "textures/armor/vida_wand_model.png");
    private final TextureSection INVENTORY = new TextureSection(CRAFTING_SCREEN, 0, 150, 176, 90);
    private final TextureSection SLOT = new TextureSection(CRAFTING_SCREEN, 76, 12, 24, 24);
    /**旋转角度*/
    private int rotateY = 0;
    /**法杖渲染大小*/
    private final int WAND_SIZE = 64;
    /**法杖开始渲染位置*/
    private final int WAND_START_X = 30;
    private final int WAND_START_Y = 20;
    /**法杖高度与宽度*/
    private final int WAND_HEIGHT = 108;
    private final int WAND_WIDTH = 50;
    /**SlotX和Y字段*/
    private final Field slotFieldY;
    private final Field slotFieldX;
    /**装备栏*/
    private Map<Position,VidaWandEquipmentSlot> equipmentSlots = new HashMap<>();

    public VidaWandCraftingScreen(VidaWandCraftingTableMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.slotFieldY = ObfuscationReflectionHelper.findField(Slot.class,"f_40221_");;
        this.slotFieldY.setAccessible(true);
        this.slotFieldX = ObfuscationReflectionHelper.findField(Slot.class,"f_40220_");
        this.slotFieldX.setAccessible(true);
    }

    @Override
    protected void init() {
        super.init();

        //背包槽开始渲染位置
        this.leftPos = (this.width - INVENTORY.w()) / 2;
        this.topPos = (int) ((this.height - INVENTORY.h()));

        //槽位
        this.equipmentSlots.clear();
        int offsetY = 12;
        //改变slot的实际位置
        try {
            List<Position> positions = Arrays.stream(Position.values()).toList();
            Map<Position, VidaWandEquipmentSlot> slots = this.menu.getEquipmentSlots();
            for(Position position : slots.keySet()){
                VidaWandEquipmentSlot slot = slots.get(position);
                this.slotFieldY.set(slot, -topPos + offsetY + positions.indexOf(position) * 36);
                this.slotFieldX.set(slot, -leftPos + WAND_START_X + WAND_WIDTH + 30);
                this.equipmentSlots.put(position, slot);
            }

        } catch (IllegalAccessException  e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int x, int y) {

    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {

    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        renderInventory(graphics);
        renderModel(graphics, mouseX, mouseY);

        super.render(graphics, mouseX, mouseY, partialTicks);
        RenderSystem.setShaderColor(1, 1, 1,1);
        renderTooltip(graphics, mouseX, mouseY);
    }


    /**渲染槽位*/
    public void renderInventory(GuiGraphics graphics){
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

        for(VidaWandEquipmentSlot slot : equipmentSlots.values()){
            RenderSystem.enableBlend();

            if(hoveredStack.getItem() instanceof VidaWandEquipment equipment && equipment.getAttribute().getPosition() == slot.getPosition()){
                RenderSystem.setShaderColor(1, 1, 1, 1);
            }else if(!slot.getItem().isEmpty()){
                RenderSystem.setShaderColor(0.99f, 0.99f, 0.8f, 1);
            }else{
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

    }

    /**渲染模型*/
    public void renderModel(GuiGraphics graphics, int mouseX, int mouseY){
        RenderSystem.enableBlend();
        RenderSystem.disableCull();

        final PoseStack pPoseStack = graphics.pose();
        RenderSystem.setShaderTexture(0, VIDA_WAND_MODEL);

        pPoseStack.pushPose();
        pPoseStack.translate(60f,80f,30f);
        pPoseStack.scale(WAND_SIZE, WAND_SIZE, WAND_SIZE);

        // 按X轴旋转
        pPoseStack.mulPose(Axis.XP.rotationDegrees(20));
        // 再按Y轴转45度，让方块左边和右边面向玩家，呈现三视图的状态
        pPoseStack.mulPose(Axis.YP.rotationDegrees(95 + rotateY));

        // 渲染模型
        VidaWandModel model = LayerRegistryHandler.getModelSupplier(VidaWandModel.LAYER_LOCATION, VidaWandModel.class).get();
        this.equipmentSlots.forEach(((position, slot) -> {
            int packedLight = 3 << 4 | 7 << 20;

            if(this.isHovering(slot, mouseX, mouseY) && slot.isActive()){
                packedLight = 15 << 4 | 15 << 20;
            }
            ItemStack slotItem = slot.getItem();
            if(!slotItem.isEmpty() && slotItem.getItem() instanceof VidaWandEquipment equipment){
                ResourceLocation texture = equipment.getAttribute().getModelTexture();;
                VidaWandModel equipmentModel = LayerRegistryHandler.getModelSupplier(equipment.getAttribute().getModelLayerLocation(), VidaWandModel.class).get();
                equipmentModel.renderPartToBuffer(position, pPoseStack, graphics.bufferSource().getBuffer(RenderType.entityShadow(texture)), packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
                return;
            }

            model.renderPartToBuffer(position, pPoseStack, graphics.bufferSource().getBuffer(RenderType.entityTranslucent(VIDA_WAND_MODEL)), packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        }));

        RenderSystem.setShaderColor(1, 1, 1, 1);
        pPoseStack.popPose();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();

    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        rotateY = (int) ((rotateY + dragX) % 361);
        return true;
    }

    private boolean isHovering(Slot p_97775_, double p_97776_, double p_97777_) {
        return this.isHovering(p_97775_.x, p_97775_.y, 16, 16, p_97776_, p_97777_);
    }
}
