package teamHTBP.vidaReforged.server.blocks.liquid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.joml.Vector3d;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.helper.VidaRecipeHelper;
import teamHTBP.vidaReforged.server.blocks.VidaFluidsLoader;
import teamHTBP.vidaReforged.server.entity.FloatingItemEntity;

import java.util.function.Supplier;


public class VividLiquidBlock extends LiquidBlock {

    public VividLiquidBlock() {
        super(() -> VidaFluidsLoader.VIVID_FLUID_STILL.get(), BlockBehaviour.Properties.of().lightLevel((level) -> 6).mapColor(MapColor.WATER).replaceable().noCollission().strength(100.0F).pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY));
    }

    /**当有掉落在水里时，检测物品是否可以活化*/
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        long gameTime = level.getGameTime();
        // 延时检测
        if(!level.isClientSide && gameTime % 120 == 0 && state.getValue(LEVEL).equals(0) && level.isLoaded(pos) && entity.getType() == EntityType.ITEM){
            ItemEntity itemEntity = (ItemEntity) entity;
            // 方块匹配
            Supplier<Item> resultItem = VidaRecipeHelper.getResultItem(itemEntity.getItem());
            if(resultItem != null){
                FloatingItemEntity floatingItemEntity = new FloatingItemEntity(level, pos.getCenter(), new Vector3d(0, 0.05f, 0));
                floatingItemEntity.init(new ItemStack(resultItem.get(), itemEntity.getItem().getCount()), pos.above().getCenter(), new ARGBColor(255, 182, 255, 2));
                level.addFreshEntity(floatingItemEntity);
                itemEntity.discard();
                return;
            }

        }
        super.entityInside(state, level, pos, entity);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
        if (randomSource.nextInt(20) == 0) {
            level.addParticle(ParticleTypes.GLOW, true, pos.getX() + 0.5F, pos.getY() + 1, pos.getZ() + 0.5F, 0,0.002, 0);
        }
    }
}
