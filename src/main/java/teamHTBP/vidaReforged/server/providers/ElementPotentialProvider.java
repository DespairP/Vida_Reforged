package teamHTBP.vidaReforged.server.providers;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

/**未开始开发*/
@Deprecated
public class ElementPotentialProvider implements DataProvider {

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        return null;
    }

    @Override
    public @NotNull String getName() {
        return String.format("%s:element_potential", MOD_ID);
    }
}
