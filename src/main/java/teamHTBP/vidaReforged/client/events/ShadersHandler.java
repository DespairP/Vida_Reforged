package teamHTBP.vidaReforged.client.events;

import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.events.registries.MobsModelRegistryHandler;

import java.io.IOException;

/**
 * @author DustW
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShadersHandler {
    protected static final Logger LOGGER = LogManager.getLogger();
    public static ShaderInstance simplicity;
    public static ShaderInstance orb;
    public static ShaderInstance stars;
    public static PostChain glowShadow;
    public static PostChain spidew_bloom;


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

    @SubscribeEvent
    public static void onEffectEvent(RegisterShadersEvent event){
        if(glowShadow != null){
            glowShadow.close();
        }

        try {
            Minecraft mc = Minecraft.getInstance();
            TextureManager textureManager = mc.getTextureManager();
            ResourceManager resourceManager = mc.getResourceManager();
            glowShadow = new PostChain(textureManager, resourceManager, mc.getMainRenderTarget(), new ResourceLocation(VidaReforged.MOD_ID, "shaders/post/glow_shadow.json"));
            spidew_bloom = new PostChain(textureManager, resourceManager, mc.getMainRenderTarget(), new ResourceLocation(VidaReforged.MOD_ID, "shaders/post/spidew_bloom.json"));
        } catch (IOException | JsonSyntaxException e) {
            LOGGER.error(e);
            glowShadow = null;
        }

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

    public static void setUniforms(ShaderInstance shader, Object sampler) {
        shader.setSampler("sampler0",  sampler);
    }
}
