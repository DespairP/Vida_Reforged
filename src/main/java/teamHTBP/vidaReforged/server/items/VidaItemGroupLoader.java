package teamHTBP.vidaReforged.server.items;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import teamHTBP.vidaReforged.VidaReforged;

public class VidaItemGroupLoader {
    public static CreativeModeTab vidaItemGroup = new CreativeModeTab(VidaReforged.MOD_ID) {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(Blocks.FERN);
        }
    };
}
