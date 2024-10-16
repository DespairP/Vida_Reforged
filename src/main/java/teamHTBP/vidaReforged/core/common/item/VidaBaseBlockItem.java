package teamHTBP.vidaReforged.core.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import teamHTBP.vidaReforged.server.components.VidaTooltipComponent;

import java.util.List;
import java.util.Optional;

public class VidaBaseBlockItem extends BlockItem {
    public VidaBaseBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        // 清空标题组件
        components.clear();
        // 给标题空格来渲染物品和方块
        MutableComponent mutablecomponent = Component.literal("      ")
                .append(itemStack.getHoverName())
                .withStyle(itemStack.getRarity().getStyleModifier());
        components.add(mutablecomponent);
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
        return Optional.of(new VidaTooltipComponent(itemStack.copy()));
    }
}
