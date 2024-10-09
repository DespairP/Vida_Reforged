package teamHTBP.vidaReforged.server.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import teamHTBP.vidaReforged.server.blockEntities.ElementHarmonizeTableBlockEntity;

public class FakeHarmonizeTableItemEntity extends FloatingItemEntity {

    ElementHarmonizeTableBlockEntity ownerBlockEntity = null;

    protected FakeHarmonizeTableItemEntity(EntityType<FakeHarmonizeTableItemEntity> entityType, Level level) {
        super(entityType, level);
    }

    public FakeHarmonizeTableItemEntity(@NotNull ElementHarmonizeTableBlockEntity entity, Level level, @NonNull Vector3d deltaPos) {
        super(VidaEntityLoader.FAKE_HARMONIZE_TABLE_ITEM_ENTITY.get(), level, entity.getBlockPos().above().getCenter(), deltaPos);
        this.ownerBlockEntity = entity;
    }

    @Override
    public void tick() {
        super.tick();
        if(!level().isClientSide && this.ownerBlockEntity == null){
            discard();
        }
        if(!level().isClientSide && this.ownerBlockEntity != null && !this.ownerBlockEntity.isProcessing()){
            discard();
        }
    }

    /**玩家不能触碰到物品*/
    @Override
    public void playerTouch(Player player) {}

    /**没有碰撞体积*/
    @Override
    public boolean isColliding(BlockPos pos, BlockState state) {
        return false;
    }
}
