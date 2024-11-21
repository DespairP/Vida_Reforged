package teamHTBP.vidaReforged.client.screen.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

@Deprecated
public abstract class AbstractVidaWidget implements Renderable, GuiEventListener {
    /**组件width*/
    protected float width;
    /**组件height*/
    protected float height;
    /**绝对x位置*/
    protected float x;
    /**绝对y位置*/
    protected float y;
    /**活跃状态*/
    public boolean active = false;
    /**可见*/
    public boolean visible = true;




    /**获取组件宽度*/
    public float getWidth() {
        return width;
    }

    /**获取组件高度*/
    public float getHeight() {
        return height;
    }

    /**绝对位置X*/
    public float getX() {
        return x;
    }

    /**绝对位置Y*/
    public float getY() {
        return y;
    }

    /**光标是否在组件之上*/
    public boolean isMouseOver(double pMouseX, double pMouseY) {
        return this.visible
                && pMouseX >= (double)this.getX()
                && pMouseY >= (double)this.getY()
                && pMouseX < (double)(this.getX() + this.width)
                && pMouseY < (double)(this.getY() + this.height);
    }

    /**播放按钮的声音*/
    public void playDownSound(SoundManager soundManager) {
        soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    @Override
    public void setFocused(boolean p_265728_) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }
}
