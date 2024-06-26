package teamHTBP.vidaReforged.core.common.system.magic;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import org.joml.Vector3d;
import teamHTBP.vidaReforged.core.common.system.magic.particle.MagicParticle;
import teamHTBP.vidaReforged.core.common.system.magic.particle.MagicParticleAttribute;
import teamHTBP.vidaReforged.core.common.system.magic.particle.MagicParticleType;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.math.Bezier3Curve;
import teamHTBP.vidaReforged.server.entity.SparkEntity;
import teamHTBP.vidaReforged.server.entity.LazerEntity;
import teamHTBP.vidaReforged.server.entity.VidaEntityLoader;
import teamHTBP.vidaReforged.server.entity.projectile.PartyParrotProjecttile;

import java.util.Map;
import java.util.Optional;

public class VidaMagicInvokableManager {
    public static final VidaMagic.IInvokable PARTY_PARROT = (stack, invokeMagic, container, mana, level, player) -> {
        Entity entity = VidaEntityLoader.PARTY_PARROT.get().create(level);
        //TODO
        MagicParticle particle = new MagicParticle(
                0xFFFFFF,
                0xFFFFFF,
                new MagicParticleAttribute(0.1f),
                new MagicParticleAttribute(1),
                new MagicParticleAttribute(1),
                new MagicParticleType(),
                new MagicParticleAttribute((float) 1),
                Optional.ofNullable(invokeMagic).orElse(new VidaMagic()).element()
        );
        if (entity instanceof PartyParrotProjecttile mpp) {
            mpp.initProjectile(player, particle);
            level.addFreshEntity(entity);
        }
    };

    public static final VidaMagic.IInvokable TRAILS = (stack, invokeMagic, container, mana, level, player) -> {
        Vec3 lookAngle = player.getLookAngle();
        Direction direction = player.getDirection();
        Vec3 lookAt = player.getEyePosition().add(lookAngle.scale(2));
        Vec3 lookAtP = lookAt.add(new Vec3(direction.step().mul(10)));
        double x = lookAt.x;
        double y = lookAt.y;
        double z = lookAt.z;
        double speedX = lookAtP.x;
        double speedY = lookAtP.y;
        double speedZ = lookAtP.z;

//        Entity _entity1 = VidaEntityLoader.TRAIL.get().create(level);
//        ((LazerEntity)_entity1).initParticle(player, new Bezier3Curve(
//                new Vector3d(x, y, z),
//                new Vector3d(x + 10, y + 10, z),
//                new Vector3d(x + 10, y + 10, z + 10),
//                new Vector3d(speedX, speedY, speedZ)
//        ), new ARGBColor(255, 255, 255, 255), new ARGBColor(255, 226, 235, 240));
//        level.addFreshEntity(_entity1);
//
//        Entity _entity2 = VidaEntityLoader.TRAIL.get().create(level);
//        ((LazerEntity)_entity2).initParticle(player, new Bezier3Curve(
//                new Vector3d(x, y, z),
//                new Vector3d(x, y + 10, z - 10),
//                new Vector3d(x - 10, y + 10, z - 10),
//                new Vector3d(speedX, speedY, speedZ)
//        ), new ARGBColor(255, 255, 255, 255), new ARGBColor(255, 226, 235, 240));
//        level.addFreshEntity(_entity2);
    };

    public static final VidaMagic.IInvokable SPARK = (stack, invokeMagic, container, mana, level, player) -> {
        Entity entity = VidaEntityLoader.SPARK.get().create(level);
        if (entity instanceof SparkEntity mpp) {
            mpp.initEntity(player, 10, ARGBColor.of(76, 255, 0));
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

    public static LazyOptional<VidaMagic.IInvokable> getMagicInvokable(VidaMagic magic){
        if(magic.id() != null){
            return VidaMagicInvokableManager.getMagicInvokable(magic.id());
        }
        return LazyOptional.empty();
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
