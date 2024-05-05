package teamHTBP.vidaReforged.client.screen.screens.guidebook;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import teamHTBP.vidaReforged.client.screen.components.guidebooks.IVidaGuidebookComponent;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaGuidebookDetailPageViewModel;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaScreenEventChannelViewModel;
import teamHTBP.vidaReforged.core.common.system.guidebook.IVidaPageComponent;
import teamHTBP.vidaReforged.core.common.system.guidebook.VidaPageBaseComponent;
import teamHTBP.vidaReforged.core.common.system.guidebook.VidaPageComponents;
import teamHTBP.vidaReforged.core.common.system.guidebook.VidaPageDetail;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class VidaGuidebookPageDetail extends VidaGuidebookPage{
    final VidaPageDetail detail;
    final List<IVidaGuidebookComponent> widgets;
    private GridLayout gridLayout;
    private FrameLayout frameLayout;
    private FrameLayout detailedFrameLayout;
    final List<IVidaGuidebookComponent> detailWidgets;
    VidaGuidebookDetailPageViewModel detailModel;
    VidaScreenEventChannelViewModel pageModel;


    public VidaGuidebookPageDetail(int x, int y, int width, int height, Component component, VidaPageDetail detail) {
        super(x, y, width, height, component);
        this.detail = detail;
        widgets = new ArrayList<>();
        detailWidgets = new ArrayList<>();
        this.initComponent();
    }

    @Override
    public void onInit() {

    }

    public void initComponent(){
        detailModel = new ViewModelProvider(requireParent()).get(VidaGuidebookDetailPageViewModel.class);
        pageModel = new ViewModelProvider(requireParent()).get(VidaScreenEventChannelViewModel.class);

        detailModel.setCurrent(detail);
        detailModel.paginationMap.observe(this, newValue -> initComponent(newValue.get(detail.getId())));

        initComponent(detailModel.getPage(detail.getId()));
    }

    private void initComponent(int page){
        widgets.clear();
        detailWidgets.clear();

        this.gridLayout = new GridLayout(0, 0);
        this.frameLayout = new FrameLayout(0, 0,  width - 20, height - 20);
        this.detailedFrameLayout = new FrameLayout(0, 0,  width - 20, height - 20);

        VidaPageComponents pageComponents = detail.getPages().get(page - 1);
        for (int i = 0; i < pageComponents.getComponents().size(); i++){
            IVidaPageComponent component = pageComponents.getComponents().get(i);
            IVidaGuidebookComponent cmp = component.generateComponent();
            widgets.add(cmp);
            gridLayout.addChild(cmp, i, 0, gridLayout.defaultCellSetting().alignHorizontallyCenter());
            frameLayout.addChild(gridLayout, frameLayout.defaultChildLayoutSetting().alignHorizontallyCenter().alignVerticallyTop());
        }

        for(IVidaPageComponent component : detail.getDetailComponents()){
            IVidaGuidebookComponent cmp = component.generateComponent();
            if(component instanceof VidaPageBaseComponent componentData){
                detailedFrameLayout.addChild(cmp, frameLayout.newChildLayoutSettings().alignHorizontallyLeft().padding(componentData.x, componentData.y));
            }
            detailWidgets.add(cmp);
        }
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        gridLayout.setX(getX());
        gridLayout.setY(getY());
        frameLayout.setX(getX());
        frameLayout.setY(getY());
        detailedFrameLayout.setX(getX());
        detailedFrameLayout.setY(getY());
        gridLayout.arrangeElements();
        frameLayout.arrangeElements();
        detailedFrameLayout.arrangeElements();

        widgets.forEach(widget -> widget.render(graphics, mouseX, mouseY, partialTicks));
        detailWidgets.forEach(widget -> widget.render(graphics, mouseX, mouseY, partialTicks));
    }

    @Override
    public List<GuiEventListener> children() {
        List<GuiEventListener> listeners = new ArrayList<>();
        for(IVidaGuidebookComponent cmp : this.detailWidgets){
            listeners.addAll(cmp.children());
        }
        listeners.add(this);
        return listeners;
    }
}
