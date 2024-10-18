package teamHTBP.vidaReforged.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.events.ShadersHandler;
import teamHTBP.vidaReforged.plugin.ModsInfo;

import javax.annotation.Nonnull;
import java.util.SortedMap;
import java.util.function.Function;


public class RenderTypeHandler extends RenderStateShard{
    public static final ResourceLocation ENCHANTED_GLINT_CLOUD = new ResourceLocation(VidaReforged.MOD_ID, "textures/colormap/enchanted_glint_cloud.png");
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
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_GUI_SHADER)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(LEQUAL_DEPTH_TEST)
                    .createCompositeState(false)
    );

    public static final RenderType GUI_LINE = RenderType.create(
            "triangle_fan",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.DEBUG_LINE_STRIP,
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_GUI_SHADER)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(LEQUAL_DEPTH_TEST)
                    .createCompositeState(false)
    );

    public static final RenderType ENTITY_GLINT_FOIL = RenderType.create(
            "entity_glint_foil",
            DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS,
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_ENTITY_GLINT_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(RenderTypeHandler.ENCHANTED_GLINT_CLOUD, true, false))
                    .setWriteMaskState(COLOR_WRITE)
                    .setCullState(NO_CULL)
                    .setDepthTestState(LEQUAL_DEPTH_TEST)
                    .setTransparencyState(GLINT_TRANSPARENCY)
                    .setTexturingState(ENTITY_GLINT_TEXTURING)
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .createCompositeState(false)
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
            RenderSystem.enableBlend();
            RenderSystem.enableCull();
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(true);
            RenderSystem.setShaderTexture(0, TAIL);
            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.defaultBlendFunc();
        }

        void setShader() {
            RenderSystem.setShader(GameRenderer::getParticleShader);
        }

        @Override
        public void end(@Nonnull Tesselator tesselator) {
            RenderSystem.depthMask(true);
        }
    };

    public static final ParticleRenderType PARTICLE_SHEET_NO_MASK = new ParticleRenderType() {
        @Override
        public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
            RenderSystem.enableBlend();
            int j = RenderSystem.getShaderTexture(0);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.defaultBlendFunc();
            RenderSystem.depthMask(false);
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(@Nonnull Tesselator tesselator) {
            tesselator.end();
            RenderSystem.defaultBlendFunc();
            RenderSystem.depthMask(true);
        }

        @Override
        public String toString() {
            return "PARTICLE_SHEET_NO_MASK";
        }
    };

    public static final Function<ResourceLocation, RenderType> ENTITY_GLOW_WAND = Util.memoize((p_286151_) -> {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENTITY_ALPHA_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(p_286151_, false, false))
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .createCompositeState(true);
        return RenderType.create("entity_glowing_wand", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, false, rendertype$compositestate);
    });

    private static RenderType.CompositeState translucentState(RenderStateShard.ShaderStateShard p_173208_) {
        return RenderType.CompositeState.builder().setLightmapState(LIGHTMAP).setShaderState(p_173208_).setTextureState(BLOCK_SHEET_MIPPED).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setOutputState(TRANSLUCENT_TARGET).createCompositeState(true);
    }

    public static final Function<ResourceLocation, RenderType> IMAGE = Util.memoize((resourceLocation) -> {
        RenderType.CompositeState compositeRenderType = RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_COLOR_TEX_SHADER).setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST).setTextureState(new TextureStateShard(resourceLocation, false, false)).setWriteMaskState(COLOR_DEPTH_WRITE).createCompositeState(true);
        return RenderType.create("vida_reforged:world_altases", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 1024, false, false, compositeRenderType);
    });


    public static final SortedMap<RenderType, BufferBuilder> FIXED_BUFFERS = Util.make(new Object2ObjectLinkedOpenHashMap<>(), (p_269658_) -> {
        put(p_269658_, Sheets.shieldSheet());
        put(p_269658_, Sheets.bedSheet());
        put(p_269658_, Sheets.shulkerBoxSheet());
        put(p_269658_, Sheets.signSheet());
        put(p_269658_, Sheets.hangingSignSheet());
        put(p_269658_, Sheets.chestSheet());
        put(p_269658_, RenderType.translucentNoCrumbling());
        put(p_269658_, RenderType.armorGlint());
        put(p_269658_, RenderType.armorEntityGlint());
        put(p_269658_, RenderType.glint());
        put(p_269658_, RenderType.glintDirect());
        put(p_269658_, RenderType.glintTranslucent());
        put(p_269658_, RenderType.entityGlint());
        put(p_269658_, RenderType.entityGlintDirect());
        put(p_269658_, RenderType.waterMask());
        put(p_269658_, RenderType.cutout());
        ModelBakery.DESTROY_TYPES.forEach((p_173062_) -> {
            put(p_269658_, p_173062_);
        });
    });

    private static void put(Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder> p_110102_, RenderType p_110103_) {
        p_110102_.put(p_110103_, new BufferBuilder(p_110103_.bufferSize()));
    }
}
