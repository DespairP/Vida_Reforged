package teamHTBP.vidaReforged.client.particles.options;

import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import teamHTBP.vidaReforged.client.particles.VidaParticleTypeLoader;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.json.JsonUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BaseBezierParticleType extends ParticleType<BaseBezierParticleType> implements ParticleOptions{
    protected List<Vector3d> tails = new ArrayList<>();
    /**颜色*/
    public ARGBColor color;
    /**粒子大小*/
    public double size = 1.0;
    /**年龄*/
    public int age = 1000;

    private final static Codec<Vector3d> POS3D = RecordCodecBuilder.create(it -> it.group(
            Codec.DOUBLE.fieldOf("x").forGetter(Vector3d::x),
            Codec.DOUBLE.fieldOf("y").forGetter(Vector3d::y),
            Codec.DOUBLE.fieldOf("z").forGetter(Vector3d::z)
    ).apply(it,Vector3d::new));

    /**序列化器*/
    private final static Codec<BaseBezierParticleType> CODEC = RecordCodecBuilder.create(it -> it.group(
            Codec.INT.fieldOf("alpha").forGetter(BaseBezierParticleType::getAlpha),
            Codec.INT.fieldOf("red").forGetter(BaseBezierParticleType::getColorRed),
            Codec.INT.fieldOf("green").forGetter(BaseBezierParticleType::getColorGreen),
            Codec.INT.fieldOf("blue").forGetter(BaseBezierParticleType::getColorBlue),
            Codec.DOUBLE.fieldOf("size").forGetter(BaseBezierParticleType::getSize),
            Codec.INT.fieldOf("lifeTime").forGetter(BaseBezierParticleType::getAge),
            POS3D.listOf().fieldOf("vectors").forGetter(BaseBezierParticleType::getTails)
    ).apply(it, BaseBezierParticleType::new));

    public BaseBezierParticleType(){
        this(1, 0, 0, 0, 1, 10, new ArrayList<>());
    }

    public BaseBezierParticleType(ParticleType<BaseBezierParticleType> type, int alpha, int colorRed, int colorGreen, int colorBlue, double size, int age, List<Vector3d> vector3ds) {
        super(true, DESERIALIZER);
        tails = vector3ds;
        this.color = new ARGBColor(alpha,colorRed,colorGreen,colorBlue);
        this.size = size;
        this.age = age;
    }

    public BaseBezierParticleType(int alpha, int colorRed, int colorGreen, int colorBlue, double size, int age, List<Vector3d> vector3ds) {
        super(true, DESERIALIZER);
        tails = vector3ds;
        this.color = new ARGBColor(alpha,colorRed,colorGreen,colorBlue);
        this.size = size;
        this.age = age;
    }

    private static final ParticleOptions.Deserializer<BaseBezierParticleType> DESERIALIZER = new ParticleOptions.Deserializer<BaseBezierParticleType>(){

        @Override
        public @NotNull BaseBezierParticleType fromCommand(ParticleType<BaseBezierParticleType> p_123733_, StringReader p_123734_) throws CommandSyntaxException {
            return new BaseBezierParticleType();
        }

        @Override
        public @NotNull BaseBezierParticleType fromNetwork(ParticleType<BaseBezierParticleType> pParticleType, FriendlyByteBuf pBuffer) {
            Type vectorType = new TypeToken<List<Vector3d>>() {}.getType();
            return new BaseBezierParticleType(
                    pParticleType,
                    pBuffer.getInt(0),
                    pBuffer.getInt(1),
                    pBuffer.getInt(2),
                    pBuffer.getInt(3),
                    pBuffer.getDouble(4),
                    pBuffer.getInt(5),
                    JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL).fromJson(pBuffer.readUtf(), vectorType)
            );
        }
    };

    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        pBuffer.writeInt(this.color.getA());
        pBuffer.writeInt(this.color.getR());
        pBuffer.writeInt(this.color.getG());
        pBuffer.writeInt(this.color.getB());
        pBuffer.writeDouble(this.size);
        pBuffer.writeInt(this.age);
        pBuffer.writeUtf(JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL).toJson(tails));
    }

    @Override
    public String writeToString() {
        return String.format(Locale.ROOT,"%s %d %d %d %d %.2f %s",this.toString(), getAlpha(), getColorRed(), getColorGreen(), getColorBlue(), getSize(), getTails());
    }

    @Override
    public @NotNull Codec<BaseBezierParticleType> codec() {
        return CODEC;
    }

    @Override
    public @NotNull ParticleType<BaseBezierParticleType> getType() {
        return VidaParticleTypeLoader.BEZIER_PARTICLE.get();
    }



    public void setTails(List<Vector3d> tails) {
        this.tails = tails;
    }

    public List<Vector3d> getTails() {
        return tails;
    }


    public int getColorRed() {
        return color.getR();
    }

    public int getColorGreen() {
        return color.getG();
    }

    public int getColorBlue() {
        return color.getB();
    }

    public int getAlpha(){
        return color.getA();
    }

    public double getSize() {
        return size;
    }

    public int getAge() {
        return age;
    }

    public void setColor(int r, int g, int b){
        this.color = new ARGBColor(255,r,g,b);
    }

    public void toTag(CompoundTag tag){
        CompoundTag tagParticle = new CompoundTag();
        tagParticle.putInt("a", getAlpha());
        tagParticle.putInt("r", getColorRed());
        tagParticle.putInt("g", getColorGreen());
        tagParticle.putInt("b", getColorBlue());
        tagParticle.putDouble("size", getSize());
        tagParticle.putInt("age", getAge());
        tagParticle.putString("tails", JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL).toJson(tails));

        tag.put("particleTail", tagParticle);
    }

    public static BaseBezierParticleType fromTag(CompoundTag tag){
        BaseBezierParticleType type = new BaseBezierParticleType();
        CompoundTag tagParticle = (CompoundTag) tag.get("particleTail");
        assert tagParticle != null;
        type.color = new ARGBColor(
                tagParticle.getInt("a"),
                tagParticle.getInt("r"),
                tagParticle.getInt("g"),
                tagParticle.getInt("b")
        );
        Type vectorType = new TypeToken<List<Vector3d>>() {}.getType();
        type.size = tagParticle.getDouble("size");
        type.age = tagParticle.getInt("age");
        type.tails = JsonUtils.getGson(JsonUtils.JsonUtilType.NORMAL).fromJson(tagParticle.getString("tails"), vectorType);

        return type;
    }
}
