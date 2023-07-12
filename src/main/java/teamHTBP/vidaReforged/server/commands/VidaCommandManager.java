package teamHTBP.vidaReforged.server.commands;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.Command;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicContainerCapability;
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagic;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagicContainer;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;
import teamHTBP.vidaReforged.server.providers.MagicTemplateManager;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.Map;

public class VidaCommandManager {
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
            manaCap.ifPresent(cap -> cap.setMaxMana(maxMana));
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
        VidaMagicContainer.MagicContainerArgument argument = context.getArgument("container_argument", VidaMagicContainer.MagicContainerArgument.class);
        String value = context.getArgument("container_value", String.class);
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack handInItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        if(!handInItem.is(VidaItemLoader.VIDA_WAND.get())){
            context.getSource().sendFailure(Component.literal("only vida wand can set max mana"));
            return 1;
        }
        LazyOptional<IVidaMagicContainerCapability> containerCap = handInItem.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER);
        containerCap.ifPresent(cap ->{
            switch (argument){
                case DAMAGE -> {
                    cap.getContainer().damage(Long.parseLong(value));
                }
                case MULTIPLIER -> {
                    cap.getContainer().multiplier(Double.parseDouble(value));
                }
                case DECREASER -> {
                    cap.getContainer().decreaser(Double.parseDouble(value));
                }
                case COST_MANA -> {
                    cap.getContainer().costMana(Double.parseDouble(value));
                }
                case AMOUNT -> {
                    cap.getContainer().amount(Integer.parseInt(value));
                }
                case INVOKE_COUNT -> {
                    cap.getContainer().invokeCount(Integer.parseInt(value));
                }
                case MAX_INVOKE_COUNT -> {
                    cap.getContainer().maxInvokeCount(Integer.parseInt(value));
                }
                case COOLDOWN -> {
                    cap.getContainer().coolDown(Long.parseLong(value));
                }
                case LAST_INVOKE_MILLSEC -> {
                    cap.getContainer().lastInvokeMillSec(Long.parseLong(value));
                }
                case LEVEL -> {
                    cap.getContainer().level(Integer.parseInt(value));
                }
            }
        });
        context.getSource().sendSuccess(() -> Component.literal("argument set success"), false);
        return 1;
    };

    private static Component getMagicList(){
        Map<String,VidaMagic> magicMap = MagicTemplateManager.getAllMagicAsString();
        return Component.translatable(
                "Available Magics: %s",
                ComponentUtils.formatList(magicMap.values(), VidaMagic::getCommandHoverComponents)
        );
    }

    public static <T> T convertInstanceOfObject(Object o, Class<T> clazz) {
        try {
            return (T) clazz.getMethod("valueOf",String.class).invoke(null, o);
        } catch(ClassCastException | InvocationTargetException | IllegalAccessException | NoSuchMethodException  e) {
            return null;
        }
    }
}
