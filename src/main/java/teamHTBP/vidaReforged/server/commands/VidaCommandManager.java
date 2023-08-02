package teamHTBP.vidaReforged.server.commands;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
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
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagic;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagicContainer;
import teamHTBP.vidaReforged.core.common.system.magicWord.MagicWord;
import teamHTBP.vidaReforged.server.commands.arguments.MagicArgument;
import teamHTBP.vidaReforged.server.commands.arguments.MagicArgumentInfo;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;
import teamHTBP.vidaReforged.server.packets.UnlockMagicWordCraftingPacket;
import teamHTBP.vidaReforged.server.packets.VidaPacketManager;
import teamHTBP.vidaReforged.server.providers.MagicTemplateManager;
import teamHTBP.vidaReforged.server.providers.MagicWordManager;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

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
                case SPEED -> {
                    cap.getContainer().speed(Double.parseDouble(value));
                }
                case MAX_AGE -> {
                    cap.getContainer().maxAge(Integer.parseInt(value));
                }
            }
        });
        context.getSource().sendSuccess(() -> Component.literal("argument set success"), false);
        return 1;
    };

    public final static Command<CommandSourceStack> MAGIC_CONTAINER_ADD_SOURCE = context -> {
        String magicId = context.getArgument("magic_id", String.class);
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack handInItem = player.getItemInHand(InteractionHand.MAIN_HAND);

        //
        if(!handInItem.is(VidaItemLoader.VIDA_WAND.get())){
            context.getSource().sendFailure(Component.literal("only vida wand can add Magic"));
            return 1;
        }
        //
        VidaMagic magic = MagicTemplateManager.getMagicById(magicId);
        if(magic == null){
            context.getSource().sendFailure(Component.translatable("magic %s not exists", magicId));
            return 1;
        }

        //
        LazyOptional<IVidaMagicContainerCapability> containerCapability = handInItem.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER);
        AtomicBoolean isAdded = new AtomicBoolean(false);
        containerCapability.ifPresent((cap) ->{
            List<String> magics = cap.getContainer().magic();
            if(!magics.contains(magicId)){
                magics.add(magicId);
                isAdded.set(true);
            }
        });

        if(!isAdded.get()){
            context.getSource().sendFailure(Component.translatable("magic %s is already added", magicId));
            return 1;
        }

        context.getSource().sendSuccess(()->Component.translatable("magic %s is added", magicId), false);
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

    public final static Command<CommandSourceStack> WAND_DICE = context -> {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack handInItem = player.getItemInHand(InteractionHand.MAIN_HAND);

        //
        try {
            if (!handInItem.is(VidaItemLoader.VIDA_WAND.get())) {
                context.getSource().sendFailure(Component.literal("only vida wand can random dice"));
                return 1;
            }

            RandomSource source = RandomSource.create();
            VidaElement randomElement = VidaElement.values()[2 + source.nextInt(4)];
            int randomMaxAge = source.nextBoolean() ? 120 + source.nextInt(50) : 120 + source.nextInt(10);
            double randomSpeed =  source.nextBoolean() ? source.nextDouble() * 2F / 3F : source.nextDouble() / 2.0F;
            int randomCost = source.nextInt(10) + 30;
            int randomCoolDown = source.nextInt(3000) + source.nextInt(1000);

            //
            String randomMagicId = String.format("vida_reforged:summon_partyparrot_%s", randomElement.toString().toLowerCase(Locale.ROOT));
            VidaMagic magic = MagicTemplateManager.getMagicById(randomMagicId);
            if (magic == null) {
                context.getSource().sendFailure(Component.literal("cannot execute the command"));
                return 1;
            }

            LazyOptional<IVidaMagicContainerCapability> containerCapability = handInItem.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER);
            AtomicBoolean isAdded = new AtomicBoolean(false);
            containerCapability.ifPresent((cap) ->{
                VidaMagicContainer container = cap.getContainer();
                if(container != null) {
                    container.magic(new LinkedList<>(ImmutableList.of(randomMagicId)))
                            .costMana(randomCost)
                            .coolDown(randomCoolDown)
                            .maxAge(randomMaxAge)
                            .speed(randomSpeed)
                            .amount(1);
                    isAdded.set(true);
                }
            });
            LazyOptional<IVidaManaCapability> manaCap = handInItem.getCapability(VidaCapabilityRegisterHandler.VIDA_MANA);
            manaCap.ifPresent(cap -> cap.setMaxMana(3000));
            context.getSource().sendSuccess(() -> Component.literal("dice complete"), false);
            return 1;
        }catch (Exception ex){
            context.getSource().sendFailure(Component.literal("cannot execute the command"));
            return 1;
        }
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
