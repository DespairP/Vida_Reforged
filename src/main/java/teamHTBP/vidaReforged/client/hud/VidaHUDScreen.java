package teamHTBP.vidaReforged.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;

/**
 *
 *
 * */
@OnlyIn(Dist.CLIENT)
public abstract class VidaHUDScreen extends GuiGraphics {

    public VidaHUDScreen(Minecraft minecraft, MultiBufferSource.BufferSource bufferSource) {
        super(minecraft, bufferSource);
    }

    public int centerX(TextureSection section){
        return (guiWidth() - section.width()) / 2;
    }

    public int centerY(TextureSection section){
        return (guiHeight() - section.height()) / 2;
    }


    public void renderWithAlpha(){

    }

}
