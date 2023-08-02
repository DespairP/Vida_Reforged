package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.core.BlockPos;
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
import net.minecraftforge.common.util.LazyOptional;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;

public class MagicManaProvider extends Block {
    VidaElement element = VidaElement.EMPTY;

    public MagicManaProvider(VidaElement element) {
        super(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK));
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
}
