package teamHTBP.vidaReforged.server.blocks.liquid;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import teamHTBP.vidaReforged.server.blocks.VidaFluidsLoader;

import java.util.function.Supplier;

public class VividLiquidBlock extends LiquidBlock {
    public VividLiquidBlock() {
        super(() -> VidaFluidsLoader.VIVID_FLUID_STILL.get(), BlockBehaviour.Properties.of().lightLevel((level) -> 6).mapColor(MapColor.WATER).replaceable().noCollission().strength(100.0F).pushReaction(PushReaction.DESTROY).noLootTable().liquid().sound(SoundType.EMPTY));
    }
}
