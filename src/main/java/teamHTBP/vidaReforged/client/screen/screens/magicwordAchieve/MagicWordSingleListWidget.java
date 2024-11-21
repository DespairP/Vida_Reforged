package teamHTBP.vidaReforged.client.screen.screens.magicwordAchieve;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.screen.components.VidaLifecycleSection;
import teamHTBP.vidaReforged.client.screen.components.common.MagicWordButton;
import teamHTBP.vidaReforged.client.screen.components.common.ScrolledContainer;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaViewMagicWordViewModel;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.system.magicWord.MagicWord;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;
import teamHTBP.vidaReforged.server.providers.MagicWordManager;

import java.util.*;

public class MagicWordSingleListWidget extends VidaLifecycleSection {
    VidaViewMagicWordViewModel viewModel;
    public final Map<VidaElement, List<MagicWordButton>> widgetMap;
    public ScrolledContainer scrolledContainer;
    public MagicWordButton.ClickListener clickListener = (element, magicId) -> this.viewModel.setSelectWord(magicId);

    public MagicWordSingleListWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Component.literal("magic word single list"));
        this.viewModel = new ViewModelProvider(requireParent()).get(VidaViewMagicWordViewModel.class);
        this.widgetMap = new LinkedHashMap<>();
        initWidget();
    }



    public void initWidget(){
        scrolledContainer = new ScrolledContainer(getX(), getY(), this.width, this.height, new ResourceLocation(VidaReforged.MOD_ID, "magic_word_list"));

        for(VidaElement element : VidaElement.values()){
            widgetMap.put(element,new LinkedList<>());
        }

        int offset = 0;

        for(MagicWord word : MagicWordManager.getAllMagicWords()){
            int i = offset ++;
            int x = 0;
            int y = i * MagicWordButton.HEIGHT;
            int offsetX = 5;
            int offsetY = i  * 10;
            boolean isUnLocked = this.viewModel.playerMagicWords.getValue().contains(word.name());

            MagicWordButton magicWordWidget = new MagicWordButton(getX() + x + offsetX, getY() + y + offsetY, width - 10, MagicWordButton.HEIGHT, word.name(), new ResourceLocation(VidaReforged.MOD_ID, word.name()));
            magicWordWidget.setOnClickListener(clickListener);
            magicWordWidget.setLocked(!isUnLocked);

            widgetMap.get(word.element()).add(magicWordWidget);
            scrolledContainer.add(magicWordWidget);
        }

        this.viewModel.selectedMagicWord.observeForever(this::onSelectedMagicWordChange);
        this.viewModel.setSelectWord(this.viewModel.selectedMagicWord.getValue());
    }

    public void onSelectedMagicWordChange(String magicId){
        List<MagicWordButton> allWidget = getMagicWordButtons();
        allWidget.forEach(widget -> {
            if(Objects.equals(widget.getMagicWordId(), magicId)){
                widget.setSelected(true);
                return;
            }
            widget.setSelected(false);
        });
    }



    public List<MagicWordButton> getMagicWordButtons(){
        List<MagicWordButton> allWidget = widgetMap.values().stream().flatMap(Collection::stream).toList();
        return allWidget;
    }


    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        scrolledContainer.render(graphics, mouseX, mouseY, partialTicks);
    }




    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

    }

    public Collection<? extends GuiEventListener> children(){
        List<VidaWidget> listeners = new ArrayList<>();
        this.widgetMap.forEach((key,value)-> listeners.addAll(getMagicWordButtons()));
        listeners.add(scrolledContainer);
        return listeners;
    }

    @Override
    public void playDownSound(SoundManager p_93665_) {

    }
}
