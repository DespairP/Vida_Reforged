package teamHTBP.vidaReforged.client.screen.screens.common;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelStore;
import teamHTBP.vidaReforged.client.screen.components.common.VidaWidget;
import teamHTBP.vidaReforged.core.api.hud.IVidaNodes;
import teamHTBP.vidaReforged.core.common.ui.component.IViewModelStoreProvider;
import teamHTBP.vidaReforged.core.common.ui.lifecycle.ILifeCycleOwner;
import teamHTBP.vidaReforged.core.common.ui.lifecycle.LifeCycle;
import teamHTBP.vidaReforged.core.common.ui.lifecycle.LifeCycleRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class VidaContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> implements ILifeCycleOwner, IViewModelStoreProvider {
    /**触屏模式下点击的格子*/
    protected Slot clickedSlot;
    /**触屏模式下最后点击的格子*/
    protected Slot lastClickSlot;
    /**触屏模式下拖拽的物品*/
    private ItemStack draggingItem = ItemStack.EMPTY;
    private ItemStack lastQuickMoved  = ItemStack.EMPTY;
    /**是否可以投掷*/
    protected boolean isAllowThrow = true;
    /**触屏模式下是否右键分割*/
    private boolean isSplittingStack;
    /***/
    private int quickCraftingType;
    private int quickCraftingRemainder;
    private boolean doubleclick;
    private boolean skipNextRelease;
    private long lastClickTime;
    private int lastClickButton;
    private int quickCraftingButton;
    private int snapbackStartX;
    private int snapbackStartY;
    private long snapbackTime;
    private ItemStack snapbackItem = ItemStack.EMPTY;
    private long quickdropTime;
    @Nullable
    private Slot snapbackEnd;
    @Nullable
    private Slot quickdropSlot;
    /**/
    protected List<GuiEventListener> children;
    private ViewModelStore store;
    /**是否被*/
    private boolean isRendered;
    /***/
    private final LifeCycleRegistry registry;

    protected static final Logger LOGGER = LogManager.getLogger();

    protected VidaContainerScreen(T menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.skipNextRelease = true;
        this.children = new ArrayList<>();
        this.registry = new LifeCycleRegistry(this);
    }

    @Override
    public void added() {
        super.added();
        this.children.clear();
        registry.handleLifecycleEvent(LifeCycle.Event.ON_CREATE);
    }

    @Override
    protected void init() {
        super.init();
        registry.handleLifecycleEvent(LifeCycle.Event.ON_START);
        registry.handleLifecycleEvent(LifeCycle.Event.ON_RESUME);
    }

    @Override
    protected void rebuildWidgets() {
        registry.handleLifecycleEvent(LifeCycle.Event.ON_PAUSE);
        this.children.clear();
        super.rebuildWidgets();
    }

    @Override
    public LifeCycle getLifeCycle() {
        return registry;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderSlots(graphics, mouseX, mouseY, partialTicks);
        renderDraggingItem(graphics, mouseX, mouseY, partialTicks);
        for(Renderable renderable : renderables){
            renderable.render(graphics, mouseX, mouseY, partialTicks);
        }
    }

    public void addComponentAndChild(VidaWidget widget){
        if(widget.children() != null){
            children.addAll(widget.children());
        }
        children.add(widget);
        renderables.add(widget);
    }

    protected <T extends GuiEventListener & NarratableEntry & IVidaNodes> T addListener(T widget) {
        children.add(widget);
        if(widget.children() != null){
            children.addAll(widget.children());
        }
        return super.addWidget(widget);
    }

    @Override
    protected <T extends GuiEventListener & NarratableEntry> T addWidget(T p_96625_) {
        this.children.add(p_96625_);
        return p_96625_;
    }

    protected void renderSlots(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        // 获取开始渲染位置
        int left = this.leftPos;
        int top = this.topPos;

        // 渲染
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        PoseStack poseStack = graphics.pose();

        poseStack.pushPose();
        poseStack.translate((float)left, (float)top, 0.0F);

        // 设置正在悬浮的格子为空
        this.hoveredSlot = null;

        for(int i = 0; i < this.menu.slots.size(); ++i) {
            Slot slot = this.getMenu().slots.get(i);
            // 渲染每个格子
            if (slot.isActive()) {
                this.renderSingleSlot(graphics, slot);
            }

            // 设置正在悬浮的格子
            if (this.isHovering(slot, (double)mouseX, (double)mouseY) && slot.isActive()) {
                this.hoveredSlot = slot;
                if (this.hoveredSlot.isHighlightable()) {
                    this.renderSingleSlotHighlight(graphics, slot.x, slot.y, 0, getSlotColor(i));
                }
            }
        }

        poseStack.popPose();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }


    /**绘制正在拖拽的物品*/
    public void renderDraggingItem(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        // 获取开始渲染位置
        int left = this.leftPos;
        int top = this.topPos;

        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate((float)left, (float)top, 0.0F);

        // 渲染
        ItemStack draggingItem = this.draggingItem.isEmpty() ? this.menu.getCarried() : this.draggingItem;
        if (!draggingItem.isEmpty()) {
            int offsetX = 8;
            int offsetY = this.draggingItem.isEmpty() ? 8 : 16;
            String s = null;
            if (!this.draggingItem.isEmpty() && this.isSplittingStack) {
                draggingItem = draggingItem.copyWithCount(Mth.ceil((float) draggingItem.getCount() / 2.0F));
            } else if (this.isQuickCrafting && this.quickCraftSlots.size() > 1) {
                draggingItem = draggingItem.copyWithCount(this.quickCraftingRemainder);
                if (draggingItem.isEmpty()) {
                    s = ChatFormatting.YELLOW + "0";
                }
            }

            this.renderFloatingItem(graphics, draggingItem, mouseX - left - offsetX, mouseY - top - offsetY, s);
        }

        // 拽回动画
        if (!this.snapbackItem.isEmpty()) {
            float f = (float)(Util.getMillis() - this.snapbackTime) / 100.0F;
            if (f >= 1.0F) {
                f = 1.0F;
                this.snapbackItem = ItemStack.EMPTY;
            }

            int j2 = this.snapbackEnd.x - this.snapbackStartX;
            int k2 = this.snapbackEnd.y - this.snapbackStartY;
            int j1 = this.snapbackStartX + (int)((float)j2 * f);
            int k1 = this.snapbackStartY + (int)((float)k2 * f);
            this.renderFloatingItem(graphics, this.snapbackItem, j1, k1, (String)null);
        }


        poseStack.popPose();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }

    /**渲染单个槽位*/
    protected void renderSingleSlot(GuiGraphics graphics, Slot slot) {
        int slotX = slot.x;
        int slotY = slot.y;
        PoseStack poseStack = graphics.pose();
        ItemStack slotItem = slot.getItem();

        boolean flag = false;

        boolean isNoTouchScreenSplit = slot == this.clickedSlot && !this.draggingItem.isEmpty() && !this.isSplittingStack;
        boolean isTouchMode = this.minecraft.options.touchscreen().get();

        ItemStack carriedItem = this.menu.getCarried();
        String s = null;
        if (isTouchMode && slot == this.clickedSlot && !this.draggingItem.isEmpty() && this.isSplittingStack && !slotItem.isEmpty()) {
            slotItem = slotItem.copyWithCount(slotItem.getCount() / 2);
        } else if (this.isQuickCrafting && this.quickCraftSlots.contains(slot) && !carriedItem.isEmpty()) {
            // 快速合成
            if (this.quickCraftSlots.size() == 1) {
                return;
            }

            if (AbstractContainerMenu.canItemQuickReplace(slot, carriedItem, true) && this.menu.canDragTo(slot)) {
                flag = true;
                int k = Math.min(carriedItem.getMaxStackSize(), slot.getMaxStackSize(carriedItem));
                int l = slot.getItem().isEmpty() ? 0 : slot.getItem().getCount();
                int i1 = AbstractContainerMenu.getQuickCraftPlaceCount(this.quickCraftSlots, this.quickCraftingType, carriedItem) + l;
                if (i1 > k) {
                    i1 = k;
                    s = ChatFormatting.YELLOW.toString() + k;
                }

                slotItem = carriedItem.copyWithCount(i1);
            } else {
                this.quickCraftSlots.remove(slot);
                this.recalculateQuickCraftRemaining();
            }
        }

        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 100.0F);

        if (slotItem.isEmpty() && slot.isActive()) {
            Pair<ResourceLocation, ResourceLocation> pair = slot.getNoItemIcon();
            if (pair != null) {
                TextureAtlasSprite textureatlassprite = this.minecraft.getTextureAtlas(pair.getFirst()).apply(pair.getSecond());
                graphics.blit(slotX, slotY, 0, 16, 16, textureatlassprite);
                isNoTouchScreenSplit = true;
            }
        }

        if (!isNoTouchScreenSplit) {
            if (flag) {
                fill(graphics, slotX, slotY, slotX + 16, slotY + 16, -2130706433);
            }

            // 渲染物品/物品数量/物品框
            graphics.renderItem(slotItem, slotX, slotY, slot.x + slot.y * this.imageWidth);
            graphics.renderItemDecorations(this.font, slotItem, slotX, slotY, s);
        }

        poseStack.popPose();
    }

    /**绘制矩形*/
    public void fill(GuiGraphics graphics, int x0, int y0, int x1, int y1, int color){
        PoseStack poseStack = graphics.pose();

        poseStack.pushPose();
        graphics.fill(x0, y0, x1, y1, color);
        poseStack.popPose();
    }

    /**绘制鼠标拿着的物品*/
    private void renderFloatingItem(GuiGraphics graphics, ItemStack itemstack, int mouseX, int mouseY, String p_282568_) {
        graphics.pose().pushPose();
        graphics.pose().translate(0.0F, 0.0F, 232.0F);
        graphics.renderItem(itemstack, mouseX, mouseY);
        var font = net.minecraftforge.client.extensions.common.IClientItemExtensions.of(itemstack).getFont(itemstack, net.minecraftforge.client.extensions.common.IClientItemExtensions.FontContext.ITEM_COUNT);
        graphics.renderItemDecorations(font == null ? this.font : font, itemstack, mouseX, mouseY - (this.draggingItem.isEmpty() ? 0 : 8), p_282568_);
        graphics.pose().popPose();
    }


    /**重新计算快速合成剩余*/
    private void recalculateQuickCraftRemaining() {
        ItemStack itemstack = this.menu.getCarried();
        if (!itemstack.isEmpty() && this.isQuickCrafting) {
            if (this.quickCraftingType == 2) {
                this.quickCraftingRemainder = itemstack.getMaxStackSize();
            } else {
                this.quickCraftingRemainder = itemstack.getCount();

                for(Slot slot : this.quickCraftSlots) {
                    ItemStack itemstack1 = slot.getItem();
                    int i = itemstack1.isEmpty() ? 0 : itemstack1.getCount();
                    int j = Math.min(itemstack.getMaxStackSize(), slot.getMaxStackSize(itemstack));
                    int k = Math.min(AbstractContainerMenu.getQuickCraftPlaceCount(this.quickCraftSlots, this.quickCraftingType, itemstack) + i, j);
                    this.quickCraftingRemainder -= k - i;
                }

            }
        }
    }

    /**计算鼠标是否悬浮在某个槽位上*/
    protected boolean isHovering(Slot slot, double mouseX, double mouseY) {
        return this.isHovering(slot.x, slot.y, 16, 16, mouseX, mouseY);
    }

    /**高亮某个槽位*/
    public void renderSingleSlotHighlight(GuiGraphics graphics, int x, int y, int p_283504_, int color) {
        fill(graphics, x, y, x + 16, y + 16, color);
    }

    /**鼠标点击监听事件*/
    public boolean mouseClicked(double mouseX, double mouseY, int key) {
        if(mouseClickedListener(mouseX, mouseY, key)){
            return true;
        }
        return mouseClickedSlot(mouseX, mouseY, key);
    }

    /**通知子监听器*/
    public boolean mouseClickedListener(double mouseX, double mouseY, int key) {
        for(GuiEventListener guieventlistener : this.children()) {
            if (guieventlistener.mouseClicked(mouseX, mouseY, key)) {
                this.setFocused(guieventlistener);
                if (key == 0) {
                    this.setDragging(true);
                }

                return true;
            }
        }
        return false;
    }

    /**通知槽位点击事件*/
    public boolean mouseClickedSlot(double mouseX, double mouseY, int key){
        //中键
        InputConstants.Key mouseKey = InputConstants.Type.MOUSE.getOrCreate(key);
        boolean isMidKeyDown = this.minecraft.options.keyPickItem.isActiveAndMatches(mouseKey);

        Slot slot = this.findSlot(mouseX, mouseY);
        long i = Util.getMillis();
        this.doubleclick = this.lastClickSlot == slot && i - this.lastClickTime < 250L && this.lastClickButton == key;
        this.skipNextRelease = false;
        if (key != 0 && key != 1 && !isMidKeyDown) {
            //处理热键被按下的情况
            this.checkHotbarMouseClicked(key);
        } else {
            int left = this.leftPos;
            int top = this.topPos;
            // 检测是否点击了外部非槽位部分
            boolean isClickOutSide = this.hasClickedOutside(mouseX, mouseY, left, top, key);
            if (slot != null) isClickOutSide = false; // Forge, prevent dropping of items through slots outside of GUI boundaries
            // 记录被点击的槽位下标，-1代表没有，-999代表要投掷物品
            int clickedSlotIndex = -1;
            if (slot != null) {
                clickedSlotIndex = slot.index;
            }

            if (isClickOutSide) {
                clickedSlotIndex = -999;
            }

            // 如果没有拖拽的物品但是点击了外部，关闭界面
            if (isTouchScreenMode() && isClickOutSide && this.menu.getCarried().isEmpty()) {
                this.onClose();
                return true;
            }

            if (clickedSlotIndex != -1) {
                if (isTouchScreenMode()) {
                    //触屏模式处理逻辑
                    if (slot != null && slot.hasItem()) {
                        this.clickedSlot = slot;
                        this.draggingItem = ItemStack.EMPTY;
                        this.isSplittingStack = key == 1;
                    } else {
                        this.clickedSlot = null;
                    }
                } else if (!this.isQuickCrafting) {
                    //非快速合成逻辑
                    if (this.menu.getCarried().isEmpty()) {
                        //普通模式（没有物品在鼠标上）处理逻辑
                        if (isMidKeyDown) {
                            // 中键创造模式复制物品
                            this.slotClicked(slot, clickedSlotIndex, key, ClickType.CLONE);
                        } else {
                            ClickType clicktype = ClickType.PICKUP;
                            boolean isShiftDown = clickedSlotIndex != -999
                                    && (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LSHIFT)
                                    || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_RSHIFT)
                            );
                            if (isShiftDown) {
                                // 左右SHIFT键按下时，快速移动物品
                                this.lastQuickMoved = slot != null && slot.hasItem() ? slot.getItem().copy() : ItemStack.EMPTY;
                                clicktype = ClickType.QUICK_MOVE;
                            } else if (clickedSlotIndex == -999 && isAllowThrow) {
                                // 其他状态下默认抛出物品
                                clicktype = ClickType.THROW;
                            } else if(clickedSlotIndex == -999){
                                clicktype = ClickType.PICKUP;
                            }

                            this.slotClicked(slot, clickedSlotIndex, key, clicktype);
                        }

                        // 下次释放鼠标时，取消处理事件
                        this.skipNextRelease = true;
                    } else {
                        // 快速合成
                        this.isQuickCrafting = true;
                        this.quickCraftingButton = key;
                        this.quickCraftSlots.clear();
                        // 根据键位定义快速合成模式
                        if (key == 0) {
                            this.quickCraftingType = 0;
                        } else if (key == 1) {
                            this.quickCraftingType = 1;
                        } else if (this.minecraft.options.keyPickItem.isActiveAndMatches(mouseKey)) {
                            this.quickCraftingType = 2;
                        }
                    }
                }
            }
        }

        this.lastClickSlot = slot;
        this.lastClickTime = i;
        this.lastClickButton = key;
        return true;
    }

    /**找到某个位置的槽位*/
    protected Slot findSlot(double mouseX, double mouseY) {
        for(int i = 0; i < this.menu.slots.size(); ++i) {
            Slot slot = this.menu.slots.get(i);
            if (this.isHovering(slot, mouseX, mouseY) && slot.isActive()) {
                return slot;
            }
        }

        return null;
    }

    /**检测鼠标热键是否被按下*/
    protected void checkHotbarMouseClicked(int key) {
        if (this.hoveredSlot != null && this.menu.getCarried().isEmpty()) {
            if (this.minecraft.options.keySwapOffhand.matchesMouse(key)) {
                this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, 40, ClickType.SWAP);
                return;
            }

            for(int i = 0; i < 9; ++i) {
                if (this.minecraft.options.keyHotbarSlots[i].matchesMouse(key)) {
                    this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, i, ClickType.SWAP);
                }
            }
        }
    }

    /**当鼠标松开时*/
    public boolean mouseReleased(double mouseX, double mouseY, int key) {
        // 通知监听器
        mouseReleasedListener(mouseX, mouseY, key); //Forge, Call parent to release buttons
        return mouseReleasedSlot(mouseX, mouseY, key);
    }

    /**通知监听器松开事件*/
    protected boolean mouseReleasedListener(double mouseX, double mouseY, int key) {
        this.setDragging(false);
        return this.getChildAt(mouseX, mouseY).filter((p_94708_) -> {
            return p_94708_.mouseReleased(mouseX, mouseY, key);
        }).isPresent();
    }

    /**通知槽位松开事件*/
    public boolean mouseReleasedSlot(double mouseX, double mouseY, int key){
        //找到正在悬浮的slot
        Slot slot = this.findSlot(mouseX, mouseY);
        int left = this.leftPos;
        int top = this.topPos;

        //是否点击到了外部
        boolean isClickedOutSide = this.hasClickedOutside(mouseX, mouseY, left, top, key);
        if (slot != null) isClickedOutSide = false; // Forge, prevent dropping of items through slots outside of GUI boundaries
        InputConstants.Key mouseKey = InputConstants.Type.MOUSE.getOrCreate(key);

        //悬浮的slot下标
        int hoveredSlotIndex = -1;
        if (slot != null) {
            hoveredSlotIndex = slot.index;
        }

        if (isClickedOutSide) {
            hoveredSlotIndex = -999;
        }


        if (this.doubleclick && slot != null && key == 0 && this.menu.canTakeItemForPickAll(ItemStack.EMPTY, slot)) {
            // 双击时处理逻辑
            if (hasShiftDown()) {
                // shift双击，快速移动物品
                if (!this.lastQuickMoved.isEmpty()) {
                    for(Slot iSlot: this.menu.slots) {
                        if (iSlot != null && iSlot.mayPickup(this.minecraft.player) && iSlot.hasItem() && iSlot.isSameInventory(slot) && AbstractContainerMenu.canItemQuickReplace(iSlot, this.lastQuickMoved, true)) {
                            this.slotClicked(iSlot, iSlot.index, key, ClickType.QUICK_MOVE);
                        }
                    }
                }
            } else {
                // 非shift处理逻辑，如果双击了会拿取所有物品
                this.slotClicked(slot, hoveredSlotIndex, key, ClickType.PICKUP_ALL);
            }

            this.doubleclick = false;
            this.lastClickTime = 0L;
        } else {
            // 其他逻辑
            if (this.isQuickCrafting && this.quickCraftingButton != key) {
                // 快速合成
                this.isQuickCrafting = false;
                this.quickCraftSlots.clear();
                this.skipNextRelease = true;
                return true;
            }

            // 非触屏模式下，点击后不应该继续处理
            if (this.skipNextRelease) {
                this.skipNextRelease = false;
                return true;
            }

            // 触屏模式处理逻辑
            if (this.clickedSlot != null && isTouchScreenMode()) {
                if (key == 0 || key == 1) {
                    // 松开了鼠标键位
                    if (this.draggingItem.isEmpty() && slot != this.clickedSlot) {
                        this.draggingItem = this.clickedSlot.getItem();
                    }

                    boolean flag2 = AbstractContainerMenu.canItemQuickReplace(slot, this.draggingItem, false);
                    if (hoveredSlotIndex != -1 && !this.draggingItem.isEmpty() && flag2) {
                        this.slotClicked(this.clickedSlot, this.clickedSlot.index, key, ClickType.PICKUP);
                        this.slotClicked(slot, hoveredSlotIndex, 0, ClickType.PICKUP);
                        if (this.menu.getCarried().isEmpty()) {
                            this.snapbackItem = ItemStack.EMPTY;
                        } else {
                            this.slotClicked(this.clickedSlot, this.clickedSlot.index, key, ClickType.PICKUP);
                            this.snapbackStartX = Mth.floor(mouseX - (double)left);
                            this.snapbackStartY = Mth.floor(mouseY - (double)top);
                            this.snapbackEnd = this.clickedSlot;
                            this.snapbackItem = this.draggingItem;
                            this.snapbackTime = Util.getMillis();
                        }
                    } else if (!this.draggingItem.isEmpty()) {
                        this.snapbackStartX = Mth.floor(mouseX - (double)left);
                        this.snapbackStartY = Mth.floor(mouseY - (double)top);
                        this.snapbackEnd = this.clickedSlot;
                        this.snapbackItem = this.draggingItem;
                        this.snapbackTime = Util.getMillis();
                    }

                    this.clearDraggingState();
                }
            } else if (this.isQuickCrafting && !this.quickCraftSlots.isEmpty()) {
                this.slotClicked((Slot)null, -999, AbstractContainerMenu.getQuickcraftMask(0, this.quickCraftingType), ClickType.QUICK_CRAFT);

                for(Slot slot1 : this.quickCraftSlots) {
                    this.slotClicked(slot1, slot1.index, AbstractContainerMenu.getQuickcraftMask(1, this.quickCraftingType), ClickType.QUICK_CRAFT);
                }

                this.slotClicked((Slot)null, -999, AbstractContainerMenu.getQuickcraftMask(2, this.quickCraftingType), ClickType.QUICK_CRAFT);
            } else if (!this.menu.getCarried().isEmpty()) {
                if (this.minecraft.options.keyPickItem.isActiveAndMatches(mouseKey)) {
                    this.slotClicked(slot, hoveredSlotIndex, key, ClickType.CLONE);
                } else {
                    boolean flag1 = hoveredSlotIndex != -999 && (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 340) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 344));
                    if (flag1) {
                        this.lastQuickMoved = slot != null && slot.hasItem() ? slot.getItem().copy() : ItemStack.EMPTY;
                    }

                    this.slotClicked(slot, hoveredSlotIndex, key, flag1 ? ClickType.QUICK_MOVE : ClickType.PICKUP);
                }
            }
        }

        if (this.menu.getCarried().isEmpty()) {
            this.lastClickTime = 0L;
        }

        this.isQuickCrafting = false;
        return true;
    }

    /**鼠标拖拽事件*/
    public boolean mouseDragged(double mouseX, double mouseY, int key, double dragX, double dragY) {
        Slot slot = this.findSlot(mouseX, mouseY);
        ItemStack itemstack = this.menu.getCarried();
        if (this.clickedSlot != null && this.minecraft.options.touchscreen().get()) {
            if (key == 0 || key == 1) {
                if (this.draggingItem.isEmpty()) {
                    if (slot != this.clickedSlot && !this.clickedSlot.getItem().isEmpty()) {
                        this.draggingItem = this.clickedSlot.getItem().copy();
                    }
                } else if (this.draggingItem.getCount() > 1 && slot != null && AbstractContainerMenu.canItemQuickReplace(slot, this.draggingItem, false)) {
                    long i = Util.getMillis();
                    if (this.quickdropSlot == slot) {
                        if (i - this.quickdropTime > 500L) {
                            this.slotClicked(this.clickedSlot, this.clickedSlot.index, 0, ClickType.PICKUP);
                            this.slotClicked(slot, slot.index, 1, ClickType.PICKUP);
                            this.slotClicked(this.clickedSlot, this.clickedSlot.index, 0, ClickType.PICKUP);
                            this.quickdropTime = i + 750L;
                            this.draggingItem.shrink(1);
                        }
                    } else {
                        this.quickdropSlot = slot;
                        this.quickdropTime = i;
                    }
                }
            }
        } else if (this.isQuickCrafting && slot != null && !itemstack.isEmpty() && (itemstack.getCount() > this.quickCraftSlots.size() || this.quickCraftingType == 2) && AbstractContainerMenu.canItemQuickReplace(slot, itemstack, true) && slot.mayPlace(itemstack) && this.menu.canDragTo(slot)) {
            this.quickCraftSlots.add(slot);
            this.recalculateQuickCraftRemaining();
        }

        if(children() != null && children().size() > 0){
            children().forEach(child -> child.mouseDragged(mouseX, mouseY, key, dragX, dragY));
        }

        return true;
    }
    
    private boolean isTouchScreenMode(){
        return this.minecraft.options.touchscreen().get();
    }

    public void onClose() {
        this.registry.handleLifecycleEvent(LifeCycle.Event.ON_REMOVE);
        this.close();
        this.minecraft.player.closeContainer();
        this.minecraft.popGuiLayer();
    }

    protected void close(){}

    @Override
    public List<? extends GuiEventListener> children() {
        return this.children;
    }

    @Override
    public IViewModelStoreProvider getHolder() {
        return this;
    }

    @Override
    public ViewModelStore getStore() {
        if(store == null){
            this.store = new ViewModelStore();
        }
        return this.store;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double partialTicks) {
        List<GuiEventListener> listeners = new ArrayList<>();

        for(GuiEventListener guieventlistener : this.children()) {
            if (guieventlistener.isMouseOver(mouseX, mouseY)) {
                listeners.add(guieventlistener);
            }
        }

        boolean isScrolled = listeners.stream().reduce(false, (total, listener) -> listener.mouseScrolled(mouseX, mouseY, partialTicks), Boolean::logicalOr);

        return isScrolled;
    }

    @Override
    public boolean keyPressed(int p_97765_, int p_97766_, int p_97767_) {
        InputConstants.Key mouseKey = InputConstants.getKey(p_97765_, p_97766_);
        if (super.keyPressed(p_97765_, p_97766_, p_97767_)) {
            return true;
        } else if (this.minecraft.options.keyInventory.isActiveAndMatches(mouseKey)) {
            this.onClose();
            return true;
        } else {
            boolean handled = this.checkHotbarKeyPressed(p_97765_, p_97766_);// Forge MC-146650: Needs to return true when the key is handled
            if (this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
                if (this.minecraft.options.keyPickItem.isActiveAndMatches(mouseKey)) {
                    this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, 0, ClickType.CLONE);
                    handled = true;
                } else if (this.minecraft.options.keyDrop.isActiveAndMatches(mouseKey) && isAllowThrow) {
                    this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, hasControlDown() ? 1 : 0, ClickType.THROW);
                    handled = true;
                }
            } else if (this.minecraft.options.keyDrop.isActiveAndMatches(mouseKey)) {
                handled = true;
            }

            return handled;
        }
    }
}
