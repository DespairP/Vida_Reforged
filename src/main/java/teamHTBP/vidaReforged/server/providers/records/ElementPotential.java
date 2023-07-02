package teamHTBP.vidaReforged.server.providers.records;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.world.item.Item;
import teamHTBP.vidaReforged.core.api.VidaElement;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ElementPotential {
        public Item item;
        @Expose(deserialize = false)
        public VidaElement element;
        public float energy;
}
