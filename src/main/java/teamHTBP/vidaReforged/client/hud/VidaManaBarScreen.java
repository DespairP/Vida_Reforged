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
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;
import teamHTBP.vidaReforged.core.utils.math.FloatRange;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;
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
    private TextureSection barSection = new TextureSection(GUI_LOCATION, 0, 18, 182, 12, 256, 256);
    /**能量条的材质路径*/
    private TextureSection progressSection = new TextureSection(GUI_LOCATION, 8, 6, 166, 4, 256, 256);
    /**是否正在渲染*/
    private boolean isRendered = false;
    /**计数器*/
    VidaGuiHelper.TickHelper ticker = new VidaGuiHelper.TickHelper();


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
    public void render(GuiGraphics graphics, float partialTicks) {
        ItemStack handInItem = getHandInItem();
        ticker.tick(partialTicks);
        // 获取手中的物品
        if(handInItem.isEmpty() || !handInItem.is(VidaItemLoader.VIDA_WAND.get()) && !handInItem.is(VidaItemLoader.VIDA_ENCHANTED_BRANCH.get())){
            this.isRendered = false;
            alpha.decrease(ticker.getTickPercent(0.05f));
            progressMap.values().forEach(progress -> progress.set(0));
            return;
        }
        // 如果要显示alpha就增加反之就减少
        float guiAlpha = alpha.increase(ticker.getTickPercent(0.03f));

        // 根据物品的capabilities来计算每种元素要渲染的长度
        final LazyOptional<IVidaManaCapability> manaCap = handInItem.getCapability(VidaCapabilityRegisterHandler.VIDA_MANA);
        final Map<VidaElement, Integer> elementRenderWidth = new LinkedHashMap<>();

        manaCap.ifPresent(cap->{
            cap.getAllElementsMana().forEach((element, manaAmount) -> {
                elementRenderWidth.put( element, cap.getMaxMana() <= 0 ? 0 : (int)(manaAmount * MAX_BAR_WIDTH / cap.getMaxMana()) );
            });
        });

        // 渲染位置在玩家物品栏之上
        int renderX = (guiWidth() - 182) / 2;
        int renderY = guiHeight() - 32 - 3;

        //
        PoseStack poseStack = graphics.pose();

        // 压栈1.1
        poseStack.pushPose();
        // 压栈1.2
        RenderSystem.enableBlend();
        // 设置全局的透明度和绑定材质
        RenderSystem.setShaderTexture(0, GUI_LOCATION);
        RenderSystem.setShaderColor(1,1,1, guiAlpha);

        // 绘制槽位
        blit(
                barSection.location(),
                renderX, renderY, 0,
                barSection.minU(), barSection.minV(),
                barSection.width(), barSection.height(),
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
                    progressSection.location(),
                    renderX + offsetX, renderY + offsetY, 0,
                    progressSection.minU() + offsetX - 8, progressSection.minV(),
                    (int)renderWidth, progressSection.height(),
                    progressSection.texWidth(), progressSection.texHeight()
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

        if(isInCoolDown.get()){
            poseStack.pushPose();

            blit(
                    progressSection.location(),
                    renderX + 8,  renderY + 4 + 3, 0,
                    progressSection.minU(), progressSection.minV(),
                    (int)166 - coolDownWidth.get(), progressSection.height() - 3,
                    progressSection.texWidth(), progressSection.texHeight()
            );

            poseStack.popPose();
        }

        RenderSystem.setShaderColor( 1, 1, 1, 1);
        RenderSystem.disableBlend();
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
