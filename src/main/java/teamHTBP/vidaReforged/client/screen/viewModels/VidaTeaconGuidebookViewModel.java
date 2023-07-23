package teamHTBP.vidaReforged.client.screen.viewModels;

import teamHTBP.vidaReforged.core.common.component.LiveData;
import teamHTBP.vidaReforged.core.common.component.ViewModel;
import teamHTBP.vidaReforged.core.common.system.guidebook.TeaconGuideBook;

public class VidaTeaconGuidebookViewModel extends ViewModel {
    public LiveData<Integer> page = new LiveData<>(1);

    private final LiveData<TeaconGuideBook> guidebook;

    public VidaTeaconGuidebookViewModel(TeaconGuideBook guidebook) {
        this.guidebook = new LiveData<>(guidebook);
    }

    public TeaconGuideBook getGuidebook(){
        return guidebook.getValue();
    }
}
