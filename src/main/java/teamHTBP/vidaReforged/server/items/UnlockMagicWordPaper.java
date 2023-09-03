package teamHTBP.vidaReforged.server.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicWordCapability;
import teamHTBP.vidaReforged.core.common.system.magicWord.MagicWord;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;
import teamHTBP.vidaReforged.server.packets.UnlockMagicWordCraftingPacket;
import teamHTBP.vidaReforged.server.packets.VidaPacketManager;
import teamHTBP.vidaReforged.server.providers.MagicWordManager;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class UnlockMagicWordPaper extends Item {
    public UnlockMagicWordPaper() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(interactionHand);
        if(level.isClientSide){
            return InteractionResultHolder.pass(stack);
        }

        String wordId = stack.getOrCreateTag().getString("wordId");
        MagicWord word = MagicWordManager.getMagicWord(wordId);
        if(word == null){
            return InteractionResultHolder.pass(stack);
        }
        LazyOptional<IVidaMagicWordCapability> wordCapability = player.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_WORD);
        AtomicBoolean isAdded = new AtomicBoolean(false);
        wordCapability.ifPresent(cap -> {
            isAdded.set(cap.unlockMagicWord(wordId));
        });
        if(!isAdded.get()){
            return InteractionResultHolder.fail(stack);
        }
        VidaPacketManager.sendToPlayer(new UnlockMagicWordCraftingPacket(wordId), player);
        stack.shrink(1);
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        String wordId = itemStack.getOrCreateTag().getString("wordId");
        components.add(Component.translatable("tootip.vida_reforged.unlock_magic_word_paper").withStyle(style -> style.withItalic(true).withColor(ChatFormatting.GRAY)));
        components.add(Component.translatable(String.format("%s.%s", "magic_word.name", wordId)).withStyle(style -> style.withBold(true).withColor(ChatFormatting.GREEN)));
    }
}
