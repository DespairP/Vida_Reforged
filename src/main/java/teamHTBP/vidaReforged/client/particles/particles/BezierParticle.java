package teamHTBP.vidaReforged.client.particles.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.client.RenderTypeHandler;
import teamHTBP.vidaReforged.core.utils.anim.SecondOrderDynamics;
import teamHTBP.vidaReforged.core.utils.math.Bezier3Curve;

public class BezierParticle extends VidaBaseParticle{
    private final Bezier3Curve bezier3Curve;
    private final SecondOrderDynamics timer;
    private final Vector3f destinationLife;

    public BezierParticle(ClientLevel level, double x, double y, double z, double speedX, double speedY, double speedZ, VidaParticleAttributes attributes) {
        super(level, x, y, z, speedX, speedY, speedZ, attributes);
        this.bezier3Curve = new Bezier3Curve(new Vector3d(x, y, z), new Vector3d(x, y, z), new Vector3d(attributes.extraPos()), new Vector3d(attributes.toPos()));
        this.timer = new SecondOrderDynamics(1f, 1f, 0.5f, new Vector3f());
        this.hasPhysics = false;
        this.destinationLife = new Vector3f(lifetime);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        // 透明度
        if (this.lifetime - 20 < this.age) this.alpha = Math.max(this.alpha - 0.05f, 0);
        if (this.age++ >= this.lifetime || this.attributes.toPos().distance((float) x,(float) y,(float) z) <= 0.1) {
            this.remove();
        } else {
            float agePercent = (float) timer.update(Minecraft.getInstance().getDeltaFrameTime(), destinationLife, null).x / this.lifetime;
            Vector3d toPos = bezier3Curve.getPoint(agePercent);
            this.x = toPos.x;
            this.y = toPos.y;
            this.z = toPos.z;
        }
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return RenderTypeHandler.PARTICLE_SHEET_NO_MASK;
    }
}
