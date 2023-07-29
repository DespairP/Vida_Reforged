package teamHTBP.vidaReforged.client.events;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.NamedGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import teamHTBP.vidaReforged.VidaConfig;
import teamHTBP.vidaReforged.client.hud.VidaCauldronScreen;
import teamHTBP.vidaReforged.client.hud.VidaDebugScreen;
import teamHTBP.vidaReforged.client.hud.VidaManaBarScreen;
import teamHTBP.vidaReforged.client.hud.VidaUnlockScreen;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.server.blockEntities.BasePurificationCauldronBlockEntity;
import teamHTBP.vidaReforged.server.blocks.VidaBlockLoader;

import java.util.List;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(Dist.CLIENT)
public class HudHandler {
    protected static VidaManaBarScreen screen;
    protected static VidaUnlockScreen unlockScreen;
    protected static final float MAX_PLAYER_BAR_OFFSET = 12f;
    protected static FloatRange playerBarOffset = new FloatRange(0,0,MAX_PLAYER_BAR_OFFSET);
    protected static FloatRange globalHudAlpha = new FloatRange(0,0, 1);

    @SubscribeEvent
    public static void onOverlayRender(RenderGuiOverlayEvent event) {
        // 防止默认HUD覆盖
        if (Minecraft.getInstance().player == null || Minecraft.getInstance().level == null) {
            return;
        }

        // 渲染debug界面
        renderDebugOverlay(event);

        // 渲染
        renderCauldronOverlay(event);

        // 渲染法杖魔力界面
        renderVidaManaScreen(event);

        //渲染解锁界面
        renderUnlockOverlay(event);
    }

    /**当渲染魔力界面时，原版血条等等ui都往上偏移12个像素，入栈*/
    @SubscribeEvent
    public static void onOverlayRenderPre(RenderGuiOverlayEvent.Pre event) {
        // 确定要偏移的ui
        final List<NamedGuiOverlay> TYPE_OFFSET_OVERLAY = ImmutableList.of(
                VanillaGuiOverlay.PLAYER_HEALTH.type(),
                VanillaGuiOverlay.EXPERIENCE_BAR.type(),
                VanillaGuiOverlay.FOOD_LEVEL.type(),
                VanillaGuiOverlay.ITEM_NAME.type()
        );
        final boolean isVidaManaHudRendered = getOrCreateVidaManaScreen(event.getGuiGraphics().bufferSource()).isRendered();
        final PoseStack poseStack = event.getGuiGraphics().pose();
        if(TYPE_OFFSET_OVERLAY.contains(event.getOverlay())){
            final float offset = playerBarOffset.change(isVidaManaHudRendered,0.1f);
            poseStack.pushPose();
            poseStack.translate(0f, -1 * offset, 0);
        }
    }

    /**当渲染魔力界面时，原版血条等等ui都往上偏移12个像素，出栈*/
    @SubscribeEvent
    public static void onOverlayRenderPost(RenderGuiOverlayEvent.Post event){
        final List<NamedGuiOverlay> TYPE_OFFSET_OVERLAY = ImmutableList.of(
                VanillaGuiOverlay.PLAYER_HEALTH.type(),
                VanillaGuiOverlay.EXPERIENCE_BAR.type(),
                VanillaGuiOverlay.FOOD_LEVEL.type(),
                VanillaGuiOverlay.ITEM_NAME.type()
        );
        final PoseStack poseStack = event.getGuiGraphics().pose();
        if(TYPE_OFFSET_OVERLAY.contains(event.getOverlay())){
            poseStack.popPose();
        }
    }

    public static void renderUnlockOverlay(RenderGuiOverlayEvent event){
        Player player = Minecraft.getInstance().player;
        Minecraft mc = Minecraft.getInstance();
        VidaUnlockScreen unlockScreen = getOrUnlockVidaManaScreen(event.getGuiGraphics().bufferSource());
        if(player == null || event.getOverlay() != VanillaGuiOverlay.EXPERIENCE_BAR.type()){
            return;
        }

        unlockScreen.render(event.getGuiGraphics().pose());
    }

    /**显示debug界面*/
    public static void renderDebugOverlay(RenderGuiOverlayEvent event){
        if(!VidaConfig.DEBUG_MODE.get()){
            return;
        }
        //获取玩家对准的方块
        PoseStack matrixStack = event.getGuiGraphics().pose();
        MultiBufferSource.BufferSource bufferSource = event.getGuiGraphics().bufferSource();

        VidaDebugScreen debugScreen = new VidaDebugScreen(
                bufferSource,
                Minecraft.getInstance().crosshairPickEntity,
                getBlockEntityPlayerLookAt(Minecraft.getInstance().player)
        );


        debugScreen.render(matrixStack);
    }

    public static void renderCauldronOverlay(RenderGuiOverlayEvent event){
        Player player = Minecraft.getInstance().player;
        Minecraft mc = Minecraft.getInstance();
        if(player == null || event.getOverlay() != VanillaGuiOverlay.EXPERIENCE_BAR.type()){
            return;
        }
        // 渲染
        BlockEntity entity = getBlockEntityPlayerLookAt(player);
        GuiGraphics graphics = event.getGuiGraphics();
        MultiBufferSource.BufferSource bufferSource = event.getGuiGraphics().bufferSource();
        if(entity != null && entity.getBlockState().is(VidaBlockLoader.PURIFICATION_CAULDRON.get())){
            new VidaCauldronScreen(mc, bufferSource).render(graphics.pose(), entity, globalHudAlpha.increase(0.02f));
            return;
        }
        globalHudAlpha.decrease(0.02f);
    }

    /**显示法杖魔力界面*/
    public static void renderVidaManaScreen(RenderGuiOverlayEvent event){
        if(event.getOverlay() == VanillaGuiOverlay.EXPERIENCE_BAR.type()){
            VidaManaBarScreen screen = getOrCreateVidaManaScreen(event.getGuiGraphics().bufferSource());
            screen.render(event.getGuiGraphics().pose());
        }

    }

    public static VidaManaBarScreen getOrCreateVidaManaScreen(MultiBufferSource.BufferSource bufferSource){
        if(screen == null){
            screen = new VidaManaBarScreen(Minecraft.getInstance(), bufferSource);
        }
        return screen;
    }

    public static VidaUnlockScreen getOrUnlockVidaManaScreen(MultiBufferSource.BufferSource bufferSource){
        return new VidaUnlockScreen(Minecraft.getInstance(), bufferSource);
    }


    public static HitResult.Type getHitType(Player player){
        return player.pick(20.0D, 0.0F, false).getType();
    }

    @OnlyIn(Dist.CLIENT)
    public static BlockEntity getBlockEntityPlayerLookAt(Player player){
        HitResult block =  player.pick(20.0D, 0.0F, false);
        if(block.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockHitResult)block).getBlockPos();
            return player.getCommandSenderWorld().getBlockEntity(blockpos);
        }
        return null;
    }

}
