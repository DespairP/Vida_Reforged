package teamHTBP.vidaReforged.client.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import teamHTBP.vidaReforged.VidaReforged;
import teamHTBP.vidaReforged.core.api.VidaElement;
import teamHTBP.vidaReforged.core.api.hud.IVidaEntityScreen;
import teamHTBP.vidaReforged.core.api.hud.IVidaScreen;
import teamHTBP.vidaReforged.core.utils.render.TextureSection;
import teamHTBP.vidaReforged.helper.VidaGuiHelper;
import teamHTBP.vidaReforged.server.blockEntities.FloatingCrystalBlockEntity;
import teamHTBP.vidaReforged.server.events.VidaCapabilityRegisterHandler;

import java.util.Map;

public class VidaCrystalManaScreen extends AbstractVidaHUDScreen implements IVidaEntityScreen {
    private BlockEntity blockEntity;
    private static final ResourceLocation LOCATION = new ResourceLocation(VidaReforged.MOD_ID, "textures/gui/crystal.png");
    private static final Map<VidaElement, TextureSection> ELEMENT_BACKGROUND = Map.of(
            VidaElement.GOLD, new TextureSection(LOCATION, 66, 2, 28, 11, 528, 528),
            VidaElement.WOOD, new TextureSection(LOCATION, 193, 1, 30, 12, 528, 528),
            VidaElement.AQUA, new TextureSection(LOCATION, 257, 1, 30, 14, 528, 528),
            VidaElement.FIRE, new TextureSection(LOCATION, 128, 2, 32, 12, 528, 528),
            VidaElement.EARTH, new TextureSection(LOCATION, 66, 2, 28, 11, 528, 528)
    );

    private static final Map<VidaElement, TextureSection> ELEMENT_BACKGROUND_FILL = Map.of(
            VidaElement.GOLD, new TextureSection(LOCATION, 34, 2, 28, 11, 528, 528),
            VidaElement.WOOD, new TextureSection(LOCATION, 161, 1, 30, 12, 528, 528),
            VidaElement.AQUA, new TextureSection(LOCATION, 225, 1, 30, 14, 528, 528),
            VidaElement.FIRE, new TextureSection(LOCATION, 96, 2, 32, 12, 528, 528),
            VidaElement.EARTH, new TextureSection(LOCATION, 34, 2, 28, 11, 528, 528)
    );

    private static final Map<VidaElement, TextureSection> ELEMENT_CUBE = Map.of(
            VidaElement.GOLD, new TextureSection(LOCATION, 18, 65, 12, 14, 528, 528),
            VidaElement.WOOD, new TextureSection(LOCATION, 18, 49, 12, 14, 528, 528),
            VidaElement.AQUA, new TextureSection(LOCATION, 18, 33, 12, 14, 528, 528),
            VidaElement.FIRE, new TextureSection(LOCATION, 18, 17, 12, 14, 528, 528),
            VidaElement.EARTH, new TextureSection(LOCATION, 18, 81, 12, 14, 528, 528)
    );

    public VidaCrystalManaScreen(Minecraft minecraft, MultiBufferSource.BufferSource bufferSource, BlockEntity blockEntity) {
        super(minecraft, bufferSource);
        this.blockEntity = blockEntity;

    }

    @Override
    public void render(GuiGraphics graphics, float partialTicks) {
        FloatingCrystalBlockEntity crystal = null;
        if(blockEntity instanceof FloatingCrystalBlockEntity entity){
            crystal = entity;
        }
        if(crystal == null || crystal.getElement() == null || crystal.getElement() == VidaElement.VOID || crystal.getElement() == VidaElement.EMPTY){
            return;
        }
        final VidaElement element = crystal.getElement();
        final TextureSection background = ELEMENT_BACKGROUND.get(element);
        final TextureSection backgroundFill = ELEMENT_BACKGROUND_FILL.get(element);
        final TextureSection cube = ELEMENT_CUBE.get(element);

        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();

        final int centerX = centerX(background);
        final int centerY = centerY(background) - 30;
        final int cubeCenterX = centerX(cube);
        VidaGuiHelper.blitWithTexture(graphics, centerX, centerY, 1, background);
        crystal.getCapability(VidaCapabilityRegisterHandler.VIDA_MANA).ifPresent(cap -> {
            final double percentage = cap.getManaByElement(element) / cap.getMaxMana();
            final int offset = (int) ((backgroundFill.w() / 2.0F) * percentage);
            graphics.blit(
                    backgroundFill.location(),
                    centerX + backgroundFill.w() / 2 - offset, centerY, 1,
                    backgroundFill.minU() + backgroundFill.w() / 2 - offset, backgroundFill.minV(),
                    offset, backgroundFill.h(),
                    backgroundFill.texWidth(), backgroundFill.texHeight()
            );
            graphics.blit(
                    backgroundFill.location(),
                    centerX + backgroundFill.w() / 2 , centerY, 1,
                    backgroundFill.minU() + backgroundFill.w() / 2 , backgroundFill.minV(),
                    offset, backgroundFill.h(),
                    backgroundFill.texWidth(), backgroundFill.texHeight()
            );
        });
        //VidaGuiHelper.blitWithTexture(graphics, cubeCenterX, centerY + 30, 1, cube);
        poseStack.popPose();
    }

    @Override
    public void setBlockEntity(BlockEntity blockEntity) {

    }
}
