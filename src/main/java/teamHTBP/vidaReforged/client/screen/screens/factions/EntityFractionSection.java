package teamHTBP.vidaReforged.client.screen.screens.factions;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.client.screen.components.fraction.LivingEntityDisplayComponent;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaFractionViewModel;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;

public class EntityFractionSection extends FactionSection{
    /**ViewModel*/
    private VidaFractionViewModel viewModel;
    /**生物显示*/
    private LivingEntityDisplayComponent entityDisplay;

    public EntityFractionSection(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void onInit() {
        viewModel = new ViewModelProvider(requireParent()).get(VidaFractionViewModel.class);
        // 初始化
        entityDisplay = new LivingEntityDisplayComponent(
                getX(),
                getY(),
                LivingEntityDisplayComponent.DEFAULT_WIDTH,
                LivingEntityDisplayComponent.DEFAULT_HEIGHT,
                new ResourceLocation(VidaReforged.MOD_ID, "entity_container")
        );
        //

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {

    }
}
