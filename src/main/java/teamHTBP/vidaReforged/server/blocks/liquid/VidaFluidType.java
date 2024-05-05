package teamHTBP.vidaReforged.server.blocks.liquid;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class VidaFluidType extends FluidType {
    private ResourceLocation stillTexture;
    private ResourceLocation flowingTexture;
    private int tintColor;

    /**
     * Default constructor.
     *
     * @param properties     the general properties of the fluid type
     * @param stillTexture
     * @param flowingTexture
     * @param tintColor
     */
    public VidaFluidType(Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture, int tintColor) {
        super(properties);
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
        this.tintColor = tintColor;
    }

    @Override
    public int getDensity() {
        return super.getDensity();
    }

    @Override
    public FluidState getStateForPlacement(BlockAndTintGetter getter, BlockPos pos, FluidStack stack) {
        return super.getStateForPlacement(getter, pos, stack);
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public int getTintColor() {
                return tintColor;
            }

            @Override
            public ResourceLocation getStillTexture() {
                return stillTexture;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return flowingTexture;
            }

            @Override
            public @Nullable ResourceLocation getOverlayTexture() {
                return stillTexture;
            }

            @Override
            public int getTintColor(FluidStack stack) {
                return tintColor;
            }


        });
    }
}
