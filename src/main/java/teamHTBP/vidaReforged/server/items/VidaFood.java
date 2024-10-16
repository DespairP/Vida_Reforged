package teamHTBP.vidaReforged.server.items;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import teamHTBP.vidaReforged.core.api.items.IVidaItemWithToolTip;
import teamHTBP.vidaReforged.core.common.item.VidaBaseItem;

import java.util.function.Supplier;

public class VidaFood extends VidaBaseItem implements IVidaItemWithToolTip {
    /**装饰*/
    private Supplier<Block> blockDecorator = () -> Blocks.AIR;

    public VidaFood(FoodProperties properties) {
        super(new Item.Properties().food(properties));
    }

    @Override
    public InteractionResult useOn(UseOnContext p_41427_) {
        return super.useOn(p_41427_);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        return super.onItemUseFirst(stack, context);
    }
}
