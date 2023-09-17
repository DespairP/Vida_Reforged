package teamHTBP.vidaReforged.server.items;

import net.minecraft.world.item.Item;
import teamHTBP.vidaReforged.core.api.VidaElement;

public class VidaWandEquipment extends Item {
    /**提供的魔法*/
    public String magicName = "";
    /**能消耗的能量*/
    public double manaCost = 0;
    /**消耗能量系数，如果相克使用的是倍数，相生使用的是除数*/
    public double manaUserMultiplier = 1;
    public double manaUserDivider = 1;
    /**所属元素*/
    public VidaElement element = VidaElement.EMPTY;
    /***/
    public int count = 0;



    public VidaWandEquipment() {
        super(new Properties().stacksTo(1));
    }
}
