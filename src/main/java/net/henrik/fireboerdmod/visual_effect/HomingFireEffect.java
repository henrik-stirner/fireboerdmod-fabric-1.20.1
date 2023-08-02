package net.henrik.fireboerdmod.visual_effect;

import net.henrik.fireboerdmod.FireboerdMod;
import net.henrik.fireboerdmod.event.VisualEffectTickHandler;
import net.henrik.fireboerdmod.visual_effect.shape.LineShape;
import net.henrik.fireboerdmod.visual_effect.shape.RingShape;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class HomingFireEffect extends VisualEffect {
    private final int RESOLUTION = 5;
    private final double LENGTH = 10.0d;

    private final double MAX_STOPOVER_OFFSET = 16.0d;
    private final double MIN_STOPOVER_OFFSET = 8.0d;
    private final double MAX_STOPOVER_YAW = 45.0d;
    private final double MIN_STOPOVER_YAW = 0.0d;

    private final double PLAYER_DETECTION_RANGE = 16.0d;

    private final Random random = new Random();

    private Vec3d stopoverPoint;
    private List<Vec3d> originToStopoverPoints;
    private int pausePhaseEndTick;
    private Vec3d destinationPoint;
    private List<Vec3d> stopoverToDestinationPoints;

    private final LineShape lineShape = new LineShape(this.RESOLUTION);

    public HomingFireEffect(World world, Vec3d position, int duration) {
        super(world, position, duration);

        if (world == null || position == null) {
            return;
        }

        VisualEffectTickHandler.homingFireEffects.add(this);
    }

    private Vec3d getStopoverPoint() {
        double offset = this.random.nextDouble() * (
                this.MAX_STOPOVER_OFFSET - this.MIN_STOPOVER_OFFSET
        ) + this.MIN_STOPOVER_OFFSET;
        double theta = random.nextDouble() * 359;
        double phi = random.nextDouble() * (this.MAX_STOPOVER_YAW - this.MIN_STOPOVER_YAW) + this.MIN_STOPOVER_YAW;

        double x = offset * Math.sin(theta) * Math.cos(phi);
        double y = offset * Math.sin(theta) * Math.sin(phi);
        double z = offset * Math.cos(theta);

        return this.position.add(x, y, z);
    }

    @Override
    public void onStartTick() {
        if (this.world.isClient()) {
            return;
        }

        if (this.ticks >= this.duration) {
            VisualEffectTickHandler.homingFireEffects.remove(this);
        }

        if (this.ticks == 0) {
            this.stopoverPoint = getStopoverPoint();
            this.originToStopoverPoints = this.lineShape.calculateCorrespondingPositions(this.position,
                    this.stopoverPoint);
            this.pausePhaseEndTick = this.duration + this.originToStopoverPoints.size();
            this.duration = this.duration + this.originToStopoverPoints.size() + 2;
        } else if (0 < this.ticks && this.ticks <= this.originToStopoverPoints.size()) {
            Vec3d point = this.originToStopoverPoints.get(this.ticks - 1);
            ((ServerWorld)this.world).spawnParticles(
                    ParticleTypes.SMOKE,
                    point.getX(),
                    point.getY(),
                    point.getZ(),
                    2,
                    0.1, 0.1, 0.1,
                    0.0
            );
            ((ServerWorld)this.world).spawnParticles(
                    ParticleTypes.POOF,
                    point.getX(),
                    point.getY(),
                    point.getZ(),
                    1,
                    0.1, 0.1, 0.1,
                    0.0
            );
        } else if (this.originToStopoverPoints.size() < this.ticks && this.ticks < this.pausePhaseEndTick) {
            ((ServerWorld)this.world).spawnParticles(
                    ParticleTypes.LARGE_SMOKE,
                    this.stopoverPoint.getX(),
                    this.stopoverPoint.getY(),
                    this.stopoverPoint.getZ(),
                    1,
                    0.1, 0.1, 0.1,
                    0.0
            );
        } else if (this.ticks == this.pausePhaseEndTick) {
            PlayerEntity closestPlayer = world.getClosestPlayer(
                    this.stopoverPoint.getX(),
                    this.stopoverPoint.getY(),
                    this.stopoverPoint.getZ(),
                    this.PLAYER_DETECTION_RANGE,
                    true
            );
            if (closestPlayer != null) {
                this.destinationPoint = closestPlayer.getPos().add(0.0d, 1.0d, 0.0d);
                this.stopoverToDestinationPoints = lineShape.calculateCorrespondingPositions(
                        this.stopoverPoint,
                        this.destinationPoint
                );
                for (Vec3d point : this.stopoverToDestinationPoints) {
                    ((ServerWorld)this.world).spawnParticles(
                            ParticleTypes.SMOKE,
                            point.getX(),
                            point.getY(),
                            point.getZ(),
                            2,
                            0.05, 0.05, 0.05,
                            0.0
                    );
                    ((ServerWorld)this.world).spawnParticles(
                            ParticleTypes.FLAME,
                            point.getX(),
                            point.getY(),
                            point.getZ(),
                            1,
                            0.125, 0.125, 0.125,
                            0.0
                    );
                }
            } else {
                this.destinationPoint = this.stopoverPoint;
            }
        } else if (this.ticks == this.pausePhaseEndTick + 1) {
            for (int i = 0; i < 8; ++i) {
                this.world.createExplosion(
                        null,
                        this.destinationPoint.getX(),
                        this.destinationPoint.getY(),
                        this.destinationPoint.getZ(),
                        0.5f,
                        true,
                        World.ExplosionSourceType.MOB
                );

                ((ServerWorld)this.world).spawnParticles(
                        ParticleTypes.LAVA,
                        this.destinationPoint.getX(),
                        this.destinationPoint.getY(),
                        this.destinationPoint.getZ(),
                        8,
                        0.25, 0.25, 0.25,
                        1
                );
                ((ServerWorld)this.world).spawnParticles(
                        ParticleTypes.POOF,
                        this.stopoverPoint.getX(),
                        this.stopoverPoint.getY(),
                        this.stopoverPoint.getZ(),
                        8,
                        0.5, 0.5, 0.5,
                        1
                );
            }
        }

        ++this.ticks;
    }

    @Override
    public Object create(World world, Vec3d position) {
        return new HomingFireEffect(
                world,
                position,
                this.duration
        );
    }
}
