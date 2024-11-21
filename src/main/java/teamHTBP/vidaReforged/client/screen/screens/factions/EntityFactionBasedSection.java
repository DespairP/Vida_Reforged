package teamHTBP.vidaReforged.client.screen.screens.factions;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.renderer.ui.BorderRendererManager;
import teamHTBP.vidaReforged.client.renderer.ui.IBorderRenderer;
import teamHTBP.vidaReforged.client.screen.components.fraction.LivingEntityDisplayComponent;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaFractionViewModel;
import teamHTBP.vidaReforged.core.api.screen.StyleSheet;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;
import teamHTBP.vidaReforged.core.utils.anim.SecondOrderDynamics;
import teamHTBP.vidaReforged.core.utils.color.ARGBColor;

public class EntityFactionBasedSection extends FactionBasedSection {
    /**ViewModel*/
    private VidaFractionViewModel viewModel;
    /**生物显示*/
    private LivingEntityDisplayComponent entityDisplay;
    /**边框*/
    @StyleSheet
    IBorderRenderer border;
    SecondOrderDynamics offset;
    @StyleSheet
    private Vector3f hoverOffset = new Vector3f(0, 0, 0);
    private SecondOrderDynamics alpha = new SecondOrderDynamics(1,1,0.5f, new Vector3f(1));

    public EntityFactionBasedSection(int x, int y, int width, int height) {
        super(x, y, width, height);
        //
        border = BorderRendererManager.getRender(new ResourceLocation(VidaReforged.MOD_ID, "picture_frame_border"));
        // 初始化
        entityDisplay = new LivingEntityDisplayComponent(
                getX(),
                getY(),
                LivingEntityDisplayComponent.DEFAULT_WIDTH,
                LivingEntityDisplayComponent.DEFAULT_HEIGHT,
                new ResourceLocation(VidaReforged.MOD_ID, "entity_container")
        );
        //
        offset = new SecondOrderDynamics(1, 1, 0.5f, new Vector3f());
        //
    }

    @Override
    public void onInit() {
        this.viewModel = new ViewModelProvider(requireParent()).get(VidaFractionViewModel.class);
        this.entityDisplay.setEntity(mc.player);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.hoverOffset.x = -1F * getWidth() / 4F;
        Vector3f currentOffset = offset.update(partialTicks * 0.08f, isHovered ? hoverOffset : new Vector3f(), null);
        Vector3f currentAlpha = alpha.update(partialTicks * 0.08f, isHovered ? new Vector3f(254) : new Vector3f(1), null);
        //
        FrameLayout.alignInRectangle(entityDisplay, getX() + (int) currentOffset.x(), getY(), getWidth(), getHeight(), 0.5f, 0.5f);


        this.entityDisplay.render(graphics, mouseX, mouseY, partialTicks);
        //this.border.renderBorder(graphics, entityDisplay.getX(), entityDisplay.getY(), entityDisplay.getWidth(), entityDisplay.getHeight(), 0xffffffff);
    }
}
