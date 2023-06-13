package teamHTBP.vidaReforged.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teamHTBP.vidaReforged.client.particles.ParticleTypeLoader;
import teamHTBP.vidaReforged.client.particles.options.BaseParticleType;
import teamHTBP.vidaReforged.client.particles.providers.BaseParticleProvider;
import teamHTBP.vidaReforged.core.utils.reg.RegisterParticleType;

import java.lang.reflect.Field;
import java.util.*;

import static teamHTBP.vidaReforged.client.particles.ParticleTypeLoader.*;
public class ParticleProviderRegHandler {
    /** logger */
    public static final Logger LOGGER = LogManager.getLogger();
    /** 准备注册的Particle */
    public static Map<String, Map.Entry<RegistryObject<ParticleType<BaseParticleType>>, Class<? extends Particle>>> registerParticleType = new LinkedHashMap<>();
    @SubscribeEvent
    public static void onParticleFactoryRegistration(RegisterParticleProvidersEvent event) {
        //扫描
        try {
            init();
        } catch (IllegalAccessException | IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
        } finally {
            //
            registerParticleType.forEach((particleKey, particleInfo) ->{
                final RegistryObject<ParticleType<BaseParticleType>> particleType = particleInfo.getKey();
                final Class<? extends Particle> particleClazz = particleInfo.getValue();
                //注册
                registerProvider(particleType.get(), (particleSprite)->new BaseParticleProvider(particleSprite, particleClazz));
            });
        }
    }

    /**
     * 获取类中所有待注册的ParticleType
     * */
    private static void init() throws IllegalAccessException,IllegalArgumentException {
        final Map<String, Map.Entry<RegistryObject<ParticleType<BaseParticleType>>, Class<? extends Particle>>> registerObjs = new LinkedHashMap<>();
        //获取注释的字段
        for (Field decoratedParticleType : ParticleTypeLoader.class.getDeclaredFields()) {
            //通过注释的字段获取粒子对应的type和粒子Class
            if (decoratedParticleType.getType() == RegistryObject.class && decoratedParticleType.isAnnotationPresent(RegisterParticleType.class)) {
                decoratedParticleType.setAccessible(true);
                //获取class，在注释的value中
                final Class<? extends Particle> particleClazz = decoratedParticleType.getAnnotation(RegisterParticleType.class).value();
                if(particleClazz == null){
                    LOGGER.warn("{} is not set to the correct particle class,will skip register the particle,please have a check", decoratedParticleType.getName());
                    continue;
                }
                //获取粒子type
                final RegistryObject<ParticleType<BaseParticleType>> particleType = (RegistryObject<ParticleType<BaseParticleType>>) decoratedParticleType.get(null);
                //生成需要注册的粒子entrySet
                Map.Entry<RegistryObject<ParticleType<BaseParticleType>>, Class<? extends Particle>> particleEntry = new AbstractMap.SimpleImmutableEntry<>(
                        particleType,
                        particleClazz
                );
                //放入map中
                assert particleType.getKey() != null;
                registerObjs.put(particleType.getKey().registry().getPath(), particleEntry);
            }
        }
        //最后
        ParticleProviderRegHandler.registerParticleType = registerObjs;
    }

    /**注册*/
    public static <T extends ParticleOptions> void registerProvider(ParticleType<T> pParticleType, ParticleEngine.SpriteParticleRegistration<T> pParticleFactory){
        Minecraft.getInstance().particleEngine.register(pParticleType, pParticleFactory);
    }
}
