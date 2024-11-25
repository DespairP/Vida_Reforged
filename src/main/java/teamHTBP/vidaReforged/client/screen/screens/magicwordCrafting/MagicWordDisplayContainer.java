package teamHTBP.vidaReforged.client.screen.screens.magicwordCrafting;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.screen.components.VidaLifecycleSection;
import teamHTBP.vidaReforged.client.screen.components.magicWords.MagicWordButton;
import teamHTBP.vidaReforged.client.screen.components.common.ScrolledContainer;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaMagicWordViewModel;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.system.magicWord.MagicWord;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;
import teamHTBP.vidaReforged.core.common.ui.layouts.GridLayoutMutation;
import teamHTBP.vidaReforged.server.providers.MagicWordManager;

import java.util.*;
import java.util.stream.Collectors;

/**
 * VidaWandCraftingTableScreen子组件
 * 用于显示右边的词条
 */
public class MagicWordDisplayContainer extends VidaLifecycleSection {
    /**
     * 组件宽度
     */
    public final static int WIDTH = 180;
    /**
     * 每个属性的词条
     */
    public final Map<VidaElement, List<MagicWordButton>> widgetMap;
    /**
     * 现在选择的元素
     */
    public VidaElement currentSelectedElement = VidaElement.GOLD;
    /**
     * 滚动组件
     */
    public ScrolledContainer scrolledContainer;
    /**
     * ViewModel
     */
    private VidaMagicWordViewModel model;
    /**布局*/
    private GridLayoutMutation gridLayout;
    /**LOGGER*/
    private static final Logger LOGGER = LogManager.getLogger();

    MagicWordButton.ClickListener clickListener = (element, magicId) -> {
        this.model.setSelectWord(element, magicId);
    };


    public MagicWordDisplayContainer(int x, int y, int width, int height) {
        super(x, y, width, height, Component.literal("Magic Word List"), new ResourceLocation(VidaReforged.MOD_ID, "magic_word_list"));
        this.widgetMap = new LinkedHashMap<>();
        this.initWidget();
    }

    public void initWidget() {
        try {
            // 初始化ViewModel
            this.model = new ViewModelProvider(requireParent()).get(VidaMagicWordViewModel.class);
            // 初始化滚动容器
            this.scrolledContainer = new ScrolledContainer(getX(), getY(), this.width, this.height, new ResourceLocation(VidaReforged.MOD_ID, "magic_word_list_box"));
            this.scrolledContainer.setBottomBorderSize(10);
            //
            for (VidaElement element : VidaElement.values()) {
                widgetMap.put(element, new LinkedList<>());
            }

            // 开始初始化词条按钮
            List<MagicWord> allWords = MagicWordManager.getAllMagicWords();
            List<String> playerWordIds = Optional.ofNullable(this.model.playerMagicWords.getValue()).orElse(new ArrayList<>());
            // 组件
            Map<VidaElement, List<MagicWord>> elementToWords = allWords.stream().collect(Collectors.groupingBy(magicWord -> magicWord.element()));
            for (VidaElement element : elementToWords.keySet()) {
                for (MagicWord word : elementToWords.get(element)) {
                    // 元素内第n个词条
                    boolean isUnLocked = playerWordIds.contains(word.name());
                    MagicWordButton button = new MagicWordButton(
                            getX(),
                            getY(),
                            MagicWordButton.WIDTH,
                            MagicWordButton.HEIGHT,
                            word.name(),
                            new ResourceLocation(VidaReforged.MOD_ID, "magic_word_list_button_" + new ResourceLocation(word.name()).getPath())
                    );
                    button.setOnClickListener(clickListener);
                    button.setLocked(!isUnLocked);
                    button.setVisible(false);
                    widgetMap.get(word.element()).add(button);
                }
            }

            // 布局初始化
            this.gridLayout = new GridLayoutMutation();
            this.arrangeButtons();

            // 初始化过滤器
            this.currentSelectedElement = this.model.selectedFilterElement.getValue();
            this.model.selectedFilterElement.observe(this, this::onFilterChange);

            // 设置
            this.model.selectedMagicWord.observe(this, this::onMagicWordChange);
            this.onFilterChange(this.currentSelectedElement);
            this.onMagicWordChange(this.model.selectedMagicWord.getValue());

        } catch (Exception exception) {
            LOGGER.error(exception);
        }
    }

    public void arrangeButtons(){
        // 清空
        this.gridLayout.clear();
        // 子组件获取
        List<MagicWordButton> widgets = this.widgetMap.get(currentSelectedElement);
        for(int i = 0; i < widgets.size(); i++){
            this.gridLayout.addChild(widgets.get(i), i / 2, i % 2, this.gridLayout.defaultCellSetting().paddingHorizontal(2).paddingVertical(6));
        }
        FrameLayout.alignInRectangle(this.gridLayout, getX() + 1, getY() + 1, width - 1, height - 1, 0.5f, 0f);
        this.gridLayout.arrangeElements();
    }

    protected void onFilterChange(VidaElement newValue) {
        this.currentSelectedElement = newValue;
        // 设置显示当前filter显示的词条
        List<MagicWordButton> filteredWords = this.widgetMap.get(this.currentSelectedElement);
        filteredWords.forEach(filteredWord -> filteredWord.setVisible(true));

        // 其他词条隐藏
        this.widgetMap.keySet()
                .stream()
                .filter(element -> element != this.currentSelectedElement)
                .forEach(element -> this.widgetMap.get(element).forEach(filteredWord -> filteredWord.setVisible(false)));

        //
        this.scrolledContainer.clearAllComponent();
        filteredWords.forEach(widget -> this.scrolledContainer.add(widget));
        arrangeButtons();
    }


    protected void onMagicWordChange(Map<VidaElement, String> newValue) {
        this.widgetMap.forEach((element, magicWordButtons) -> {
            String selectedWordId = newValue.get(element);

            magicWordButtons.forEach(button -> {
                if (Objects.equals(selectedWordId, button.getMagicWordId())) {
                    button.setSelected(true);
                    return;
                }
                button.setSelected(false);
            });
        });
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        //
        this.scrolledContainer.render(graphics, mouseX, mouseY, partialTicks);
    }

    /**
     * 取消播放点击声音
     */
    @Override
    public void playDownSound(SoundManager manager) {
    }

    public Collection<? extends GuiEventListener> children() {
        List<VidaWidget> listeners = new ArrayList<>();
        this.widgetMap.forEach((key, value) -> listeners.addAll(value));
        listeners.add(this.scrolledContainer);
        return listeners;
    }

    @Override
    public Collection<VidaWidget> childrenNode() {
        List<VidaWidget> widgets = new ArrayList<>();
        widgets.add(scrolledContainer);
        for(List<MagicWordButton> buttons : widgetMap.values()){
            widgets.addAll(buttons);
        }
        return widgets;
    }
}
