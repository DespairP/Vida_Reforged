package teamHTBP.vidaReforged.core.common.system.guidebook;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/** SubStep相当于在这个时间段内一组变化的集合 */
public class VidaPageAnimationSubStep {

    private final int subStep;
    private final List<VidaPageAnimationObjectHistory> changes;

    public VidaPageAnimationSubStep(int subStep, List<VidaPageAnimationObjectHistory> changes) {
        this.subStep = subStep;
        this.changes = changes;
    }

    public List<VidaPageAnimationObjectHistory> getChanges() {
        return changes;
    }

    /** 获取最耗时的变动 */
    public int getCostTime(){
        return changes.stream()
                .max(Comparator.comparingInt(VidaPageAnimationObjectHistory::getTime))
                .orElse(new VidaPageAnimationObjectHistory(null, null, 0, VidaPageAnimationObjectHistory.Type.OTHER))
                .getTime();
    }

    public int getSubStep() {
        return subStep;
    }

    public static class Builder{
        /**父节点*/
        private VidaPageAnimationStep.Builder parent;
        /**标识*/
        int subStep = 1;
        /**所有要变化的东西*/
        private List<VidaPageAnimationObjectHistory> changes;

        public Builder(int subStep, VidaPageAnimationStep.Builder parent) {
            this.changes = new LinkedList<>();
            this.subStep = subStep;
            this.parent = parent;
        }


        public VidaPageAnimationObject.VidaPageAnimationObjectProxy getObjectById(String id){
            return new VidaPageAnimationObject.VidaPageAnimationObjectProxy(id, parent.getObjectById(id), this);
        }

        protected void addChanges(VidaPageAnimationObjectHistory history){
            this.changes.add(history);
        }

        public VidaPageAnimationStep.Builder end(){
            parent.addSubStep(new VidaPageAnimationSubStep(subStep, changes));
            return parent;
        }
    }
}
