package teamHTBP.vidaReforged.server.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TrunkVineDecorator;

public class FallenTreeDecorator extends TreeDecorator {
    public static final Codec<FallenTreeDecorator> CODEC = Codec.unit(() -> {
        return FallenTreeDecorator.INSTANCE;
    });
    public static final FallenTreeDecorator INSTANCE = new FallenTreeDecorator();

    @Override
    protected TreeDecoratorType<?> type() {
        return null;
    }

    @Override
    public void place(Context context) {
        RandomSource randomsource = context.random();
        // 尝试摆放藤蔓方块
        context.logs().forEach((pos) -> {
            if (randomsource.nextInt(3) > 0) {
                BlockPos blockpos = pos.west();
                if (context.isAir(blockpos)) {
                    context.placeVine(blockpos, VineBlock.EAST);
                }
            }

            if (randomsource.nextInt(3) > 0) {
                BlockPos blockpos1 = pos.east();
                if (context.isAir(blockpos1)) {
                    context.placeVine(blockpos1, VineBlock.WEST);
                }
            }

            if (randomsource.nextInt(3) > 0) {
                BlockPos blockpos2 = pos.north();
                if (context.isAir(blockpos2)) {
                    context.placeVine(blockpos2, VineBlock.SOUTH);
                }
            }

            if (randomsource.nextInt(3) > 0) {
                BlockPos blockpos3 = pos.south();
                if (context.isAir(blockpos3)) {
                    context.placeVine(blockpos3, VineBlock.NORTH);
                }
            }
            
            if (randomsource.nextInt(3) > 0){
                BlockPos blockPos4 = pos.relative(Direction.UP);
                if(context.isAir(blockPos4)){
                    placeMoss(context, blockPos4);
                }
            }

        });
    }


    public void placeMoss(Context context,BlockPos p_226065_) {
        context.setBlock(p_226065_, Blocks.MOSS_CARPET.defaultBlockState());
    }
}
