package teamHTBP.vidaReforged.server.items;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryObject;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.server.items.armors.ItemArmorApprentice;
import teamHTBP.vidaReforged.server.items.armors.ItemArmorBlackMetal;

public class VidaItemLoader {
    /**注册器*/
    public final static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, VidaReforged.MOD_ID);

    /**vida法杖*/
    public final static RegistryObject<Item> VIDA_WAND = ITEMS.register("vida_wand", VidaWand::new);

    @ObjectHolder(registryName = "vida_reforged:crism_crest", value = "vida_reforged:crism_crest")
    public final static RegistryObject<Item> CRISM_CREST = null;

    public final static RegistryObject<Item> BREATH_CATCHER = ITEMS.register("breath_catcher", BreathCatcher::new);
    public final static RegistryObject<Item> GOLD_GEM = ITEMS.register("gold_gem", ElementGem::new);
    public final static RegistryObject<Item> GOLD_ELEMENT_CORE = ITEMS.register("gold_element_core", ElementGem::new);
    public final static RegistryObject<Item> AQUA_ELEMENT_CORE = ITEMS.register("aqua_element_core", ElementGem::new);
    public final static RegistryObject<Item> FIRE_ELEMENT_CORE = ITEMS.register("fire_element_core", ElementGem::new);
    public final static RegistryObject<Item> WOOD_ELEMENT_CORE = ITEMS.register("wood_element_core", ElementGem::new);
    public final static RegistryObject<Item> EARTH_ELEMENT_CORE = ITEMS.register("earth_element_core", ElementGem::new);
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

    public final static RegistryObject<Item> BLACK_METAL_HELMET = ITEMS.register("black_metal_helmet", () -> new ItemArmorBlackMetal(ArmorItem.Type.HELMET));
    public final static RegistryObject<Item> BLACK_METAL_CHESTPLATE = ITEMS.register("black_metal_chestplate", () -> new ItemArmorBlackMetal(ArmorItem.Type.CHESTPLATE));
    public final static RegistryObject<Item> BLACK_METAL_BOOTS = ITEMS.register("black_metal_boots", () -> new ItemArmorBlackMetal(ArmorItem.Type.BOOTS));


    public final static RegistryObject<Item> APPRENTICE_HELMET = ITEMS.register("apprentice_helmet", () -> new ItemArmorApprentice(ArmorItem.Type.HELMET));
    public final static RegistryObject<Item> APPRENTICE_CHESTPLATE = ITEMS.register("apprentice_chestplate", () -> new ItemArmorApprentice(ArmorItem.Type.CHESTPLATE));
    public final static RegistryObject<Item> APPRENTICE_LEGGINGS = ITEMS.register("apprentice_leggings", () -> new ItemArmorApprentice(ArmorItem.Type.LEGGINGS));
    public final static RegistryObject<Item> APPRENTICE_BOOTS = ITEMS.register("apprentice_boots", () -> new ItemArmorApprentice(ArmorItem.Type.BOOTS));



    public static final FoodProperties BALANCE_FOOD = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.3F).build();
    public static final FoodProperties UNHEALTH_FOOD = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.1F).build();

    public final static RegistryObject<Item> FRIED_CRIMSON_CREST = ITEMS.register("fried_crimson_crest", () -> new VidaFood(BALANCE_FOOD));
    public final static RegistryObject<Item> HEART_OF_WAL_JUICE = ITEMS.register("heart_of_wal_juice", () -> new VidaFood(BALANCE_FOOD));
    public final static RegistryObject<Item> PLAM_STEM_TEA = ITEMS.register("plam_stem_tea", () -> new VidaFood(BALANCE_FOOD));
    public final static RegistryObject<Item> PROCESSED_SULLEN_HYDRANGEA_BERRY = ITEMS.register("processed_sullen_hydrangea_berry", () -> new VidaFood(BALANCE_FOOD));
    public final static RegistryObject<Item> SULLEN_HYDRANGEA_SOUP = ITEMS.register("sullen_hydrangea_soup", () -> new VidaFood(BALANCE_FOOD));
    public final static RegistryObject<Item> NITRITE_TEA = ITEMS.register("nitrite_tea", () -> new VidaFood(BALANCE_FOOD));
    public final static RegistryObject<Item> DRIED_SWEET_CYAN_REED = ITEMS.register("dried_sweet_cyan_reed", () -> new VidaFood(BALANCE_FOOD));
}
