package teamHTBP.vidaReforged.server.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.util.ExtraCodecs;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.particles.options.BaseBezierParticleType;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.math.Bezier3Curve;

import java.util.ArrayList;
import java.util.List;

public class VidaEntityDataSerializer {
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

    public static final Codec<Vector3d> VECTOR3D = Codec.DOUBLE.listOf().comapFlatMap((p_253502_) -> {
        return Util.fixedSize(p_253502_, 3).map((p_253489_) -> {
            return new Vector3d(p_253489_.get(0), p_253489_.get(1), p_253489_.get(2));
        });
    }, (p_269787_) -> {
        return List.of(p_269787_.x(), p_269787_.y(), p_269787_.z());
    });

    public final static Codec<List<Vector3d>> VEC_CODEC = VECTOR3D.listOf().orElse(new ArrayList<>());

    public static final EntityDataSerializer<List<Vector3d>> VEC3F_LIST = new EntityDataSerializer.ForValueType<List<Vector3d>>(){
        @Override
        public void write(FriendlyByteBuf buf, @NotNull List<Vector3d> vecList) {
            buf.writeJsonWithCodec(VEC_CODEC, vecList);
        }

        @Override
        public List<Vector3d> read(FriendlyByteBuf buf) {
            return buf.readJsonWithCodec(VEC_CODEC);
        }
    };

    public final static DeferredRegister<net.minecraft.network.syncher.EntityDataSerializer<?>> DATA_SERIALIZER = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, VidaReforged.MOD_ID);


    public final static RegistryObject<EntityDataSerializer<List<Vector3d>>> VEC3D_SERIALIZER = DATA_SERIALIZER.register("vec3f_list", ()->VidaEntityDataSerializer.VEC3F_LIST );


    //public final static RegistryObject<net.minecraft.network.syncher.EntityDataSerializer<BaseBezierParticleType>> BEZIER_PARTICLE_SERIALIZER = DATA_SERIALIZER.register("bezier_particle", ()-> BEZIER_PARTICLE);

    //public final static RegistryObject<net.minecraft.network.syncher.EntityDataSerializer<Bezier3Curve>> CURVE_SERIALIZER = DATA_SERIALIZER.register("curve", ()-> CURVE);

    public final static RegistryObject<net.minecraft.network.syncher.EntityDataSerializer<ARGBColor>> COLOR_SERIALIZER = DATA_SERIALIZER.register("color", ()-> COLOR);

}
