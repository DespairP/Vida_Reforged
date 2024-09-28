package teamHTBP.vidaReforged.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import teamHTBP.vidaReforged.VidaReforged;

import static teamHTBP.vidaReforged.helper.VidaRenderHelper.*;


public class VidaSkyEffects extends DimensionSpecialEffects {
    Minecraft minecraft;
    private VertexBuffer skyBuffer;
    private VertexBuffer darkBuffer;
    private VertexBuffer starBuffer;
    private VertexBuffer starGoldBuffer;
    private VertexBuffer starWoodBuffer;
    private VertexBuffer starAquaBuffer;
    private VertexBuffer starFireBuffer;
    private VertexBuffer starEarthBuffer;

    private static final ResourceLocation SUN_LOCATION = new ResourceLocation("textures/environment/sun.png");
    private static final ResourceLocation MOON_LOCATION = new ResourceLocation("textures/environment/moon_phases.png");

    public VidaSkyEffects() {
        super(192.0f, true, SkyType.NORMAL, false, false);
        this.minecraft = Minecraft.getInstance();
        this.skyBuffer = createLightSky();
        this.darkBuffer = createDarkSky();
        this.starBuffer = createStars(RandomSource.create(10842L), 1200, 130.0D);
        this.starGoldBuffer = createStars(RandomSource.create(10230L), 30, 120.0D);
        this.starWoodBuffer = createStars(RandomSource.create(20410L), 20, 120.0D);
        this.starAquaBuffer = createStars(RandomSource.create(40230L), 10, 120.0D);
        this.starFireBuffer = createStars(RandomSource.create(32230L), 30, 120.0D);
        this.starEarthBuffer = createStars(RandomSource.create(32230L), 60, 120.0D);
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 biomeFogColor, float daylight) {
        return biomeFogColor.multiply((double)(daylight * 0.94F + 0.06F), (double)(daylight * 0.94F + 0.06F), (double)(daylight * 0.91F + 0.09F));
    }

    @Override
    public boolean isFoggyAt(int x, int y) {
        return false;
    }

    private boolean doesMobEffectBlockSky(Camera camera) {
        Entity entity = camera.getEntity();
        if (!(entity instanceof LivingEntity livingentity)) {
            return false;
        } else {
            return livingentity.hasEffect(MobEffects.BLINDNESS) || livingentity.hasEffect(MobEffects.DARKNESS);
        }
    }

    boolean isFoggy(Minecraft minecraft, Camera camera) {
        Vec3 cameraPos = camera.getPosition();
        boolean isFoggy = minecraft.level.effects().isFoggyAt(Mth.floor(cameraPos.x()), Mth.floor(cameraPos.y())) || minecraft.gui.getBossOverlay().shouldCreateWorldFog();
        if(isFoggy)
            return true;

        FogType fogtype = camera.getFluidInCamera();
        return fogtype == FogType.POWDER_SNOW || fogtype == FogType.LAVA || doesMobEffectBlockSky(camera);
    }

    @Override
    public boolean renderSky(ClientLevel level, int ticks, float partialTicks, PoseStack poseStack, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog) {
        setupFog.run();
        if(isFoggy(this.minecraft, camera)) {
            return false;
        }
        Vec3 skyColor = level.getSkyColor(Minecraft.getInstance().gameRenderer.getMainCamera().getPosition(), partialTicks);
        float skyR = (float)skyColor.x;
        float skyG = (float)skyColor.y;
        float skyB = (float)skyColor.z;
        ShaderInstance shaderinstance = RenderSystem.getShader();

        FogRenderer.levelFogColor();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        RenderSystem.depthMask(false);

        // 渲染天空盒
        this.renderSkyBox(level, partialTicks, poseStack, projectionMatrix, bufferbuilder, shaderinstance);
        // 渲染光辉
        this.renderSunrise(level, partialTicks, poseStack, projectionMatrix, bufferbuilder);
        // 渲染日月
        this.renderSunAndMoon(level, partialTicks, poseStack, projectionMatrix, bufferbuilder);
        // 渲染星空
        this.renderStars(level, partialTicks, poseStack, projectionMatrix, bufferbuilder, shaderinstance);
        // TODO: 星座渲染
        //this.renderAstrologicalSign(level, partialTicks, poseStack, projectionMatrix, bufferbuilder);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();

        // Phase 2
        // 渲染虚空
        RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
        this.renderDark(level, partialTicks, poseStack, projectionMatrix, bufferbuilder, shaderinstance);


        if(level.effects().hasGround()){
            RenderSystem.setShaderColor(skyR * 0.2F + 0.04F, skyG * 0.2F + 0.04F, skyB * 0.6F + 0.1F, 1.0F);
        } else{
            RenderSystem.setShaderColor(skyR, skyG, skyB, 1.0F);
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        RenderSystem.depthMask(true);
        return true;
    }

    // From StellarView
    private void renderSunrise(ClientLevel level, float partialTicks, PoseStack stack, Matrix4f projectionMatrix, BufferBuilder bufferbuilder) {
        float[] sunriseColor = level.effects().getSunriseColor(level.getTimeOfDay(partialTicks), partialTicks);
        if(sunriseColor != null) {
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            stack.pushPose();
            stack.mulPose(Axis.XP.rotationDegrees(90.0F));
            float sunAngle = Mth.sin(level.getSunAngle(partialTicks)) < 0.0F ? 180.0F : 0.0F;
            stack.mulPose(Axis.ZP.rotationDegrees(sunAngle));
            stack.mulPose(Axis.ZP.rotationDegrees(90.0F));
            float sunriseR = sunriseColor[0];
            float sunriseG = sunriseColor[1];
            float sunriseB = sunriseColor[2];
            float sunriseA = sunriseColor[2];
            Matrix4f sunriseMatrix = stack.last().pose();
            bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
            bufferbuilder.vertex(sunriseMatrix, 0.0F, 100.0F, 0.0F).color(sunriseR, sunriseG, sunriseB, sunriseA).endVertex();
            for(int i = 0; i <= 16; ++i)
            {
                float rotation = (float)i * ((float)Math.PI * 2F) / 16.0F;
                float x = Mth.sin(rotation);
                float y = Mth.cos(rotation);
                bufferbuilder.vertex(sunriseMatrix, x * 120.0F, y * 120.0F, -y * 40.0F * sunriseA).color(sunriseR, sunriseG, sunriseB, 0.0F).endVertex();
            }

            BufferUploader.drawWithShader(bufferbuilder.end());
            stack.popPose();
        }
    }

    private void renderSunAndMoon(ClientLevel level, float partialTicks, PoseStack stack, Matrix4f projectionMatrix, BufferBuilder bufferbuilder){
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        stack.pushPose();
        float f11 = 1.0F - level.getRainLevel(partialTicks);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, f11);
        stack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        stack.mulPose(Axis.XP.rotationDegrees(level.getTimeOfDay(partialTicks) * 360.0F));
        Matrix4f matrix4f1 = stack.last().pose();
        float f12 = 30.0F;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, SUN_LOCATION);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f1, -f12, 100.0F, -f12).uv(0.0F, 0.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, f12, 100.0F, -f12).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, f12, 100.0F, f12).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, -f12, 100.0F, f12).uv(0.0F, 1.0F).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());

        f12 = 20.0F;
        RenderSystem.setShaderTexture(0, MOON_LOCATION);
        int k = level.getMoonPhase();
        int l = k % 4;
        int i1 = k / 4 % 2;
        float f13 = (float)(l + 0) / 4.0F;
        float f14 = (float)(i1 + 0) / 2.0F;
        float f15 = (float)(l + 1) / 4.0F;
        float f16 = (float)(i1 + 1) / 2.0F;
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f1, -f12, -100.0F, f12).uv(f15, f16).endVertex();
        bufferbuilder.vertex(matrix4f1, f12, -100.0F, f12).uv(f13, f16).endVertex();
        bufferbuilder.vertex(matrix4f1, f12, -100.0F, -f12).uv(f13, f14).endVertex();
        bufferbuilder.vertex(matrix4f1, -f12, -100.0F, -f12).uv(f15, f14).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());

        stack.popPose();
    }

    private void renderAstrologicalSign(ClientLevel level, float partialTicks, PoseStack stack, Matrix4f projectionMatrix, BufferBuilder bufferbuilder){
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        stack.pushPose();
        float f11 = 1.0F - level.getRainLevel(partialTicks);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, f11);
        stack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        stack.mulPose(Axis.XP.rotationDegrees(130.0f + level.getTimeOfDay(partialTicks) * 360.0F));
        Matrix4f matrix4f1 = stack.last().pose();
        float f12 = 20.0F;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        //RenderSystem.setShaderTexture(0, new ResourceLocation(VidaReforged.MOD_ID, "textures/environment/background_discidia.png"));
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f1, -f12, 100.0F, -f12).uv(0.0F, 0.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, f12, 100.0F, -f12).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, f12, 100.0F, f12).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(matrix4f1, -f12, 100.0F, f12).uv(0.0F, 1.0F).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        stack.popPose();
    }

    public void renderSkyBox(ClientLevel level, float partialTicks, PoseStack stack, Matrix4f projectionMatrix, BufferBuilder bufferbuilder, ShaderInstance shaderInstance){
        Vec3 skyColor = level.getSkyColor(Minecraft.getInstance().gameRenderer.getMainCamera().getPosition(), partialTicks);
        float skyR = (float)skyColor.x;
        float skyG = (float)skyColor.y;
        float skyB = (float)skyColor.z;
        RenderSystem.setShaderColor(skyR, skyG, skyB, 1F);
        this.skyBuffer.bind();
        this.skyBuffer.drawWithShader(stack.last().pose(), projectionMatrix, shaderInstance);
        VertexBuffer.unbind();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
    }

    public void renderDark(ClientLevel level, float partialTicks, PoseStack stack, Matrix4f projectionMatrix, BufferBuilder bufferbuilder, ShaderInstance shaderInstance){
        double height = minecraft.player.getEyePosition(partialTicks).y - level.getLevelData().getHorizonHeight(level);
        if(height < 0.0D) {
            stack.pushPose();
            stack.translate(0.0F, 12.0F, 0.0F);
            darkBuffer.bind();
            darkBuffer.drawWithShader(stack.last().pose(), projectionMatrix, shaderInstance);
            VertexBuffer.unbind();
            stack.popPose();
        }
    }

    public void renderStars(ClientLevel level, float partialTicks, PoseStack stack, Matrix4f projectionMatrix, BufferBuilder bufferbuilder, ShaderInstance shaderInstance){
        float timeOfDay = level.getTimeOfDay(partialTicks);
        float rainAlpha = 1.0F - level.getRainLevel(partialTicks);
        final float maxAlpha = 0.9f;
        // 0.2 ~ 0.7(0.2~0.4),(0.6~0.8)
        rainAlpha = timeOfDay < 0.2 || timeOfDay > 0.8 ? Math.min(rainAlpha, 0.1f) : (
                timeOfDay >= 0.2 && timeOfDay <= 0.4 ? Math.min(rainAlpha, 0.1f + maxAlpha * ((timeOfDay - 0.2f) / 0.2f)) :  // 落山的时候的透明度
                (timeOfDay >= 0.6 && timeOfDay <= 0.8 ? Math.min(rainAlpha, 0.1f + maxAlpha * ((0.8f - timeOfDay) / 0.2f)) :
                        1F) // 夜晚
        );

        stack.pushPose();
        RenderSystem.setShaderColor(1, 1, 1, rainAlpha);
        stack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        stack.mulPose(Axis.XP.rotationDegrees(timeOfDay * 360.0F));

        starBuffer.bind();
        starBuffer.drawWithShader(stack.last().pose(), projectionMatrix, shaderInstance);
        VertexBuffer.unbind();
        stack.popPose();
        RenderSystem.setShaderColor(1,1,1,1);

        RenderSystem.setShaderColor(1f, 0.7f, 0f, rainAlpha);
        renderStarWithBuffer(level, partialTicks, stack, projectionMatrix, shaderInstance, starGoldBuffer);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1F);

        RenderSystem.setShaderColor(1f, 0.97f, 0.1f, rainAlpha);
        renderStarWithBuffer(level, partialTicks, stack, projectionMatrix, shaderInstance, starWoodBuffer);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1F);

        RenderSystem.setShaderColor(1f, 0.65f, 1.0f, rainAlpha);
        renderStarWithBuffer(level, partialTicks, stack, projectionMatrix, shaderInstance, starAquaBuffer);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1F);

        RenderSystem.setShaderColor(1f, 0.78f, 0.78f, rainAlpha);
        renderStarWithBuffer(level, partialTicks, stack, projectionMatrix, shaderInstance, starFireBuffer);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1F);

        RenderSystem.setShaderColor(0.8f, 0.68f, 0.68f, rainAlpha);
        renderStarWithBuffer(level, partialTicks, stack, projectionMatrix, shaderInstance, starEarthBuffer);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1F);
    }

    private void renderStarWithBuffer(ClientLevel level,float partialTicks, PoseStack stack, Matrix4f projectionMatrix, ShaderInstance shaderInstance, VertexBuffer buffer){
        stack.pushPose();
        stack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        stack.mulPose(Axis.XP.rotationDegrees(level.getTimeOfDay(partialTicks) * 360.0F));
        stack.mulPose(Axis.ZP.rotationDegrees(level.getTimeOfDay(partialTicks) * 360.0F));
        buffer.bind();
        buffer.drawWithShader(stack.last().pose(), projectionMatrix, shaderInstance);
        VertexBuffer.unbind();
        stack.popPose();
    }

}
