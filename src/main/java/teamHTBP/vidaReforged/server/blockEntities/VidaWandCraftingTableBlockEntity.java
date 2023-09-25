package teamHTBP.vidaReforged.server.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import teamHTBP.vidaReforged.core.api.blockentity.IVidaTickableBlockEntity;
import teamHTBP.vidaReforged.server.blocks.VidaBlockLoader;
import teamHTBP.vidaReforged.server.items.VidaWandEquipment;

public class VidaWandCraftingTableBlockEntity extends VidaBlockEntity implements IVidaTickableBlockEntity {
    /**饰品槽位*/
    SimpleContainer equipmentSlots = new SimpleContainer(4);
    /**法杖槽位*/
    SimpleContainer vidaWandSlot = new SimpleContainer(1);


    public VidaWandCraftingTableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(VidaBlockEntityLoader.VIDA_WAND_CRAFTING_TABLE.get(), pPos, pBlockState);
    }

    @Override
    public void doServerTick(Level pLevel, BlockPos pPos, BlockState pState, BlockEntity entity) {

    }

    public SimpleContainer getEquipmentSlots() {
        return equipmentSlots;
    }
}
