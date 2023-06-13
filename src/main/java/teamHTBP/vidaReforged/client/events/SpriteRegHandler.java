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
public class SpriteRegHandler {
    public static final ResourceLocation ICON_ENTITY_FRIENDLY = new ResourceLocation(VidaReforged.MOD_ID, "icons/entity_friendly");
    public static final ResourceLocation ICON_ENTITY_ENEMY = new ResourceLocation(VidaReforged.MOD_ID, "icons/entity_enemy");

}
