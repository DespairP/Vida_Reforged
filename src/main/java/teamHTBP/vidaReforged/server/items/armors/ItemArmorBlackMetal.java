package teamHTBP.vidaReforged.server.items.armors;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import teamHTBP.vidaReforged.client.events.registries.LayerRegistryHandler;
import teamHTBP.vidaReforged.client.model.armors.chestplate.BlackMetalChestPlate;
import teamHTBP.vidaReforged.client.model.armors.head.BlackMetalHelmet;
import teamHTBP.vidaReforged.client.model.armors.leggings.BlackMetalLeggings;

import javax.annotation.Nullable;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

public class ItemArmorBlackMetal extends ArmorItem {
    public ItemArmorBlackMetal(Type type) {
        super(ArmorMaterials.IRON, type, new Properties().stacksTo(1));
    }

    @Override
    public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.extensions.common.IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                switch (equipmentSlot){
                    case LEGS -> {
                        return (HumanoidModel<?>) LayerRegistryHandler.getModelSupplier(BlackMetalLeggings.LAYER_LOCATION, BlackMetalLeggings.class).get();
                    }
                    case CHEST -> {
                        return (HumanoidModel<?>) LayerRegistryHandler.getModelSupplier(BlackMetalChestPlate.LAYER_LOCATION, BlackMetalChestPlate.class).get();
                    }
                    case HEAD -> {
                        return (HumanoidModel<?>) LayerRegistryHandler.getModelSupplier(BlackMetalHelmet.LAYER_LOCATION, BlackMetalHelmet.class).get();

                    }
                }

                return (HumanoidModel<?>) LayerRegistryHandler.getModelSupplier(BlackMetalLeggings.LAYER_LOCATION, BlackMetalLeggings.class).get();
            }

        });
    }


    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return String.format("%s:textures/armor/%s", MOD_ID, "black_metal_armor.png");
    }
}
