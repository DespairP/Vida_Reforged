package teamHTBP.vidaReforged.client.screen.components.guidebooks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.client.screen.components.VidaWidget;
import teamHTBP.vidaReforged.core.common.system.guidebook.VidaPageAnimation;
import teamHTBP.vidaReforged.core.common.system.guidebook.VidaPageAnimationStep;
import teamHTBP.vidaReforged.core.common.system.guidebook.VidaPageAnimationSubStep;
import teamHTBP.vidaReforged.core.utils.anim.SecondOrderDynamics;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;

public class VidaPageAnimationProgressBar extends VidaWidget {

    /** 目前进度 */
    private SecondOrderDynamics progress = new SecondOrderDynamics(1, 1, 0, new Vector3f(0));
    private Vector3f target = new Vector3f();

    /** 每小步计算时间 */
    private int waitSecondsPerStep = 0;

    /** 这个动画运行了多久 */
    private int ticks;

    /** 计算出的这个动画最小*/
    private int animationLength = 0;

    final VidaPageAnimation animation;

    public VidaPageAnimationProgressBar(int x, int y, int width, int height, int waitSecondsPerStep, VidaPageAnimation animation) {
        super(x, y, width, height, Component.literal("progress bar"));
        this.animation = animation;
        this.animationLength = getAnimationMaxWidth();
    }

    public int getAnimationMaxWidth(){
        int length = 0;
        for(VidaPageAnimationStep step : this.animation.getSteps().values()){
            for(VidaPageAnimationSubStep subStep : step.getSubSteps()){
                length += waitSecondsPerStep + subStep.getCostTime();
            }
        }
        return length;
    }

    /** 根据现在组件的大小计算现在要渲染的长度 */
    public float getProgressLength(){
        final int ticks = this.ticks;
        final int width = this.getWidth();
        final int animationMaxTicks = this.animationLength;

        float tickPerSize = width * 1.0f / animationMaxTicks;

        return ticks * tickPerSize;
    }

    public void setTick(int ticks){
        this.ticks = ticks;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        Vector3f toTarget = target.set(getProgressLength());
        Vector3f currentSize = progress.update(Minecraft.getInstance().getDeltaFrameTime() * 0.5f, toTarget, null);


        graphics.fillGradient(getX(), getY(), getX() + width, getY() + height, 0xA0101010, 0xB0101010 );
        VidaGuiHelper.fillGradient(graphics, getX(), getY(), getX() + currentSize.x(), getY() + getHeight(), 1, 0xA0108EE9, 0xB087D069);
    }
}
