package teamHTBP.vidaReforged.server.events;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.EnumArgument;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagicAttribute;
import teamHTBP.vidaReforged.server.commands.VidaCommandManager;
import teamHTBP.vidaReforged.server.providers.VidaMagicManager;
import teamHTBP.vidaReforged.server.providers.MagicWordManager;

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
        //
        LiteralArgumentBuilder<CommandSourceStack> setMagic = Commands.literal("setMagic")
                .requires(source -> source.hasPermission(3))
                .then(Commands.argument("slot", IntegerArgumentType.integer())
                        .then(Commands.argument("magic", ResourceLocationArgument.id())
                                .suggests((context, builder) -> SharedSuggestionProvider.suggest(VidaMagicManager.getMagicsKey().stream().map(ResourceLocation::toString), builder))
                                .executes(VidaCommandManager.MAGIC_SET)
                        )
                );


        //组装Magic命令集
        LiteralArgumentBuilder<CommandSourceStack> magic = Commands.literal("magic")
                .requires(source -> source.hasPermission(3))
                .then(setMaxMana)
                .then(setMana)
                .then(setMagic)
                .then(list);

        //MagicContainer子命令集
        // set
        LiteralArgumentBuilder<CommandSourceStack> setArgs = Commands
                .literal("setArgs")
                .then(Commands.argument("container_argument", EnumArgument.enumArgument(VidaMagicAttribute.MagicContainerArgument.class))
                        .then(Commands.argument("container_value", StringArgumentType.string()).executes(VidaCommandManager.MAGIC_CONTAINER))
                );

        // 组装MagicContainer命令集
        LiteralArgumentBuilder<CommandSourceStack> magicContainer = Commands.literal("magicContainer")
                .requires(source -> source.hasPermission(3))
                .then(setArgs);

        //MagicWord子命令
        LiteralArgumentBuilder<CommandSourceStack> addWord = Commands
                .literal("get")
                .then(Commands.argument("word_id", StringArgumentType.greedyString())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(MagicWordManager.getAllMagicWordIds(), builder))
                        .executes(VidaCommandManager.WORD_GET_SOURCE)
                );

        LiteralArgumentBuilder<CommandSourceStack> lockWord = Commands
                .literal("unlock")
                .then(Commands.argument("word_id", StringArgumentType.greedyString())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(MagicWordManager.getAllMagicWordIds(), builder))
                        .executes(VidaCommandManager.WORD_UNLOCK_SOURCE)
                );

        LiteralArgumentBuilder<CommandSourceStack> achieve = Commands
                .literal("achieveAll")
                .executes(VidaCommandManager.WORD_UNLOCK_ALL_SOURCE);

        //组装MagicWord命令集
        LiteralArgumentBuilder<CommandSourceStack> magicWord = Commands.literal("magicWord")
                .requires(source -> source.hasPermission(3))
                .then(achieve)
                .then(lockWord)
                .then(addWord);

        //Guidebook子命令
        LiteralArgumentBuilder<CommandSourceStack> guidebook = Commands.literal("guidebook")
                .requires(source -> source.hasPermission(3))
                .executes(VidaCommandManager.OPEN_GUIDEBOOK_LIST);

        LiteralArgumentBuilder<CommandSourceStack> testFraction = Commands.literal("fraction")
                        .requires(source -> source.hasPermission(3))
                        .executes(VidaCommandManager.OPEN_TEST_FRACTION);

        dispatcher.register(
                Commands.literal("vida")
                        .then(magic)
                        .then(magicContainer)
                        .then(magicWord)
                        .then(guidebook)
                        .then(testFraction)
        );
    }
}
