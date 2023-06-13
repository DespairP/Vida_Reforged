package teamHTBP.vidaReforged.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.api.debug.IDebugObj;
import teamHTBP.vidaReforged.core.api.hud.IVidaScreen;
import teamHTBP.vidaReforged.core.utils.render.SpriteUtils;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class VidaDebugScreen extends GuiComponent implements IVidaScreen {
    /**是否开启debug模式*/
    public static Boolean isDebug = true;
    /**实体*/
    private Entity entity;
    /**间距*/
    private final int marginHeightBelow = 2;
    /**字体*/
    private final Font font;
    /**图片路径*/
    public static final ResourceLocation LOCATION_ENTITY_FRIENDLY = new ResourceLocation(VidaReforged.MOD_ID, "textures/icons/entity_friendly.png");
    /**图片属性*/
    private final TextureSection iconEntityFriendly = new TextureSection(LOCATION_ENTITY_FRIENDLY,0,0,16,16);


    public VidaDebugScreen() {
        this.entity = mc.crosshairPickEntity;
        this.font = mc.font;
    }

    @Override
    public void render(PoseStack matrixStack) {
        if(!isDebug){
            return;
        }
        //刷新数据
        this.entity = mc.crosshairPickEntity;

        //渲染生物数据
        renderEntity(matrixStack);
    }


    public void renderEntity(PoseStack matrixStack){
        if(!checkEntityShouldRender()){
            return;
        }
        final float scaledWeight = 0.8f;
        //获取屏幕大小
        final int width = mc.getWindow().getGuiScaledWidth();
        final int height = mc.getWindow().getGuiScaledHeight();
        //获取需要渲染的数据
        List<String> info = getEntityInformation();
        //计算需要渲染文字高度，计算文字起始渲染高度，计算图片渲染起始高度
        final int totalHeight = (font.lineHeight + marginHeightBelow) * info.size();
        final float beginTextY = height / 2.0f - totalHeight;
        int beginImgY = (int) (beginTextY - 20.0f);
        //标题
        final String titleName = "Entity";
        //1.渲染图片
        matrixStack.pushPose();
        RenderSystem.setShaderTexture(0, iconEntityFriendly.location());
        int beginX = width - font.width(titleName) - 8 - iconEntityFriendly.width();
        blit(
                matrixStack,
                beginX, beginImgY,0,
                iconEntityFriendly.mu(), iconEntityFriendly.mv(),
                16,16,
                16,16
        );
        matrixStack.popPose();

        //2.渲染标题
        matrixStack.pushPose();
        beginX += iconEntityFriendly.width();
        beginImgY += iconEntityFriendly.width() / 2;
        font.draw(matrixStack, titleName, (float)(beginX + 1), (float)beginImgY, 0);
        font.draw(matrixStack, titleName, (float)(beginX - 1), (float)beginImgY, 0);
        font.draw(matrixStack, titleName, (float)beginX, (float)(beginImgY + 1), 0);
        font.draw(matrixStack, titleName, (float)beginX, (float)(beginImgY - 1), 0);
        font.draw(matrixStack, titleName, (float)beginX, (float)beginImgY, 8453920);
        matrixStack.popPose();

        //3.渲染信息文字
        matrixStack.pushPose();
        matrixStack.scale(scaledWeight,scaledWeight,scaledWeight);

        //获取缩放因子
        final float factor = 1.0f / scaledWeight;
        //开始渲染
        for(var i = 0;i < info.size();i++){
            String text = info.get(i);
            float x = width * factor - font.width(text) - 10;
            float y = beginTextY * factor + (marginHeightBelow + font.lineHeight) * i;
            font.drawShadow(matrixStack, text, x , y, 0xFFFFFF);
        }
        matrixStack.popPose();
        //结束渲染
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
    public List<String> getEntityInformation(){
        List<String> infos = new LinkedList<>();
        //获取Debug Entity info，变成KEY:VALUE的格式输出
        if(entity instanceof IDebugObj){
            infos = ((IDebugObj) entity).getDebugAttributes()
                    .entrySet()
                    .stream()
                    .map(entry-> "%s: %s".formatted(entry.getKey(),entry.getValue()))
                    .collect(Collectors.toList());
        }
        //
        return Optional.of(infos).orElse(new LinkedList<>());
    }
}
