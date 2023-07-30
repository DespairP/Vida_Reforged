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
import teamHTBP.vidaReforged.core.api.VidaElement;

import javax.annotation.Nullable;
import java.util.Locale;

public class ItemArmorElementBoots extends ArmorItem {  //盔甲属于什么元素
    protected VidaElement element = VidaElement.EMPTY;

    public ItemArmorElementBoots() {
        super(ArmorMaterials.DIAMOND, Type.BOOTS, new Properties());
    }

    public ItemArmorElementBoots(VidaElement element) {
        this();
        this.element = element;
    }

    @Override
    public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.extensions.common.IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                return IClientItemExtensions.super.getHumanoidArmorModel(livingEntity, itemStack, equipmentSlot, original);
            }

        });
    }


    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return String.format("vida:textures/armor/%s_element_armor.png", element.toString().toLowerCase(Locale.ROOT));
    }


}
