package teamHTBP.vidaReforged.server.blocks.crops;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.server.blocks.state.VidaBlockStateProperties;

import java.util.function.Supplier;

/**
 * 可变异农作物
 * */
public class MutationCrop extends BushBlock implements BonemealableBlock {
    /**变异会变成的方块*/
    private final Supplier<Block> mutateBlockSupplier;
    /**变异道具*/
    private final ItemLike mutateItemProvider;
    /***/
    private final int maxAge;
    /**最大可以接受变异的生长年龄*/
    private final int maxMutateAge;
    /**最小可以接受变异的生长年龄*/
    private final int minMutateAge;
    /**生长周期*/
    public static final IntegerProperty ageProperty = IntegerProperty.create("age", 0, 7);
    /**是否变异*/
    public static final BooleanProperty mutateProperty = VidaBlockStateProperties.MUTATE;
    /**是否接受骨粉催熟*/
    public boolean bonemealAvailable = false;
    /**额外生长逻辑*/
    public GrowCalculator extraGrowLogic;
    /***/
    public final VoxelShape shape = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D);
    /***/
    VidaElement element = VidaElement.EMPTY;

    public MutationCrop(VidaElement element, int maxAge, Supplier<Block> mutateBlockProvider, ItemLike mutateItemProvider) {
        super(Properties.of().noCollission().sound(SoundType.CROP).strength(0.5f, 0).randomTicks());
        this.mutateBlockSupplier = mutateBlockProvider;
        this.mutateItemProvider = mutateItemProvider;
        this.maxMutateAge = 0;
        this.minMutateAge = 0;
        this.maxAge = maxAge;
        this.registerDefaultState(this.stateDefinition.any().setValue(this.getAgeProperty(), Integer.valueOf(0)).setValue(mutateProperty, false));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return shape;
    }

    public int getMaxAge(){
        return maxAge;
    }

    public IntegerProperty getAgeProperty() {
        return ageProperty;
    }

    public int getAge(BlockState state){
        return state.getValue(getAgeProperty());
    }

    public final boolean isMaxAge(BlockState state) {
        return this.getAge(state) >= this.getMaxAge();
    }

    public final boolean isMutate(BlockState state) {
        return state.getValue(mutateProperty);
    }

    public boolean canMutate(BlockState state){
        int age = this.getAge(state);
        boolean isMutate = this.isMutate(state);
        return age >= minMutateAge && age <= maxMutateAge && !isMutate;
    }

    public boolean mayPlaceOn(BlockState state, BlockGetter getter, BlockPos pos) {
        return state.is(Blocks.FARMLAND);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(this.ageProperty);
        builder.add(this.mutateProperty);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos pos, BlockState state, boolean isClientSide) {
        return !this.isMaxAge(state) && !this.isMutate(state) && bonemealAvailable;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return bonemealAvailable;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource randomSource, BlockPos pos, BlockState state) {
        growCrops(level, pos, state);
    }

    /*当玩家碰撞的时候，破坏方块*/
    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof Ravager && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(worldIn, entityIn)) {
            worldIn.destroyBlock(pos, true, entityIn);
        }
        super.entityInside(state, worldIn, pos, entityIn);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, @NotNull RandomSource random) {
        // 防止区块未加载
        if (!level.isAreaLoaded(pos, 1)) return;
        //
        int i = this.getAge(state);
        boolean isMutate = this.isMutate(state);
        if (i < this.getMaxAge() || isMutate) {
            float growSpeed = getGrowthSpeed(this, level, pos);
            boolean shouldGrow = random.nextInt((int)(25.0F / growSpeed) + 1) == 0;
            // 通知事件
            net.minecraftforge.common.ForgeHooks.onCropsGrowPre(level, pos, state, shouldGrow);
            if (shouldGrow) {
                growCrops(level, pos, state);
                net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, pos, state);
            }
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
        if(!state.getValue(mutateProperty)){
            return;
        }
        float x = pos.getX();
        float y = pos.getY();
        float z = pos.getZ();
        if (rand.nextFloat() >= 0.75f) {
            double offsetX = rand.nextDouble() * 0.5D + 0.2D;
            double offsetZ = rand.nextDouble() * 0.5D + 0.2D;
            ARGBColor argbColor = element.baseColor.toARGB();
            level.addParticle(
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

    /**获取植物生长概率*/
    public float getGrowthSpeed(Block block, BlockGetter getter, BlockPos pos) {
        float baseSpeed = 1.0f;
        BlockPos below = pos.below();

        // 底部是否湿润
        if(getter.getBlockState(below).isFertile(getter, below)){
            baseSpeed += 4.0f;
        }

        if(extraGrowLogic != null){
            return extraGrowLogic.getGrowthSpeed(baseSpeed, block, getter, pos);
        }

        return baseSpeed;
    }


    /**作物生长*/
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        int currentAge = this.getAge(state);
        boolean isMutateActive = this.isMutate(state);

        // 如果已经触发了变异，直接变为另一个方块
        if(isMutateActive && mutateBlockSupplier != null){
            Block block = mutateBlockSupplier.get();
            level.setBlock(pos, block.defaultBlockState(), 2);
            return;
        }

        // 如果已经达到最大年龄，就不再生长
        int i = currentAge + 1;
        int j = this.getMaxAge();
        if (i > j) {
            i = j;
        }

        level.setBlock(pos, this.getStateForAge(state, i), 2);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return !this.isMaxAge(state) && this.isMutate(state);
    }

    protected BlockState getStateForAge(BlockState state, int i) {
        return this.defaultBlockState().setValue(mutateProperty, state.getValue(mutateProperty)).setValue(ageProperty, i);
    }

    protected BlockState getStateForMutate(BlockState state, boolean mutate) {
        return this.defaultBlockState().setValue(mutateProperty, mutate).setValue(ageProperty, state.getValue(ageProperty));
    }


    public interface GrowCalculator{
        public abstract float getGrowthSpeed(float prevSpeed, Block block, BlockGetter getter, BlockPos pos);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if(level.isClientSide){
            return super.use(blockState, level, pos, player, hand, result);
        }

        ItemStack handInItem = player.getItemInHand(hand);
        Item mutateItem = mutateItemProvider.asItem();

        if(!handInItem.isEmpty() && handInItem.is(mutateItem) && canMutate(blockState)){
            level.setBlock(pos, this.getStateForMutate(blockState, true),  2);
            handInItem.shrink(1);
            return InteractionResult.CONSUME;
        }

        return super.use(blockState, level, pos, player, hand, result);
    }

}
