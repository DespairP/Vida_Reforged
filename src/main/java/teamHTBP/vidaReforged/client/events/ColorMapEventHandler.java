package teamHTBP.vidaReforged.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.block.BlockTintCache;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.core.common.level.VidaBlueLeavesColor;
import teamHTBP.vidaReforged.core.common.level.VidaLeavesColor;
import teamHTBP.vidaReforged.core.utils.math.VidaMath;
import teamHTBP.vidaReforged.helper.VidaMathHelper;
import teamHTBP.vidaReforged.server.blocks.VidaBlockLoader;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ColorMapEventHandler {
    public static final ColorResolver VIDA_LEAVES_RESOLVER = (biome, x, z) -> {
        double d0 = (double) Mth.clamp(biome.getModifiedClimateSettings().temperature(), 0.0F, 1.0F);
        double d1 = (double)Mth.clamp(biome.getModifiedClimateSettings().downfall(), 0.0F, 1.0F);
        return VidaLeavesColor.get(d0, d1);
    };
    public static final ColorResolver VIDA_BLUE_LEAVES_RESOLVER = (biome, x, z) -> {
        double d0 = (double) Mth.clamp(biome.getModifiedClimateSettings().temperature(), 0.0F, 1.0F);
        double d1 = (double)Mth.clamp(biome.getModifiedClimateSettings().downfall(), 0.0F, 1.0F);
        return VidaBlueLeavesColor.get(d0, d1);
    };
    public static final BlockTintCache cache = new BlockTintCache((pos) -> {
        assert Minecraft.getInstance().level != null;
        return Minecraft.getInstance().level.calculateBlockTint(pos, VIDA_LEAVES_RESOLVER);
    });

    @SubscribeEvent
    public static void handleRegColorResolver(RegisterColorHandlersEvent.ColorResolvers event){
        event.register(VIDA_LEAVES_RESOLVER);
        event.register(VIDA_BLUE_LEAVES_RESOLVER);
    }

    @SubscribeEvent
    public static void handleRegColor(RegisterColorHandlersEvent.Block event){
        event.register(((BlockState state, @Nullable BlockAndTintGetter getter, @Nullable BlockPos pos, int param) -> {
            return getter != null && pos != null ? VidaLeavesColor.get(VidaMathHelper.getCyclicalityNumber(pos.getY(), 15), 1.0F) : VidaLeavesColor.get(0F,1F);
        }) , VidaBlockLoader.VIDA_LEAVES.get());
        event.register(((BlockState state, @Nullable BlockAndTintGetter getter, @Nullable BlockPos pos, int param) -> {
            return getter != null && pos != null ? VidaLeavesColor.get(VidaMathHelper.getCyclicalityNumber(pos.getY(), 15), 1.0F) : VidaLeavesColor.get(0F,1F);
        }) , VidaBlockLoader.VIDA_FALLEN_LEAVES.get());
        event.register(((BlockState state, @Nullable BlockAndTintGetter getter, @Nullable BlockPos pos, int param) -> {
            return getter != null && pos != null ? VidaBlueLeavesColor.get(VidaMathHelper.getCyclicalityNumber(pos.getY(), 20),1F) : VidaBlueLeavesColor.get(0F,1F);
        }) , VidaBlockLoader.VIDA_BLUE_LEAVES.get());
        event.register(((BlockState state, @Nullable BlockAndTintGetter getter, @Nullable BlockPos pos, int param) -> {
            return getter != null && pos != null ? VidaBlueLeavesColor.get(VidaMathHelper.getCyclicalityNumber(pos.getY(), 20),1F) : VidaBlueLeavesColor.get(0F,1F);
        }) , VidaBlockLoader.VIDA_BLUE_FALLEN_LEAVES.get());
    }

    @SubscribeEvent
    public static void addBlockColor(RegisterColorHandlersEvent.Item event){
        BlockColors colors = event.getBlockColors();
        event.getItemColors().register((itemstack, var_2)->{
            BlockState state = ((BlockItem)itemstack.getItem()).getBlock().defaultBlockState();
            return colors.getColor(state, null, null, var_2);
        }, VidaItemLoader.VIDA_LEAVES.get(), VidaItemLoader.VIDA_BLUE_LEAVES.get());
    }

}
