package teamHTBP.vidaReforged.client.model.blockEntities;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.ChestType;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.server.blockEntities.VividBlockChestEntity;

import static net.minecraft.client.renderer.Sheets.CHEST_SHEET;

public class VividBlockChestEntityRenderer extends ChestRenderer<VividBlockChestEntity> {
    public static final Material CHEST_LOCATION = chestMaterial("vivid_block_chest");


    public VividBlockChestEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }


    @Override
    protected Material getMaterial(VividBlockChestEntity blockEntity, ChestType chestType) {
        return CHEST_LOCATION;
    }

    private static Material chestMaterial(String sheetId) {
        return new Material(CHEST_SHEET, new ResourceLocation(VidaReforged.MOD_ID, "entity/chest/" + sheetId));
    }
}
