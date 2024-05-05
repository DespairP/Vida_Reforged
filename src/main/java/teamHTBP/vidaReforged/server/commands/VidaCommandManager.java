package teamHTBP.vidaReforged.server.commands;

import com.mojang.brigadier.Command;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicContainerCapability;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicWordCapability;
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;
import teamHTBP.vidaReforged.core.api.capability.Result;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagic;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagicAttribute;
import teamHTBP.vidaReforged.core.common.system.magicWord.MagicWord;
import teamHTBP.vidaReforged.server.commands.arguments.MagicArgumentInfo;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;
import teamHTBP.vidaReforged.server.packets.UnlockMagicWordCraftingPacket;
import teamHTBP.vidaReforged.server.packets.VidaPacketManager;
import teamHTBP.vidaReforged.server.providers.VidaMagicManager;
import teamHTBP.vidaReforged.server.providers.MagicWordManager;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class VidaCommandManager {
    public final static DeferredRegister<ArgumentTypeInfo<?,?>> ARGUMENT_TYPE = DeferredRegister.create(ForgeRegistries.COMMAND_ARGUMENT_TYPES, VidaReforged.MOD_ID);

    public final static RegistryObject<ArgumentTypeInfo<?,?>> MAGIC_TYPE = ARGUMENT_TYPE.register("magic_type", MagicArgumentInfo::new);

    public final static Command<CommandSourceStack> WAND_MAX_MANA_SOURCE = (context)->{
        try {
            double maxMana = context.getArgument("max_mana", Double.class);
            ServerPlayer player = context.getSource().getPlayerOrException();
            ItemStack handInItem = player.getItemInHand(InteractionHand.MAIN_HAND);
            if(!handInItem.is(VidaItemLoader.VIDA_WAND.get())){
                context.getSource().sendFailure(Component.literal("only vida wand can set max mana"));
                return 1;
            }
            LazyOptional<IVidaManaCapability> manaCap = handInItem.getCapability(VidaCapabilityRegisterHandler.VIDA_MANA);
            manaCap.ifPresent(cap -> cap.resetMaxMana(maxMana));
            context.getSource().sendSuccess(() -> Component.literal("vida wand max mana setted successfully"), false);
            return 1;
        }catch (Exception ex){
            context.getSource().sendFailure(Component.literal("cannot execute the command"));
        }

        return 1;
    };

    public final static Command<CommandSourceStack> WAND_MANA_SOURCE = (context)->{
        try {
            double mana = context.getArgument("mana", Double.class);
            VidaElement element = context.getArgument("element", VidaElement.class);
            ServerPlayer player = context.getSource().getPlayerOrException();
            ItemStack handInItem = player.getItemInHand(InteractionHand.MAIN_HAND);
            if(!handInItem.is(VidaItemLoader.VIDA_WAND.get())){
                context.getSource().sendFailure(Component.literal("only vida wand can set max mana"));
                return 1;
            }
            LazyOptional<IVidaManaCapability> manaCap = handInItem.getCapability(VidaCapabilityRegisterHandler.VIDA_MANA);
            manaCap.ifPresent(cap -> cap.setMana(element, mana));
            context.getSource().sendSuccess(() -> Component.literal("vida wand mana setted successfully"), false);
            return 1;
        }catch (Exception ex){
            context.getSource().sendFailure(Component.literal("cannot execute the command"));
        }

        return 1;
    };

    public final static Command<CommandSourceStack> MAGIC_SOURCE = (context)->{
        try{
            context.getSource().sendSuccess(VidaCommandManager::getMagicList,false);
        }catch (Exception ex){
            context.getSource().sendFailure(Component.literal("cannot execute the command"));
        }
        return 1;
    };

    public final static Command<CommandSourceStack> MAGIC_CONTAINER = context -> {
        VidaMagicAttribute.MagicContainerArgument argument = context.getArgument("container_argument", VidaMagicAttribute.MagicContainerArgument.class);
        String value = context.getArgument("container_value", String.class);
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack handInItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        if(!handInItem.is(VidaItemLoader.VIDA_WAND.get())){
            context.getSource().sendFailure(Component.literal("only vida wand can set max mana"));
            return 1;
        }
        LazyOptional<IVidaMagicContainerCapability> containerCap = handInItem.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER);

        context.getSource().sendFailure(Component.literal("argument set failed"));
        return 0;
    };

    public final static Command<CommandSourceStack> MAGIC_SET = context -> {
        ResourceLocation magicId = context.getArgument("magic", ResourceLocation.class);
        Integer slotNumber = context.getArgument("slot", Integer.class);


        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack handInItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        Integer slotIndex = slotNumber - 1;

        // validation
        // 检查输入槽位编号
        if(slotIndex < 0){
            context.getSource().sendFailure(Component.translatable("message.command.vida_reforged.add_magic.no_slot"));
            return 1;
        }

        // 检查物品是否正确
        if(!handInItem.is(VidaItemLoader.VIDA_WAND.get())){
            context.getSource().sendFailure(Component.translatable("message.command.vida_reforged.add_magic.illegal_item"));
            return 1;
        }

        // 检查魔法是否存在
        VidaMagic magic = VidaMagicManager.getMagicByMagicId(magicId);
        if(magic == null){
            context.getSource().sendFailure(Component.translatable("message.command.vida_reforged.illegal_id_magic", magicId));
            return 1;
        }

        // set
        // 设置魔法
        LazyOptional<IVidaMagicContainerCapability> toolCapability = handInItem.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER);
        AtomicReference<Result> result = new AtomicReference<>(Result.FAILED);
        toolCapability.ifPresent(cap -> result.set(cap.setSingleMagic(slotIndex, magicId) ));

        switch (result.get()){
            case SUCCESS, PASS -> { context.getSource().sendSuccess( () -> Component.translatable("message.command.vida_reforged.add_magic.success", magic.getFormattedDisplayName()), false);}
            case FAILED -> {context.getSource().sendFailure(Component.translatable("message.command.vida_reforged.add_magic.failed", magic.getFormattedDisplayName()));}
        }

        return 1;
    };

    public final static Command<CommandSourceStack> WORD_ADD_SOURCE = context -> {
        String wordId = context.getArgument("word_id", String.class);
        MagicWord word = MagicWordManager.getMagicWord(wordId);
        if(word == null){
            context.getSource().sendFailure(Component.translatable("magic word %s not exists", wordId));
            return 1;
        }
        try {
            ItemStack handInItem = context.getSource().getPlayer().getItemInHand(InteractionHand.MAIN_HAND);

            if(handInItem.is(VidaItemLoader.UNLOCK_MAGIC_WORD_PAPER.get())){
                handInItem.getOrCreateTag().putString("wordId", wordId);
                context.getSource().sendSuccess(()->Component.translatable("word %s is added", wordId), false);
                return 1;
            }
            LazyOptional<IVidaMagicWordCapability> wordCapability = context
                    .getSource()
                    .getPlayerOrException()
                    .getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_WORD);
            AtomicBoolean isAdded = new AtomicBoolean(false);
            wordCapability.ifPresent(cap -> {
                isAdded.set(cap.unlockMagicWord(wordId));
            });
            if(!isAdded.get()){
                context.getSource().sendFailure(Component.translatable("magic word  %s is already added", wordId));
                return 1;
            }
            VidaPacketManager.sendToEntity(new UnlockMagicWordCraftingPacket(wordId), context.getSource().getEntity());
            context.getSource().sendSuccess(()->Component.translatable("magic word  %s is added", wordId), false);
            return 1;
        }catch (Exception ex){
            context.getSource().sendFailure(Component.literal("cannot execute the command"));
        }
        return 1;
    };

    public final static Command<CommandSourceStack> OPEN_GUIDEBOOK_LIST = context -> {
        ServerPlayer player = context.getSource().getPlayer();

        return 1;
    };

    private static Component getMagicList(){
        Map<ResourceLocation, VidaMagic> magicMap = VidaMagicManager.getMagicIdMap();
        return Component.translatable(
                "message.command.vida_reforged.list_magic",
                ComponentUtils.formatList(magicMap.values(), VidaMagic::getCommandHoverComponents)
        );
    }

    public static <T> T convertInstanceOfObject(Object o, Class<T> clazz) {
        try {
            return (T) clazz.getMethod("valueOf", String.class).invoke(null, o);
        } catch(ClassCastException | InvocationTargetException | IllegalAccessException | NoSuchMethodException  e) {
            return null;
        }
    }
}
