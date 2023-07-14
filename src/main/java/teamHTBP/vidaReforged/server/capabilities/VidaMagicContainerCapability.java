package teamHTBP.vidaReforged.server.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicContainerCapability;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagicContainer;

import java.util.LinkedList;
import java.util.List;

public class VidaMagicContainerCapability implements IVidaMagicContainerCapability {
    private VidaMagicContainer magicContainer;

    public VidaMagicContainerCapability() {
        this.magicContainer = VidaMagicContainer.empty();
    }

    @Override
    public VidaMagicContainer getContainer() {
        if(this.magicContainer == null){
            this.magicContainer = VidaMagicContainer.empty();
        }
        return this.magicContainer;
    }

    @Override
    public boolean setContainer(VidaMagicContainer container) {
        this.magicContainer = container;
        return true;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        VidaMagicContainer container = getContainer();
        tag.putDouble("damage", container.damage());
        tag.putDouble("multiplier", container.multiplier());
        tag.putDouble("decreaser", container.decreaser());
        tag.putDouble("costMana", container.costMana());
        tag.putInt("amount", container.amount());
        tag.putInt("invokeCount", container.invokeCount());
        tag.putInt("maxInvokeCount", container.maxInvokeCount());
        tag.putLong("coolDown", container.coolDown());
        tag.putLong("lastInvokeMillSec", container.lastInvokeMillSec());
        tag.putInt("level", container.level());

        ListTag magicListTag = new ListTag();

        for(int i = 0; i < magicContainer.magic().size(); ++i){
            CompoundTag magicTag = new CompoundTag();
            magicTag.putByte("Magic", (byte)i);
            magicTag.putString("magicId", magicContainer.magic().get(i));
            magicListTag.add(magicTag);
        }

        tag.put("magic", magicListTag);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        double damage = nbt.getDouble("damage");
        double multiplier = nbt.getDouble("multiplier");
        double decreaser = nbt.getDouble("decreaser");
        double costMana = nbt.getDouble("costMana");
        int amount = nbt.getInt("amount");
        int invokeCount = nbt.getInt("invokeCount");
        int maxInvokeCount = nbt.getInt("maxInvokeCount");
        long coolDown  = nbt.getLong("coolDown");
        long lastInvokeMillSec  = nbt.getLong("lastInvokeMillSec");
        int level  = nbt.getInt("level");
        List<String> magics = new LinkedList<>();

        //获取魔法
        ListTag magicListTag = (ListTag) nbt.get("magic");
        if(magicListTag != null){
            for(int i = 0; i < magicListTag.size(); ++i) {
                CompoundTag magicTag = magicListTag.getCompound(i);
                magics.add(magicTag.getString("magicId"));
            }
        }


        getContainer().damage(damage)
                .multiplier(multiplier)
                .decreaser(decreaser)
                .costMana(costMana)
                .amount(amount)
                .invokeCount(invokeCount)
                .maxInvokeCount(maxInvokeCount)
                .coolDown(coolDown)
                .lastInvokeMillSec(lastInvokeMillSec)
                .level(level)
                .magic(magics);
    }

}
