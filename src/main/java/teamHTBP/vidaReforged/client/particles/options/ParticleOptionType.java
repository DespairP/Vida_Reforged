package teamHTBP.vidaReforged.client.particles.options;

import net.minecraft.core.particles.SimpleParticleType;

/**
 * 粒子数据类型
 * pos:初始位置
 * delta:移动矢量（count = 0）或者生成空间 （count > 0）
 * speed:速度
 * force/normal：
 * */
public enum ParticleOptionType {
    /**基础的类型，遵循 {<pos> <delta> <speed> <count> [force|normal] [<viewers>]},{@link SimpleParticleType}*/
    BASIC,
    /**基础准色类型，遵循 {<rgb> <scale> <pos> <delta> <speed> <count> [force|normal] [<viewers>]},{@link net.minecraft.core.particles.DustParticleOptions}*/
    DUST_RGB,
    /**进阶类型，遵循 {<rgba> <scale> <pos> <delta> <speed> <count> [force|normal] [<viewers>]},{@link}*/
    ADVANCED_RGBA,
    /**收拢颜色类型，遵循 {<rgba> <to_pos> <scale> <life_time>  <pos> <delta> <speed> <count> [force|normal] [<viewers>]},{@link SimpleParticleType}*/
    ADVANCED_RGBA_DEST
}
