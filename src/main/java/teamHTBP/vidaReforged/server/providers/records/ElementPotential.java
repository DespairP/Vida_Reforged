package teamHTBP.vidaReforged.server.providers.records;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.item.Item;
import teamHTBP.vidaReforged.core.api.VidaElement;

@Getter
@Setter
public class ElementPotential {
        public Item item;
        @Expose(deserialize = false)
        public VidaElement element;
        public float energy;
}
