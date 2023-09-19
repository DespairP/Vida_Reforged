package teamHTBP.vidaReforged.server.entity;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3d;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.particles.options.BaseBezierParticleType;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.math.Bezier3Curve;

public class EntityDataSerializer {
    public static final net.minecraft.network.syncher.EntityDataSerializer<BaseBezierParticleType> BEZIER_PARTICLE = new net.minecraft.network.syncher.EntityDataSerializer.ForValueType<BaseBezierParticleType>() {
        public void write(FriendlyByteBuf p_238133_, BaseBezierParticleType p_238134_) {
            p_238133_.writeId(BuiltInRegistries.PARTICLE_TYPE, p_238134_.getType());
            p_238134_.writeToNetwork(p_238133_);
        }

        public BaseBezierParticleType read(FriendlyByteBuf p_238139_) {
            return this.readParticle(p_238139_, (ParticleType<BaseBezierParticleType>) p_238139_.readById(BuiltInRegistries.PARTICLE_TYPE));
        }

        private <T extends ParticleOptions> T readParticle(FriendlyByteBuf p_238136_, ParticleType<T> p_238137_) {
            return p_238137_.getDeserializer().fromNetwork(p_238137_, p_238136_);
        }
    };

    public static final net.minecraft.network.syncher.EntityDataSerializer<Bezier3Curve> CURVE = new net.minecraft.network.syncher.EntityDataSerializer.ForValueType<Bezier3Curve>() {

        @Override
        public void write(FriendlyByteBuf buf, Bezier3Curve curve) {
            buf.writeDouble(curve.pos0.x);
            buf.writeDouble(curve.pos0.y);
            buf.writeDouble(curve.pos0.z);
            buf.writeDouble(curve.pos1.x);
            buf.writeDouble(curve.pos1.y);
            buf.writeDouble(curve.pos1.z);
            buf.writeDouble(curve.pos2.x);
            buf.writeDouble(curve.pos2.y);
            buf.writeDouble(curve.pos2.z);
            buf.writeDouble(curve.pos3.x);
            buf.writeDouble(curve.pos3.y);
            buf.writeDouble(curve.pos3.z);
        }

        @Override
        public Bezier3Curve read(FriendlyByteBuf buf) {
            return new Bezier3Curve(
                    new Vector3d(
                            buf.readDouble(),
                            buf.readDouble(),
                            buf.readDouble()
                    ),
                    new Vector3d(
                            buf.readDouble(),
                            buf.readDouble(),
                            buf.readDouble()
                    ),
                    new Vector3d(
                            buf.readDouble(),
                            buf.readDouble(),
                            buf.readDouble()
                    ),
                    new Vector3d(
                            buf.readDouble(),
                            buf.readDouble(),
                            buf.readDouble()
                    )
            );
        }
    };

    public static final net.minecraft.network.syncher.EntityDataSerializer<ARGBColor> COLOR = new net.minecraft.network.syncher.EntityDataSerializer.ForValueType<ARGBColor>() {

        @Override
        public void write(FriendlyByteBuf buf, ARGBColor color) {
            buf.writeInt(color.a());
            buf.writeInt(color.r());
            buf.writeInt(color.g());
            buf.writeInt(color.b());

        }

        @Override
        public ARGBColor read(FriendlyByteBuf buf) {
            return new ARGBColor(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());
        }
    };

    public final static DeferredRegister<net.minecraft.network.syncher.EntityDataSerializer<?>> DATA_SERIALIZER = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, VidaReforged.MOD_ID);

    public final static RegistryObject<net.minecraft.network.syncher.EntityDataSerializer<BaseBezierParticleType>> BEZIER_PARTICLE_SERIALIZER = DATA_SERIALIZER.register("bezier_particle", ()-> BEZIER_PARTICLE);

    public final static RegistryObject<net.minecraft.network.syncher.EntityDataSerializer<Bezier3Curve>> CURVE_SERIALIZER = DATA_SERIALIZER.register("curve", ()-> CURVE);

    public final static RegistryObject<net.minecraft.network.syncher.EntityDataSerializer<ARGBColor>> COLOR_SERIALIZER = DATA_SERIALIZER.register("color", ()-> COLOR);

}
