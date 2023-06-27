package teamHTBP.vidaReforged.server.blocks.crops;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.color.VidaColor;

import java.util.Random;

public class ParticleCropBlock extends BaseElementCropBlock{
    /**种子*/
    private final ItemLike provider;
    /***/
    protected boolean isMutations = false;


    /**
     * 一个可以在成年时生成粒子的植物
     *
     * @param stage    最大生长数
     * @param element  所属元素
     * @param provider 所提供的itemseed
     * @param color   粒子颜色，使用ColorHelper来表现rgb
     */
    public ParticleCropBlock(VidaElement element, ItemLike provider) {
        super(element);
        this.provider = provider;
    }

    /**
     * 一个*可变异*|*不变异时的植物成年时生成粒子*的植物
     *
     * @param stage       最大生长数
     * @param element     所属元素
     * @param provider    所提供的itemseed
     * @param color      粒子颜色，使用ColorHelper来表现rgb
     * @param isMutations 是否可以变异
     */
    public ParticleCropBlock(VidaElement element, ItemLike provider, VidaColor color, boolean isMutations) {
        super(element);
        this.provider = provider;
        this.isMutations = isMutations;
    }


    @Override
    protected ItemLike getSeedsItem() {
        return provider != null ? provider : super.getSeedsItem();
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
