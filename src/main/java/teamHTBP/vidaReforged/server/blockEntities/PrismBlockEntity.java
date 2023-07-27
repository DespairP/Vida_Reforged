package teamHTBP.vidaReforged.server.blockEntities;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Random;
import teamHTBP.vidaReforged.core.api.blockentity.IVidaTickableBlockEntity;

public class PrismBlockEntity extends VidaBlockEntity implements IVidaTickableBlockEntity {
    private SimpleContainer inputAndResult = new SimpleContainer(ItemStack.EMPTY, ItemStack.EMPTY);
    private RandomSource random = RandomSource.create();
    private int rotateRad$0 = 0;
    private int rotateRad$1 = 0;

    public PrismBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(VidaBlockEntityLoader.PRISM.get(), pPos, pBlockState);
    }

    /**随机生成位置*/
    public void generateFire(){
        this.rotateRad$0 = random.nextInt(360);
        this.rotateRad$1 = random.nextInt(360);
    }

    @Override
    public void doServerTick(Level pLevel, BlockPos pPos, BlockState pState, BlockEntity entity) {
        if(pLevel.isClientSide){
            return;
        }
    }
}
