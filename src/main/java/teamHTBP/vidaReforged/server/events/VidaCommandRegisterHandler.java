package teamHTBP.vidaReforged.server.events;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.EnumArgument;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.server.commands.VidaCommandManager;

@Mod.EventBusSubscriber
public class VidaCommandRegisterHandler {
    @SubscribeEvent
    public static void onCommandRegistry(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        //Magic子命令集
        //setMaxMana
        LiteralArgumentBuilder<CommandSourceStack> setMaxMana = Commands.literal("setMaxMana")
                .requires(source -> source.hasPermission(3))
                .then(Commands.argument("max_mana", DoubleArgumentType.doubleArg()).executes(VidaCommandManager.WAND_MAX_MANA_SOURCE));
        //setMana
        LiteralArgumentBuilder<CommandSourceStack> setMana = Commands.literal("setMana")
                .requires(source -> source.hasPermission(3))
                .then(Commands.argument("element", EnumArgument.enumArgument(VidaElement.class))
                        .then(Commands.argument("mana", DoubleArgumentType.doubleArg()).executes(VidaCommandManager.WAND_MANA_SOURCE))
                );
        //组装Magic命令集
        LiteralArgumentBuilder<CommandSourceStack> magic = Commands.literal("magic")
                .then(setMaxMana)
                .then(setMana);

        dispatcher.register(
                Commands.literal("vida")
                        .then(magic)
        );
    }
}
