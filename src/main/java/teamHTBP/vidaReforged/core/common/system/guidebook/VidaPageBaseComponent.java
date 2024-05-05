package teamHTBP.vidaReforged.core.common.system.guidebook;

import com.mojang.serialization.Codec;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Display;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import teamHTBP.vidaReforged.client.screen.components.guidebooks.*;
import teamHTBP.vidaReforged.client.screen.screens.guidebook.VidaGuidebookListScreen;
import teamHTBP.vidaReforged.client.screen.screens.guidebook.VidaGuidebookPageDetail;

/**
 * 指导书组件数据体基类 <br/>
 * 流程：
 * 读取json -> json数据会包装成IVidaPageComponent子类实例 -> client根据这个实例数据体生成IVidaGuidebookComponent组件 -> 渲染
 * {@link VidaTitleComponent} 标题组件
 * {@link VidaWhiteComponent} 间隔组件
 * {@link VidaBackButtonComponent} 返回按钮组件
 * {@link }
 * */
@Data
public abstract class VidaPageBaseComponent implements IVidaPageComponent{
    public int x;
    public int y;
    public int width;
    public int height;
    public String type;
    public Display.TextDisplay.Align align;

    /**获取该组件序列化codec*/
    @Override
    public Codec<? extends IVidaPageComponent> codec() {
        return null;
    }

    /**标题组件*/
    public static class VidaTitleComponent extends VidaPageBaseComponent{
        public Component title;

        @OnlyIn(Dist.CLIENT)
        @Override
        public IVidaGuidebookComponent generateComponent() {
            return new VidaGuidebookTitle(x, y, width, height, title);
        }

        @Override
        public String getType() {
            return "title";
        }
    }

    /**空格组件*/
    public static class VidaWhiteComponent extends VidaPageBaseComponent{
        @OnlyIn(Dist.CLIENT)
        @Override
        public IVidaGuidebookComponent generateComponent() {
            return new VidaGuidebookWhite(x, y, width, height, Component.empty());
        }

        @Override
        public String getType() {
            return "white";
        }
    }

    /**返回按钮组件*/
    public static class VidaBackButtonComponent extends VidaPageBaseComponent{

        @OnlyIn(Dist.CLIENT)
        @Override
        public IVidaGuidebookComponent generateComponent() {
            return new VidaGuidebookBackButton(x, y, VidaGuidebookBackButton.ICON_SECTION.width(), VidaGuidebookBackButton.ICON_SECTION.height(), Component.literal("back"));
        }

        @Override
        public String getType() {
            return "back";
        }
    }

    /**文本块组件*/
    public static class VidaTextAreaComponent extends VidaPageBaseComponent{
        String key = "";

        @OnlyIn(Dist.CLIENT)
        @Override
        public IVidaGuidebookComponent generateComponent() {
            return new VidaGuidebookText(key, x, y, 147, height);
        }

        @Override
        public String getType() {
            return "text";
        }
    }

    /**物品显示组件*/
    public static class VidaDisplayComponent extends VidaPageBaseComponent{
        ItemStack itemstack = ItemStack.EMPTY;

        @OnlyIn(Dist.CLIENT)
        @Override
        public IVidaGuidebookComponent generateComponent() {
            return new VidaGuidebookDisplaySection(x, y, 128, 74, itemstack);
        }

        @Override
        public String getType() {
            return "displayItem";
        }
    }

    /**翻页组件*/
    public static class VidaPaginationComponent extends VidaPageBaseComponent{

        @OnlyIn(Dist.CLIENT)
        @Override
        public IVidaGuidebookComponent generateComponent() {
            return new VidaGuidebookPagination(x, y, VidaGuidebookListScreen.GUIDEBOOK_SECTION.width() - 15, 30, Component.literal("pagination"));
        }

        @Override
        public String getType() {
            return "pagination";
        }
    }

    /**合成组件*/
    public static class VidaRecipeComponent extends VidaPageBaseComponent{
        ItemStack result = ItemStack.EMPTY;

        @OnlyIn(Dist.CLIENT)
        @Override
        public IVidaGuidebookComponent generateComponent() {
            return new VidaGuidebookCraftingRecipe(x, y,145, 100, result, Component.literal("recipe"));
        }

        @Override
        public String getType() {
            return "recipe_simple";
        }
    }
}
