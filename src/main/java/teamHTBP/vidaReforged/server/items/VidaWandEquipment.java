package teamHTBP.vidaReforged.server.items;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.item.Position;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagicEquipmentAttribute;

public class VidaWandEquipment extends Item {
    private VidaMagicEquipmentAttribute attribute;

    public VidaWandEquipment() {
        super(new Properties().stacksTo(1));
    }

    public VidaWandEquipment(VidaMagicEquipmentAttribute attribute) {
        super(new Properties().stacksTo(1));
        this.attribute = attribute;
    }

    public VidaMagicEquipmentAttribute getAttribute() {
        return attribute;
    }

    public static class Builder{
        VidaMagicEquipmentAttribute attribute;


        public Builder(){
            this.attribute = new VidaMagicEquipmentAttribute();
        }

        public Builder setMagic(String magicId){
            this.attribute.setMagicId(magicId);
            return this;
        }

        public Builder setManaBaseCost(double baseCost){
            this.attribute.setManaBaseCost(baseCost);
            return this;
        }

        public Builder setCostFactor(double factor){
            this.attribute.setFactor(factor);
            return this;
        }

        public Builder setElement(VidaElement element){
            this.attribute.setElement(element);
            return this;
        }

        public Builder setModelLocation(String modId, String layerLocation,String layerUsed){
            this.attribute.setModelLayerLocation(new ModelLayerLocation(new ResourceLocation(modId, layerLocation), layerUsed));
            return this;
        }


        public Builder setModelLocation(ModelLayerLocation location){
            this.attribute.setModelLayerLocation(location);
            return this;
        }

        public Builder setTextureLocation(ResourceLocation textureLocation){
            this.attribute.setModelTexture(textureLocation);
            return this;
        }

        public Builder setShootBaseCount(int count){
            this.attribute.setShootBaseCount(count);
            return this;
        }

        public Builder setPosition(Position position){
            this.attribute.setPosition(position);
            return this;
        }

        public VidaWandEquipment build(){
            return new VidaWandEquipment(this.attribute);
        }
    }

}
