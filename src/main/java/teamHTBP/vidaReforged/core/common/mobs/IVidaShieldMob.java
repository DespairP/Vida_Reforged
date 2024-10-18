package teamHTBP.vidaReforged.core.common.mobs;

import teamHTBP.vidaReforged.core.api.VidaElement;

public interface IVidaShieldMob {
    public boolean hasShield();

    public int getShield();

    public VidaElement getShieldType();

    public int decreaseShield(int hurt);
}
