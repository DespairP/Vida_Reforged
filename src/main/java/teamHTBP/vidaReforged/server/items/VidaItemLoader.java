package teamHTBP.vidaReforged.server.items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryObject;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.item.Position;
import teamHTBP.vidaReforged.core.utils.reg.RegisterGroup;
import teamHTBP.vidaReforged.server.blocks.VidaBlockLoader;
import teamHTBP.vidaReforged.server.blocks.VidaFluidsLoader;
import teamHTBP.vidaReforged.server.items.armors.ItemArmorApprentice;
import teamHTBP.vidaReforged.server.items.armors.ItemArmorBlackMetal;

import static net.minecraft.world.item.Items.BUCKET;
import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class VidaItemLoader {
    /**注册器*/
    public final static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    /**vida法杖*/
    public final static RegistryObject<Item> VIDA_WAND = ITEMS.register("vida_wand", VidaWand::new);

    /**生命之泉*/
    public final static RegistryObject<Item> VIVID_BUCKET = ITEMS.register("vivid_bucket", () -> new BucketItem(VidaFluidsLoader.VIVID_FLUID_STILL.get(), (new Item.Properties()).craftRemainder(BUCKET).stacksTo(1)));

    public final static RegistryObject<Item> VIDA_LEAVES = ITEMS.register("vida_leaves", () -> new BlockItem(VidaBlockLoader.VIDA_LEAVES.get(),new Item.Properties()));
    public final static RegistryObject<Item> VIDA_BLUE_LEAVES = ITEMS.register("vida_blue_leaves", () -> new BlockItem(VidaBlockLoader.VIDA_BLUE_LEAVES.get(),new Item.Properties()));

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
    public final static RegistryObject<Item> WRATH_THORN_SEED_BAG = ITEMS.register("wrath_thorn_seed_bag", () -> new ItemNameBlockItem(VidaBlockLoader.WRATH_THORN.get(), new Item.Properties()));
    public final static RegistryObject<Item> FIERY_STUMP_SEED_BAG = ITEMS.register("fiery_stump_seed_bag", () -> new ItemNameBlockItem(VidaBlockLoader.FIERY_STUMP.get(), new Item.Properties()));
    public final static RegistryObject<Item> CRIMSON_CREST_SEED_ITEM = ITEMS.register("crimson_crest_seed_item", () -> new ItemNameBlockItem(VidaBlockLoader.CRIMSON_CREST.get(), new Item.Properties()));
    public final static RegistryObject<Item> PLAM_STEM_SEED_ITEM = ITEMS.register("plam_stem_seed_item", () -> new ItemNameBlockItem(VidaBlockLoader.PLAM_STEM.get(), new Item.Properties()));
    public final static RegistryObject<Item> HEART_OF_WAL_SEED_ITEM = ITEMS.register("heart_of_wal_seed_item", () -> new ItemNameBlockItem(VidaBlockLoader.HEART_OF_WAL.get(), new Item.Properties()));
    public final static RegistryObject<Item> NITRITE_THORNS_SEED_ITEM = ITEMS.register("nitrite_thorns_seed_item", () -> new ItemNameBlockItem(VidaBlockLoader.NITRITE_THORNS.get(), new Item.Properties()));
    public final static RegistryObject<Item> SULLEN_HYDRANGEA_SEED_ITEM = ITEMS.register("sullen_hydrangea_seed_item", () -> new ItemNameBlockItem(VidaBlockLoader.SULLEN_HYDRANGEA.get(), new Item.Properties()));
    public final static RegistryObject<Item> SWEET_CYAN_REED_SEED_ITEM = ITEMS.register("sweet_cyan_reed_seed_item", () -> new ItemNameBlockItem(VidaBlockLoader.SWEET_CYAN_REED.get(), new Item.Properties()));

    @RegisterGroup
    public final static RegistryObject<Item> WRATH_THORN = ITEMS.register("wrath_thorn", () -> new Item(new Item.Properties()));
    @RegisterGroup
    public final static RegistryObject<Item> FIERY_STUMP = ITEMS.register("fiery_stump", () -> new Item(new Item.Properties()));
    @RegisterGroup
    public final static RegistryObject<Item> CRIMSON_CREST = ITEMS.register("crimson_crest", () -> new Item(new Item.Properties()));
    @RegisterGroup
    public final static RegistryObject<Item> PLAM_STEM = ITEMS.register("plam_stem", () -> new Item(new Item.Properties()));
    @RegisterGroup
    public final static RegistryObject<Item> HEART_OF_WAL = ITEMS.register("heart_of_wal", () -> new Item(new Item.Properties()));
    @RegisterGroup
    public final static RegistryObject<Item> NITRITE_THORNS = ITEMS.register("nitrite_thorns", () -> new Item(new Item.Properties()));
    @RegisterGroup
    public final static RegistryObject<Item> SULLEN_HYDRANGEA = ITEMS.register("sullen_hydrangea", () -> new Item(new Item.Properties()));
    @RegisterGroup
    public final static RegistryObject<Item> SWEET_CYAN_REED = ITEMS.register("sweet_cyan_reed", () -> new Item(new Item.Properties()));

    @ObjectHolder(registryName = "minecraft:item", value = "vida_reforged:vida_grass")
    public static Item VIDA_GRASS  = null;
    @ObjectHolder(registryName = "minecraft:item", value = "vida_reforged:vivid_stone")
    public static Item VIVID_STONE = null;
    @ObjectHolder(registryName = "minecraft:item", value = "vida_reforged:vivid_log")
    public static Item VIVID_LOG = null;
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
    @RegisterGroup
    public final static RegistryObject<Item> VIVID_CHEST_BLOCK = ITEMS.register("vivid_chest",  () -> new VividChestBlockItem(VidaBlockLoader.VIVID_CHEST_BLOCK.get(), new Item.Properties()));

    public final static RegistryObject<Item> TEST_EQUIPMENT = ITEMS.register("test_wand_equipment", () ->
            new VidaWandEquipment.Builder()
                    .setElement(VidaElement.WOOD)   // 配件属性
                    .setManaBaseCost(100)           // 装配上去的基础魔力花费
                    .setPosition(Position.TOP)      // 能装在哪个槽位
                    .setModelLocation(new ResourceLocation(MOD_ID, "vida_wand_model_test"))    // 模型位置
                    .setTextureLocation(new ResourceLocation(MOD_ID, "textures/armor/vida_wand_model_2.png"))   // 贴图位置
                    .build()
    );

    public final static RegistryObject<Item> TEST_CORE_EQUIPMENT = ITEMS.register("test_wand_core_equipment", () ->
            new VidaWandEquipment.Builder()
                    .setElement(VidaElement.FIRE)
                    .setManaBaseCost(100)
                    .setPosition(Position.CORE)
                    .setModelLocation(new ResourceLocation(MOD_ID, "vida_wand_model_core_test"))
                    .setTextureLocation(new ResourceLocation(MOD_ID, "textures/armor/vida_wand_model_core.png"))   // 贴图位置
                    .build()
    );

    public final static RegistryObject<Item> HEART_OF_WOOD = ITEMS.register("heart_of_wood", () ->
            new VidaWandEquipment.Builder()
                    .setElement(VidaElement.WOOD)
                    .setManaBaseCost(100)
                    .setPosition(Position.TOP)
                    .setModelLocation(new ResourceLocation(MOD_ID, "wand_style_001_model_top"))
                    .setTextureLocation(new ResourceLocation(MOD_ID, "textures/armor/vida_wand_model_2.png"))   // 贴图位置
                    .build()
    );

    public final static RegistryObject<Item> HEART_OF_WOOD_CORE = ITEMS.register("heart_of_wood_core", () ->
            new VidaWandEquipment.Builder()
                    .setElement(VidaElement.WOOD)
                    .setManaBaseCost(100)
                    .setPosition(Position.CORE)
                    .setModelLocation(new ResourceLocation(MOD_ID, "wand_style_001_model_core"))
                    .setTextureLocation(new ResourceLocation(MOD_ID, "textures/armor/vida_wand_model_2.png"))   // 贴图位置
                    .build()
    );
    public final static RegistryObject<Item> HEART_OF_WOOD_CENTER = ITEMS.register("heart_of_wood_center", () ->
            new VidaWandEquipment.Builder()
                    .setElement(VidaElement.WOOD)
                    .setManaBaseCost(100)
                    .setPosition(Position.CENTER)
                    .setModelLocation(new ResourceLocation(MOD_ID, "wand_style_001_model_center"))
                    .setTextureLocation(new ResourceLocation(MOD_ID, "textures/armor/vida_wand_model_2.png"))   // 贴图位置
                    .build()
    );
    public final static RegistryObject<Item> HEART_OF_WOOD_BOTTOM = ITEMS.register("heart_of_wood_bottom", () ->
            new VidaWandEquipment.Builder()
                    .setElement(VidaElement.WOOD)
                    .setManaBaseCost(100)
                    .setPosition(Position.BOTTOM)
                    .setModelLocation(new ResourceLocation(MOD_ID, "wand_style_001_model_bottom"))
                    .setTextureLocation(new ResourceLocation(MOD_ID, "textures/armor/vida_wand_model_2.png"))   // 贴图位置
                    .build()
    );

}
