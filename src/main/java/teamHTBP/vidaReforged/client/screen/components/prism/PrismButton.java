package teamHTBP.vidaReforged.client.screen.components.prism;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;

import static java.lang.Math.*;
import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class PrismButton extends AbstractWidget {
    private float length = 0;

    private final Point center;

    ResourceLocation location = new ResourceLocation(MOD_ID, "textures/gui/prism_table_gui.png");

    TextureSection buttonSection$1 = new TextureSection(location,96,16,16,16, 256, 256);

    TextureSection buttonSection$2 = new TextureSection(location,96,32,16,16, 256, 256);

    private int style = 0;

    private double degree = 0;

    public PrismButton(int length, double degree, Point center,int style) {
        super(
                (int)calculatePoint(center, length, degree).x,
                (int)calculatePoint(center, length, degree).y,
                20,
                20,
                Component.translatable("spin button")
        );
        this.center = center;
        this.length = length;
        this.style = style;
        this.degree = degree;
    }

    private static Point calculatePoint(Point center, int length, double degree){
        int u = (int)(length * cos(toRadians(degree)) + center.x) - 20;
        int v = (int)(length * sin(toRadians(degree)) + center.y) - 20;
        return new Point(u,v);
    }


    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        //renderBg(graphics,getX(), getY(), 20, 20, partialTicks);
        this.renderButton(graphics, mouseX, mouseY, partialTicks);
    }

    public void renderButton(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks){
        PoseStack poseStack = graphics.pose();

        TextureSection buttonSection = this.style == 1 ? this.buttonSection$1 : this.buttonSection$2;

        poseStack.pushPose();
        graphics.blit(
                buttonSection.location(),
                getX(), getY(), 0,
                buttonSection.minU(), buttonSection.minV(),
                buttonSection.w(), buttonSection.w(),
                256, 256
        );

        poseStack.popPose();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double dragX, double dragY) {
        double radius = calcRotationAngleInDegrees(center, new Point(mouseX, mouseY)) - 90;
        Point afterPoint = calculatePoint(center, (int)length, radius);
        setX((int)afterPoint.x);
        setY((int)afterPoint.y);
        this.degree = radius;
        return true;
    }

    public static double calcRotationAngleInDegrees(Point centerPt, Point targetPt)
    {
        // calculate the angle theta from the deltaY and deltaX values
        // (atan2 returns radians values from [-PI,PI])
        // 0 currently points EAST.
        // NOTE: By preserving Y and X param order to atan2,  we are expecting
        // a CLOCKWISE angle direction.
        double theta = Math.atan2(targetPt.y - centerPt.y, targetPt.x - centerPt.x);

        // rotate the theta angle clockwise by 90 degrees
        // (this makes 0 point NORTH)
        // NOTE: adding to an angle rotates it clockwise.
        // subtracting would rotate it counter-clockwise
        theta += Math.PI/2.0;

        // convert from radians to degrees
        // this will give you an angle from [0->270],[-180,0]
        double angle = Math.toDegrees(theta);

        // convert to positive range [0-360)
        // since we want to prevent negative angles, adjust them now.
        // we can assume that atan2 will not return a negative value
        // greater than one partial rotation
        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }

    public double getDegree(){
        return this.degree;
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

    public static class Point{
        public double x;
        public double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }


}
