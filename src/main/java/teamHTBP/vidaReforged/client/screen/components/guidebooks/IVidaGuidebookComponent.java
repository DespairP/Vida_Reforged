package teamHTBP.vidaReforged.client.screen.components.guidebooks;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import teamHTBP.vidaReforged.core.api.hud.IVidaNodes;

/**
 * 指导书渲染组件接口
 * */
@OnlyIn(Dist.CLIENT)
public interface IVidaGuidebookComponent extends LayoutElement, IVidaNodes {
    /**
     * 渲染组件
     * @param graphics
     * @param mouseX
     * @param mouseY
     * @param partialTicks
     */
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks);


    public void init();
}
