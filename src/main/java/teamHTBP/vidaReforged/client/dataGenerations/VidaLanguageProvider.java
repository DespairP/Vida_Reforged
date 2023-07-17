package teamHTBP.vidaReforged.client.dataGenerations;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import teamHTBP.vidaReforged.server.events.BlockItemAutoRegisterHandler;

public class VidaLanguageProvider extends LanguageProvider {
    public VidaLanguageProvider(DataGenerator generator, String modid, String locale) {
        super(generator.getPackOutput(), modid, locale);
    }

    @Override
    public void addTranslations() {
        BlockItemAutoRegisterHandler.REGISTRY_BLOCK_LIST.forEach((id, block)-> this.addBlock(block, ""));
    }
}
