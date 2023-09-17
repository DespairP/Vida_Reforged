package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import teamHTBP.vidaReforged.client.particles.VidaParticleTypeLoader;
import teamHTBP.vidaReforged.client.particles.options.BaseParticleType;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;

public class MagicManaProvider extends Block {
    VidaElement element = VidaElement.EMPTY;

    public MagicManaProvider(VidaElement element) {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion());
        this.element = element;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if(level.isClientSide){
            return;
        }
        if(entity instanceof Player player){
            ItemStack handInItem = player.getItemInHand(InteractionHand.MAIN_HAND);
            if(!handInItem.is(VidaItemLoader.VIDA_WAND.get())){
                return;
            }

            LazyOptional<IVidaManaCapability> manaCap = handInItem.getCapability(VidaCapabilityRegisterHandler.VIDA_MANA);
            manaCap.ifPresent(cap -> cap.addMana(element, RandomSource.create().nextInt(20)));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource source) {
        if(!level.isClientSide){
            return;
        }
        if(source.nextBoolean()){
            return;
        }
        double d1 = 0.3 + (double)source.nextFloat() * 0.5;
        double d3 = 0.3 + (double)source.nextFloat() * 0.5;
        ARGBColor color = element.getBaseColor().toARGB();
        color.setA(source.nextInt(155) + 100);
        level.addParticle(
                new BaseParticleType(
                    VidaParticleTypeLoader.CUBOID_PARTICLE_TYPE.get(),
                    color,
                    0.5f + source.nextDouble(),
                    source.nextInt(150) + 30
                ),
                pos.getX() + d1,
                pos.getY() + 1.2f,
                pos.getZ() + d3,
                0.0D,
                0.01D,
                0.0D
        );
    }
}
