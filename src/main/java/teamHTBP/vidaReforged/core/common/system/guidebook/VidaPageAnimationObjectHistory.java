package teamHTBP.vidaReforged.core.common.system.guidebook;

import lombok.Data;
import org.joml.Vector3f;

import java.util.HashMap;

public class VidaPageAnimationObjectHistory {
    /** 属性 */
    private final Attribute attribute;
    /** 物体 */
    private final VidaPageAnimationObject object;
    /** 花费时间 */
    private final int time;

    private String id;

    private final Type type;

    public VidaPageAnimationObjectHistory(VidaPageAnimationObject object, Attribute attribute, int time, Type type) {
        this.attribute = attribute;
        this.type = type;
        this.object = object;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public int getTime() {
        return time;
    }

    public Type getType() {
        return type;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    protected void setId(String id){
        this.id = id;
    }


    /**object属性*/
    @Data
    public static class Attribute implements Cloneable{
        /** 位置 */
        public Vector3f pos;
        /** 额外属性 */
        public HashMap<String, Object> extraAttributes = new HashMap<>();
        /** 是否显示 */
        public boolean isShown = false;

        @Override
        public Attribute clone() {
            try {
                Attribute clone = (Attribute) super.clone();
                clone.extraAttributes = (HashMap<String, Object>) extraAttributes.clone();
                return clone;
            } catch (Exception ex){
                throw new RuntimeException(ex.getMessage());
            }
        }


        public Attribute setPos(int x, int y, int z){
            pos = new Vector3f(x, y ,z);
            return this;
        }
    }


    public enum Type{
        SHOW, MOVE, OTHER;
    }
}
