package teamHTBP.vidaReforged.client.screen.viewModels;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import teamHTBP.vidaReforged.core.common.item.Position;
import teamHTBP.vidaReforged.core.common.ui.component.LiveData;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModel;
import teamHTBP.vidaReforged.server.menu.slots.VidaWandEquipmentSlot;
import teamHTBP.vidaReforged.server.packets.MagicSelectionPacket;
import teamHTBP.vidaReforged.server.packets.VidaPacketManager;
import teamHTBP.vidaReforged.server.providers.VidaMagicManager;

import java.util.HashMap;
import java.util.Map;

public class VidaWandCraftingViewModel extends ViewModel {
    public LiveData<ItemStack> itemVidaWand = new LiveData<>(ItemStack.EMPTY);

    public LiveData<Map<Position, VidaWandEquipmentSlot>> slots = new LiveData<>();
    public LiveData<Integer> skillIndex = new LiveData<>(-1);
    public LiveData<Map<Integer, ResourceLocation>> magics = new LiveData<>(new HashMap<>());

    /**初始化槽位数据*/
    public void setSlots(Map<Position, VidaWandEquipmentSlot> slots){
        this.slots.setValue(slots);
    }

    /**获取槽位数据*/
    public VidaWandEquipmentSlot getSlot(Position position){
        return this.slots.getValue().get(position);
    }

    /**初始化魔法数据*/
    public void setMagics(Map<Integer, ResourceLocation> magics){
        this.magics.setValue(magics);
    }

    /**设置魔法参数*/
    public void setMagicWithIndex(int index, ResourceLocation magic){
        Map<Integer, ResourceLocation> magicSelected = magics.getValue();
        magicSelected.put(index, magic);
        this.magics.setValue(magicSelected);
        VidaPacketManager.sendToServer(new MagicSelectionPacket(magicSelected));
    }

    public Map<Integer, ResourceLocation> getMagics() {
        return magics.getValue();
    }
}
