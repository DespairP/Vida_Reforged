package teamHTBP.vidaReforged.client.hud;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.capability.IVidaMagicContainerCapability;
import teamHTBP.vidaReforged.core.api.capability.IVidaManaCapability;
import teamHTBP.vidaReforged.core.api.hud.IVidaScreen;
import teamHTBP.vidaReforged.core.common.system.magic.VidaMagicContainer;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.helper.GuiHelper;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;
import teamHTBP.vidaReforged.server.items.VidaItemLoader;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@OnlyIn(Dist.CLIENT)
public class VidaManaBarScreen extends GuiGraphics implements IVidaScreen {
    /**最大能量条长度*/
    private static final float MAX_BAR_WIDTH = 166.0f;
    /**元素能量条的长度*/
    private Map<VidaElement,FloatRange> progressMap = ImmutableMap.of(
            VidaElement.GOLD, new FloatRange(0,0,MAX_BAR_WIDTH),
            VidaElement.WOOD, new FloatRange(0,0,MAX_BAR_WIDTH),
            VidaElement.AQUA, new FloatRange(0,0,MAX_BAR_WIDTH),
            VidaElement.FIRE, new FloatRange(0,0,MAX_BAR_WIDTH),
            VidaElement.EARTH, new FloatRange(0,0,MAX_BAR_WIDTH)
    );
    /**整体透明度*/
    private FloatRange alpha = new FloatRange(0,0,1);
    /**ui材质路径*/
    private final static ResourceLocation GUI_LOCATION = new ResourceLocation(VidaReforged.MOD_ID, "textures/gui/vida_wand_mana_bar.png");
    /**槽位区域的材质路径*/
    private TextureSection barSec = new TextureSection(GUI_LOCATION, 0, 18, 182, 12);
    /**能量条的材质路径*/
    private TextureSection progressSec = new TextureSection(GUI_LOCATION, 8, 6, 166, 4);
    /**是否正在渲染*/
    private boolean isRendered = false;
    /**计数器*/
    GuiHelper.TickHelper ticker = new GuiHelper.TickHelper();


    public VidaManaBarScreen(Minecraft minecraft, MultiBufferSource.BufferSource bufferSource) {
        super(minecraft, bufferSource);
    }

    public ItemStack getHandInItem(){
        if(mc.player == null){
            return ItemStack.EMPTY;
        }
        return mc.player.getItemInHand(InteractionHand.MAIN_HAND);
    }

    @Override
    public void render(PoseStack poseStack, float partialTicks) {
        ItemStack handInItem = getHandInItem();
        ticker.tick(partialTicks);
        // 获取手中的物品
        if(handInItem.isEmpty() || !handInItem.is(VidaItemLoader.VIDA_WAND.get())){
            this.isRendered = false;
            alpha.decrease(ticker.getTickPercent(0.05f));
            return;
        }
        // 如果要显示alpha就增加反之就减少
        float guiAlpha = alpha.increase(ticker.getTickPercent(0.03f));

        // 根据物品的capabilities来计算每种元素要渲染的长度
        final LazyOptional<IVidaManaCapability> manaCap = handInItem.getCapability(VidaCapabilityRegisterHandler.VIDA_MANA);
        final Map<VidaElement, Integer> elementRenderWidth = new LinkedHashMap<>();

        manaCap.ifPresent(cap->{
            cap.getCurrentMana().forEach((element, manaAmount) -> {
                elementRenderWidth.put( element, cap.maxMana() <= 0 ? 0 : (int)(manaAmount * MAX_BAR_WIDTH / cap.maxMana()) );
            });
        });

        // 渲染位置在玩家物品栏之上
        int renderX = (guiWidth() - 182) / 2;
        int renderY = guiHeight() - 32 - 3;

        // 压栈1.1
        poseStack.pushPose();
        // 压栈1.2
        RenderSystem.enableBlend();
        // 设置全局的透明度和绑定材质
        RenderSystem.setShaderTexture(0, GUI_LOCATION);
        RenderSystem.setShaderColor(1,1,1, guiAlpha);

        // 绘制槽位
        blit(
                barSec.location(),
                renderX, renderY, 0,
                barSec.minU(), barSec.minV(),
                barSec.width(), barSec.height(),
                256, 256
        );

        // 槽位绘制完之后开始绘制进度条
        int offsetX = 8;
        final int offsetY = 4;

        // 绘制每种元素的能量条长度
        for(VidaElement renderElement : progressMap.keySet()){
            // 压栈2
            RenderSystem.enableBlend();
            // 元素对应颜色
            ARGBColor color = renderElement.baseColor.toARGB();
            RenderSystem.setShaderColor(
                    color.r() / 255.0f,
                    color.g() / 255.0f,
                    color.b() / 255.0f,
                    guiAlpha
            );

            // 获取当前动画帧下元素需要绘制的长度
            FloatRange rangeValue = progressMap.get(renderElement);
            int maxRenderWidth = elementRenderWidth.getOrDefault(renderElement, 0);
            float renderWidth = rangeValue.change(maxRenderWidth > rangeValue.get(), 0.5f);

            blit(
                    progressSec.location(),
                    renderX + offsetX, renderY + offsetY, 0,
                    progressSec.minU() + offsetX - 8, progressSec.minV(),
                    (int)renderWidth, progressSec.height(),
                    256, 256
            );

            // 下一个元素x偏移度
            offsetX += (int)renderWidth;
            // 出栈2
            RenderSystem.disableBlend();
        }

        // 出栈1.2
        poseStack.popPose();
        // 出栈1.1
        RenderSystem.disableBlend();

        final LazyOptional<IVidaMagicContainerCapability> containerCap = handInItem.getCapability(VidaCapabilityRegisterHandler.VIDA_MAGIC_CONTAINER);
        AtomicBoolean isInCoolDown = new AtomicBoolean(false);
        AtomicInteger coolDownWidth = new AtomicInteger(0);
        containerCap.ifPresent(cap->{
            if(cap.getContainer() != null){
                VidaMagicContainer container = cap.getContainer();
                long current = System.currentTimeMillis();
                float cooldown = Math.max(1, container.coolDown());
                isInCoolDown.set(cap.isInCoolDown(current));
                coolDownWidth.set(isInCoolDown.get() ? (int) ((current - container.lastInvokeMillSec()) * 166.0f / cooldown) : 0);
            }
        });

        if(isInCoolDown.get()){
            poseStack.pushPose();

            blit(
                    progressSec.location(),
                    renderX + 8,  renderY + 4 + 3, 0,
                    progressSec.minU(), progressSec.minV(),
                    (int)166 - coolDownWidth.get(), progressSec.height() - 3,
                    256, 256
            );

            poseStack.popPose();
        }


        this.isRendered = true;
    }

    /**
     * 当前帧数下是否被渲染
     * @return 是否被渲染
     * */
    public boolean isRendered() {
        return isRendered;
    }
}
