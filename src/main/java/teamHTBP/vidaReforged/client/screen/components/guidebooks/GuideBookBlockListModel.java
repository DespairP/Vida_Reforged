package teamHTBP.vidaReforged.client.screen.components.guidebooks;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Deprecated
public class GuideBookBlockListModel extends GuideBookBlockModel{
    List<ResourceLocation> blockList;
    int index = 0;
    Button leftButton;
    Button rightButton;

    public GuideBookBlockListModel(int x, int y, int width, int height, List<ResourceLocation> blockList) {
        super(x, y, width, height, blockList == null || blockList.isEmpty() ? new ResourceLocation("minecraft:air") : blockList.get(0));
        this.blockList = blockList;
        this.leftButton = new Button(
                ()-> index = Math.max(index - 1, 0),
                (int) (getX() + (getWidth() - 80) / 2.0F) - 8,
                (int) ((getY() - 16.0F + getHeight()) * 2.7F / 3.0F),
                Component.literal("<")
        );
        this.rightButton = new Button(
                ()-> index = Math.min(index + 1, blockList.size() -1),
                (int) (getX() + (getWidth() - 80) / 2.0F) + 80,
                (int) ((getY() - 16.0F + getHeight()) * 2.7F / 3.0F),
                Component.literal(">")
        );
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int modelX = (int) (getX() + (getWidth() - 16.0F) / 2.0F);
        int modelY = (int) ((getY() - 16.0F + getHeight()) * 1.3F / 3.0F);
        int textX = (int) (getX() + (getWidth() - 80) / 2.0F);
        int textY = (int) ((getY() - 16.0F + getHeight()) * 2.7F / 3.0F);

        renderBg(graphics, getX(), getY(), getWidth(), getHeight(), partialTicks);
        Block block = ForgeRegistries.BLOCKS.getValue(blockList.get(index));
        renderBlockWithState(graphics, new ItemStack(block), modelX, modelY,partialTicks);
        renderBlockText(graphics, block.getName().withStyle(style -> style.withBold(true)), textX, textY, partialTicks);
        this.leftButton.render(graphics, mouseX, mouseY, partialTicks);
        this.rightButton.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public Collection<? extends GuiEventListener> getChildren() {
        List<GuiEventListener> listeners = new ArrayList<>();
        listeners.add(leftButton);
        listeners.add(rightButton);
        return listeners;
    }

    @Override
    public void onClick(double p_93634_, double p_93635_) {
        this.leftButton.mouseClicked(p_93634_, p_93635_,0);
        this.rightButton.mouseClicked(p_93634_, p_93635_, 0);
    }

    public boolean mouseClicked(double p_93641_, double p_93642_, int p_93643_) {
        return super.mouseClicked(p_93641_, p_93642_, p_93643_);
    }

    public class Button extends AbstractWidget{
        private Runnable clickEvent;

        public Button(Runnable clickEvent, int x, int y, Component component) {
            super(x, y, 8, 15, component);
            this.clickEvent = clickEvent;
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            renderBg(graphics,getX(),getY(),getWidth(),getHeight(),partialTicks);
            PoseStack poseStack = graphics.pose();

            poseStack.pushPose();
            graphics.drawCenteredString(font, this.getMessage(), getX() + 3, getY() + 3, 0xFFFFFF);
            poseStack.popPose();
        }

        /**渲染*/
        protected void renderBg(GuiGraphics graphics, int x, int y, int width, int height, float partialTicks){
            PoseStack poseStack = graphics.pose();
            poseStack.pushPose();
            graphics.fillGradient(
                    x,
                    y,
                    x + width,
                    y + height,
                    0x50000000,
                    0x20000000
            );
            poseStack.popPose();
        }

        @Override
        public void onClick(double p_93634_, double p_93635_) {
            this.clickEvent.run();
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

        }
    }
}
