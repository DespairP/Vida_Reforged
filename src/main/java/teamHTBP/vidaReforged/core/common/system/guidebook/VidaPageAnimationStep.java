package teamHTBP.vidaReforged.core.common.system.guidebook;

import java.util.LinkedList;
import java.util.List;

public class VidaPageAnimationStep {
    int step;
    final LinkedList<VidaPageAnimationSubStep> subSteps;

    public VidaPageAnimationStep(int step, LinkedList<VidaPageAnimationSubStep> subSteps) {
        this.step = step;
        this.subSteps = subSteps;
    }

    public int getStep() {
        return step;
    }

    public VidaPageAnimationSubStep getSubStep(int subStep){
        return subSteps.stream().filter(animSubStep -> animSubStep.getSubStep() == subStep).findFirst().orElseThrow();
    }

    public List<VidaPageAnimationSubStep> getSubSteps() {
        return subSteps;
    }

    public static class Builder{
        private final int step;
        private final VidaPageAnimationSetup parentSetup;
        private LinkedList<VidaPageAnimationSubStep> subSteps;
        private VidaPageAnimation.Builder parent;


        public Builder(int step, VidaPageAnimation.Builder builder) {
            this.step = step;
            this.subSteps = new LinkedList<>();
            this.parentSetup = builder.setup;
            this.parent = builder;
        }

        public VidaPageAnimationSubStep.Builder subStep(int subStep){
            return new VidaPageAnimationSubStep.Builder(subStep, this);
        }

        protected VidaPageAnimationObject getObjectById(String id){
            return parentSetup.findObjectById(id);
        }

        protected void addSubStep(VidaPageAnimationSubStep subStep){
            this.subSteps.add(subStep);
        }

        public VidaPageAnimation.Builder end(){
            this.parent.addStep(new VidaPageAnimationStep(step, subSteps));

            return parent;
        }
    }
}
