package teamHTBP.vidaReforged.server.events;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class VidaDispenseBehaviorHandler {
    /**发射器消耗水桶动作*/
    private static DispenseItemBehavior dispenseItemBehavior = new DefaultDispenseItemBehavior() {
        private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

        public ItemStack execute(BlockSource source, ItemStack itemstack) {
            DispensibleContainerItem dispensiblecontaineritem = (DispensibleContainerItem)itemstack.getItem();
            BlockPos blockpos = source.getPos().relative((Direction)source.getBlockState().getValue(DispenserBlock.FACING));
            Level level = source.getLevel();
            if (dispensiblecontaineritem.emptyContents((Player)null, level, blockpos, (BlockHitResult)null, itemstack)) {
                dispensiblecontaineritem.checkExtraContent((Player)null, level, itemstack, blockpos);
                return new ItemStack(Items.BUCKET);
            } else {
                return this.defaultDispenseItemBehavior.dispense(source, itemstack);
            }
        }
    };

    @SubscribeEvent
    public static void onDispense(FMLCommonSetupEvent event){
        DispenserBlock.registerBehavior(VidaItemLoader.VIVID_BUCKET.get(), dispenseItemBehavior);
    }
}
