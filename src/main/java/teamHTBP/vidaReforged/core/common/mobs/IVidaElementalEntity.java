package teamHTBP.vidaReforged.core.common.mobs;

import teamHTBP.vidaReforged.core.api.VidaElement;

public interface IVidaElementalEntity {
    public VidaElement getElement();

    public boolean isIgnoreArmorDefense(IVidaShieldMob mob);
}
