package teamHTBP.vidaReforged.core.common.block.templates;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import teamHTBP.vidaReforged.core.common.block.DecoBlockProperties;

import java.util.Optional;

public class DecoButtonBlock extends ButtonBlock {

    public DecoButtonBlock(Properties pProperties, DecoBlockProperties decoBlockProperties) {
        super(
            pProperties,
            Optional.ofNullable(decoBlockProperties.blockSetType()).orElse(BlockSetType.OAK),
            30,
            true
        );
    }

    @Override
    protected SoundEvent getSound(boolean pIsOn) {
        return SoundEvents.STONE_BUTTON_CLICK_OFF;
    }
}
