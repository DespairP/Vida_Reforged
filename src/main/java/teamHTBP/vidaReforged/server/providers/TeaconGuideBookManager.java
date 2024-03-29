package teamHTBP.vidaReforged.server.providers;

import com.google.gson.JsonElement;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import teamHTBP.vidaReforged.core.common.system.guidebook.TeaconGuideBook;
import teamHTBP.vidaReforged.core.common.system.guidebook.TeaconGuideBookPage;
import teamHTBP.vidaReforged.core.common.system.magicWord.MagicWord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static teamHTBP.vidaReforged.core.common.VidaConstant.DATA_GUIDE;

public class TeaconGuideBookManager extends AbstractVidaManager{
    public static Map<ResourceLocation, TeaconGuideBook> pageMap = new HashMap<>();

    public static Map<String, TeaconGuideBook> pageIdMap = new HashMap<>();

    public TeaconGuideBookManager() {
        super(DATA_GUIDE);
    }


    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> pObject, @NotNull ResourceManager pResourceManager, @NotNull ProfilerFiller pProfiler) {
        pObject.forEach(this::handleGuides);
    }

    private void handleGuides(ResourceLocation location, JsonElement element){
        try{
            TeaconGuideBook book = gson.fromJson(element, TeaconGuideBook.class);

            // 设置数据
            book.guideBookId(location);

            pageMap.put(location, book);
            pageIdMap.put(book.id(), book);
        }catch (Exception ex){
            LOGGER.error(ex.getMessage());
        }
    }

    public static Map<ResourceLocation, TeaconGuideBook> getPageMap() {
        return pageMap;
    }

    public static HashMap<String, TeaconGuideBook> getPageIdMap() {
        return new HashMap<>(pageIdMap);
    }

    @OnlyIn(Dist.CLIENT)
    public static void setPageIdMap(Map<String, TeaconGuideBook> pageIdMap) {
        TeaconGuideBookManager.pageIdMap = pageIdMap;
        TeaconGuideBookManager.pageMap = pageIdMap.values().stream().collect(Collectors.toMap(TeaconGuideBook::guideBookId, teaconGuideBook -> teaconGuideBook, (a,b) -> a));
    }
}
