package teamHTBP.vidaReforged.core.common.system.guidebook;

import java.util.*;

/**
 * 指导动画
 *
 * <p>
 *     如何构造一个指导动画:
 *     <pre>
 *         <code>
 *             new VidaPageAnimation.Builder()
 *                  .setup()  // 构造场景
 *                      .size(3,3)  // 3D场景大小，比如这里是3X3的场景
 *                      .enableRotate() // 是否允许玩家自行旋转
 *                      .defineObject("id_1", Blocks.Grass_Block) // 定义场景物品
 *                      .defineTexture("id_2", new TextureSection(...)) // 定义材质
 *                  .step(1)  // 开始第一步
 *                      .subStep(1) // 1.1步
 *                          .findObject("id_1").show(ANIMATION.POP_UP, 0, 0, 10) // 在（0，0）位置10帧弹出方块
 *                          .popText("放置物品") // 出现文字
 *                      .subStep(2)
 *                          .findObject("id_2").moveTo(0, 2)
 *                          .popText("移动物品")
 *                       .pack() // 第一步终止
 *
 *          </code>
 *     </pre>
 * </p>
 * */
public class VidaPageAnimation {
    final Map<Integer, VidaPageAnimationStep> steps;

    final VidaPageAnimationSetup setup;

    public VidaPageAnimation(Map<Integer, VidaPageAnimationStep> steps, VidaPageAnimationSetup setup) {
        this.steps = steps;
        this.setup = setup;
    }

    public VidaPageAnimationSetup getSetup() {
        return setup;
    }

    public Map<Integer, VidaPageAnimationStep> getSteps() {
        return steps;
    }

    /**建造类*/
    public static class Builder{
        Map<Integer, VidaPageAnimationStep> steps = new LinkedHashMap<>();
        VidaPageAnimationSetup setup;

        public Builder(){

        }

        public VidaPageAnimationSetup.Builder setup(){
            return new VidaPageAnimationSetup.Builder(this);
        }


        public VidaPageAnimationStep.Builder step(int step){
            return new VidaPageAnimationStep.Builder(step, this);
        }

        protected void addStep(VidaPageAnimationStep step){
            this.steps.put(step.getStep(), step);
        }

        public VidaPageAnimation build(){
            return new VidaPageAnimation(steps, setup);
        }

    }
}
