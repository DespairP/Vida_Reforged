package teamHTBP.vidaReforged.client.screen.screens.wandCrafting;

import com.google.gson.JsonObject;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import teamHTBP.vidaReforged.client.screen.components.common.DisplayBlock;
import teamHTBP.vidaReforged.client.screen.components.common.IconButton;
import teamHTBP.vidaReforged.client.screen.components.common.VidaWidget;
import teamHTBP.vidaReforged.client.screen.components.guidebooks.VidaGuidebookDisplaySection;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaScreenEventChannelViewModel;
import teamHTBP.vidaReforged.core.common.system.guidebook.VidaScreenEvent;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VidaWandSelectSection extends VidaWandCraftSection {
    IconButton equipButton;
    IconButton statsButton;
    IconButton magicButton;
    DescribeInfo info;
    GridLayout gridLayout;

    public VidaWandSelectSection(int x, int y, int width, int height, Component component) {
        super(x, y, width, height, component);
        add();
    }

    private void add() {
        equipButton = new IconButton.Builder().width(getWidth() / 2  - 60).isBackground(true).height(80).listener(() -> open("equipment")).message(Component.translatable("gui.vida_reforged.vida_wand_crafting.equipment_section_choose")).build(getX(), getY());
        statsButton = new IconButton.Builder().width(getWidth() / 2  - 60).height(80).message(Component.translatable("gui.vida_reforged.vida_wand_crafting.stats_section_choose")).isBackground(true).build(getX(), getY());
        magicButton = new IconButton.Builder().width(getWidth() / 2  - 60).isBackground(true).height(80).listener(() -> open("magic")).message(Component.translatable("gui.vida_reforged.vida_wand_crafting.magic_section_choose")).build(getX(), getY());
        info = new DescribeInfo(getX() + getWidth() / 2 + 60, 0, getWidth() / 2, getHeight(), Component.literal("hovered infos"));
    }

    private void resize(){
        this.gridLayout = new GridLayout(getX(), 0);
        this.equipButton.setHeight(getHeight() / 3);
        this.statsButton.setHeight(getHeight() / 3);
        this.magicButton.setHeight(getHeight() / 3);
        this.equipButton.setWidth(getWidth() / 2 - 60);
        this.statsButton.setWidth(getWidth() / 2 - 60);
        this.magicButton.setWidth(getWidth() / 2 - 60);
        this.gridLayout.addChild(equipButton, 0, 0);
        this.gridLayout.addChild(statsButton, 1, 0);
        this.gridLayout.addChild(magicButton, 2, 0);
        this.gridLayout.arrangeElements();
        this.equipButton.refresh();
        this.statsButton.refresh();
        this.magicButton.refresh();
    }

    @Override
    public void setX(int x) {
        if(x != getX()){
            super.setX(x);
            resize();
        }
        info.setWidth(getWidth() / 2);
        info.setX(getX() + equipButton.getWidth());
    }

    @Override
    public void setY(int y) {
        if(y != getY()){
            super.setY(y);
            resize();
        }
    }

    @Override
    public void setHeight(int height) {
        if(height != getHeight()){
            super.setHeight(height);
            resize();
            info.setHeight(height);
        }
    }

    @Override
    public void setWidth(int width) {
        if(width != getWidth()){
            super.setWidth(width);
            resize();
            info.setWidth(getWidth() / 2);
            info.setX(getX() + equipButton.getWidth());
        }
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.gridLayout.arrangeElements();
        equipButton.render(graphics, mouseX, mouseY, partialTicks);
        statsButton.render(graphics, mouseX, mouseY, partialTicks);
        magicButton.render(graphics, mouseX, mouseY, partialTicks);
        if(equipButton.isHovered()){
            info.setType(DescribeType.EQUIP);
        }
        if(statsButton.isHovered()){
            info.setType(DescribeType.STATS);
        }
        if(magicButton.isHovered()){
            info.setType(DescribeType.MAGIC);
        }
        info.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        this.equipButton.setVisible(visible);
        this.statsButton.setVisible(visible);
        this.magicButton.setVisible(visible);
        this.equipButton.refresh();
        this.statsButton.refresh();
        this.magicButton.refresh();
    }

    public void open(String place){
        JsonObject object = new JsonObject();
        object.addProperty("place", place );
        VidaScreenEventChannelViewModel channelViewModel = new ViewModelProvider(requireParent()).get(VidaScreenEventChannelViewModel.class);
        channelViewModel.pushMessage(new VidaScreenEvent("open", object));
    }

    @Override
    public void onClick(double x, double y) {

    }

    @Override
    public Collection<? extends GuiEventListener> children() {
        if(!this.visible){
            return List.of();
        }
        return List.of(equipButton, statsButton, magicButton);
    }


    public static class DescribeInfo extends VidaWidget {
        Map<DescribeType, DisplayBlock> descriptions = new HashMap<>();
        DescribeType type = DescribeType.NONE;

        public void setType(DescribeType type) {
            this.type = type;
        }

        /**设置子组件高度*/
        @Override
        public void setWidth(int width) {
            super.setWidth(width);
            for(DisplayBlock description : descriptions.values()){
                description.setWidth(width - 20);
            }
        }

        public DescribeInfo(int x, int y, int width, int height, Component component) {
            super(x, y, width, height, component);
            this.descriptions.put(DescribeType.EQUIP, new DisplayBlock(getX(), getY(), getWidth(), 60, new ItemStack(Items.ANVIL.asItem()), DescribeType.EQUIP.component));
            this.descriptions.put(DescribeType.STATS, new DisplayBlock(getX(), getY(), getWidth(), 60, new ItemStack(Items.GLOWSTONE.asItem()), DescribeType.STATS.component));
            this.descriptions.put(DescribeType.MAGIC, new DisplayBlock(getX(), getY(), getWidth(), 60, new ItemStack(Items.WHITE_WOOL.asItem()), DescribeType.MAGIC.component));
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            if(type == DescribeType.NONE){
                return;
            }
            for(DescribeType type : descriptions.keySet()){
                DisplayBlock description = this.descriptions.get(type);
                description.setVisible(this.type == type);
                FrameLayout.alignInRectangle(description, getX(), getY(), getWidth(), getHeight(), 0.5f, 0.5f);
                description.render(graphics, mouseX, mouseY, partialTicks);
            }

        }
    }

    /**当按钮被悬浮时，出现的介绍类型*/
    public enum DescribeType{
        /**饰品装备*/
        EQUIP(Component.translatable("gui.vida_reforged.vida_wand_crafting.equipment_section_description")),
        /**法杖状态*/
        STATS(Component.translatable("gui.vida_reforged.vida_wand_crafting.stats_section_description")),
        /**魔法设置*/
        MAGIC(Component.translatable("gui.vida_reforged.vida_wand_crafting.magic_section_description")),
        /**没有悬浮在任何按钮上*/
        NONE(Component.empty());

        /**介绍文字*/
        private Component component;

        DescribeType(Component component){
            this.component = component;
        }

        public Component getComponent() {
            return component;
        }
    }
}
