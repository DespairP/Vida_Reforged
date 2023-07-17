package teamHTBP.vidaReforged.server.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicWordCapability;
import teamHTBP.vidaReforged.server.providers.MagicWordManager;

import java.util.LinkedList;
import java.util.List;

public class VidaMagicWordCapability implements IVidaMagicWordCapability {
    public List<String> unlockedMagicWord;

    @Override
    public List<String> getAccessibleMagicWord() {
        return unlockedMagicWord;
    }

    @Override
    public boolean unlockMagicWord(String magicWordId) {
        if(!unlockedMagicWord.contains(magicWordId) && MagicWordManager.getMagicWord(magicWordId) != null){
            unlockedMagicWord.add(magicWordId);
        }
        return unlockedMagicWord.contains(magicWordId);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag listTag = new ListTag();

        for(int i = 0; i < unlockedMagicWord.size(); ++i){
            CompoundTag magicWordTag = new CompoundTag();
            magicWordTag.putByte("MagicWord", (byte)i);
            magicWordTag.putString("magicWordId", unlockedMagicWord.get(i));
            listTag.add(magicWordTag);
        }

        tag.put("unlockedMagicWord", listTag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        //
        unlockedMagicWord = new LinkedList<>();
        //获取词条
        ListTag magicListTag = (ListTag) nbt.get("unlockedMagicWord");
        if(magicListTag != null){
            for(int i = 0; i < magicListTag.size(); ++i) {
                CompoundTag magicTag = magicListTag.getCompound(i);
                unlockedMagicWord.add(magicTag.getString("magicWordId"));
            }
        }
    }
}
