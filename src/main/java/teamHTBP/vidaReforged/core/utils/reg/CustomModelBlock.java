package teamHTBP.vidaReforged.core.utils.reg;

import teamHTBP.vidaReforged.client.events.RenderTypeRegisterHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 一个工具注释，标明注册的Block会自动注册渲染类型
 * 当触发RenderTypeSetup事件时，event会自动扫描{@link teamHTBP.vidaReforged.server.blocks.VidaBlockLoader}中所有标有这样注释的Block
 * 并且进行模型渲染的注册
 * @see teamHTBP.vidaReforged.server.blocks.VidaBlockLoader
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Deprecated
public @interface CustomModelBlock {
    /**创建的渲染类型*/
    RenderTypeRegisterHandler.CustomModelRenderType value();
}
