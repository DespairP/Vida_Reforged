package teamHTBP.vidaReforged.client.particles.options;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ExtraCodecs;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.client.events.registries.VidaParticleProviderAutoRegistryHandler;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;

import java.util.*;
import java.util.function.Supplier;

/**
 * 额外粒子的数据包
 *
 * */
public class BaseParticleType extends ParticleType<BaseParticleType> implements ParticleOptions {
    /**粒子颜色*/
    public ARGBColor color;
    /**粒子年龄*/
    public int lifeTime = 1000;
    /**大小*/
    public float scale = 1f;
    /**去往的坐标*/
    public Vector3f toPos = new Vector3f(0);
    /**option类型*/
    public static ParticleOptionType TYPE = ParticleOptionType.ADVANCED_RGBA_DEST;
    /**懒加载*/
    Supplier<ParticleType<BaseParticleType>> type;

    /**通用序列化器*/
    private final static Codec<BaseParticleType> CODEC = RecordCodecBuilder.create(it -> it.group(
            Codec.INT.fieldOf("alpha").orElse(1).forGetter(BaseParticleType::getAlpha),
            Codec.INT.fieldOf("red").orElse(1).forGetter(BaseParticleType::getColorRed),
            Codec.INT.fieldOf("green").orElse(1).forGetter(BaseParticleType::getColorGreen),
            Codec.INT.fieldOf("blue").orElse(1).forGetter(BaseParticleType::getColorBlue),
            ExtraCodecs.VECTOR3F.fieldOf("toPos").forGetter(BaseParticleType::getToPos),
            Codec.FLOAT.fieldOf("scale").orElse(1f).forGetter(BaseParticleType::getScale),
            Codec.INT.fieldOf("lifetime").forGetter(BaseParticleType::getLifeTime)
    ).apply(it,BaseParticleType::new));

    /**命令/网络序列化器*/
    private static final ParticleOptions.Deserializer<BaseParticleType> DESERIALIZER = new ParticleOptions.Deserializer<BaseParticleType>(){

        /**从命令行中获取额外数据,命令行不会读取toPos*/
        @Override
        public BaseParticleType fromCommand(ParticleType<BaseParticleType> pParticleType, StringReader pReader) throws CommandSyntaxException {
            //获取输入的字符串
            String params = pReader.getString();

            //获取实际的vida粒子
            List<String> paramList = Arrays.stream(params.split("\\s+")).toList();
            final String particleName = paramList.size() >= 2 ? paramList.get(1) : "";
            if(!VidaParticleProviderAutoRegistryHandler.registerParticleType.containsKey(particleName)){
                final String message = "unknown vida particle type";
                throw new CommandSyntaxException(new SimpleCommandExceptionType(Component.literal(message)), Component.literal(message), particleName, pReader.getCursor());
            }

            //颜色
            pReader.skipWhitespace();
            final int r = pReader.readInt();
            pReader.skipWhitespace();
            final int g = pReader.readInt();
            pReader.skipWhitespace();
            final int b = pReader.readInt();
            pReader.skipWhitespace();
            final int a = pReader.readInt();

            //大小
            pReader.skipWhitespace();
            float scale = pReader.readFloat();
            pReader.skipWhitespace();
            int lifeTime = pReader.readInt();

            return new BaseParticleType(VidaParticleProviderAutoRegistryHandler.registerParticleType.get(particleName).getKey().get(), a, r, g, b, new Vector3f(), scale, lifeTime);
        }

        /**从数据包获取数据*/
        @Override
        public BaseParticleType fromNetwork(ParticleType<BaseParticleType> pParticleType, FriendlyByteBuf pBuffer) {
            return new BaseParticleType(
                    pParticleType,
                    pBuffer.getInt(0),
                    pBuffer.getInt(1),
                    pBuffer.getInt(2),
                    pBuffer.getInt(3),
                    BaseParticleType.readVector3f(pBuffer),
                    pBuffer.getFloat(4),
                    pBuffer.getInt(5)
            );
        }
    };

    /**通用构造方法*/
    public BaseParticleType(ParticleType<BaseParticleType> type, int a, int r, int g, int b, Vector3f toPos, float scale, int age) {
        super(true, DESERIALIZER);
        this.type = () -> type;
        this.color = new ARGBColor(a, r, g, b);
        this.toPos = toPos;
        this.scale = scale;
        this.lifeTime = age;
    }

    /**通用构造方法，颜色用argb wrapper传入*/
    public BaseParticleType(ParticleType<BaseParticleType> type, ARGBColor color, Vector3f toPos, float scale, int age) {
        this(type, color.a(), color.r(), color.g(), color.b(), toPos, scale, age);
        this.type = () -> type;
    }

    /**注册使用*/
    public BaseParticleType(RegistryObject<ParticleType<BaseParticleType>> type) {
        super(true, DESERIALIZER);
        this.type = type::get;
        this.color = new ARGBColor(0, 0, 0, 0);
        this.toPos = new Vector3f();
        this.scale = 1;
        this.lifeTime = 0;
    }

    /**Codec*/
    private BaseParticleType(int a, int r, int g, int b, Vector3f toPos, float scale, int age) {
        super(true, DESERIALIZER);
        this.color = new ARGBColor(a, r, g, b);
        this.toPos = toPos;
        this.scale = scale;
        this.lifeTime = age;
    }


    /**获取粒子的类型*/
    @Override
    public @NotNull ParticleType<BaseParticleType> getType() {
        return type == null ? this : type.get();
    }

    /**发送粒子数据包时，写入额外数据*/
    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        pBuffer.writeInt(this.color.getA());
        pBuffer.writeInt(this.color.getR());
        pBuffer.writeInt(this.color.getG());
        pBuffer.writeInt(this.color.getB());
        pBuffer.writeVector3f(toPos);
        pBuffer.writeFloat(this.scale);
        pBuffer.writeInt(this.lifeTime);
    }

    /**debug*/
    @Override
    public @NotNull String writeToString() {
        return String.format(Locale.ROOT,"%s %d %d %d %d %.2f %s %d", this, getAlpha(), getColorRed(), getColorGreen(), getColorBlue(), getScale(), getToPos(), getLifeTime());
    }

    @Override
    public @NotNull Codec<BaseParticleType> codec() {
        return CODEC;
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

    public int getLifeTime() {
        return lifeTime;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector3f getToPos() {
        return toPos;
    }

    public void setToPos(Vector3f toPos) {
        this.toPos = toPos;
    }

    public void setColor(int r, int g, int b){
        this.color = new ARGBColor(255,r,g,b);
    }

    public static Vector3f readVector3f(FriendlyByteBuf p_254279_) {
        return new Vector3f(p_254279_.readFloat(), p_254279_.readFloat(), p_254279_.readFloat());
    }
}
