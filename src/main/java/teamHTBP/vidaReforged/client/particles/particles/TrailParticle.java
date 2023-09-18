package teamHTBP.vidaReforged.client.particles.particles;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import org.joml.Vector3d;
import org.joml.Vector3f;
import teamHTBP.vidaReforged.core.utils.math.Bezier3Curve;

import java.util.ArrayList;

import static teamHTBP.vidaReforged.client.RenderTypeHandler.TRAIL_SHADER;

public class TrailParticle extends TextureSheetParticle {
    public Minecraft mc = Minecraft.getInstance();
    private Bezier3Curve curve;
    protected ArrayList<Vector3d> tails = new ArrayList<>();
    protected int maxTail;

    protected TrailParticle(ClientLevel p_108323_, double p_108324_, double p_108325_, double p_108326_) {
        super(p_108323_, p_108324_, p_108325_, p_108326_);

    }

    @Override
    public ParticleRenderType getRenderType() {
        return TRAIL_SHADER;
    }


    @Override
    public void tick() {
        if (getLifetime() > 0) {
            var t = (mc.getFrameTime() + age) / lifetime;
            Vector3d point = curve.getPoint(t);
            setPos(point.x, point.y, point.z);
        }

        if (this.lifetime - this.age < 15) {
            tails.remove(0);
        }
    }
}
