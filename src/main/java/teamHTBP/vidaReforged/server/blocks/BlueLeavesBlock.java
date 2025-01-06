package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.client.particles.VidaParticleTypeLoader;
import teamHTBP.vidaReforged.client.particles.options.BaseParticleType;
import teamHTBP.vidaReforged.client.particles.particles.VidaBaseParticle;
import teamHTBP.vidaReforged.client.particles.particles.VidaParticleAttributes;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.color.ColorTheme;

public class BlueLeavesBlock extends LeavesBlock {
    public BlueLeavesBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean decaying(BlockState p_221386_) {
        return false;
    }
    public void animateTick(BlockState state, Level level, BlockPos blockPos, RandomSource rand) {
        super.animateTick(state, level, blockPos, rand);
        if (rand.nextInt(10) == 0) {
            BlockPos blockpos = blockPos.below();
            BlockState blockstate = level.getBlockState(blockpos);
            if (!isFaceFull(blockstate.getCollisionShape(level, blockpos), Direction.UP)) {
                double d0 = (double)blockPos.getX() + rand.nextDouble();
                double d1 = (double)blockPos.getY() - 0.05D;
                double d2 = (double)blockPos.getZ() + rand.nextDouble();
                level.addParticle(new BaseParticleType(VidaParticleTypeLoader.VIDA_LEAVES_LIGHT_PARTICLE, new VidaParticleAttributes(
                        300,
                        0.1f,
                        ColorTheme.getColorThemeById("blue_leaves").getRandomColor(rand),
                        null,
                        new Vector3f()
                )), d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
