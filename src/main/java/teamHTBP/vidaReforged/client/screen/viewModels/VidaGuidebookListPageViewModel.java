package teamHTBP.vidaReforged.client.screen.viewModels;

import teamHTBP.vidaReforged.core.common.system.guidebook.VidaPageList;
import teamHTBP.vidaReforged.core.common.system.guidebook.VidaPageListItem;
import teamHTBP.vidaReforged.core.common.ui.component.LiveData;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModel;
import teamHTBP.vidaReforged.server.providers.VidaGuidebookManager;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.min;
import static java.util.stream.Collectors.toMap;

public class VidaGuidebookListPageViewModel extends ViewModel {
    public LiveData<VidaPageList> pageList = new LiveData<>();
    public LiveData<Integer> currentPage = new LiveData<>();
    public Integer maxPage = 0;
    private List<VidaPageListItem> items = new LinkedList<>();
    private Map<Integer, List<VidaPageListItem>> paginationItems = new HashMap<>();

    /**载入列表*/
    public void loadPageList(VidaPageList list) {
        if(list == null){
            return;
        }
        this.items = VidaGuidebookManager.LIST_ITEM_MAP
                .values()
                .stream()
                .sorted(Comparator.comparingInt(VidaPageListItem::getPriority))
                .filter(items -> items.getList().equals(list.id))
                .collect(Collectors.toList());
        this.pageList.setValue(list);
        this.paginationItems.putAll(partition(this.items, 8));
        this.maxPage = paginationItems.size();
    }

    static Map<Integer, List<VidaPageListItem>> partition(List<VidaPageListItem> list, int pageSize) {
        return IntStream.iterate(0, i -> i + pageSize)
                .limit((list.size() + pageSize - 1) / pageSize)
                .boxed()
                .collect(toMap(i -> i / pageSize, i -> list.subList(i, min(i + pageSize, list.size()))));
    }

    public List<VidaPageListItem> getPageItems(){
        return this.items;
    }

    public List<VidaPageListItem> getPageItemWithPage(Integer page){
        return page > paginationItems.size() ? new ArrayList<>() : paginationItems.get(page);
    }
}
