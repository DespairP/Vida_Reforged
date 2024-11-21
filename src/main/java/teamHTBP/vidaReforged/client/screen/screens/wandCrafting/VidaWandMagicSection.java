package teamHTBP.vidaReforged.client.screen.screens.wandCrafting;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.screen.components.common.IconButton;
import teamHTBP.vidaReforged.client.screen.components.common.ScrolledContainer;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;
import teamHTBP.vidaReforged.client.screen.components.guidebooks.VidaGuidebookText;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaWandCraftingViewModel;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagic;
import teamHTBP.vidaReforged.client.screen.components.VidaLifecycleSection;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;
import teamHTBP.vidaReforged.core.utils.anim.SecondOrderDynamics;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;
import teamHTBP.vidaReforged.server.providers.VidaMagicManager;

import java.util.*;
import java.util.function.Consumer;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class VidaWandMagicSection extends VidaLifecycleSection {
    VidaWandCraftingViewModel viewModel;
    ScrolledContainer scrolledContainer;
    public final static ResourceLocation NON_SELECTED = VidaMagic.MAGIC_UNKNOWN;
    ResourceLocation currentFocusMagicId = NON_SELECTED;
    public Map<ResourceLocation, VidaWandMagicButton> allMagicsButtons = new HashMap<>();
    public List<VidaWandMagicButton> currentShownButtons = new ArrayList<>();
    public String filter = "all";
    /** 整个区域布局 */
    public GridLayout sectionLayout;
    /** 技能区域布局 */
    private GridLayout skillGridLayout;
    public VidaMagicInfoArea magicInfo;

    public Optional<IconButton> button = Optional.empty();
    /** 技能区域宽度 */
    private SecondOrderDynamics skillGridWidth = new SecondOrderDynamics(1, 0.5f, 0, new Vector3f());
    private Vector3f targetSkillGridWidth = new Vector3f();

    public VidaWandMagicSection(int x, int y, int width, int height, Component component) {
        super(x, y, width, height, component);
        viewModel = new ViewModelProvider(requireParent()).get(VidaWandCraftingViewModel.class);
        viewModel.magics.observe(this, this::onNewMagicSelected);
    }

    /** 每次屏幕调整大小时，调整滚动条高度，魔法选择栏目位置 */
    @Override
    public void onInit() {
        // 技能调整滚动条
        this.scrolledContainer = new ScrolledContainer(getX(), getY(), getWidth() / 2, getHeight(), new ResourceLocation(MOD_ID, "skill_container"));
        this.scrolledContainer.setCacheable(false);
        this.scrolledContainer.thumbWidth = 2;

        // 技能文字信息
        this.magicInfo = new VidaMagicInfoArea(0 , 0, getWidth() * 3 / 7, getHeight() * 2 / 3, Component.empty());
        this.magicInfo.setMagicId(this.currentFocusMagicId);

        // 动画
        this.skillGridWidth = new SecondOrderDynamics(1, 1, 0, new Vector3f(this.scrolledContainer.getWidth(),0,0));
        this.targetSkillGridWidth = new Vector3f(this.scrolledContainer.getWidth(), 0, 0);

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

        allMagicsButtons.forEach((id, button) -> {
            if(currentShownButtons.contains(button)){
                button.setVisible(true);
                button.setClickListener(this::onClickMagic);
                scrolledContainer.add(button);
                return;
            }
            button.setVisible(false);
        });

        this.sectionLayout = null;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        this.scrolledContainer.setVisible(visible);
        this.magicInfo.setVisible(visible);
        this.button.ifPresent(button -> button.setVisible(visible));
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        scrolledContainer.setWidth((int) skillGridWidth.update(partialTicks * 0.2f, targetSkillGridWidth, null).x);
        adjustChildComponents();
        scrolledContainer.render(graphics, mouseX, mouseY, partialTicks);
        magicInfo.render(graphics, mouseX, mouseY, partialTicks);
        button.ifPresent(button -> button.render(graphics, mouseX, mouseY, partialTicks));
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
                .listener(this::onSelectMagic)
                .build(0,0)
        );
        this.targetSkillGridWidth = new Vector3f(32, 0, 0);
    }

    /**当点击按钮时*/
    public void onSelectMagic(){
        viewModel.setMagicWithIndex(0, currentFocusMagicId);
    }

    /***/
    public void onNewMagicSelected(Map<Integer, ResourceLocation> selectedMagics){
        Collection<ResourceLocation> selectedKeys = selectedMagics.values();
        for(Map.Entry<ResourceLocation, VidaWandMagicButton> buttonItem : allMagicsButtons.entrySet()){
            VidaWandMagicButton magicButton = buttonItem.getValue();
            if(selectedKeys.contains(buttonItem.getKey())){
                Integer index = getKeyByValue(selectedMagics, buttonItem.getKey());
                magicButton.setSelectedIndex(index == null ? VidaWandMagicButton.UNSELECTED : index);
                continue;
            }
            magicButton.setSelectedIndex(VidaWandMagicButton.UNSELECTED);
        }
        this.targetSkillGridWidth = new Vector3f(getWidth() / 2 , 0, 0);
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void adjustChildComponents(){
        if(this.sectionLayout == null){
            this.sectionLayout = new GridLayout(getX(), getY());
            this.sectionLayout.addChild(scrolledContainer, 0, 0);
            this.sectionLayout.addChild(magicInfo, 0, 1);
        }
        this.sectionLayout.setX(getX());
        this.sectionLayout.setY(getY());
        this.sectionLayout.spacing(24);

        this.sectionLayout.arrangeElements();

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
        /**对应的魔法*/
        private VidaMagic magic;
        /**透明度*/
        private FloatRange hoverAlpha = new FloatRange(0,0.0f,0.2f);
        private FloatRange focusAlpha = new FloatRange(0, 0.0f, 0.3f);
        /**点击监听器*/
        private Consumer<ResourceLocation> clickListener;
        /**是否被focus*/
        private boolean isChosen;
        /**魔法所处法杖的下标，如果该魔法并没有装备在法杖上，则下标为-1*/
        private int selectedIndex = -1;
        public final static int UNSELECTED = -1;
        /***/
        public final static TextureSection SELECTED_ICON = new TextureSection(new ResourceLocation(VidaReforged.MOD_ID, "textures/icons/skills_002.png"), 24, 0, 24, 24, 384, 384);
        public final static ResourceLocation DINKFONT = new ResourceLocation(MOD_ID, "dinkie");

        private final FloatRange alpha = new FloatRange(0.1F, 0.1F, 1);

        public void setChosen(boolean chosen) {
            isChosen = chosen;
        }

        public void setSelectedIndex(int selectedIndex) {
            this.selectedIndex = selectedIndex;
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
            this.hoverAlpha.change(isHovered, Minecraft.getInstance().getDeltaFrameTime() * 0.05f);
            this.focusAlpha.change(isChosen, Minecraft.getInstance().getDeltaFrameTime() * 0.05f);
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
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1, 1, 1, alpha.change(selectedIndex >= 0, mc.getDeltaFrameTime() * 0.2f));
            // 渲染选中
            if(selectedIndex >= 0){
                VidaGuiHelper.blitWithTexture(graphics, getX(), getY(), 0, SELECTED_ICON);
                VidaGuiHelper.drawStringWithFont(graphics, getX() + 22, getY() + 20, 1, Component.literal("1").withStyle(style -> style.withFont(DINKFONT)));
            }
            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.disableBlend();
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
