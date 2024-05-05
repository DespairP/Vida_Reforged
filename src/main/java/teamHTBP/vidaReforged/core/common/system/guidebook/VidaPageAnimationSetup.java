package teamHTBP.vidaReforged.core.common.system.guidebook;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3i;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class VidaPageAnimationSetup {
    HashMap<String, VidaPageAnimationObject> objects = new HashMap<>();
    boolean isResize = false;
    boolean isRotate = false;

    final Vector3i size;

    public VidaPageAnimationSetup(HashMap<String, VidaPageAnimationObject> objects, boolean enableResize, boolean enableRotate, int xWidth, int zWidth) {
        this.objects = objects;
        this.isResize = enableResize;
        this.isRotate = enableRotate;
        this.size = new Vector3i(xWidth, 255, zWidth);
    }

    public Vector3i getSize() {
        return size;
    }

    public List<VidaPageAnimationObject> getDefinedObjects(){
        return objects.values().stream().toList();
    }

    public VidaPageAnimationObject findObjectById(String id){
        return objects.get(id);
    }

    public static class Builder{
        private final HashMap<String, VidaPageAnimationObject> objects = new HashMap<>();
        private boolean enableResize = false;
        private boolean enableRotate = false;
        private VidaPageAnimation.Builder parentBuilder;
        private int x;
        private int z;

        public Builder(VidaPageAnimation.Builder builder) {
            this.parentBuilder = builder;
        }


        public Builder enableResize(){
            this.enableResize = true;
            return this;
        }

        public Builder enableRotate(){
            this.enableRotate = true;
            return this;
        }

        public Builder defineObject(String id, BlockState block){
            VidaPageAnimationObject object = new VidaPageAnimationObject(id, block, null, null, VidaPageAnimationObject.Type.BLOCK);
            objects.putIfAbsent( id, object );
            return this;
        }


        public Builder defineObject(String id, Entity entity){
            VidaPageAnimationObject object = new VidaPageAnimationObject(id, null, null, entity, VidaPageAnimationObject.Type.ENTITY);
            objects.putIfAbsent( id, object );
            return this;
        }

        public VidaPageAnimation.Builder done(){
            this.parentBuilder.setup =  new VidaPageAnimationSetup(objects, enableResize, enableRotate, x, z);
            return this.parentBuilder;
        }

        public Builder size(int x, int z) {
            this.x = x;
            this.z = z;
            return this;
        }
    }
}
