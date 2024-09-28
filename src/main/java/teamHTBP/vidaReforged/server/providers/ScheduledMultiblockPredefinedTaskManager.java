package teamHTBP.vidaReforged.server.providers;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.common.system.multiblock.ScheduledMultiblockPredefinedTask;
import teamHTBP.vidaReforged.server.blocks.VidaBlockLoader;

import java.util.HashMap;
import java.util.Map;

public class ScheduledMultiblockPredefinedTaskManager {
    public final static Map<ResourceLocation, ScheduledMultiblockPredefinedTask> TASK = new HashMap<>();

    static {

        ScheduledMultiblockPredefinedTask task = new ScheduledMultiblockPredefinedTask
                .TaskBuilder()
                .ticks(200)
                .start(new ResourceLocation(VidaReforged.MOD_ID, "magic"), Blocks.CRAFTING_TABLE.defaultBlockState())
                .end()
                .result()
                .addResultBlock(VidaBlockLoader.MAGIC_WORD_CRAFTING.get().defaultBlockState(), BlockPos.ZERO)
                .end().build();

        ScheduledMultiblockPredefinedTask torch = new ScheduledMultiblockPredefinedTask
                .TaskBuilder()
                .ticks(200)
                .start(new ResourceLocation(VidaReforged.MOD_ID, "harmonize"), Blocks.QUARTZ_BLOCK.defaultBlockState())
                .addValidateBlock(Blocks.QUARTZ_BLOCK.defaultBlockState(), new BlockPos(0, -1, 0))
                .end()
                .result()
                .addResultBlock(VidaBlockLoader.ELEMENT_HARMONIZE_EARTH_TABLE.get().defaultBlockState().setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER), BlockPos.ZERO)
                .addResultBlock(VidaBlockLoader.ELEMENT_HARMONIZE_EARTH_TABLE.get().defaultBlockState().setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER), BlockPos.ZERO.below())
                .end().build();

        TASK.put(task.getId(), task);
        TASK.put(torch.getId(), torch);
    }
}
