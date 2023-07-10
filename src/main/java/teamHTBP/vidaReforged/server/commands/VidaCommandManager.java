package teamHTBP.vidaReforged.server.commands;

import com.mojang.brigadier.Command;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;

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
}
