package teamHTBP.vidaReforged.server.events;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.EnumArgument;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagicContainer;
import teamHTBP.vidaReforged.server.commands.VidaCommandManager;
import teamHTBP.vidaReforged.server.commands.arguments.MagicArgument;
import teamHTBP.vidaReforged.server.providers.MagicTemplateManager;
import teamHTBP.vidaReforged.server.providers.MagicWordManager;

import java.util.stream.Stream;

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
        //list
        LiteralArgumentBuilder<CommandSourceStack> list = Commands.literal("list")
                .requires(source -> source.hasPermission(3))
                .executes(VidaCommandManager.MAGIC_SOURCE);

        //组装Magic命令集
        LiteralArgumentBuilder<CommandSourceStack> magic = Commands.literal("magic")
                .requires(source -> source.hasPermission(3))
                .then(setMaxMana)
                .then(setMana)
                .then(list);

        //MagicContainer子命令集
        // set
        LiteralArgumentBuilder<CommandSourceStack> setArgs = Commands
                .literal("setArgs")
                .then(Commands.argument("container_argument", EnumArgument.enumArgument(VidaMagicContainer.MagicContainerArgument.class))
                        .then(Commands.argument("container_value", StringArgumentType.string()).executes(VidaCommandManager.MAGIC_CONTAINER))
                );

        //add magic
        LiteralArgumentBuilder<CommandSourceStack> addMagic = Commands
                .literal("addMagic")
                .then(Commands.argument("magic_id", StringArgumentType.greedyString())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(MagicTemplateManager.getMagicsKey(), builder))
                        .executes(VidaCommandManager.MAGIC_CONTAINER_ADD_SOURCE)
                );

        // 组装MagicContainer命令集
        LiteralArgumentBuilder<CommandSourceStack> magicContainer = Commands.literal("magicContainer")
                .requires(source -> source.hasPermission(3))
                .then(setArgs)
                .then(addMagic);

        //MagicWord子命令
        LiteralArgumentBuilder<CommandSourceStack> addWord = Commands
                .literal("add")
                .then(Commands.argument("word_id", StringArgumentType.greedyString())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(MagicWordManager.getAllMagicWordIds(), builder))
                        .executes(VidaCommandManager.WORD_ADD_SOURCE)
                );

        //组装MagicWord命令集
        LiteralArgumentBuilder<CommandSourceStack> magicWord = Commands.literal("magicWord")
                .requires(source -> source.hasPermission(3))
                .then(addWord);

        //
        LiteralArgumentBuilder<CommandSourceStack> dice = Commands.literal("magicDice")
                        .executes(VidaCommandManager.WAND_DICE);

        dispatcher.register(
                Commands.literal("vida")
                        .then(magic)
                        .then(magicContainer)
                        .then(magicWord)
                        .then(dice)
        );
    }
}
