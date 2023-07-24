package teamHTBP.vidaReforged.client.screen.viewModels;

import teamHTBP.vidaReforged.core.common.component.LiveData;
import teamHTBP.vidaReforged.core.common.component.ViewModel;
import teamHTBP.vidaReforged.core.common.system.guidebook.TeaconGuideBook;

public class VidaTeaconGuidebookViewModel extends ViewModel {
    public LiveData<Integer> page = new LiveData<>(1);

    public Integer maxPage = 1;

    private final LiveData<TeaconGuideBook> guidebook;

    public VidaTeaconGuidebookViewModel(TeaconGuideBook guidebook) {
        this.guidebook = new LiveData<>(guidebook);
    }

    public TeaconGuideBook getGuidebook(){
        return guidebook.getValue();
    }

    public void setMaxPage(Integer maxPage) {
        this.maxPage = maxPage;
    }

    public void increasePage(){
        this.page.setValue(Math.min(this.page.getValue() + 1, maxPage));
    }

    public void decreasePage(){
        this.page.setValue(Math.max(1,this.page.getValue() - 1));
    }
}
