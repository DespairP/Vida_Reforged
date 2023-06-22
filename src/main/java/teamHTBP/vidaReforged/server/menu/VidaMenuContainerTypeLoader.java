package teamHTBP.vidaReforged.server.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static teamHTBP.vidaReforged.VidaReforged.MOD_ID;

/**
 * 注册MenuContainer
 * */
public class VidaMenuContainerTypeLoader {
    public final static DeferredRegister<MenuType<?>> MENU_CONTAINER_TYPE = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MOD_ID);

    public final static RegistryObject<MenuType<TimeElementCraftingTableMenuContainer>> TIME_ELEMENT_MENU = MENU_CONTAINER_TYPE.register(
            TimeElementCraftingTableMenuContainer.MENU_NAME,
            () -> IForgeMenuType.create(
                    (windowId, inv, data) -> new TimeElementCraftingTableMenuContainer(
                            windowId,
                            ContainerLevelAccess.create( inv.player.getCommandSenderWorld(), data.readBlockPos()),
                            inv,
                            data.readBlockPos()
                    )
            )
    );

}
