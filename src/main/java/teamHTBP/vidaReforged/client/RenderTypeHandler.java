package teamHTBP.vidaReforged.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.plugin.ModsInfo;

import javax.annotation.Nonnull;
import java.util.function.Function;


public class RenderTypeHandler extends RenderStateShard{
    static final ResourceLocation TAIL = new ResourceLocation(VidaReforged.MOD_ID, "textures/particle/trail.png");

    public RenderTypeHandler(String p_110161_, Runnable p_110162_, Runnable p_110163_) {
        super(p_110161_, p_110162_, p_110163_);
    }

    public static final RenderType TRIANGLE_FAN = RenderType.create(
            "triangle_fan",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.TRIANGLE_FAN,
            256,
            false,
            false,
            RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GUI_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setDepthTestState(LEQUAL_DEPTH_TEST).createCompositeState(false)
    );

    public static final RenderType GUI_LINE = RenderType.create(
            "triangle_fan",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.DEBUG_LINE_STRIP,
            256,
            false,
            false,
            RenderType.CompositeState.builder().setShaderState(RENDERTYPE_GUI_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setDepthTestState(LEQUAL_DEPTH_TEST).createCompositeState(false)
    );

    /*来自ars_nouveau*/
    public static final ParticleRenderType EMBER_RENDER  = new ParticleRenderType() {
        @Override
        public void begin(BufferBuilder buffer, TextureManager textureManager) {
            Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
            RenderSystem.enableBlend();
            RenderSystem.enableCull();
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE.value);
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator tessellator) {
            tessellator.end();
        }

        @Override
        public String toString() {
            return "vida_reforged:em_rend";
        }
    };

    public static final ParticleRenderType TRAIL_SHADER = new ParticleRenderType() {
        @Override
        public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
            setShader();
            RenderSystem.setShaderTexture(0, TAIL);
            RenderSystem.setShaderColor(
                    1,
                    1,
                    1,
                    1
            );
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.depthMask(true);
            RenderSystem.enableCull();
        }

        void setShader() {
            RenderSystem.setShader(GameRenderer::getParticleShader);
        }

        @Override
        public void end(@Nonnull Tesselator tesselator) {
            RenderSystem.depthMask(true);
        }
    };

    private static RenderType.CompositeState translucentState(RenderStateShard.ShaderStateShard p_173208_) {
        return RenderType.CompositeState.builder().setLightmapState(LIGHTMAP).setShaderState(p_173208_).setTextureState(BLOCK_SHEET_MIPPED).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setOutputState(TRANSLUCENT_TARGET).createCompositeState(true);
    }

    public static final Function<ResourceLocation, RenderType> IMAGE = Util.memoize((resourceLocation) -> {
        RenderType.CompositeState compositeRenderType = RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_COLOR_TEX_SHADER).setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST).setTextureState(new TextureStateShard(resourceLocation, false, false)).setWriteMaskState(COLOR_DEPTH_WRITE).createCompositeState(true);
        return RenderType.create("vida_reforged:world_altases", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 1024, false, false, compositeRenderType);
    });
}
