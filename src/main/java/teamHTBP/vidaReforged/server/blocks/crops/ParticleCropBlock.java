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
    private final ItemLike itemProvider;

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
