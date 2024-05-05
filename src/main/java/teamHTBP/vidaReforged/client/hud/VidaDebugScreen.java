package teamHTBP.vidaReforged.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import teamHTBP.vidaReforged.VidaConfig;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.api.debug.IDebugObj;
import teamHTBP.vidaReforged.core.api.hud.IVidaScreen;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class VidaDebugScreen extends GuiGraphics implements IVidaScreen {
    /***/
    private static final Minecraft minecraft = Minecraft.getInstance();
    /**是否开启debug模式*/
    //public static Boolean isDebug;
    /**实体*/
    private Entity entity;
    /***/
    public static BlockEntity blockEntity;
    /**间距*/
    private final int marginHeightBelow = 2;
    /**字体*/
    private final Font font;
    /**图片路径*/
    public static final ResourceLocation LOCATION_ENTITY_FRIENDLY = new ResourceLocation(VidaReforged.MOD_ID, "textures/icons/entity_friendly.png");
    /**图片属性*/
    private final static TextureSection ICON_ENTITY_FRIENDLY_SECTION = new TextureSection(LOCATION_ENTITY_FRIENDLY,0,0,16,16, 16, 16);


    public VidaDebugScreen(MultiBufferSource.BufferSource bufferSource, Entity entity ,BlockEntity blockEntity) {
        super(minecraft, bufferSource);
        this.entity = entity;
        VidaDebugScreen.blockEntity = blockEntity;
        this.font = mc.font;
    }

    public void render(PoseStack poseStack, float partialTicks) {
        if(!VidaConfig.DEBUG_MODE.get()){
            return;
        }
        //刷新数据
        this.entity = mc.crosshairPickEntity;


        //渲染生物数据
        renderEntity();
        renderBlockEntity();
    }


    public void renderEntity(){
        if(!checkEntityShouldRender()){
            return;
        }
        final float scaledWeight = 0.8f;
        //获取屏幕大小
        final int width = mc.getWindow().getGuiScaledWidth();
        final int height = mc.getWindow().getGuiScaledHeight();
        //获取需要渲染的数据
        List<String> info = getEntityInformation(this.entity);
        //计算需要渲染文字高度，计算文字起始渲染高度，计算图片渲染起始高度
        final int totalHeight = (font.lineHeight + marginHeightBelow) * info.size();
        final float beginTextY = height / 2.0f - totalHeight;
        int beginImgY = (int) (beginTextY - 20.0f);
        //标题
        final String titleName = "Entity";
        //1.渲染图片
        pose().pushPose();
        RenderSystem.setShaderTexture(0, ICON_ENTITY_FRIENDLY_SECTION.location());
        int beginX = width - font.width(titleName) - 8 - ICON_ENTITY_FRIENDLY_SECTION.width();
        blit(
                ICON_ENTITY_FRIENDLY_SECTION.location(),
                beginX, beginImgY, 0,
                ICON_ENTITY_FRIENDLY_SECTION.mu(), ICON_ENTITY_FRIENDLY_SECTION.mv(),
                16,16,
                16,16
        );
        pose().popPose();

        //2.渲染标题
        pose().pushPose();
        beginX += ICON_ENTITY_FRIENDLY_SECTION.width();
        beginImgY += ICON_ENTITY_FRIENDLY_SECTION.width() / 2;

        drawString(font, titleName, (float)(beginX + 1), (float)beginImgY, 0, false);
        drawString(font, titleName, (float)(beginX - 1), (float)beginImgY, 0, false);
        drawString(font, titleName, (float)beginX, (float)(beginImgY + 1), 0, false);
        drawString(font, titleName, (float)beginX, (float)(beginImgY - 1), 0, false);
        drawString(font, titleName, (float)beginX, (float)beginImgY, 8453920, false);
        pose().popPose();

        //3.渲染信息文字
        pose().pushPose();
        pose().scale(scaledWeight, scaledWeight, scaledWeight);

        //获取缩放因子
        final float factor = 1.0f / scaledWeight;
        //开始渲染
        for(var i = 0;i < info.size();i++){
            String text = info.get(i);
            float x = width * factor - font.width(text) - 10;
            float y = beginTextY * factor + (marginHeightBelow + font.lineHeight) * i;
            drawString(font, text, x , y, 0xFFFFFF, true);
        }
        pose().popPose();
        //结束渲染
    }


    public void renderBlockEntity(){
        if(!checkBlockEntityShouldRender()){
            return;
        }
        final float scaledWeight = 0.8f;
        //获取屏幕大小
        final int width = mc.getWindow().getGuiScaledWidth();
        final int height = mc.getWindow().getGuiScaledHeight();
        //获取需要渲染的数据
        List<String> info = getEntityInformation(this.blockEntity);
        //计算需要渲染文字高度，计算文字起始渲染高度，计算图片渲染起始高度
        final int totalHeight = (font.lineHeight + marginHeightBelow) * info.size();
        final float beginTextY = (height - totalHeight) / 2.0f;
        int beginImgY = (int) (beginTextY - 20.0f);
        //标题
        final String titleName = "BlockEntity";
        //1.渲染图片
        pose().pushPose();
        RenderSystem.setShaderTexture(0, ICON_ENTITY_FRIENDLY_SECTION.location());
        int beginX = width - font.width(titleName) - 8 - ICON_ENTITY_FRIENDLY_SECTION.width();
        blit(
                ICON_ENTITY_FRIENDLY_SECTION.location(),
                beginX, beginImgY, 0,
                ICON_ENTITY_FRIENDLY_SECTION.mu(), ICON_ENTITY_FRIENDLY_SECTION.mv(),
                16,16,
                16,16
        );
        pose().popPose();

        //2.渲染标题
        pose().pushPose();
        beginX += ICON_ENTITY_FRIENDLY_SECTION.width();
        beginImgY += ICON_ENTITY_FRIENDLY_SECTION.width() / 2;

        drawString(font, titleName, (float)(beginX + 1), (float)beginImgY, 0, false);
        drawString(font, titleName, (float)(beginX - 1), (float)beginImgY, 0, false);
        drawString(font, titleName, (float)beginX, (float)(beginImgY + 1), 0, false);
        drawString(font, titleName, (float)beginX, (float)(beginImgY - 1), 0, false);
        drawString(font, titleName, (float)beginX, (float)beginImgY, 8453920, false);
        pose().popPose();

        //3.渲染信息文字
        pose().pushPose();
        pose().scale(scaledWeight, scaledWeight, scaledWeight);

        //获取缩放因子
        final float factor = 1.0f / scaledWeight;
        //开始渲染
        for(var i = 0;i < info.size();i++){
            String text = info.get(i);
            float x = width * factor - font.width(text) - 10;
            float y = beginTextY * factor + (marginHeightBelow + font.lineHeight) * i;
            drawString(font, text, x , y, 0xFFFFFF, true);
        }
        pose().popPose();
        //结束渲染
    }

    private boolean checkBlockEntityShouldRender() {
        return blockEntity != null && blockEntity instanceof IDebugObj;
    }


    /**
     * 是否渲染生物数据
     * */
    public boolean checkEntityShouldRender(){
        return entity != null && entity instanceof IDebugObj;
    }

    /**
     * 获取玩家指向的实体信息
     * @return entity实体信息
     * */
    public List<String> getEntityInformation(Object obj){
        List<String> infos = new LinkedList<>();
        //获取Debug Entity info，变成KEY:VALUE的格式输出
        if(obj instanceof IDebugObj){
            infos = ((IDebugObj) obj).getDebugAttributes()
                    .entrySet()
                    .stream()
                    .map(entry-> "%s: %s".formatted(entry.getKey(),entry.getValue()))
                    .collect(Collectors.toList());
        }
        //
        return Optional.of(infos).orElse(new LinkedList<>());
    }

    @Override
    public void render(GuiGraphics graphics, float partialTicks) {}
}
