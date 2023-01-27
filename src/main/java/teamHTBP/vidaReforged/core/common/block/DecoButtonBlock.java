package teamHTBP.vidaReforged.core.common.block;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.ButtonBlock;

public class DecoButtonBlock extends ButtonBlock {

    protected DecoButtonBlock(Properties pProperties) {
        super(true, pProperties);
    }

    @Override
    protected SoundEvent getSound(boolean pIsOn) {
        return SoundEvents.STONE_BUTTON_CLICK_OFF;
    }
}
