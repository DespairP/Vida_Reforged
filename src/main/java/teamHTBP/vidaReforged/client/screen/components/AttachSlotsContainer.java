package teamHTBP.vidaReforged.client.screen.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;

import java.util.LinkedList;
import java.util.List;

public class AttachSlotsContainer implements Renderable {
    private int rowNum = 0;
    private int colNum = 0;
    private float startX = 0;
    private float startY = 0;
    private float step = 20.5f;

    private List<AttachSlot> slots;

    private AttachSlotsContainer(){}

    private AttachSlotsContainer(float startX, float startY) {
        this.startX = startX;
        this.startY = startY;
    }

    public static AttachSlotsContainer create(int startX, int startY, int rowNum, int colNum){
        AttachSlotsContainer container = new AttachSlotsContainer(startX, startY);
        container.slots = new LinkedList<>();
        for(int row = 0; row < rowNum; row ++){
            for(int col = 0; col < colNum; col ++){
                float colPosY = col * container.step;
                float rowPosX = row * container.step;
                container.slots.add(new AttachSlot(container.startX, container.startY, rowPosX, colPosY));
            }
        }
        return container;
    }

    public AttachSlotsContainer modify(int posX,int posY){
        this.startX = posX;
        this.startY = posY;
        return this;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int p_253973_, int p_254325_, float p_254004_) {
        PoseStack poseStack = guiGraphics.pose();

        poseStack.pushPose();
        poseStack.translate(10,10,0);
        slots.forEach(slot -> {slot.render(guiGraphics, p_253973_, p_254325_, p_254004_);});
        poseStack.popPose();
    }


}
