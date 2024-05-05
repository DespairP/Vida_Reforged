package teamHTBP.vidaReforged.server.blocks.liquid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.WaterFluid;
import net.minecraftforge.fluids.FluidType;
import teamHTBP.vidaReforged.server.blocks.VidaFluidsLoader;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;

public abstract class VividLiquidFluid extends FlowingFluid {
    @Override
    public Fluid getFlowing() {
        return VidaFluidsLoader.VIVID_FLUID_FLOW.get();
    }

    @Override
    public Fluid getSource() {
        return VidaFluidsLoader.VIVID_FLUID_STILL.get();
    }

    /**是否能产生水源*/
    protected boolean canConvertToSource(Level level) {
        return level.getGameRules().getBoolean(GameRules.RULE_WATER_SOURCE_CONVERSION);
    }

    /**在流水破坏方块前*/
    @Override
    protected void beforeDestroyingBlock(LevelAccessor levelAccessor, BlockPos pos, BlockState state) {
        BlockEntity blockentity = state.hasBlockEntity() ? levelAccessor.getBlockEntity(pos) : null;
    }

    @Override
    protected int getSlopeFindDistance(LevelReader levelReader) {
        return 4;
    }

    @Override
    protected int getDropOff(LevelReader p_76087_) {
        return 1;
    }

    @Override
    public Item getBucket() {
        return VidaItemLoader.VIVID_BUCKET.get();
    }

    @Override
    protected boolean canBeReplacedWith(FluidState p_76127_, BlockGetter p_76128_, BlockPos p_76129_, Fluid p_76130_, Direction p_76131_) {
        return false;
    }

    @Override
    public int getTickDelay(LevelReader levelReader) {
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 100;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState state) {
        return null;
    }

    @Override
    public int getAmount(FluidState p_164509_) {
        return 8;
    }

    @Override
    public FluidType getFluidType() {
        return super.getFluidType();
    }

    /**流动状态*/
    public static class Flowing extends VividLiquidFluid {
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        public boolean isSource(FluidState state) {
            return false;
        }
    }

    /**静止状态*/
    public static class Source extends VividLiquidFluid {
        public int getAmount(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }
}
