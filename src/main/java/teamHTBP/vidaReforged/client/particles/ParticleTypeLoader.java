package teamHTBP.vidaReforged.client.particles;

import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeDeferredRegistriesSetup;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.particles.options.BaseParticleType;
import teamHTBP.vidaReforged.client.particles.particles.Cube3DParticle;
import teamHTBP.vidaReforged.core.utils.reg.RegisterParticleType;

public class ParticleTypeLoader {
    public final static DeferredRegister<ParticleType<?>> PARTICLE_TYPE_REGISTER = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, VidaReforged.MOD_ID);

    @RegisterParticleType(Cube3DParticle.class)
    public final static RegistryObject<ParticleType<BaseParticleType>> BASE_PARTICLE_TYPE = PARTICLE_TYPE_REGISTER.register("cube_particle", BaseParticleType::new);
}
