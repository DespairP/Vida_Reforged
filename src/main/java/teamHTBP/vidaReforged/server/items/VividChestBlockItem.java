package teamHTBP.vidaReforged.server.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import teamHTBP.vidaReforged.client.model.blockModel.VividChestModel;
import teamHTBP.vidaReforged.server.blockEntities.VidaBlockEntityLoader;
import teamHTBP.vidaReforged.server.blockEntities.VividBlockChestEntity;
import teamHTBP.vidaReforged.server.blocks.VidaBlockLoader;
import teamHTBP.vidaReforged.server.blocks.VividBlockChest;

import java.util.function.Consumer;

public class VividChestBlockItem extends BlockItem {
    public VividChestBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            VividChestModel<VividBlockChestEntity> model;
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if(model == null){
                    model = new VividChestModel<>(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels(), () -> new VividBlockChestEntity(BlockPos.ZERO, VidaBlockLoader.VIVID_CHEST_BLOCK.get().defaultBlockState()));
                }
                return model;
            }
        });
    }
}
