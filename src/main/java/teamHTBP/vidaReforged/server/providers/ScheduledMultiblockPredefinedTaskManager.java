package teamHTBP.vidaReforged.server.providers;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
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
        TASK.put(task.getId(), task);

    }
}
