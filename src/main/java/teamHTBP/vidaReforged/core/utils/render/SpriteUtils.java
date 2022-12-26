package teamHTBP.vidaReforged.core.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

/**
 * 精灵图帮助类
 * */
public class SpriteUtils {

    public static TextureAtlasSprite getSprite(ResourceLocation location){
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(location);
    }
}
