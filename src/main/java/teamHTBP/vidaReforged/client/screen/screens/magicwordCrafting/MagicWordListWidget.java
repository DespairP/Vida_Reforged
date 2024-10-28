package teamHTBP.vidaReforged.client.screen.screens.magicwordCrafting;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import teamHTBP.vidaReforged.client.screen.components.common.MagicWordButton;
import teamHTBP.vidaReforged.client.screen.components.common.ScrolledContainer;
import teamHTBP.vidaReforged.client.screen.components.common.VidaWidget;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaMagicWordViewModel;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.hud.IVidaNodes;
import teamHTBP.vidaReforged.core.common.system.magicWord.MagicWord;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;
import teamHTBP.vidaReforged.server.providers.MagicWordManager;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * VidaWandCraftingTableScreen子组件
 * 用于显示右边的词条
 * */
public class MagicWordListWidget extends AbstractWidget implements IVidaNodes {
    public final static int WIDTH = 180;
    public final Map<VidaElement, List<MagicWordButton>> widgetMap;
    public VidaElement currentSelectedElement = VidaElement.GOLD;
    public ScrolledContainer<MagicWordButton> scrolledContainer;
    private VidaMagicWordViewModel model;
    MagicWordButton.ClickListener clickListener = (element, magicId)->{
        this.model.setSelectWord(element, magicId);
    };


    public MagicWordListWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Component.literal("Magic Word List"));
        this.widgetMap = new LinkedHashMap<>();
        this.initWidget();
    }

    public void initWidget(){
        this.model = new ViewModelProvider(requireParent()).get(VidaMagicWordViewModel.class);

        //
        this.scrolledContainer = new ScrolledContainer<>(getX(), getY(), this.width, this.height);

        //
        for(VidaElement element : VidaElement.values()){
            widgetMap.put(element,new LinkedList<>());
        }

        Map<VidaElement,AtomicInteger> offset = ImmutableMap.of(
                VidaElement.GOLD, new AtomicInteger(0),
                VidaElement.WOOD, new AtomicInteger(0),
                VidaElement.AQUA, new AtomicInteger(0),
                VidaElement.FIRE, new AtomicInteger(0),
                VidaElement.EARTH, new AtomicInteger(0)
        );

        // 开始初始化词条按钮
        for (int k = 0; k < 10; k++) {
        for(MagicWord word : MagicWordManager.getAllMagicWords()){
            // 元素内第n个词条
            int i = offset.getOrDefault(word.element(), new AtomicInteger(0)).getAndIncrement();

            int x = (i % 2) * MagicWordButton.WIDTH;
            int y = (int)Math.floor(i / 2.0f) * MagicWordButton.HEIGHT;
            int offsetX = (i % 2) * 5;
            int offsetY = (int)Math.floor(i / 2.0f) * 10;
            boolean isUnLocked = Optional.ofNullable(this.model.playerMagicWords.getValue()).orElse(new ArrayList<>()).contains(word.name());

            MagicWordButton button = new MagicWordButton(
                    getX() + x + offsetX,
                    getY() + y + offsetY,
                    MagicWordButton.WIDTH,
                    MagicWordButton.HEIGHT,
                    word.name()
            );
            button.setOnClickListener(clickListener);
            button.setLocked(!isUnLocked);
            button.setVisible(false);

            widgetMap.get(word.element()).add(button);
        }}


        // 初始化过滤器
        this.currentSelectedElement = this.model.selectedFilterElement.getValue();
        this.model.selectedFilterElement.observeForever(this::onFilterChange);

        // 设置
        this.model.selectedMagicWord.observeForever(this::onMagicWordChange);

        this.onFilterChange(this.currentSelectedElement);
        this.onMagicWordChange(this.model.selectedMagicWord.getValue());
    }

    protected void onFilterChange(VidaElement newValue){
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
    }


    protected void onMagicWordChange(Map<VidaElement, String> newValue){
        this.widgetMap.forEach((element, magicWordButtons) -> {
            String selectedWordId = newValue.get(element);

            magicWordButtons.forEach(button ->{
                if(Objects.equals(selectedWordId, button.getMagicWordId())){
                    button.setSelected(true);
                    return;
                }
                button.setSelected(false);
            });

        });
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        // 背景
        this.scrolledContainer.render(graphics, mouseX, mouseY, partialTicks);
    }


    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {}



    /**取消播放点击声音*/
    @Override
    public void playDownSound(SoundManager manager) {}

    public Collection<? extends GuiEventListener> children(){
        List<VidaWidget> listeners = new ArrayList<>();
        this.widgetMap.forEach( (key,value)-> listeners.addAll( value ) );
        listeners.add(this.scrolledContainer);
        return listeners;
    }
}
