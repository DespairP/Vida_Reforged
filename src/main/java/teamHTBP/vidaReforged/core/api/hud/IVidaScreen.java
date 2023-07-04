package teamHTBP.vidaReforged.core.api.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;

public interface IVidaScreen {
    public final Minecraft mc = Minecraft.getInstance();

    public void render(PoseStack poseStack);
}
