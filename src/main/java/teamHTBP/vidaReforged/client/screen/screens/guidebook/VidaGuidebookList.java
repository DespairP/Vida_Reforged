package teamHTBP.vidaReforged.client.screen.screens.guidebook;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.client.screen.components.common.VidaWidget;
import teamHTBP.vidaReforged.client.screen.components.guidebooks.VidaGuidebookListItemButton;
import teamHTBP.vidaReforged.client.screen.components.guidebooks.VidaGuidebookPagination;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaGuidebookListPageViewModel;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaScreenEventChannelViewModel;
import teamHTBP.vidaReforged.core.common.system.guidebook.VidaScreenEvent;
import teamHTBP.vidaReforged.core.common.system.guidebook.VidaPageListItem;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VidaGuidebookList extends VidaGuidebookPage{
    VidaGuidebookListPageViewModel pageModel;
    VidaScreenEventChannelViewModel bookModel;
    List<VidaWidget> children = new LinkedList<>();
    public final static int ITEMS_COUNT_PER_LIST = 10;
    private VidaGuidebookPagination paginationCmp;

    public VidaGuidebookList(int x, int y, int width, int height, Component component) {
        super(x, y, width, height, component);
        this.pageModel = new ViewModelProvider(requireParent()).get(VidaGuidebookListPageViewModel.class);
        this.bookModel = new ViewModelProvider(requireParent()).get(VidaScreenEventChannelViewModel.class);
        this.initComponent();
    }

    @Override
    public void onInit() {

    }

    public void initComponent() {
        this.pageModel.pageList.observe(this, value -> {
            initChildren();
        });
        initChildren();
        paginationCmp = new VidaGuidebookPagination(getX(), getY(), VidaGuidebookListScreen.GUIDEBOOK_SECTION.width() - 15, 30, Component.literal("pagination" ));
    }


    public void initChildren(){
        children.clear();
        List<VidaPageListItem> childItems = this.pageModel.getPageItems();
        for (int i = 0; i < childItems.size(); i++) {
            int xi = 0;
            int yi = (i % ITEMS_COUNT_PER_LIST) * 20;
            VidaPageListItem item = childItems.get(i);
            this.children.add(new VidaGuidebookListItemButton(xi, yi, getWidth() - 20, 20, Component.literal(""), item, this::onChildrenClick));
        }
    }

    public void onChildrenClick(VidaScreenEvent event, ResourceLocation id){
        this.bookModel.pushMessage(event);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        for (VidaWidget child : this.children) {
            child.setOffset(getX(), getY());
            child.render(graphics, mouseX, mouseY, partialTicks);
        }
        paginationCmp.setOffset(getX(), getY() + VidaGuidebookListScreen.GUIDEBOOK_SECTION.h() - 47);
        paginationCmp.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public List<GuiEventListener> children() {
        return new ArrayList<>(this.children);
    }
}
