package teamHTBP.vidaReforged.server.blocks.crops;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.client.particles.VidaParticleTypeLoader;
import teamHTBP.vidaReforged.client.particles.options.BaseParticleType;
import teamHTBP.vidaReforged.client.particles.particles.VidaParticleAttributes;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.color.ColorTheme;
import teamHTBP.vidaReforged.core.utils.color.TwoValueGradientColor;
import teamHTBP.vidaReforged.server.blocks.state.VidaBlockStateProperties;

import java.util.function.Supplier;

public class VidaParticleCropBlock extends BaseElementCropBlock{
    /** 种子 */
    private final ItemLike seedProvider;
    /** 是否可以变异 */
    private boolean canMutate;
    /** 变异后方块植株 */
    private Supplier<BlockState> mutateBlockStateProvider;
    /** 是否变异 */
    public static final BooleanProperty MUTATE = VidaBlockStateProperties.MUTATE;

    /**
     * 一个可以在成年时生成粒子的植物
     *
     * @param element  所属元素
     * @param provider 所提供的itemseed
     */
    public VidaParticleCropBlock(VidaElement element, ItemLike provider) {
        super(element);
        this.canMutate = false;
        this.seedProvider = provider;
    }

    /**
     * 一个可以在成年时生成粒子的植物，可变异植株
     *
     * @param element  所属元素
     * @param provider 所提供的itemseed
     */
    public VidaParticleCropBlock(VidaElement element, ItemLike provider, Supplier<BlockState> mutateBlockStateProvider) {
        this(element, provider);
        this.canMutate = true;
        this.mutateBlockStateProvider = mutateBlockStateProvider;
        this.registerDefaultState(this.stateDefinition.any().setValue(this.getAgeProperty(), 0).setValue(MUTATE, false));
    }

    /**
     * 注册BlockState
     * */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(MUTATE);
    }

    /**
     * 获取该方块的种子物品
     * */
    @Override
    protected ItemLike getSeedsItem() {
        return seedProvider != null ? seedProvider : super.getSeedsItem();
    }

    /**
     * 是否可以变异
     * */
    public boolean isMutate(BlockState state) {
        return state.getValue(MUTATE) && state.getValue(getAgeProperty()) < getMaxAge();
    }

    public boolean isCanMutate() {
        return canMutate;
    }

    /**作物生长*/
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        // 如果不能变异，走子类逻辑
        if(!isCanMutate()){
            super.growCrops(level, pos, state);
            return;
        }
        // 能变异，走验证逻辑
        int currentAge = this.getAge(state);
        boolean isMutateActive = this.isMutate(state);

        // 如果已经触发了变异，直接变为另一个方块
        if(isMutateActive){
            BlockState block = mutateBlockStateProvider.get();
            level.setBlock(pos, block, 2);
            return;
        }

        // 如果已经达到最大年龄，就不再生长
        int i = currentAge + 1;
        int j = this.getMaxAge();
        if (i > j) {
            i = j;
        }

        level.setBlock(pos, this.getStateForAge(i), 2);
    }

    /**
     *
     * */
    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        float x = pos.getX();
        float y = pos.getY();
        float z = pos.getZ();
        ColorTheme theme = ColorTheme.getColorThemeByElement(getElement());
        TwoValueGradientColor gradientColor = theme.getRandomGradient(rand);
        if (isMaxAge(stateIn) && rand.nextFloat() >= 0.75f) {
            double offsetX = rand.nextDouble() * 0.5D + 0.2D;
            double offsetZ = rand.nextDouble() * 0.5D + 0.2D;
            worldIn.addParticle(
                    new BaseParticleType(
                            VidaParticleTypeLoader.CUBE_2D_PARTICLE_TYPE,
                            new VidaParticleAttributes(120, rand.nextFloat() * 0.08f, gradientColor.getFromColor(), gradientColor.getToColor(), BlockPos.ZERO.getCenter().toVector3f())),
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
