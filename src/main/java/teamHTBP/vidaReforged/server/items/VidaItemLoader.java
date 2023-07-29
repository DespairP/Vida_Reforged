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
    public final static RegistryObject<Item> GOLD_GEM = ITEMS.register("gold_gem", ElementGem::new);
    public final static RegistryObject<Item> WOOD_GEM = ITEMS.register("wood_gem", ElementGem::new);
    public final static RegistryObject<Item> AQUA_GEM = ITEMS.register("aqua_gem", ElementGem::new);
    public final static RegistryObject<Item> FIRE_GEM = ITEMS.register("fire_gem", ElementGem::new);
    public final static RegistryObject<Item> EARTH_GEM = ITEMS.register("earth_gem", ElementGem::new);
    public final static RegistryObject<Item> CRIMSON_CREST_SEED_ITEM = ITEMS.register("crimson_crest_seed_item", SeedItem::new);
    public final static RegistryObject<Item> PLAM_STEM_SEED_ITEM = ITEMS.register("plam_stem_seed_item", SeedItem::new);
    public final static RegistryObject<Item> HEART_OF_WAL_SEED_ITEM = ITEMS.register("heart_of_wal_seed_item", SeedItem::new);
    public final static RegistryObject<Item> NITRITE_THORNS_SEED_ITEM = ITEMS.register("nitrite_thorns_seed_item", SeedItem::new);
    public final static RegistryObject<Item> SULLEN_HYDRANGEA_SEED_ITEM = ITEMS.register("sullen_hydrangea_seed_item", SeedItem::new);
    public final static RegistryObject<Item> SWEET_CYAN_REED_SEED_ITEM = ITEMS.register("sweet_cyan_reed_seed_item", SeedItem::new);


    public final static RegistryObject<Item> UNLOCK_MAGIC_WORD_PAPER = ITEMS.register("unlock_magic_word_paper", UnlockMagicWordPaper::new);

}
