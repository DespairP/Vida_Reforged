package teamHTBP.vidaReforged.client.events;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.client.hud.VidaDebugScreen;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class HudHandler {
    @SubscribeEvent
    public static void onOverlayRender(RenderGuiOverlayEvent event) {
        // 防止默认HUD覆盖
        if (Minecraft.getInstance().player == null || Minecraft.getInstance().level == null) {
            return;
        }
        //获取玩家对准的方块
        PoseStack matrixStack = event.getPoseStack();
        //Player player = Minecraft.getInstance().player;

        VidaDebugScreen debugScreen = new VidaDebugScreen();
        debugScreen.renderEntity(matrixStack);


    }


    public static HitResult.Type getHitType(Player player){
        return player.pick(20.0D, 0.0F, false).getType();
    }

    @OnlyIn(Dist.CLIENT)
    public static Block getBlockPlayerLookAt(Player player){
        HitResult block =  player.pick(20.0D, 0.0F, false);
        if(block.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockHitResult)block).getBlockPos();
            BlockState blockstate = player.level.getBlockState(blockpos);
            return blockstate.getBlock();
        }
        return null;
    }




}
