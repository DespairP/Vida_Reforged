package teamHTBP.vidaReforged.server.blockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.EnchantmentTableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import teamHTBP.vidaReforged.core.api.blockentity.IVidaTickableBlockEntity;

public class TeaconGuideBookBlockEntity extends BlockEntity implements IVidaTickableBlockEntity {
    public int time;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    public float rot;
    public float oRot;
    public float tRot;

    private static final RandomSource RANDOM = RandomSource.create();
    
    public TeaconGuideBookBlockEntity(BlockPos pos, BlockState state) {
        super(VidaBlockEntityLoader.TEACON_GUIDEBOOK.get(), pos, state);
    }

    public void doServerTick(Level level, BlockPos pos, BlockState state, BlockEntity entityIn) {
        if(!level.isClientSide){
            return;
        }
        TeaconGuideBookBlockEntity blockEntity = (TeaconGuideBookBlockEntity)entityIn;
        blockEntity.oOpen = blockEntity.open;
        blockEntity.oRot = blockEntity.rot;
        Player player = level.getNearestPlayer((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, 3.0D, false);
        if (player != null) {
            double d0 = player.getX() - ((double)pos.getX() + 0.5D);
            double d1 = player.getZ() - ((double)pos.getZ() + 0.5D);
            blockEntity.tRot = (float) Mth.atan2(d1, d0);
            blockEntity.open += 0.1F;
            if (blockEntity.open < 0.5F || RANDOM.nextInt(40) == 0) {
                float f1 = blockEntity.flipT;

                do {
                    blockEntity.flipT += (float)(RANDOM.nextInt(4) - RANDOM.nextInt(4));
                } while(f1 == blockEntity.flipT);
            }
        } else {
            //blockEntity.tRot += 0.02F;
            blockEntity.open -= 0.1F;
        }

        while(blockEntity.rot >= (float)Math.PI) {
            blockEntity.rot -= ((float)Math.PI * 2F);
        }

        while(blockEntity.rot < -(float)Math.PI) {
            blockEntity.rot += ((float)Math.PI * 2F);
        }

        while(blockEntity.tRot >= (float)Math.PI) {
            blockEntity.tRot -= ((float)Math.PI * 2F);
        }

        while(blockEntity.tRot < -(float)Math.PI) {
            blockEntity.tRot += ((float)Math.PI * 2F);
        }

        float f2;
        for(f2 = blockEntity.tRot - blockEntity.rot; f2 >= (float)Math.PI; f2 -= ((float)Math.PI * 2F)) {
        }

        while(f2 < -(float)Math.PI) {
            f2 += ((float)Math.PI * 2F);
        }

        blockEntity.rot += f2 * 0.4F;
        blockEntity.open = Mth.clamp(blockEntity.open, 0.0F, 1.0F);
        ++blockEntity.time;
        blockEntity.oFlip = blockEntity.flip;
        float f = (blockEntity.flipT - blockEntity.flip) * 0.4F;
        float f3 = 0.2F;
        f = Mth.clamp(f, -0.2F, 0.2F);
        blockEntity.flipA += (f - blockEntity.flipA) * 0.9F;
        blockEntity.flip += blockEntity.flipA;
    }
    
    
}
