package teamHTBP.vidaReforged.core.utils.reg;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 一个工具注释，标明注册的Block会自动注册为BlockItem
 * 当触发ItemRegister事件时，event会自动扫描{@link teamHTBP.vidaReforged.server.blocks.VidaBlockLoader}中所有标有这样注释的Block
 * 并且包装成BlockItem进行注册
 * @see teamHTBP.vidaReforged.server.events.BlockItemAutoRegisterHandler
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RegisterItemBlock {
}
