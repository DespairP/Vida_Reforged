package teamHTBP.vidaReforged.client.renderer.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class BorderRendererManager {
    private static final Map<ResourceLocation,IBorderRenderer> BORDER_MAP = new HashMap<>();

    static {
        registerBorder(new ResourceLocation(VidaReforged.MOD_ID, "arrows_border"), new ArrowsBorder());
        registerBorder(new ResourceLocation(VidaReforged.MOD_ID, "square_border_two"), new SquareBorderTwo());
        registerBorder(new ResourceLocation(VidaReforged.MOD_ID, "square_border_two_01"), new SquareBorderTwo01());
        registerBorder(new ResourceLocation(VidaReforged.MOD_ID, "square_border_in"), new SquareBorderIn());
        registerBorder(new ResourceLocation(VidaReforged.MOD_ID, "square_border_out"), new SquareBorderOut());
        registerBorder(new ResourceLocation(VidaReforged.MOD_ID, "picture_frame_border"), new PictureFrameBorder());
        registerBorder(new ResourceLocation(VidaReforged.MOD_ID, "square_border"), new SquareBorder());
    }

    public static void registerBorder(ResourceLocation id, IBorderRenderer borderRenderer){
        BorderRendererManager.BORDER_MAP.put(id, borderRenderer);
    }

    public static IBorderRenderer getRender(ResourceLocation resourceLocation){
        return BORDER_MAP.get(resourceLocation);
    }

    public static void renderCorner(GuiGraphics graphics, TextureSection SECTION_LEFT_TOP_CORNER, TextureSection SECTION_RIGHT_TOP_CORNER, TextureSection SECTION_LEFT_BOTTOM_CORNER, TextureSection SECTION_RIGHT_BOTTOM_CORNER, int width, int height, int x, int y){
        VidaGuiHelper.blitWithTexture(graphics, x, y, 10, SECTION_LEFT_TOP_CORNER);
        VidaGuiHelper.blitWithTexture(graphics, x + width - SECTION_RIGHT_TOP_CORNER.w(), y, 10, SECTION_RIGHT_TOP_CORNER);
        VidaGuiHelper.blitWithTexture(graphics, x, y + height - SECTION_LEFT_BOTTOM_CORNER.h(), 10, SECTION_LEFT_BOTTOM_CORNER);
        VidaGuiHelper.blitWithTexture(graphics, x + width - SECTION_RIGHT_BOTTOM_CORNER.w(), y + height - SECTION_RIGHT_BOTTOM_CORNER.h(), 10, SECTION_RIGHT_BOTTOM_CORNER);
    }

    public static void renderLines(GuiGraphics graphics, TextureSection SECTION_LEFT_TOP_CORNER, TextureSection SECTION_RIGHT_TOP_CORNER, TextureSection SECTION_LEFT_BOTTOM_CORNER, TextureSection SECTION_RIGHT_BOTTOM_CORNER, int width, int height, int x, int y, int inOffset,int color) {
        int remainHeight = height - SECTION_LEFT_TOP_CORNER.h() - SECTION_LEFT_BOTTOM_CORNER.h();
        int remainWidth = width - SECTION_LEFT_BOTTOM_CORNER.w() - SECTION_RIGHT_BOTTOM_CORNER.h();
        int strokeSize = 1;
        // 左
        graphics.fill(x + inOffset, y + SECTION_LEFT_TOP_CORNER.h(), x + inOffset + strokeSize, y + SECTION_LEFT_TOP_CORNER.h() + remainHeight, color);
        // 上
        graphics.fill(x + SECTION_LEFT_TOP_CORNER.w(), y + inOffset, x + SECTION_LEFT_TOP_CORNER.w() + remainWidth, y + inOffset + strokeSize, color);
        // 下
        graphics.fill(x + SECTION_LEFT_BOTTOM_CORNER.w(), y + height - inOffset - 1 , x + SECTION_LEFT_BOTTOM_CORNER.w() + remainWidth, y + height - inOffset - 1 + strokeSize, color);
        // 右
        graphics.fill(x + width - inOffset - 1, y + SECTION_RIGHT_TOP_CORNER.h(), x + width - inOffset - 1 + strokeSize, y + SECTION_RIGHT_TOP_CORNER.h() + remainHeight, color);
    }

    public static class SquareBorder implements IBorderRenderer {
        private static final ResourceLocation BORDER = new ResourceLocation(VidaReforged.MOD_ID, "textures/gui/ui/borders/square_border.png");
        private static final TextureSection SECTION_LEFT_TOP_CORNER = new TextureSection(BORDER, 0, 0, 4, 4, 24, 24);
        private static final TextureSection SECTION_RIGHT_TOP_CORNER = new TextureSection(BORDER, 20, 0, 4, 4, 24, 24);
        private static final TextureSection SECTION_LEFT_BOTTOM_CORNER = new TextureSection(BORDER, 0, 20, 4, 4, 24, 24);
        private static final TextureSection SECTION_RIGHT_BOTTOM_CORNER = new TextureSection(BORDER, 20, 20, 4, 4, 24, 24);


        @Override
        public void renderBorder(GuiGraphics graphics, int x, int y, int width, int height, int color) {
            BorderRendererManager.renderCorner(
                    graphics,
                    SECTION_LEFT_TOP_CORNER,
                    SECTION_RIGHT_TOP_CORNER,
                    SECTION_LEFT_BOTTOM_CORNER,
                    SECTION_RIGHT_BOTTOM_CORNER,
                    width,
                    height,
                    x,
                    y
            );

            BorderRendererManager.renderLines(
                    graphics,
                    SECTION_LEFT_TOP_CORNER,
                    SECTION_RIGHT_TOP_CORNER,
                    SECTION_LEFT_BOTTOM_CORNER,
                    SECTION_RIGHT_BOTTOM_CORNER,
                    width,
                    height,
                    x,
                    y,
                    0,
                    color
            );
        }
    }

    public static class ArrowsBorder implements IBorderRenderer {
        private static final ResourceLocation BORDER = new ResourceLocation(VidaReforged.MOD_ID, "textures/gui/ui/borders/arrows_border.png");
        private static final TextureSection SECTION_LEFT_TOP_CORNER = new TextureSection(BORDER, 0, 0, 10, 10, 48, 48);
        private static final TextureSection SECTION_RIGHT_TOP_CORNER = new TextureSection(BORDER, 38, 0, 10, 10, 48, 48);
        private static final TextureSection SECTION_LEFT_BOTTOM_CORNER = new TextureSection(BORDER, 0, 38, 10, 10, 48, 48);
        private static final TextureSection SECTION_RIGHT_BOTTOM_CORNER = new TextureSection(BORDER, 38, 38, 10, 10, 48, 48);


        @Override
        public void renderBorder(GuiGraphics graphics, int x, int y, int width, int height, int color) {
            BorderRendererManager.renderCorner(
                    graphics,
                    SECTION_LEFT_TOP_CORNER,
                    SECTION_RIGHT_TOP_CORNER,
                    SECTION_LEFT_BOTTOM_CORNER,
                    SECTION_RIGHT_BOTTOM_CORNER,
                    width,
                    height,
                    x,
                    y
            );

            BorderRendererManager.renderLines(
                    graphics,
                    SECTION_LEFT_TOP_CORNER,
                    SECTION_RIGHT_TOP_CORNER,
                    SECTION_LEFT_BOTTOM_CORNER,
                    SECTION_RIGHT_BOTTOM_CORNER,
                    width,
                    height,
                    x,
                    y,
                    5,
                    color
            );
        }
    }

    public static class SquareBorderIn implements IBorderRenderer {
        private static final ResourceLocation BORDER = new ResourceLocation(VidaReforged.MOD_ID, "textures/gui/ui/borders/square_border_in.png");
        private static final TextureSection SECTION_LEFT_TOP_CORNER = new TextureSection(BORDER, 0, 0, 6, 6, 24, 24);
        private static final TextureSection SECTION_RIGHT_TOP_CORNER = new TextureSection(BORDER, 18, 0, 6, 6, 24, 24);
        private static final TextureSection SECTION_LEFT_BOTTOM_CORNER = new TextureSection(BORDER, 0, 18, 6, 6, 24, 24);
        private static final TextureSection SECTION_RIGHT_BOTTOM_CORNER = new TextureSection(BORDER, 18, 18, 6, 6, 24, 24);


        @Override
        public void renderBorder(GuiGraphics graphics, int x, int y, int width, int height, int color) {
            BorderRendererManager.renderCorner(
                    graphics,
                    SECTION_LEFT_TOP_CORNER,
                    SECTION_RIGHT_TOP_CORNER,
                    SECTION_LEFT_BOTTOM_CORNER,
                    SECTION_RIGHT_BOTTOM_CORNER,
                    width,
                    height,
                    x,
                    y
            );
            BorderRendererManager.renderLines(
                    graphics,
                    SECTION_LEFT_TOP_CORNER,
                    SECTION_RIGHT_TOP_CORNER,
                    SECTION_LEFT_BOTTOM_CORNER,
                    SECTION_RIGHT_BOTTOM_CORNER,
                    width,
                    height,
                    x,
                    y,
                    0,
                    color
            );

        }
    }

    public static class SquareBorderOut implements IBorderRenderer {
        private static final ResourceLocation BORDER = new ResourceLocation(VidaReforged.MOD_ID, "textures/gui/ui/borders/square_border_out.png");
        private static final TextureSection SECTION_LEFT_TOP_CORNER = new TextureSection(BORDER, 0, 0, 5, 5, 24, 24);
        private static final TextureSection SECTION_RIGHT_TOP_CORNER = new TextureSection(BORDER, 19, 0, 5, 5, 24, 24);
        private static final TextureSection SECTION_LEFT_BOTTOM_CORNER = new TextureSection(BORDER, 0, 19, 5, 5, 24, 24);
        private static final TextureSection SECTION_RIGHT_BOTTOM_CORNER = new TextureSection(BORDER, 19, 19, 5, 5, 24, 24);


        @Override
        public void renderBorder(GuiGraphics graphics, int x, int y, int width, int height, int color) {
            BorderRendererManager.renderCorner(
                    graphics,
                    SECTION_LEFT_TOP_CORNER,
                    SECTION_RIGHT_TOP_CORNER,
                    SECTION_LEFT_BOTTOM_CORNER,
                    SECTION_RIGHT_BOTTOM_CORNER,
                    width,
                    height,
                    x,
                    y
            );
            BorderRendererManager.renderLines(
                    graphics,
                    SECTION_LEFT_TOP_CORNER,
                    SECTION_RIGHT_TOP_CORNER,
                    SECTION_LEFT_BOTTOM_CORNER,
                    SECTION_RIGHT_BOTTOM_CORNER,
                    width,
                    height,
                    x,
                    y,
                    4,
                    color
            );
            BorderRendererManager.renderLines(
                    graphics,
                    SECTION_LEFT_TOP_CORNER,
                    SECTION_RIGHT_TOP_CORNER,
                    SECTION_LEFT_BOTTOM_CORNER,
                    SECTION_RIGHT_BOTTOM_CORNER,
                    width,
                    height,
                    x,
                    y,
                    2,
                    color
            );
        }
    }

    public static class SquareBorderTwo implements IBorderRenderer {
        private static final ResourceLocation BORDER = new ResourceLocation(VidaReforged.MOD_ID, "textures/gui/ui/borders/square_border_two.png");
        private static final TextureSection SECTION_LEFT_TOP_CORNER = new TextureSection(BORDER, 0, 0, 4, 4, 24, 24);
        private static final TextureSection SECTION_RIGHT_TOP_CORNER = new TextureSection(BORDER, 20, 0, 4, 4, 24, 24);
        private static final TextureSection SECTION_LEFT_BOTTOM_CORNER = new TextureSection(BORDER, 0, 20, 4, 4, 24, 24);
        private static final TextureSection SECTION_RIGHT_BOTTOM_CORNER = new TextureSection(BORDER, 20, 20, 4, 4, 24, 24);


        @Override
        public void renderBorder(GuiGraphics graphics, int x, int y, int width, int height, int color) {
            BorderRendererManager.renderCorner(
                    graphics,
                    SECTION_LEFT_TOP_CORNER,
                    SECTION_RIGHT_TOP_CORNER,
                    SECTION_LEFT_BOTTOM_CORNER,
                    SECTION_RIGHT_BOTTOM_CORNER,
                    width,
                    height,
                    x,
                    y
            );
            BorderRendererManager.renderLines(
                    graphics,
                    SECTION_LEFT_TOP_CORNER,
                    SECTION_RIGHT_TOP_CORNER,
                    SECTION_LEFT_BOTTOM_CORNER,
                    SECTION_RIGHT_BOTTOM_CORNER,
                    width,
                    height,
                    x,
                    y,
                    2,
                    color
            );
            BorderRendererManager.renderLines(
                    graphics,
                    SECTION_LEFT_TOP_CORNER,
                    SECTION_RIGHT_TOP_CORNER,
                    SECTION_LEFT_BOTTOM_CORNER,
                    SECTION_RIGHT_BOTTOM_CORNER,
                    width,
                    height,
                    x,
                    y,
                    0,
                    color
            );
        }
    }

    public static class PictureFrameBorder implements IBorderRenderer {
        private static final ResourceLocation BORDER = new ResourceLocation(VidaReforged.MOD_ID, "textures/gui/ui/borders/picture_shape_border.png");
        private static final TextureSection SECTION_LEFT_TOP_CORNER = new TextureSection(BORDER, 0, 0, 6, 6, 24, 24);
        private static final TextureSection SECTION_RIGHT_TOP_CORNER = new TextureSection(BORDER, 18, 0, 6, 6, 24, 24);
        private static final TextureSection SECTION_LEFT_BOTTOM_CORNER = new TextureSection(BORDER, 0, 18, 6, 6, 24, 24);
        private static final TextureSection SECTION_RIGHT_BOTTOM_CORNER = new TextureSection(BORDER, 18, 18, 6, 6, 24, 24);


        @Override
        public void renderBorder(GuiGraphics graphics, int x, int y, int width, int height, int color) {
            BorderRendererManager.renderCorner(
                    graphics,
                    SECTION_LEFT_TOP_CORNER,
                    SECTION_RIGHT_TOP_CORNER,
                    SECTION_LEFT_BOTTOM_CORNER,
                    SECTION_RIGHT_BOTTOM_CORNER,
                    width,
                    height,
                    x,
                    y
            );
            BorderRendererManager.renderLines(
                    graphics,
                    SECTION_LEFT_TOP_CORNER,
                    SECTION_RIGHT_TOP_CORNER,
                    SECTION_LEFT_BOTTOM_CORNER,
                    SECTION_RIGHT_BOTTOM_CORNER,
                    width,
                    height,
                    x,
                    y,
                    0,
                    color
            );
        }
    }

    public static class SquareBorderTwo01 implements IBorderRenderer {
        private static final ResourceLocation BORDER = new ResourceLocation(VidaReforged.MOD_ID, "textures/gui/ui/borders/square_border_two_01.png");
        private static final TextureSection SECTION_LEFT_TOP_CORNER = new TextureSection(BORDER, 0, 0, 6, 6, 24, 24);
        private static final TextureSection SECTION_RIGHT_TOP_CORNER = new TextureSection(BORDER, 18, 0, 6, 6, 24, 24);
        private static final TextureSection SECTION_LEFT_BOTTOM_CORNER = new TextureSection(BORDER, 0, 18, 6, 6, 24, 24);
        private static final TextureSection SECTION_RIGHT_BOTTOM_CORNER = new TextureSection(BORDER, 18, 18, 6, 6, 24, 24);


        @Override
        public void renderBorder(GuiGraphics graphics, int x, int y, int width, int height, int color) {
            BorderRendererManager.renderCorner(
                    graphics,
                    SECTION_LEFT_TOP_CORNER,
                    SECTION_RIGHT_TOP_CORNER,
                    SECTION_LEFT_BOTTOM_CORNER,
                    SECTION_RIGHT_BOTTOM_CORNER,
                    width,
                    height,
                    x,
                    y
            );
            BorderRendererManager.renderLines(
                    graphics,
                    SECTION_LEFT_TOP_CORNER,
                    SECTION_RIGHT_TOP_CORNER,
                    SECTION_LEFT_BOTTOM_CORNER,
                    SECTION_RIGHT_BOTTOM_CORNER,
                    width,
                    height,
                    x,
                    y,
                    0,
                    color
            );
        }
    }
}
