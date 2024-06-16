package teamHTBP.vidaReforged.server.levelgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.structures.RuinedPortalPiece;
import net.minecraft.world.level.levelgen.structure.structures.RuinedPortalStructure;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.server.levelgen.feature.FallenLogTrunkPlacer;
import teamHTBP.vidaReforged.server.levelgen.feature.FallenTreeDecorator;
import teamHTBP.vidaReforged.server.levelgen.feature.VidaGlowTrunkPlacer;
import teamHTBP.vidaReforged.server.levelgen.feature.VidaMediumTrunkPlacer;
import teamHTBP.vidaReforged.server.levelgen.structure.VidaTreeStructure;
import teamHTBP.vidaReforged.server.recipe.records.VidaMagicWordRecipe;

import java.util.List;
import java.util.Map;

public class VidaLevelGenerationLoader {

    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, VidaReforged.MOD_ID);
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACER_TYPES = DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, VidaReforged.MOD_ID);
    public static final DeferredRegister<TreeDecoratorType<?>> DECORATOR_PLACER_TYPES = DeferredRegister.create(Registries.TREE_DECORATOR_TYPE, VidaReforged.MOD_ID);


    public static final RegistryObject<StructureType<VidaTreeStructure>> VIDA_TREE_STRUCTURE_SPECIAL_TYPE = STRUCTURE_TYPES.register("vida_tree_sp",  () -> (StructureType<VidaTreeStructure>) () -> VidaTreeStructure.CODEC);
    public static final RegistryObject<TrunkPlacerType<FallenLogTrunkPlacer>> FALLEN_TRUNK_TYPE = TRUNK_PLACER_TYPES.register("fallen_trunk_placer",  () -> new TrunkPlacerType<>(FallenLogTrunkPlacer.CODEC));
    public static final RegistryObject<TrunkPlacerType<VidaMediumTrunkPlacer>> VIDA_TREE_TRUNK_PLACER_MID_TYPE = TRUNK_PLACER_TYPES.register("vida_tree_medium_trunk_placer",  () -> new TrunkPlacerType<>(VidaMediumTrunkPlacer.CODEC));
    public static final RegistryObject<TrunkPlacerType<VidaGlowTrunkPlacer>> VIDA_TREE_GLOW_TRUNK_TYPE = TRUNK_PLACER_TYPES.register("vida_tree_glow_trunk_placer",  () -> new TrunkPlacerType<>(VidaGlowTrunkPlacer.CODEC));
    public static final RegistryObject<TreeDecoratorType<FallenTreeDecorator>> FALLEN_TREE_DECORATION_TYPE = DECORATOR_PLACER_TYPES.register("fallen_tree_decorator",  () -> new TreeDecoratorType<>(FallenTreeDecorator.CODEC));

}
