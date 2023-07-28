package teamHTBP.vidaReforged.server.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import teamHTBP.vidaReforged.server.capabilities.VidaMagicWordCapability;


import java.util.ArrayList;
import java.util.List;

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


    public final static RegistryObject<MenuType<MagicJigsawMenu>> JIGSAW_EQUIP = MENU_CONTAINER_TYPE.register(
            MagicJigsawMenu.MENU_NAME,
            () -> IForgeMenuType.create(
                    (windowId, inv, data) -> new MagicJigsawMenu(
                            windowId,
                            ContainerLevelAccess.create( inv.player.getCommandSenderWorld(), data.readBlockPos()),
                            inv,
                            data.readBlockPos()
                    )
            )
    );

    public final static RegistryObject<MenuType<MagicWordCraftingTableMenu>> MAGIC_WORD_CRAFTING = MENU_CONTAINER_TYPE.register(
            MagicWordCraftingTableMenu.MENU_NAME,
            () -> IForgeMenuType.create(
                    (windowId, inv, data) ->{
                        final BlockPos pos = data.readBlockPos();
                        final Level level = inv.player.getCommandSenderWorld();
                        final List<String> magicWords = new ArrayList<>();
                        CompoundTag wordList = data.readNbt();
                        if(wordList != null) {
                            magicWords.addAll(VidaMagicWordCapability.deserialize(wordList));
                        }
                        return new MagicWordCraftingTableMenu(
                                windowId,
                                ContainerLevelAccess.create(level, pos),
                                inv,
                                pos,
                                magicWords
                        );
                    }
            )
    );

    public final static RegistryObject<MenuType<PrismMenu>> PRISM = MENU_CONTAINER_TYPE.register(
            PrismMenu.MENU_NAME,
            () -> IForgeMenuType.create(
                    (windowId, inv, data) ->{
                        final BlockPos pos = data.readBlockPos();
                        final Level level = inv.player.getCommandSenderWorld();
                        return new PrismMenu(
                                windowId,
                                ContainerLevelAccess.create(level, pos),
                                inv,
                                pos
                        );
                    }
            )
    );
}
