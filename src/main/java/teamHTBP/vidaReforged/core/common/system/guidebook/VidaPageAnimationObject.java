package teamHTBP.vidaReforged.core.common.system.guidebook;

import lombok.Data;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VidaPageAnimationObject {
    /** 物体Id */
    public final String id;
    /** 方块 */
    public final BlockState block;
    /** 掉落物品 */
    public final ItemStack stack;
    /** 生物 */
    public final Entity entity;
    /** TODO:历史的所有状态记录 */
    /** 中间记录状态 */
    private VidaPageAnimationObjectHistory.Attribute currentAttribute;
    /** 这个Object类型是什么, TODO: 接口实现多种扩展类型 */
    public final Type type;


    /** 构造物品 */
    public VidaPageAnimationObject(String id, BlockState block, ItemStack stack, Entity entity, VidaPageAnimationObject.Type type) {
        this.id = id;
        this.block = block;
        this.stack = stack;
        this.entity = entity;
        this.type = type;
    }


    /*Action*/
    /** 展示 */
    public VidaPageAnimationObjectHistory show(int x, int y, int z){
        if(currentAttribute == null){
            this.currentAttribute = new VidaPageAnimationObjectHistory.Attribute();
        }else {
            this.currentAttribute = this.currentAttribute.clone();
        }

        this.currentAttribute.setPos(x, y, z);
        this.currentAttribute.isShown = true;

        return new VidaPageAnimationObjectHistory(this, this.currentAttribute, 20, VidaPageAnimationObjectHistory.Type.SHOW);
    }

    /** 移动 */
    public VidaPageAnimationObjectHistory moveTo(int x, int y, int z){
        if(currentAttribute == null){
            this.currentAttribute = new VidaPageAnimationObjectHistory.Attribute();
        }else {
            this.currentAttribute = this.currentAttribute.clone();
        }

        this.currentAttribute.setPos(x, y, z);

        return new VidaPageAnimationObjectHistory(this, this.currentAttribute, 100, VidaPageAnimationObjectHistory.Type.MOVE);
    }



    public void changeAttribute(){

    }

    /**操作类*/
    public static class VidaPageAnimationObjectProxy{
        private final VidaPageAnimationObject object;
        private final VidaPageAnimationSubStep.Builder parent;
        private String id;

        public VidaPageAnimationObjectProxy(String id, VidaPageAnimationObject object, VidaPageAnimationSubStep.Builder parent) {
            this.id = id;
            this.object = object;
            this.parent = parent;
        }

        public VidaPageAnimationSubStep.Builder moveTo(int x, int y, int z){
            VidaPageAnimationObjectHistory history = object.moveTo(x, y, z);
            history.setId(id);
            parent.addChanges(history);
            return parent;
        }

        public VidaPageAnimationSubStep.Builder show(int x, int y, int z){
            VidaPageAnimationObjectHistory history = object.show(x, y, z);
            history.setId(id);
            parent.addChanges(history);
            return parent;
        }
    }


    public enum Type{
        BLOCK, ENTITY;
    }

}
