package teamHTBP.vidaReforged.client.events;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.client.renderer.VidaSkyEffects;
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class WorldEffectRegisterHandler {
    public static VidaSkyEffects effects;
    public static final ResourceLocation OVERWORLD_EFFECTS = new ResourceLocation("overworld");
    @SubscribeEvent
    public static void registerDimensionEffects(RegisterDimensionSpecialEffectsEvent event)
    {
        effects = new VidaSkyEffects();
        event.register(OVERWORLD_EFFECTS, effects);
        //event.register(StellarViewEndEffects.END_EFFECTS, end);
    }
}
