package teamHTBP.vidaReforged.core.common.system.magic;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import teamHTBP.vidaReforged.client.particles.VidaParticleTypeLoader;
import teamHTBP.vidaReforged.client.particles.options.BaseParticleType;
import teamHTBP.vidaReforged.core.common.system.magic.particle.MagicParticle;
import teamHTBP.vidaReforged.core.common.system.magic.particle.MagicParticleAttribute;
import teamHTBP.vidaReforged.core.common.system.magic.particle.MagicParticleType;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.server.entity.SparkEntity;
import teamHTBP.vidaReforged.server.entity.VidaEntityLoader;
import teamHTBP.vidaReforged.server.entity.projectile.PartyParrotProjecttile;

import java.util.Map;
import java.util.Optional;

public class VidaMagicManager {
    public static final VidaMagic.IInvokable PARTY_PARROT = (stack, invokeMagic, container, mana, level, player) -> {
        Entity entity = VidaEntityLoader.PARTY_PARROT.get().create(level);
        MagicParticle particle = new MagicParticle(
                0xFFFFFF,
                0xFFFFFF,
                new MagicParticleAttribute((float)container.speed()),
                new MagicParticleAttribute(container.amount()),
                new MagicParticleAttribute(container.maxAge()),
                new MagicParticleType(),
                new MagicParticleAttribute((float) container.damage()),
                Optional.ofNullable(invokeMagic).orElse(new VidaMagic("")).element()
        );
        if (entity instanceof PartyParrotProjecttile mpp) {
            mpp.initProjectile(player, particle);
            level.addFreshEntity(entity);
        }
    };

    public static final VidaMagic.IInvokable TRAILS = (stack, invokeMagic, container, mana, level, player) -> {

    };

    public static final VidaMagic.IInvokable SPARK = (stack, invokeMagic, container, mana, level, player) -> {
        Entity entity = VidaEntityLoader.SPARK.get().create(level);
        if (entity instanceof SparkEntity mpp) {
            mpp.initEntity(player, container.maxAge() * 10, ARGBColor.of(76, 255, 0));
            level.addFreshEntity(entity);
        }
    };

    public static final Map<String,VidaMagic.IInvokable> NAME_TO_MAGIC_MAP = ImmutableMap.of(
            "party_parrot", PARTY_PARROT,
            "spark", SPARK
    );


    public static LazyOptional<VidaMagic.IInvokable> getMagicInvokable(String magicId){
        return LazyOptional.of(() -> NAME_TO_MAGIC_MAP.get(magicId));
    }


    public static LazyOptional<VidaMagic.IInvokable> getMagicInvokableRegex(String magicRegex){
        for(String key : NAME_TO_MAGIC_MAP.keySet()){
            if(key.matches(magicRegex)){
                return LazyOptional.of(() -> NAME_TO_MAGIC_MAP.get(key));
            }
        }
        return LazyOptional.empty();
    }
}
