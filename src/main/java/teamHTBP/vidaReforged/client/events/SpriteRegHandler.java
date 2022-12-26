package teamHTBP.vidaReforged.client.events;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.VidaReforged;

/**
 * 用于注册精灵图，
 * 辅助类可以使用SpriteUtils中的方法
 * @see teamHTBP.vidaReforged.core.utils.render.SpriteUtils
 * */
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SpriteRegHandler {
    public static final ResourceLocation ICON_ENTITY_FRIENDLY = new ResourceLocation(VidaReforged.MOD_ID, "icons/entity_friendly");
    public static final ResourceLocation ICON_ENTITY_ENEMY = new ResourceLocation(VidaReforged.MOD_ID, "icons/entity_enemy");


    /**
     * 事件注册精灵图
     * */
    @SubscribeEvent
    public static void onAtlasEvent(TextureStitchEvent.Pre event) {
        ResourceLocation stitching = event.getAtlas().location();
        if (!stitching.equals(InventoryMenu.BLOCK_ATLAS)) {
            return;
        }
        //注册
        registerAtlas(
                event,
                ICON_ENTITY_ENEMY,
                ICON_ENTITY_FRIENDLY
        );
    }

    /**注册精灵图*/
    public static void registerAtlas(TextureStitchEvent.Pre event,ResourceLocation ...locations){
        for(var location:locations){
            event.addSprite(location);
        }
    }



}
