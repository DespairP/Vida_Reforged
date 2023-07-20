package teamHTBP.vidaReforged.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import teamHTBP.vidaReforged.client.screen.components.MagicWordFilter;
import teamHTBP.vidaReforged.client.screen.components.MagicWordFilterList;
import teamHTBP.vidaReforged.client.screen.components.MagicWordListWidget;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaMagicWordViewModel;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.server.blockEntities.MagicWordCraftingTableBlockEntity;
import teamHTBP.vidaReforged.server.menu.MagicWordCraftingTableMenu;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class MagicWordCraftingTableScreen extends AbstractContainerScreen<MagicWordCraftingTableMenu> {
    MagicWordListWidget magicWordWidget;
    MagicWordFilterList magicWordFilterLists;
    final VidaMagicWordViewModel viewModel;
    final Inventory inventory;

    private final static ResourceLocation INVENTORY_LOCATION = new ResourceLocation(MOD_ID, "textures/gui/magic_word_inventory.png");

    public MagicWordCraftingTableScreen(MagicWordCraftingTableMenu menu, Inventory inventory, Component p_97743_) {
        super(menu, inventory, Component.translatable("magic_word_crafting_table"));
        viewModel = new VidaMagicWordViewModel();
        viewModel.blockPos.setValue(menu.getBlockPos());
        viewModel.playerMagicWords.setValue(getMenu().getPlayerMagicWords());
        this.inventory = inventory;
    }

    @Override
    protected void init() {
        // 200 is right panel, 20 is button list
        this.leftPos = (Minecraft.getInstance().getWindow().getGuiScaledWidth() - 200 - 20 - 196) / 2;
        this.topPos = (Minecraft.getInstance().getWindow().getGuiScaledHeight() - 84);
        double factor = minecraft.getWindow().getGuiScale();
        int screenWidth = this.width;
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();
        int componentHeight = (int) (screenHeight - 28);
        int x = screenWidth - 200;
        int y = (int)((screenHeight - componentHeight) / 2);

        magicWordWidget = new MagicWordListWidget( viewModel, x, y, 0, componentHeight, factor);
        magicWordFilterLists = new MagicWordFilterList(viewModel, x - MagicWordFilter.PIXEL, y + componentHeight - MagicWordFilter.PIXEL * MagicWordFilterList.BUTTON_AMOUNT);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {

    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        this.renderInventory(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics,mouseX,mouseY);
        this.compareServerAndClientSideAndUpdate();
        renderMagicWords(graphics, mouseX, mouseY, partialTicks);
    }

    public void renderInventory(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        int startX = this.getGuiLeft();
        int startY = this.getGuiTop();


        TextureSection section = new TextureSection(INVENTORY_LOCATION,0,0,176,90);

        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate(0, -8f,0);
        graphics.blit(
                section.location(),
                startX, startY, 0,
                section.minU(), section.minV(),
                section.w(), section.h(),
                section.w(), section.h()
        );
        poseStack.popPose();
    }

    /**对比服务端数据和客户端数据，如果不一样，立即更换*/
    public void compareServerAndClientSideAndUpdate(){
        Map<VidaElement,String> clientMap = this.viewModel.selectedMagicWord.getValue();
        Map<VidaElement,String> serverMap = new LinkedHashMap<>();
        BlockPos pos = getMenu().getBlockPos();
        if(inventory.player.getCommandSenderWorld().getBlockEntity(pos) instanceof MagicWordCraftingTableBlockEntity entity){
            serverMap = entity.getMagicWordMap();
        }
        boolean flag = false;
        // client -> server
        for(VidaElement element : clientMap.keySet()){
            String serverWord = serverMap.get(element);
            String clientWord = clientMap.get(element);
            if(!compareString(serverWord,clientWord)){
                flag = true;
                break;
            }
        }

        // server -> client
        for(VidaElement element : serverMap.keySet()){
            String serverWord = serverMap.get(element);
            String clientWord = clientMap.get(element);
            if(!compareString(serverWord,clientWord)){
                flag = true;
                break;
            }
        }

        if(flag){
            // 更新但是不发送数据包
            HashMap<VidaElement,String> newMagicWordMap = new HashMap<>(serverMap);
            this.viewModel.selectedMagicWord.setValue(newMagicWordMap);
        }
    }

    public static boolean compareString(String str1, String str2) {
        return (str1 == null ? str2 == null : str1.equals(str2));
    }

    public void renderMagicWords(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        if(magicWordWidget != null){
            magicWordWidget.render(graphics, mouseX, mouseY, partialTicks);
            magicWordFilterLists.render(graphics, mouseX, mouseY, partialTicks);
        }

    }



    @Override
    public boolean mouseScrolled(double p_94686_, double p_94687_, double p_94688_) {
        return super.mouseScrolled(p_94686_, p_94687_, p_94688_);
    }

    @Override
    public List<? extends GuiEventListener> children() {
        List<GuiEventListener> listeners = (List<GuiEventListener>) super.children();
        listeners.addAll(this.magicWordFilterLists.getChildren());
        listeners.add(this.magicWordWidget);
        listeners.addAll(this.magicWordWidget.getChildren());
        return listeners;
    }

    @Override
    protected void clearWidgets() {
        this.viewModel.selectedMagicWord.clearObservers();
        this.viewModel.selectedFilterElement.clearObservers();
        super.clearWidgets();
    }
}
