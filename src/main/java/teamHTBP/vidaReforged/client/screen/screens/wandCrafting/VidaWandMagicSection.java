package teamHTBP.vidaReforged.client.screen.screens.wandCrafting;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.client.screen.components.common.IconButton;
import teamHTBP.vidaReforged.client.screen.components.common.ScrolledContainer;
import teamHTBP.vidaReforged.client.screen.components.common.VidaWidget;
import teamHTBP.vidaReforged.client.screen.components.guidebooks.VidaGuidebookText;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaWandCraftingViewModel;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagic;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;
import teamHTBP.vidaReforged.server.providers.VidaMagicManager;

import javax.swing.*;
import java.util.*;
import java.util.function.Consumer;

public class VidaWandMagicSection extends VidaWandCraftSection{
    VidaWandCraftingViewModel viewModel;
    ScrolledContainer<VidaWandMagicButton> scrolledContainer;
    public final static ResourceLocation NON_SELECTED = VidaMagic.MAGIC_UNKNOWN;
    ResourceLocation currentFocusMagicId = NON_SELECTED;
    public Map<ResourceLocation, VidaWandMagicButton> allMagicsButtons = new HashMap<>();
    public List<VidaWandMagicButton> currentShownButtons = new ArrayList<>();
    public String filter = "all";
    public GridLayout skillGridLayout;
    public GridLayout layout;
    public VidaMagicInfoArea magicInfo;
    public Optional<IconButton> button = Optional.empty();

    public VidaWandMagicSection(int x, int y, int width, int height, Component component) {
        super(x, y, width, height, component);
        viewModel = new ViewModelProvider(requireParent()).get(VidaWandCraftingViewModel.class);
    }

    /** 每次屏幕调整大小时，调整滚动条高度，魔法选择栏目位置 */
    @Override
    public void init() {
        super.init();
        // 调整滚动条
        this.scrolledContainer = new ScrolledContainer<>(getX(), getY(), getWidth() / 2, getHeight());
        this.scrolledContainer.setCacheable(false);
        this.scrolledContainer.thumbWidth = 2;

        // 初始化魔法选择栏，如果没有被初始化，重新生成，且只能生成一次
        for(ResourceLocation magicId : VidaMagicManager.getMagicsKey()){
            VidaMagic magic = VidaMagicManager.getMagicByMagicId(magicId);
            if(!allMagicsButtons.containsKey(magicId)){
                allMagicsButtons.put(magicId, new VidaWandMagicButton(0, 0, 24,24, Component.empty(), magic));
            }
        }
        this.currentShownButtons.clear();
        this.scrolledContainer.clearAllComponent();

        // 设置过滤器
        if(filter.equals("all")){
            currentShownButtons.addAll(allMagicsButtons.values());
        }

        //
        allMagicsButtons.forEach((id, button) -> {
            if(currentShownButtons.contains(button)){
                button.setVisible(true);
                button.setClickListener(this::onClickMagic);
                scrolledContainer.add(button);
                return;
            }
            button.setVisible(false);
        });

        this.magicInfo = new VidaMagicInfoArea(0 , 0, getWidth() * 3 / 7, getHeight() * 2 / 3, Component.empty());
        this.magicInfo.setMagicId(this.currentFocusMagicId);
        this.layout = null;
        this.setVisible(visible);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        this.scrolledContainer.setVisible(visible);
        this.magicInfo.setVisible(visible);
        this.button.ifPresent(button -> button.setVisible(visible));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        adjustChildComponents();
        scrolledContainer.render(graphics, mouseX, mouseY, partialTicks);
        magicInfo.render(graphics, mouseX, mouseY, partialTicks);
        button.ifPresent(button -> button.render(graphics, mouseX, mouseY, partialTicks));
    }

    @Override
    public void setX(int x) {
        super.setX(x);
    }

    @Override
    public void setY(int y) {
        super.setY(y);
    }

    public void onClickMagic(ResourceLocation id){
        for(ResourceLocation key : allMagicsButtons.keySet()){
            VidaWandMagicButton button = allMagicsButtons.get(key);
            if(key.equals(id)){
                button.setChosen(true);
                continue;
            }
            button.setChosen(false);
        }
        this.currentFocusMagicId = id;
        this.magicInfo.setMagicId(currentFocusMagicId);
        this.button = Optional.of(new IconButton.Builder()
                .message(Component.translatable("gui.vida_reforged.vida_wand_crafting.magic_choose", VidaMagicManager.getMagicByMagicId(currentFocusMagicId).getFormattedDisplayName()))
                .height(40)
                .width(this.magicInfo.getWidth())
                .isBackground(true)
                .listener(() -> viewModel.setMagicWithIndex(0, currentFocusMagicId))
                .build(0,0)
        );
    }

    public void adjustChildComponents(){
        if(this.layout == null){
            this.layout = new GridLayout(getX(), getY());
            this.layout.addChild(scrolledContainer, 0, 0);
            this.layout.addChild(magicInfo, 0, 1);
        }
        this.layout.setX(getX());
        this.layout.setY(getY());
        this.layout.spacing(24);

        this.layout.arrangeElements();

        this.skillGridLayout = new GridLayout(scrolledContainer.getX() + 8, scrolledContainer.getY() + 8);
        this.skillGridLayout.spacing(6);
        int col = 0;
        int xSize = 0;
        int row = 0;
        for(int i = 0 ; i < currentShownButtons.size(); i++){
            VidaWidget widget = currentShownButtons.get(i);
            if(xSize + widget.getWidth() > scrolledContainer.getWidth()){
                col = 0;
                xSize = 0;
                row ++;
            }
            this.skillGridLayout.addChild(widget, row, col);
            xSize += widget.getWidth();
            col ++;
        }


        this.skillGridLayout.arrangeElements();
        FrameLayout.alignInRectangle(skillGridLayout, getX(), getY(), scrolledContainer.getWidth(), skillGridLayout.getHeight() + 10, 0.5f, 0.5f);

        this.button.ifPresent(button -> {
            button.setX(this.magicInfo.getX());
            button.setY(this.magicInfo.getY() + this.magicInfo.getHeight() + 12);
            button.setWidth(this.magicInfo.getWidth());
            button.refresh();
        });
    }

    @Override
    public Collection<? extends GuiEventListener> children() {
        if(!visible){
            return List.of();
        }
        List<GuiEventListener> listeners = new ArrayList<>(currentShownButtons);
        button.ifPresent(listeners::add);
        return listeners;
    }

    public static class VidaWandMagicButton extends VidaWidget {
        private VidaMagic magic;
        private FloatRange hoverAlpha = new FloatRange(0,0.0f,0.2f);
        private FloatRange focusAlpha = new FloatRange(0, 0.0f, 0.3f);
        private Consumer<ResourceLocation> clickListener;
        private boolean isChosen;

        public void setChosen(boolean chosen) {
            isChosen = chosen;
        }

        public VidaWandMagicButton(int x, int y, int width, int height, Component component, VidaMagic magic) {
            super(x, y, width, height, component);
            this.setChosen(false);
            this.magic = magic;
        }

        public void setClickListener(Consumer<ResourceLocation> clickListener) {
            this.clickListener = clickListener;
        }

        @Override
        public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            this.hoverAlpha.change(isHovered, Minecraft.getInstance().getDeltaFrameTime() * 0.2f);
            this.focusAlpha.change(isChosen, Minecraft.getInstance().getDeltaFrameTime() * 0.2f);
            renderWidgetBackground(graphics);
            renderHoverBackground(graphics);
        }

        /**渲染背景*/
        protected void renderWidgetBackground(GuiGraphics graphics){
            PoseStack poseStack = graphics.pose();
            poseStack.pushPose();
            graphics.fillGradient(getX(), getY(), getX() + width, getY() + height, 0xA0131516, 0xC4131416);
            VidaGuiHelper.blitWithTexture(graphics, getX(), getY(), 0, magic.icon());
            poseStack.popPose();
        }

        protected void renderHoverBackground(GuiGraphics graphics){
            // 渲染背景
            ARGBColor color = ARGBColor.of(hoverAlpha.get() + focusAlpha.get(), 0.6f, 0.6f, 0.6f);
            graphics.fillGradient(getX(), getY(), getX() + width, getY() + height, color.argb(), color.argb());
        }

        @Override
        public void onClick(double x, double y) {
            if(clickListener == null){
                return;
            }
            clickListener.accept(magic.magicId());
        }
    }


    public static class VidaMagicInfoArea extends VidaWidget{
        StringWidget title;
        VidaGuidebookText description;
        VidaMagic magic;


        public VidaMagicInfoArea(int x, int y, int width, int height, Component component) {
            super(x, y, width, height, component);
            this.title = new StringWidget(Component.literal("介绍"), this.mc.font);
            this.description = new VidaGuidebookText("", getX(), getY(),getWidth() - 8, getHeight() / 2);
        }

        public void setMagicId(ResourceLocation magicId) {
            if(magicId == null || magicId.equals(VidaMagic.MAGIC_UNKNOWN)){
                this.description = new VidaGuidebookText("", getX(), getY(),getWidth() - 8, getHeight() / 2);
                return;
            }
            this.magic = VidaMagicManager.getMagicByMagicId(magicId);
            this.description = new VidaGuidebookText(this.magic.description(), getX(), getY(),getWidth() - 8, getHeight() / 2);
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            graphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0xa0131416);
            GridLayout layout = new GridLayout(getX(), getY() + 8);
            layout.spacing(8);
            layout.addChild(title, 0, 0, layout.defaultCellSetting().alignHorizontallyCenter());
            layout.addChild(description,1, 0, layout.defaultCellSetting().alignHorizontallyCenter());
            layout.arrangeElements();
            FrameLayout.alignInRectangle(layout, getX(), getY() + 8, getWidth(),  getHeight(), 0.5f, 0f);

            title.render(graphics, mouseX, mouseY, partialTicks);
            description.render(graphics, mouseX, mouseY, partialTicks);
        }
    }
}
