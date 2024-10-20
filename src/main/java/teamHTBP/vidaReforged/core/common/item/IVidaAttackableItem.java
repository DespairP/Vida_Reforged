package teamHTBP.vidaReforged.core.common.item;

import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.mobs.IVidaShieldMob;

/**近战元素武器*/
public interface IVidaAttackableItem {
    public VidaElement getElement();

    public boolean isIgnoreArmorDefense(IVidaShieldMob mob);
}
