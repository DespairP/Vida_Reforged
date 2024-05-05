package teamHTBP.vidaReforged.client.particles;

import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.particles.options.BaseBezierParticleType;
import teamHTBP.vidaReforged.client.particles.options.BaseParticleType;
import teamHTBP.vidaReforged.client.particles.particles.*;
import teamHTBP.vidaReforged.core.utils.reg.RegisterParticleType;


public class VidaParticleTypeLoader {
    public final static DeferredRegister<ParticleType<?>> PARTICLE_TYPE_REGISTER = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, VidaReforged.MOD_ID);

    @RegisterParticleType(Cube3DParticle.class)
    public final static RegistryObject<ParticleType<BaseParticleType>> CUBE_PARTICLE_TYPE = PARTICLE_TYPE_REGISTER.register("cube_particle", ()->new BaseParticleType(VidaParticleTypeLoader.CUBE_PARTICLE_TYPE));

    @RegisterParticleType(CuboidParticle.class)
    public final static RegistryObject<ParticleType<BaseParticleType>> CUBOID_PARTICLE_TYPE = PARTICLE_TYPE_REGISTER.register("cuboid_particle", ()->new BaseParticleType(VidaParticleTypeLoader.CUBOID_PARTICLE_TYPE));

    @RegisterParticleType(SparkParticle.class)
    public final static RegistryObject<ParticleType<BaseParticleType>> SPARK_PARTICLE_TYPE = PARTICLE_TYPE_REGISTER.register("spark_particle", ()->new BaseParticleType(VidaParticleTypeLoader.SPARK_PARTICLE_TYPE));

    @RegisterParticleType(TrailParticle.class)
    public final static RegistryObject<ParticleType<BaseParticleType>> TRAIL_PARTICLE = PARTICLE_TYPE_REGISTER.register("trail_particle", ()->new BaseParticleType(VidaParticleTypeLoader.TRAIL_PARTICLE));

    @RegisterParticleType(OrbParticle.class)
    public final static RegistryObject<ParticleType<BaseParticleType>> ORB_PARTICLE = PARTICLE_TYPE_REGISTER.register("orb_type_particle", ()->new BaseParticleType(VidaParticleTypeLoader.ORB_PARTICLE));

    @RegisterParticleType(TinkleParticle.class)
    public final static RegistryObject<ParticleType<BaseParticleType>> TICKLE_PARTICLE = PARTICLE_TYPE_REGISTER.register("tinkle_particle", ()->new BaseParticleType(VidaParticleTypeLoader.TICKLE_PARTICLE));


    public final static RegistryObject<ParticleType<BaseBezierParticleType>> BEZIER_PARTICLE = PARTICLE_TYPE_REGISTER.register("bezier_particle", BaseBezierParticleType::new);

}
