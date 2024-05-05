package teamHTBP.vidaReforged.core.common.system.guidebook;

import com.mojang.serialization.Codec;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import teamHTBP.vidaReforged.client.screen.components.guidebooks.IVidaGuidebookComponent;

/**
 * 指导书Guidebook中的组件对应的序列化数据体接口,
 * <p>
 * 流程:
 * 读取json -> json数据会包装成IVidaPageComponent子类实例 -> client根据这个实例数据体生成IVidaGuidebookComponent组件 -> 渲染
 * </p>
 * */
public interface IVidaPageComponent {
    /**根据数据来生成组件实例*/
    @OnlyIn(Dist.CLIENT)
    public IVidaGuidebookComponent generateComponent();

    /**获取codec序列化*/
    public Codec<? extends IVidaPageComponent> codec();

    /**获取组件标识*/
    public String getType();
}
