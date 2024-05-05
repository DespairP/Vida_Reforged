package teamHTBP.vidaReforged.client.events;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.VidaReforged;

import java.io.IOException;

/**
 * @author DustW
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShadersHandler {
    public static ShaderInstance simplicity;
    public static ShaderInstance orb;

    public static ShaderInstance stars;
    @SubscribeEvent
    public static void onEvent(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(
                event.getResourceProvider(),
                new ResourceLocation(VidaReforged.MOD_ID, "simplicity"), DefaultVertexFormat.POSITION_TEX),
                s -> simplicity = s);

        event.registerShader(new ShaderInstance(
                        event.getResourceProvider(),
                        new ResourceLocation(VidaReforged.MOD_ID, "orb"), DefaultVertexFormat.POSITION_TEX),
                s -> orb = s);

        event.registerShader(new ShaderInstance(
                        event.getResourceProvider(),
                        new ResourceLocation(VidaReforged.MOD_ID, "stars"), DefaultVertexFormat.POSITION_TEX),
                s -> stars = s);
    }

    public static final record Point2f(float x, float y) {}

    public static void setUniforms(ShaderInstance shader, Point2f res, Point2f mouse, float partialTick) {
        setUniforms(shader, res, mouse, (int) ClientTickHandler.ticks, partialTick);
    }

    public static void setUniforms(ShaderInstance shader, Point2f res, Point2f mouse, int tick, float partialTick) {
        Uniform iTime = shader.getUniform("iTime");
        if (iTime != null) {
            iTime.set(tick / 80.0f);
        }
        Uniform iResolution = shader.getUniform("iResolution");
        if (iResolution != null) {
            iResolution.set(res.x, res.y);
        }
    }
}
