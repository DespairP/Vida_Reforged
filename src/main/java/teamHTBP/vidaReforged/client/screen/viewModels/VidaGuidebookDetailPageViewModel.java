package teamHTBP.vidaReforged.client.screen.viewModels;

import net.minecraft.resources.ResourceLocation;
import org.checkerframework.checker.nullness.qual.NonNull;
import teamHTBP.vidaReforged.core.common.system.guidebook.VidaPageList;
import teamHTBP.vidaReforged.core.common.ui.component.LiveData;
import teamHTBP.vidaReforged.core.common.system.guidebook.VidaPageDetail;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModel;

import java.util.HashMap;
import java.util.Map;

public class VidaGuidebookDetailPageViewModel extends ViewModel {
    public LiveData<Map<ResourceLocation, Integer>> paginationMap = new LiveData<>(new HashMap<>());
    public LiveData<Map<ResourceLocation, Integer>> paginationMaxMap = new LiveData<>(new HashMap<>());
    public LiveData<ResourceLocation> current = new LiveData<>();


    public void setCurrent(VidaPageDetail detail){
        current.setValue(detail.getId());
        var pageMaxMap = paginationMaxMap.getValue();
        pageMaxMap.put(detail.getId(), detail.getPages().size());
    }
    public void setCurrent(VidaPageList detail,int pageSize){
        current.setValue(detail.getId());
        var pageMaxMap = paginationMaxMap.getValue();
        pageMaxMap.put(detail.getId(), pageSize);
    }

    public ResourceLocation getCurrent(){
        return current.getValue();
    }

    public int getPage(@NonNull ResourceLocation detail){
        Map<ResourceLocation, Integer> pages = paginationMap.getValue();
        if(!pages.containsKey(detail)){
            pages.put(detail, 1);
        }
        return pages.get(detail);
    }

    public int getMaxPage(@NonNull ResourceLocation detail){
        return paginationMaxMap.getValue().getOrDefault(detail, 1);
    }

    public int setPage(@NonNull ResourceLocation detail, int page){
        int nextPage = Math.max(1, Math.min(page, getMaxPage(detail)));
        Map<ResourceLocation, Integer> pages = this.paginationMap.getValue();
        pages.put(detail, nextPage);
        paginationMap.setValue(pages);
        return nextPage;
    }

    public int nextPage(@NonNull ResourceLocation detail){
        int currentPage = this.getPage(detail);
        if(currentPage >= getMaxPage(detail)){
            return currentPage;
        }
        return setPage(detail, currentPage + 1);
    }

    public int prevPage(@NonNull ResourceLocation detail){
        int currentPage = this.getPage(detail);
        if(currentPage <= 1){
            return 1;
        }
        return setPage(detail, currentPage - 1);
    }
}
