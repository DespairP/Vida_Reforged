package teamHTBP.vidaReforged.server.events;

import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.checkerframework.checker.units.qual.A;
import teamHTBP.vidaReforged.core.ElementInteract;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.mobs.IVidaElementalEntity;
import teamHTBP.vidaReforged.core.common.mobs.IVidaShieldMob;

@Mod.EventBusSubscriber()
public class ShieldMobHandler {

    @SubscribeEvent
    public static void onShieldMobHurt(LivingDamageEvent event){
        if(event.getEntity() instanceof IVidaShieldMob mob){
            doCalculateHurt(mob, event, event.getAmount());
        }
    }

    public static void doCalculateHurt(IVidaShieldMob mob, LivingDamageEvent event, float hurt){
        // 获取伤害
        int ceilHurt = (int) Math.ceil(hurt);
        int actualHurt = ceilHurt;
        // 获取伤害信息
        DamageSource source = event.getSource();
        VidaElement mobElement = mob.getShieldType();

        // 玩家手持
        if(!source.isIndirect() && source.getEntity() != null && source.getEntity().getType() == EntityType.PLAYER){
            Player player = (Player) source.getEntity();
            // TODO
            actualHurt = 1;
        }

        // 弹射物
        if(source.getDirectEntity() != null && source.getDirectEntity() instanceof IVidaElementalEntity causeHurtMob){
            ElementInteract interact = VidaElement.getInteract(mobElement, causeHurtMob.getElement());
            // 削减护盾量后，计算还可以削减的生命
            actualHurt = mob.decreaseShield(interact == ElementInteract.CONFLICT ? ceilHurt * 10 : 1);
            actualHurt = Math.abs(actualHurt);
        }
        event.setAmount(actualHurt);
    }
}
