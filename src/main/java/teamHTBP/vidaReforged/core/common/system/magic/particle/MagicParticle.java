package teamHTBP.vidaReforged.core.common.system.magic.particle;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.core.api.VidaElement;


/**
 * @param colorA  粒子基础色
 * @param colorB  粒子渐变色
 * @param speed   发射物速度
 * @param amount  发射物数量
 * @param maxAge  发射物最大持续时间
 * @param type    攻击
 * @param damage  伤害
 * @param element
 * @author TT432
 */
public record MagicParticle(
        int colorA,
        int colorB,
        MagicParticleAttribute speed,
        MagicParticleAttribute amount,
        MagicParticleAttribute maxAge,
        MagicParticleType type,
        MagicParticleAttribute damage,
        VidaElement element
) {
    public static final Codec<MagicParticle> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            Codec.INT.fieldOf("colorA").forGetter(MagicParticle::colorA),
            Codec.INT.fieldOf("colorB").forGetter(MagicParticle::colorB),
            MagicParticleAttribute.CODEC.fieldOf("speed").forGetter(MagicParticle::speed),
            MagicParticleAttribute.CODEC.fieldOf("amount").forGetter(MagicParticle::amount),
            MagicParticleAttribute.CODEC.fieldOf("maxAge").forGetter(MagicParticle::maxAge),
            MagicParticleType.CODEC.fieldOf("type").forGetter(MagicParticle::type),
            MagicParticleAttribute.CODEC.fieldOf("damage").forGetter(MagicParticle::damage),
            VidaElement.CODEC.fieldOf("element").forGetter(MagicParticle::element)
    ).apply(ins, MagicParticle::new));

    public CompoundTag toTag() {
        return CODEC.encodeStart(NbtOps.INSTANCE, this)
                .map(tag -> tag instanceof CompoundTag ct ? ct : new CompoundTag())
                .get()
                .left()
                .orElse(new CompoundTag());
    }

    @Nullable
    public static MagicParticle fromTag(CompoundTag tag) {
        return CODEC.decode(NbtOps.INSTANCE, tag).get().left().orElse(new Pair<>(null, null)).getFirst();
    }

    public static final MagicParticle EMPTY = new MagicParticle(
            0xFF_FF_FF_FF,
            0xFF_FF_FF_FF,
            new MagicParticleAttribute(0.2F),
            new MagicParticleAttribute(10),
            new MagicParticleAttribute(200),
            new MagicParticleType(),
            new MagicParticleAttribute(3),
            VidaElement.EMPTY
    );
}
