package teamHTBP.vidaReforged.server.blocks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.server.blocks.liquid.VidaFluidType;
import teamHTBP.vidaReforged.server.blocks.liquid.VividLiquidFluid;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class VidaFluidsLoader {
    public final static DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, MOD_ID);
    public final static DeferredRegister<FluidType> FLUID_TYPE = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, MOD_ID);

    public static final RegistryObject<FluidType> VIVID_TYPE = FLUID_TYPE.register("vivid_type",  () -> new VidaFluidType(
            FluidType.Properties.create(),
            new ResourceLocation(MOD_ID, "block/vivid_liquid_still"),
            new ResourceLocation(MOD_ID, "block/vivid_liquid_still"),
            0xF09EC69B)
    );
    public final static RegistryObject<FlowingFluid> VIVID_FLUID_STILL = FLUIDS.register("vivid_fluid_still", () -> new ForgeFlowingFluid.Source(VidaFluidsLoader.PROPERTIES));
    public final static RegistryObject<FlowingFluid> VIVID_FLUID_FLOW = FLUIDS.register("vivid_fluid_flow", () -> new ForgeFlowingFluid.Flowing(VidaFluidsLoader.PROPERTIES));
    public final static ForgeFlowingFluid.Properties PROPERTIES = new ForgeFlowingFluid.Properties(
            VIVID_TYPE::get,
            VIVID_FLUID_STILL::get,
            VIVID_FLUID_FLOW::get
    ).block(() -> (LiquidBlock) VidaBlockLoader.VIVID_LIQUID.get()).bucket(() -> VidaItemLoader.VIVID_BUCKET.get()).slopeFindDistance(2).levelDecreasePerBlock(2);



}
