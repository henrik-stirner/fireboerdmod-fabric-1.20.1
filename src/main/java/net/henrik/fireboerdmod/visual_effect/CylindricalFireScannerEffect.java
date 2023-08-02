package net.henrik.fireboerdmod.visual_effect;

import net.henrik.fireboerdmod.event.VisualEffectTickHandler;
import net.henrik.fireboerdmod.visual_effect.shape.RingShape;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class CylindricalFireScannerEffect extends VisualEffect {
    private final int RESOLUTION = 5;
    private final double HEIGHT = 10.0d;
    private final double cycles;

    private final int ticksPerCycle;

    private final RingShape ringShape;

    public CylindricalFireScannerEffect(World world, Vec3d position, int duration, double radius, double cycles) {
        super(world, position, duration);

        this.cycles = cycles;
        this.ticksPerCycle = (int)(this.duration / this.cycles);

        ringShape = new RingShape(radius, RESOLUTION);

        if (world == null || position == null) {
            return;
        }

        VisualEffectTickHandler.cylindricalFireScannerEffects.add(this);
    }

    private void drawCircle(Vec3d origin, DefaultParticleType particleType) {
        List<Vec3d> pointsToDraw = ringShape.calculateCorrespondingPositions(origin);

        for (Vec3d point : pointsToDraw) {
            ((ServerWorld)this.world).spawnParticles(
                    particleType,
                    point.getX(),
                    point.getY(),
                    point.getZ(),
                    1,
                    0.0, 0.0, 0.0,
                    0.0
            );
        }
    }

    @Override
    public void onStartTick() {
        if (this.world.isClient()) {
            return;
        }

        if (this.ticks >= this.duration) {
            VisualEffectTickHandler.cylindricalFireScannerEffects.remove(this);
        }

        double currentCycle = (double) this.ticks / this.ticksPerCycle;

        // cast: (int) currentCycle -> cuts off anything after the point
        double cycleTicks = ticks - (((int) currentCycle) * this.ticksPerCycle);

        double heightOffset = (cycleTicks / this.ticksPerCycle) * this.HEIGHT;

        Vec3d circleOrigin;
        if ((int) currentCycle % 2 == 0) {
            circleOrigin = this.position.add(0.0d, (this.HEIGHT - heightOffset), 0.0d);
        } else {
            circleOrigin = this.position.add(0.0d, heightOffset, 0.0d);
        }

        this.drawCircle(circleOrigin, ParticleTypes.FLAME);

        ++this.ticks;
    }

    @Override
    public Object create(World world, Vec3d position) {
        return new CylindricalFireScannerEffect(
                world,
                position,
                this.duration,
                this.ringShape.getRadius(),
                this.cycles
        );
    }
}
