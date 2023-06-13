package teamHTBP.vidaReforged.client.particles.options;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.DustParticleOptionsBase;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;

import java.util.*;
import java.util.function.BiFunction;

/**
 * 额外粒子的数据包
 *
 * */
public class BaseParticleType extends ParticleType<BaseParticleType> implements ParticleOptions {
    /**颜色*/
    public ARGBColor color;
    /**粒子大小*/
    public double size = 1.0;

    /**序列化器*/
    private final static Codec<BaseParticleType> CODEC = RecordCodecBuilder.create(it -> it.group(
            Codec.INT.fieldOf("alpha").forGetter(BaseParticleType::getAlpha),
            Codec.INT.fieldOf("red").forGetter(BaseParticleType::getColorRed),
            Codec.INT.fieldOf("green").forGetter(BaseParticleType::getColorGreen),
            Codec.INT.fieldOf("blue").forGetter(BaseParticleType::getColorBlue),
            Codec.DOUBLE.fieldOf("size").forGetter(BaseParticleType::getSize)
    ).apply(it,BaseParticleType::new));

    private static final ParticleOptions.Deserializer<BaseParticleType> DESERIALIZER = new ParticleOptions.Deserializer<BaseParticleType>(){

        /**从命令行中获取额外数据*/
        @Override
        public BaseParticleType fromCommand(ParticleType<BaseParticleType> pParticleType, StringReader pReader) throws CommandSyntaxException {
            String params = pReader.getRead();
            //取speedX,speedY和SpeedZ作为粒子颜色
            List<String> paramList = Arrays.stream(params.split("\\s+")).toList();
            BiFunction<List<String>,Integer,Optional<Integer>> getByIndexOrNull = (list,index) -> {
                if(index > list.size()) {
                    return Optional.empty();
                }
                return Optional.of(NumberUtils.toInt(list.get(index), 255));
            };
            ARGBColor color = new ARGBColor(
                    255,
                    getByIndexOrNull.apply(paramList,4).orElse(255),
                    getByIndexOrNull.apply(paramList,5).orElse(255),
                    getByIndexOrNull.apply(paramList,6).orElse(255)
            );


            return new BaseParticleType(color, 1);
        }

        /**从数据包获取数据*/
        @Override
        public BaseParticleType fromNetwork(ParticleType<BaseParticleType> pParticleType, FriendlyByteBuf pBuffer) {
            return new BaseParticleType(pBuffer.getInt(0),pBuffer.getInt(1), pBuffer.getInt(2),pBuffer.getInt(3),pBuffer.getDouble(4));
        }
    };

    /**deserializer构造需要*/
    public BaseParticleType(int alpha, int colorRed, int colorGreen, int colorBlue, double size) {
        super(true, DESERIALIZER);
        this.color = new ARGBColor(alpha,colorRed,colorGreen,colorBlue);
        this.size = size;
    }

    /**Argb传入*/
    public BaseParticleType(ARGBColor color, double size) {
        super(true, DESERIALIZER);
        this.color = color;
        this.size = size;
    }

    /**注册使用*/
    public BaseParticleType() {
        super(true, DESERIALIZER);
    }

    @Override
    public @NotNull ParticleType<BaseParticleType> getType() {
        return this;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf pBuffer) {
        pBuffer.writeInt(this.color.getA());
        pBuffer.writeInt(this.color.getR());
        pBuffer.writeInt(this.color.getG());
        pBuffer.writeInt(this.color.getB());
        pBuffer.writeDouble(this.size);
    }

    @Override
    public @NotNull String writeToString() {
        return String.format(Locale.ROOT,"%s %d %d %d %d %.2f",this.toString(), getAlpha(), getColorRed(), getColorGreen(), getColorBlue(), getSize());
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

    public void setColor(int r,int g,int b){
        this.color = new ARGBColor(255,r,g,b);
    }

}
