package teamHTBP.vidaReforged.server.events;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagic;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class VidaMagicRegisterLoader {
    public static final RegistryBuilder<VidaMagic.IInvokable> MAGIC_REGISTRY = new RegistryBuilder<VidaMagic.IInvokable>()
            .setName(new ResourceLocation(VidaReforged.MOD_ID, "vida_magic"))
            .setDefaultKey(new ResourceLocation(VidaReforged.MOD_ID, "empty"))
            .setMaxID(256);

    public static Supplier<IForgeRegistry<VidaMagic.IInvokable>> MAGIC_SUPPLIER = null;

    @SubscribeEvent
    static void registerRegistries(NewRegistryEvent event) {
        MAGIC_SUPPLIER = event.create(MAGIC_REGISTRY);
    }
}
