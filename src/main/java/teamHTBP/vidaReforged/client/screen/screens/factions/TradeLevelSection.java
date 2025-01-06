package teamHTBP.vidaReforged.client.screen.screens.factions;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.screen.components.VidaLifecycleSection;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;
import teamHTBP.vidaReforged.client.screen.components.common.ItemStackWidget;
import teamHTBP.vidaReforged.client.screen.components.faction.TradeItemPanel;
import teamHTBP.vidaReforged.client.screen.components.faction.TradeSelectButton;
import teamHTBP.vidaReforged.core.common.ui.layouts.GridLayoutMutation;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeLevelSection extends VidaLifecycleSection {
    private Map<Integer, List<ItemStack>> tradeItems = new HashMap<>();
    private Map<Integer, List<VidaWidget>> itemButtons = new HashMap<>();
    private TradeItemPanel panel;
    private GridLayoutMutation layout;
    private ItemStack mockItem$0 = new ItemStack(Items.REDSTONE);
    private ItemStack mockItem$1 = new ItemStack(Items.SPLASH_POTION);


    public TradeLevelSection(int x, int y, int width, int height, Component component, ResourceLocation componentId) {
        super(x, y, width, height, component, componentId);
    }

    @Override
    public void onAdded() {
        this.layout = new GridLayoutMutation(getX(), getY());
        //
        for (int i = 1; i <= 3; i++){
            itemButtons.put(i, new ArrayList<>());
        }
        //
        GridLayoutMutation subLayout = new GridLayoutMutation(0, 0);
        for (int i = 0; i < 6; i++) {
            TradeSelectButton button = new TradeSelectButton(0, 0, 12, 16, Component.literal(""), new ResourceLocation(VidaReforged.MOD_ID, "btn_" + i), mockItem$0);
            subLayout.addChild(button, i / 5, i % 5, layout.defaultCellSetting().padding(3, 1));
            itemButtons.get(1).add(button);
        }
        layout.addChild(subLayout, 0, 0);

        subLayout = new GridLayoutMutation(0, 0);
        for (int i = 0; i < 6; i++) {
            TradeSelectButton button = new TradeSelectButton(0, 0, 12, 19, Component.literal(""), new ResourceLocation(VidaReforged.MOD_ID, "btn" + i), mockItem$1);
            subLayout.addChild(button, i / 5, i % 5, layout.defaultCellSetting().padding(3, 1));
            itemButtons.get(2).add(button);
        }
        //
        int panelWidth = (int) (width * 1.0f/ 3.0f);
        panel = new TradeItemPanel(0, 0, panelWidth, (int) (panelWidth / 1.6f), Component.empty());
        layout.addChild(subLayout, 1, 0);
        layout.arrangeElements();
    }

    @Override
    protected void onResized(int width, int height) {
        int panelWidth = (int) (width * 1.3f/ 3.0f);
        panel.setHeight((int) (panelWidth * 1.6f));
        panel.setWidth(panelWidth);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        //
        FrameLayout.alignInRectangle(panel, getX(), getY(), getWidth(), getHeight(), 0.9F,0.5f);
        //
        layout.arrangeElements();
        panel.render(graphics, mouseX, mouseY, partialTicks);
        for(Integer level : itemButtons.keySet()){
            itemButtons.get(level).forEach(button -> button.render(graphics, mouseX, mouseY, partialTicks));
        }
        //VidaGuiHelper.fillGradient(graphics, getX(), getY(), getX() + getWidth(), getY() + getHeight(), 100f, 0xffffffff, 0xffffffff);
    }

    @Override
    protected void onPositionMoved(int x, int y) {
        this.layout.setX(x);
        this.layout.setY(y);
    }


}
