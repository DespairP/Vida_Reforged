package teamHTBP.vidaReforged.client.screen.components.magicWords;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import org.joml.Vector2f;
import teamHTBP.vidaReforged.client.screen.components.common.VidaWidget;
import teamHTBP.vidaReforged.client.screen.screens.common.VidaContainerScreen;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaMagicWordViewModel;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.client.screen.components.VidaLifecycleSection;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;
import teamHTBP.vidaReforged.core.common.ui.lifecycle.IDefaultLifeCycleObserver;
import teamHTBP.vidaReforged.core.common.ui.lifecycle.ILifeCycleOwner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class MagicSelectedWordContainer extends VidaLifecycleSection {
    /**容器大小*/
    public static final int WIDTH = 80;
    /**容器大小*/
    public static final int HEIGHT = 78;
    /**父组件*/
    public final VidaContainerScreen<?> screen;
    /**子组件位置*/
    private final Map<VidaElement, Vector2f> offsetMap = ImmutableMap.of(
            VidaElement.GOLD, new Vector2f(33, 1),
            VidaElement.WOOD, new Vector2f(1, 25),
            VidaElement.AQUA, new Vector2f(65, 25),
            VidaElement.FIRE, new Vector2f(14, 60),
            VidaElement.EARTH, new Vector2f(52, 60)
    );
    /**子组件*/
    private Map<VidaElement, MagicSelectedWordWidget> widgetMap = new LinkedHashMap<>();
    /**ViewModel*/
    private VidaMagicWordViewModel model;


    public MagicSelectedWordContainer(VidaContainerScreen<?> screen, int x, int y) {
        super(x, y, WIDTH, HEIGHT, Component.literal("selected_magic_words"));
        this.screen = screen;
        screen.getLifeCycle().addObserver(new IDefaultLifeCycleObserver() {
            @Override
            public void onStart(ILifeCycleOwner owner) {
                onInit();
            }

            @Override
            public void onPause(ILifeCycleOwner owner) {
                screen.getLifeCycle().removeObserver(this);
            }
        });
    }

    public void onInit(){
        this.model = new ViewModelProvider(requireParent()).get(VidaMagicWordViewModel.class);
        this.model.selectedMagicWord.observe(this, (newValue) -> {
            for(VidaElement element : newValue.keySet()){
                widgetMap.get(element).setSelectWordId(newValue.get(element));
            }
        });
        for(VidaElement element : offsetMap.keySet()){
            Vector2f offsetXY = offsetMap.get(element);
            widgetMap.put(element, new MagicSelectedWordWidget(
                    getX() + (int) offsetXY.x,
                    getY() + (int) offsetXY.y,
                    element,
                    this::onMagicWordClick
            ));
        }
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        widgetMap.forEach((element, magicSelectedWordWidget) -> magicSelectedWordWidget.render(graphics, mouseX, mouseY, partialTicks));
    }

    public Collection<? extends GuiEventListener> children(){
        return this.widgetMap.values();
    }

    @Override
    public Collection<VidaWidget> childrenNode() {
        return new ArrayList<>(this.widgetMap.values());
    }

    public void onMagicWordClick(VidaElement element) {
        this.model.selectedFilterElement.setValue(element);
    }
}
