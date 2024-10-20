package teamHTBP.vidaReforged.server.events;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.core.ElementInteract;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.item.IVidaAttackableItem;
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
        float actualHurt = hurt;
        // 获取伤害信息
        DamageSource source = event.getSource();
        VidaElement mobElement = mob.getShieldType();

        // 玩家手持
        if(!source.isIndirect() && source.getEntity() != null && source.getEntity().getType() == EntityType.PLAYER){
            Player player = (Player) source.getEntity();
            actualHurt = doCalculatePlayerHandInHurt(mob, player.getMainHandItem(), actualHurt, true);
        }

        // 弹射物
        if(source.isIndirect() && source.getDirectEntity() != null && source.getDirectEntity() instanceof IVidaElementalEntity causeHurtMob){
            actualHurt = doCalculateProjectTileHurt(mob, causeHurtMob, hurt, true);
        }
        event.setAmount(actualHurt);
    }

    public static float doCalculatePlayerHandInHurt(IVidaShieldMob mob, ItemStack itemHandIn, float damage, boolean doActualShiedDecrease){
        if(!mob.hasShield()){
            return damage;
        }
        //
        if(itemHandIn.is(item -> item.get() instanceof IVidaElementalEntity)){
            IVidaAttackableItem attackableItem = (IVidaAttackableItem) itemHandIn.getItem();
            ElementInteract interact = VidaElement.getInteract(mob.getShieldType(), attackableItem.getElement());
            // 判断武器的伤害和护盾是否是相克
            if(interact == ElementInteract.CONFLICT || interact == ElementInteract.SAME){
                int factor = interact == ElementInteract.CONFLICT ? 2 : 1;
                int actualHurt = mob.decreaseShield(doActualShiedDecrease ? (int) (Math.ceil(damage) * factor) : 0);
                return Math.abs(actualHurt);
            }
        }
        return 1;
    }

    /**弹射物计算*/
    public static float doCalculateProjectTileHurt(IVidaShieldMob mob, IVidaElementalEntity causeHurtMob, float damage, boolean doActualShiedDecrease){
        ElementInteract interact = VidaElement.getInteract(mob.getShieldType(), causeHurtMob.getElement());
        // 穿刺或者没有护盾下，就是原来的伤害
        if(causeHurtMob.isIgnoreArmorDefense(mob) || !mob.hasShield()){
            return damage;
        }
        // 如果有护盾
        // 削减护盾量后，计算还可以削减的生命
        if(interact == ElementInteract.CONFLICT || interact == ElementInteract.SAME){
            // 相克 * 2, 相同 * 1
            int factor = interact == ElementInteract.CONFLICT ? 2 : 1;
            int actualHurt = mob.decreaseShield(doActualShiedDecrease ? (int) (Math.ceil(damage) * factor) : 0);
            return Math.abs(actualHurt);
        }
        // 没有相克属性但是有护盾
        return (int) Math.ceil(damage / 2.0F);
    }
}
