package net.henrik.fireboerdmod.visual_effect;

import net.henrik.fireboerdmod.handler.tick.VisualEffectTickHandler;
import net.henrik.fireboerdmod.visual_effect.shape.RandomSphereShape;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class SmokeBallEffect extends VisualEffect {
    private final double RESOLUTION = 0.25d;

    private final RandomSphereShape randomSphereShape;

    public SmokeBallEffect(World world, Vec3d position, int duration, double radius) {
        super(world, position, duration);

        // smaller difference between min and max radius -> less particles -> better performance
        double minRadius = radius > 1 ? radius - 1 : 0;
        randomSphereShape = new RandomSphereShape(minRadius, radius, this.RESOLUTION);

        if (world == null || position == null) {
            return;
        }

        VisualEffectTickHandler.smokeBallEffects.add(this);
    }

    private void drawSphere(Vec3d origin, DefaultParticleType particleType) {
        List<Vec3d> pointsToDraw = randomSphereShape.calculateCorrespondingPositions(origin);

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
            VisualEffectTickHandler.smokeBallEffects.remove(this);
        }

        this.drawSphere(this.position, ParticleTypes.POOF);
        this.drawSphere(this.position, ParticleTypes.SMOKE);
        this.drawSphere(this.position, ParticleTypes.LARGE_SMOKE);
        this.drawSphere(this.position, ParticleTypes.CAMPFIRE_COSY_SMOKE);

        ++this.ticks;
    }

    @Override
    public Object create(World world, Vec3d position) {
        return new SmokeBallEffect(
                world,
                position,
                this.duration,
                this.randomSphereShape.getMaxRadius()
        );
    }
}
