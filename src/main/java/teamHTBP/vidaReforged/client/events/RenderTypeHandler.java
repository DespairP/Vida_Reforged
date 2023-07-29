package teamHTBP.vidaReforged.client.events;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;


public class RenderTypeHandler extends RenderStateShard{

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

    private static RenderType.CompositeState translucentState(RenderStateShard.ShaderStateShard p_173208_) {
        return RenderType.CompositeState.builder().setLightmapState(LIGHTMAP).setShaderState(p_173208_).setTextureState(BLOCK_SHEET_MIPPED).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setOutputState(TRANSLUCENT_TARGET).createCompositeState(true);
    }

    public static final Function<ResourceLocation, RenderType> IMAGE = Util.memoize((resourceLocation) -> {
        RenderType.CompositeState compositeRenderType = RenderType.CompositeState.builder().setShaderState(RenderStateShard.POSITION_COLOR_TEX_SHADER).setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST).setTextureState(new TextureStateShard(resourceLocation, false, false)).setWriteMaskState(COLOR_DEPTH_WRITE).createCompositeState(true);
        return RenderType.create("vida_reforged:world_altases", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 1024, false, false, compositeRenderType);
    });
}
