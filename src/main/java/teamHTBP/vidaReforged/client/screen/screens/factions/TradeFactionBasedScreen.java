package teamHTBP.vidaReforged.client.screen.screens.factions;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.joml.Vector3f;
import org.joml.Vector4f;
import teamHTBP.vidaReforged.client.events.ClientTickHandler;
import teamHTBP.vidaReforged.client.events.ShadersHandler;
import teamHTBP.vidaReforged.client.screen.viewModels.VidaFractionViewModel;
import teamHTBP.vidaReforged.core.api.screen.StyleSheet;
import teamHTBP.vidaReforged.core.common.ui.component.ViewModelProvider;
import teamHTBP.vidaReforged.core.utils.anim.SecondOrderDynamics;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;
import teamHTBP.vidaReforged.server.menu.TradeFractionMenu;

public class TradeFactionBasedScreen extends FactionBasedScreen<TradeFractionMenu>{
    private VidaFractionViewModel viewModel;
    @StyleSheet
    ShaderInstance background = ShadersHandler.gradientLight;
    private SecondOrderDynamics pos = new SecondOrderDynamics(1, 1, 0.5f, new Vector3f(-1));

    public TradeFactionBasedScreen(TradeFractionMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void renderBackground(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        Vector3f newPos = pos.update(partialTicks * 0.05f,new Vector3f(0), null);
        ShadersHandler.setVector4fParam(background, "backgroundColor", new Vector4f(0.16f, 0.41f, 0.43f, 1.0f));
        ShadersHandler.setVector4fParam(background, "frontColor", new Vector4f(0.77f, 0.5f, 0.44f, 1.0f));
        ShadersHandler.setVector4fParam(background, "frontColor1", new Vector4f(0.99f, 0.1f, 0.44f, 1.0f));
        ShadersHandler.setPoint2f(background, "pos", newPos.x, 0f);
        VidaGuiHelper.blitWithShader(graphics, background, 0, 0, 0, width, height, (int) ClientTickHandler.ticks, partialTicks);
    }


    @Override
    public void added() {
        super.added();
        this.viewModel = new ViewModelProvider(this).get(VidaFractionViewModel.class);
    }

    @Override
    protected FactionBasedSection addEntityFactionSection() {
        return new EntityFactionBasedSection(0, 0, vw(35), vh(40));
    }

    @Override
    protected FactionBasedSection addExperienceFactionSection() {
        return new ExperienceFactionSection(0, 0, vw(35), vh(30));
    }

    @Override
    protected FactionBasedSection addOtherFactionSection() {
        return new OwnerFactionSection(0,0, vw(35), vh(30));
    }

    @Override
    protected FactionBasedSection addDetailedFactionSection() {
        return new TradeFactionBasedSection(0, 0, vw(70), vh(100));
    }
}
