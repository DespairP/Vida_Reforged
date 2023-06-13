package teamHTBP.vidaReforged.core.common.block;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class DecoButtonBlock extends ButtonBlock {

    protected DecoButtonBlock(Properties pProperties) {
        super(pProperties, BlockSetType.f_271198_, 30, true);
    }

    @Override
    protected SoundEvent getSound(boolean pIsOn) {
        return SoundEvents.STONE_BUTTON_CLICK_OFF;
    }
}
