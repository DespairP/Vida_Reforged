package teamHTBP.vidaReforged.server.blocks.crops;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.color.VidaColor;

import java.util.Random;
import java.util.function.Supplier;

public class ParticleCropBlock extends BaseElementCropBlock{
    /**种子*/
    private final ItemLike itemProvider;
    /**装饰*/
    private Supplier<Block> blockDecorator = () -> Blocks.AIR;

    /**
     * 一个可以在成年时生成粒子的植物
     *
     * @param element  所属元素
     * @param provider 所提供的itemseed
     */
    public ParticleCropBlock(VidaElement element, ItemLike provider) {
        super(element);
        this.itemProvider = provider;
    }

    /**
     * 一个可以在成年时生成粒子的植物
     *
     * @param element  所属元素
     * @param provider 所提供的itemseed
     */
    public ParticleCropBlock(VidaElement element, ItemLike provider, Supplier<Block> blockDecorator) {
        super(element);
        this.itemProvider = provider;
        this.blockDecorator = blockDecorator;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        if(level.isClientSide){
            return;
        }
        // 种植
        if(super.mayPlaceOn(level.getBlockState(pos.offset(0, -1, 0)), level, pos.offset(0, -1, 0))){
            return;
        }
        // 装饰
        if(blockDecorator.get().defaultBlockState().isAir()){
            return;
        }
        Direction direction = entity.getDirection();
        BlockState defaultDecoratorState = blockDecorator.get().defaultBlockState();
        defaultDecoratorState = defaultDecoratorState.setValue(HorizontalDirectionalBlock.FACING, direction);
        level.setBlockAndUpdate(pos, defaultDecoratorState);
    }

    @Override
    protected ItemLike getSeedsItem() {
        return itemProvider != null ? itemProvider : super.getSeedsItem();
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        float x = pos.getX();
        float y = pos.getY();
        float z = pos.getZ();
        if (isMaxAge(stateIn) && rand.nextFloat() >= 0.75f) {
            double offsetX = rand.nextDouble() * 0.5D + 0.2D;
            double offsetZ = rand.nextDouble() * 0.5D + 0.2D;
            ARGBColor argbColor = getElement().baseColor.toARGB();
            worldIn.addParticle(
                    new DustParticleOptions(
                            new Vector3f(
                                    argbColor.r() * 1.0F / 255.0F,
                                    argbColor.g() * 1.0F / 255.0F,
                                    argbColor.b() * 1.0F / 255.0F
                            ),
                            1),
                    x + offsetX,
                    y + 0.4f,
                    z + offsetZ,
                    0,
                    0,
                    0
            );
        }
    }
}
