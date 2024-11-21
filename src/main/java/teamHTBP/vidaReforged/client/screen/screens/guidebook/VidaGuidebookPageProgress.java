package teamHTBP.vidaReforged.client.screen.screens.guidebook;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;
import teamHTBP.vidaReforged.client.screen.components.guidebooks.VidaGuidebookListItemProgressButton;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaGuidebookListPageViewModel;
import teamHTBP.vidaReforged.core.common.system.guidebook.VidaPageListItem;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;
import teamHTBP.vidaReforged.server.providers.VidaGuidebookManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VidaGuidebookPageProgress extends VidaWidget {
    /**X偏移*/
    protected int scrollX = 0;
    /**Y偏移*/
    protected int scrollY = 0;
    /***/
    private List<VidaGuidebookListItemProgressButton> buttonList = new ArrayList<>();
    private Map<ResourceLocation, VidaGuidebookListItemProgressButton> buttonMap = new HashMap<>();
    VidaGuidebookListPageViewModel model;

    public VidaGuidebookPageProgress(int x, int y, int width, int height, Component component) {
        super(x, y, width, height, component);
        init();
    }

    void init(){
        model = new ViewModelProvider(requireParent()).get(VidaGuidebookListPageViewModel.class);

        model.pageList.observe(requireParent(), value -> {
            ResourceLocation listId = value.getId();
            List<VidaPageListItem> listItems = VidaGuidebookManager.LIST_ITEM_MAP.values().stream().filter(item -> item.getList().equals(listId)).toList();
            buttonList.addAll(listItems.stream().map(item -> {
                VidaGuidebookListItemProgressButton button = new VidaGuidebookListItemProgressButton(item);
                buttonMap.put(item.getId(), button);

                return button;
            }).toList());
        });
    }


    /**渲染进度按钮和连接线*/
    void renderIcons(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        buttonMap.forEach((id, button) -> {
            VidaPageListItem item = button.item;
            if(item.getParents() == null){
                return;
            }
            for(VidaPageListItem parentItem : item.getParents()){
                var parentButton = buttonMap.get(parentItem.getId());
                if(parentButton == null){
                    continue;
                }
                drawConnectivity(graphics, 0, 0, parentButton.getX() + 12, parentButton.getY() + 12, button.getX() + 12,button.getY() + 12, true);
            }
        });

        buttonList.forEach(button -> {
            button.setOffset(scrollX, scrollY);
            button.renderWidget(graphics, mouseX, mouseY, partialTicks);
        });



    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.enableBlend();
        VidaGuiHelper.renderScissor(getX(), getY(), mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight());

        renderIcons(graphics, mouseX, mouseY, partialTicks);

        RenderSystem.disableScissor();
        RenderSystem.disableBlend();
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        if(isFocused()) {
            this.scrollX += dragX;
            this.scrollY += dragY;
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if(isMouseOver(mouseX, mouseY)){
            setFocused(true);
        }
    }

    @Override
    public void onRelease(double p_93669_, double p_93670_) {
        setFocused(false);
    }

    /**渲染连接线*/
    public void drawConnectivity(GuiGraphics graphics, int scrollX, int scrollY, int fromX, int fromY , int toX, int toY, boolean p_97302_) {
        int i = scrollX + toX;
        int j = scrollX + toX  + 4;
        int k = scrollY + toY ;
        int l = scrollX + fromX ;
        int i1 = scrollY + fromY;
        int j1 = p_97302_ ? 0xAC52231A : 0xACB87867;
        if (p_97302_) {
            graphics.hLine(j, i, k - 1, j1);
            graphics.hLine(j + 1, i, k, j1);
            graphics.hLine(j, i, k + 1, j1);
            graphics.hLine(l, j - 1, i1 - 1, j1);
            graphics.hLine(l, j - 1, i1, j1);
            graphics.hLine(l, j - 1, i1 + 1, j1);
            graphics.vLine(j - 1, i1, k, j1);
            graphics.vLine(j + 1, i1, k, j1);
        } else {
            graphics.hLine(j, i, k, j1);
            graphics.hLine(l, j, i1, j1);
            graphics.vLine(j, i1, k, j1);
        }

        graphics.hLine(fromX + scrollX, toX + scrollX, toY + scrollY, j1);
    }
}
