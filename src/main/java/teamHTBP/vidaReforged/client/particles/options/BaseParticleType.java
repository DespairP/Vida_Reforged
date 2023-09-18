package teamHTBP.vidaReforged.client.particles.options;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.DustParticleOptionsBase;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import teamHTBP.vidaReforged.client.events.ParticleProviderRegHandler;
import teamHTBP.vidaReforged.client.particles.VidaParticleTypeLoader;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * 额外粒子的数据包
 *
 * */
public class BaseParticleType extends ParticleType<BaseParticleType> implements ParticleOptions {
    /**颜色*/
    public ARGBColor color;
    /**粒子大小*/
    public double size = 1.0;
    /**年龄*/
    public int age = 1000;

    /**序列化器*/
    private final static Codec<BaseParticleType> CODEC = RecordCodecBuilder.create(it -> it.group(
            Codec.INT.fieldOf("alpha").forGetter(BaseParticleType::getAlpha),
            Codec.INT.fieldOf("red").forGetter(BaseParticleType::getColorRed),
            Codec.INT.fieldOf("green").forGetter(BaseParticleType::getColorGreen),
            Codec.INT.fieldOf("blue").forGetter(BaseParticleType::getColorBlue),
            Codec.DOUBLE.fieldOf("size").forGetter(BaseParticleType::getSize),
            Codec.INT.fieldOf("lifeTime").forGetter(BaseParticleType::getAge)
    ).apply(it,BaseParticleType::new));

    private static final ParticleOptions.Deserializer<BaseParticleType> DESERIALIZER = new ParticleOptions.Deserializer<BaseParticleType>(){

        /**从命令行中获取额外数据*/
        @Override
        public BaseParticleType fromCommand(ParticleType<BaseParticleType> pParticleType, StringReader pReader) throws CommandSyntaxException {
            String params = pReader.getRead();
            List<String> paramList = Arrays.stream(params.split("\\s+")).toList();
            final String particleName = paramList.size() >= 2 ? paramList.get(1) : "";

            pReader.skipWhitespace();
            final int r = Optional.of(pReader.readInt()).orElse(255);
            pReader.skipWhitespace();
            final int g = Optional.of(pReader.readInt()).orElse(255);
            pReader.skipWhitespace();
            final int b = Optional.of(pReader.readInt()).orElse(255);
            pReader.skipWhitespace();

            ARGBColor color = new ARGBColor(255, r, g, b);

            final int lifeTime = Optional.of(pReader.readInt()).orElse(1000);
            pReader.skipWhitespace();

            return new BaseParticleType(ParticleProviderRegHandler.registerParticleType.get(particleName).getKey().get(), color, 1, lifeTime);
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
                    pBuffer.getDouble(4),
                    pBuffer.getInt(5)
            );
        }
    };

    /**deserializer构造需要*/
    public BaseParticleType(ParticleType<BaseParticleType> type,int alpha, int colorRed, int colorGreen, int colorBlue, double size, int age) {
        super(true, DESERIALIZER);
        this.color = new ARGBColor(alpha,colorRed,colorGreen,colorBlue);
        this.size = size;
        this.age = age;
    }

    /**Argb传入*/
    public BaseParticleType(ParticleType<BaseParticleType> type,ARGBColor color, double size, int age) {
        super(true, DESERIALIZER);
        this.type = () -> type;
        this.color = color;
        this.size = size;
        this.age = age;
    }

    public BaseParticleType(int alpha, int colorRed, int colorGreen, int colorBlue, double size, int age) {
        super(true, DESERIALIZER);
        this.color = new ARGBColor(alpha, colorRed, colorGreen, colorBlue);
        this.size = size;
        this.age = age;
    }

    Supplier<ParticleType<BaseParticleType>> type;

    /**注册使用*/
    public BaseParticleType(RegistryObject<ParticleType<BaseParticleType>> type) {
        super(true, DESERIALIZER);
        this.type = type::get;
    }

    @Override
    public @NotNull ParticleType<BaseParticleType> getType() {
        return type == null ? this : type.get();
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        pBuffer.writeInt(this.color.getA());
        pBuffer.writeInt(this.color.getR());
        pBuffer.writeInt(this.color.getG());
        pBuffer.writeInt(this.color.getB());
        pBuffer.writeDouble(this.size);
        pBuffer.writeInt(this.age);
    }

    @Override
    public @NotNull String writeToString() {
        return String.format(Locale.ROOT,"%s %d %d %d %d %.2f", this.toString(), getAlpha(), getColorRed(), getColorGreen(), getColorBlue(), getSize());
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

    public double getSize() {
        return size;
    }

    public int getAge() {
        return age;
    }

    public void setColor(int r, int g, int b){
        this.color = new ARGBColor(255,r,g,b);
    }

}
