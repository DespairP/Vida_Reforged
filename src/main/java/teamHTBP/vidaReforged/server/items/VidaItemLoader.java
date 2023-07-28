package teamHTBP.vidaReforged.server.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryObject;
import teamHTBP.vidaReforged.VidaReforged;

public class VidaItemLoader {
    /**注册器*/
    public final static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, VidaReforged.MOD_ID);

    /**vida法杖*/
    public final static RegistryObject<Item> VIDA_WAND = ITEMS.register("vida_wand", VidaWand::new);

    @ObjectHolder(registryName = "vida_reforged:crism_crest", value = "vida_reforged:crism_crest")
    public final static RegistryObject<Item> CRISM_CREST = null;

    public final static RegistryObject<Item> BREATH_CATCHER = ITEMS.register("breath_catcher", BreathCatcher::new);

    public final static RegistryObject<Item> FIRE_GEM = ITEMS.register("fire_gem", ElementGem::new);
    public final static RegistryObject<Item> WOOD_GEM = ITEMS.register("wood_gem", ElementGem::new);


    public final static RegistryObject<Item> UNLOCK_MAGIC_WORD_PAPER = ITEMS.register("unlock_magic_word_paper", UnlockMagicWordPaper::new);
}
