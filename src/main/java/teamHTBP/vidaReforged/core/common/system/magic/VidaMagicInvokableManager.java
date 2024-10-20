package teamHTBP.vidaReforged.core.common.system.magic;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicContainerCapability;
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;
import teamHTBP.vidaReforged.core.common.system.magic.particle.MagicParticle;
import teamHTBP.vidaReforged.core.common.system.magic.particle.MagicParticleAttribute;
import teamHTBP.vidaReforged.core.common.system.magic.particle.MagicParticleType;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.server.entity.MultiblockSparkEntity;
import teamHTBP.vidaReforged.server.entity.SparkEntity;
import teamHTBP.vidaReforged.server.entity.StarGlintEntity;
import teamHTBP.vidaReforged.server.entity.VidaEntityLoader;
import teamHTBP.vidaReforged.server.entity.projectile.PartyParrotProjecttile;
import teamHTBP.vidaReforged.server.events.VidaMagicRegisterLoader;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;
import teamHTBP.vidaReforged.server.items.VidaWand;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class VidaMagicInvokableManager {
    public static final VidaMagic.IInvokable PARTY_PARROT = (stack, invokeMagic, level, player) -> {
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

    public static final VidaMagic.IInvokable TRAILS = (stack, invokeMagic, level, player) -> {
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

    /**活化魔法*/
    public static final VidaMagic.IInvokable PURIFY = ((stack, invokeMagic, level, player) -> {
        // 生成粒子
        IVidaMagicContainerCapability magicContainer = VidaWand.getMagicContainerCapability(stack).orElseThrow(NullPointerException::new);
        VidaElement element = magicContainer.getCurrentElementOverride() == VidaElement.EMPTY ? invokeMagic.element() : magicContainer.getCurrentElementOverride();
        MultiblockSparkEntity entity = VidaEntityLoader.MULTIBLOCK_TRAIL.get().create(level);
        if(entity != null){
            entity.init(player, element);
            level.addFreshEntity(entity);
        }
    });

    /***/
    public static final VidaMagic.IInvokable STAR_GLINT = ((stack, invokeMagic, level, player) -> {
        IVidaMagicContainerCapability magicContainer = VidaWand.getMagicContainerCapability(stack).orElseThrow(NullPointerException::new);
        IVidaManaCapability manaContainer = VidaWand.getManaCapability(stack).orElseThrow(NullPointerException::new);
        VidaElement element = magicContainer.getCurrentElementOverride() == VidaElement.EMPTY ? invokeMagic.element() : magicContainer.getCurrentElementOverride();
        // 如果是附魔树枝，选择最大的一个元素进行消耗
        if(stack.is(VidaItemLoader.VIDA_ENCHANTED_BRANCH.get())){
            Map<VidaElement, Double> allManas = manaContainer.getAllElementsMana();
            Optional<Map.Entry<VidaElement, Double>> result = allManas.entrySet().stream().filter(entry -> entry.getValue() > 0).findFirst();
            element = result.isPresent() ? result.get().getKey() : element;
        }
        StarGlintEntity entity = VidaEntityLoader.STAR_GLINT.get().create(level);
        if(entity != null){
            entity.init(player, 60, element);
            level.addFreshEntity(entity);
        }
        manaContainer.consumeMana(element, invokeMagic.attribute.baseCostMana());
    });

    public static final VidaMagic.IInvokable SPARK = (stack, invokeMagic, level, player) -> {
        Entity entity = VidaEntityLoader.SPARK.get().create(level);
        if (entity instanceof SparkEntity mpp) {
            mpp.initEntity(player, 10, ARGBColor.of(76, 255, 0));
            level.addFreshEntity(entity);
        }
    };

    public static final DeferredRegister<VidaMagic.IInvokable> VIDA_MAGIC = DeferredRegister.create(new ResourceLocation(VidaReforged.MOD_ID, "vida_magic"), VidaReforged.MOD_ID);
    public static final RegistryObject<VidaMagic.IInvokable> VIDA_PURIFY = VIDA_MAGIC.register("purification", () -> VidaMagicInvokableManager.PURIFY);
    public static final RegistryObject<VidaMagic.IInvokable> VIDA_STAR_GLINT = VIDA_MAGIC.register("star_glint", () -> VidaMagicInvokableManager.STAR_GLINT);

    public static VidaMagic.IInvokable getMagicInvokable(ResourceLocation magicId){
        return VidaMagicRegisterLoader.MAGIC_SUPPLIER.get().getValue(magicId);
    }

    public static VidaMagic.IInvokable getMagicInvokable(VidaMagic magic){
        VidaMagic.IInvokable invokable = VidaMagicInvokableManager.getMagicInvokable(magic.magicId());
        if(magic.magicId() != null || invokable == null){
            return invokable;
        }
        return ($var1,$var2,$var3,$var4) -> {};
    }
}
