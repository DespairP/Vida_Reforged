package teamHTBP.vidaReforged.core.common.mutiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public class VidaMultiblock implements BlockAndTintGetter {
    protected Level world;

    public void setWorld(Level world) {
        this.world = world;
    }

    @Override
    public float getShade(Direction p_45522_, boolean p_45523_) {
        return 1.0f;
    }

    @Override
    public LevelLightEngine getLightEngine() {
        return world.getLightEngine();
    }

    @Override
    public int getBlockTint(BlockPos pos, ColorResolver colorResolver) {
        var plains = world.registryAccess().registryOrThrow(Registries.BIOME)
                .getOrThrow(Biomes.PLAINS);
        return colorResolver.getColor(plains, pos.getX(), pos.getZ());
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        return world.getBlockEntity(pos);
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return world.getBlockState(pos);
    }

    @Override
    public FluidState getFluidState(BlockPos p_45569_) {
        return Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public int getHeight() {
        return 255;
    }

    @Override
    public int getMinBuildHeight() {
        return 0;
    }

    @Override
    public int getRawBrightness(BlockPos p_45525_, int p_45526_) {
        return world.getRawBrightness(p_45525_, p_45526_);
    }

    @Override
    public int getBrightness(LightLayer p_45518_, BlockPos p_45519_) {
        return 15;
    }

    @Override
    public float getShade(float normalX, float normalY, float normalZ, boolean shade) {
        return 1.0f;
    }


}
