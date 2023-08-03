package net.henrik.fireboerdmod.entity.boss.fireboerd.goal;

import net.henrik.fireboerdmod.entity.boss.fireboerd.FireboerdEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Objects;

public class FlyAroundTargetGoal extends Goal {
    private final double PREFERRED_HEIGHT = 8.0d;
    private final double MAX_TARGET_OFFSET = 16.0d;

    private final FireboerdEntity fireboerd;

    private int ticker;

    @Nullable
    private LivingEntity target;

    private Vec3d targetPos;

    private final Random random;

    // multi-tick movement
    private final int TICKS_UNTIL_MOVE_TO_POS_UPDATE = 10;
    private Vec3d currentVelocity;

    public FlyAroundTargetGoal(FireboerdEntity fireboerd) {
        this.setControls(EnumSet.of(Goal.Control.MOVE));

        this.fireboerd = fireboerd;
        this.random = this.fireboerd.getRandom();
    }

    @Override
    public boolean canStart() {
        return !this.fireboerd.hasPassengers();
    }

    @Override
    public boolean shouldContinue() {
        return true;
    }

    @Override
    public void start() {
        this.ticker = 0;

        this.target = this.fireboerd.getTarget();
        this.targetPos = Objects.requireNonNullElse(this.target, this.fireboerd).getPos();

        this.updateVelocity();
    }

    private void updateTargetPos() {
        if (this.target != null) {
            this.targetPos = this.target.getPos();
        }
    }

    private void updateVelocity() {
        double angle = random.nextInt(359);
        double radius = random.nextDouble() * this.MAX_TARGET_OFFSET;

        // random position on circle around target pos with y-coordinate corresponding to the preferred offset
        Vec3d moveToPos = this.targetPos.add(
                radius * Math.cos(angle),
                (this.targetPos.getY() + this.PREFERRED_HEIGHT) - this.fireboerd.getY(),
                radius * Math.sin(angle)
        );

        Vec3d direction = moveToPos.subtract(this.fireboerd.getPos());
        double magnitude = direction.length();
        Vec3d velocity = direction.multiply(FireboerdEntity.FLIGHT_SPEED / magnitude);

        // division by this.TICKS_UNTIL_MOVE_TO_POS_UPDATE
        this.currentVelocity = velocity.multiply( 1.0d / this.TICKS_UNTIL_MOVE_TO_POS_UPDATE);
    }

    /**
     * Make the FireboerdEntity look at it's target when it has one
     * otherwise, make it look forward
     */
    private void updateRotation() {
        if (this.fireboerd.getTarget() == null) {
            Vec3d vec3d = this.fireboerd.getVelocity();
            this.fireboerd.setYaw(-((float) MathHelper.atan2(vec3d.x, vec3d.z)) * 57.295776f);
            this.fireboerd.bodyYaw = this.fireboerd.getYaw();
        } else {
            LivingEntity livingEntity = this.fireboerd.getTarget();
            if (livingEntity.squaredDistanceTo(this.fireboerd) < 4096.0) {
                double e = livingEntity.getX() - this.fireboerd.getX();
                double f = livingEntity.getZ() - this.fireboerd.getZ();
                this.fireboerd.setYaw(-((float)MathHelper.atan2(e, f)) * 57.295776f);
                this.fireboerd.bodyYaw = this.fireboerd.getYaw();
            }
        }
    }

    private void updateTicker() {
        if (this.ticker == this.TICKS_UNTIL_MOVE_TO_POS_UPDATE) {
            this.ticker = 0;
        } else {
            ++this.ticker;
        }
    }

    @Override
    public void tick() {
        this.updateTargetPos();

        if (this.ticker % 5 == 0) {
            this.fireboerd.setVelocity(this.fireboerd.getVelocity().add(0.0d, 0.5d, 0.0d));
        }

        if (this.ticker % this.TICKS_UNTIL_MOVE_TO_POS_UPDATE == 0) {
            updateVelocity();
        }

        double noise = this.random.nextDouble() * (1.125 - 0.875) + 0.875;
        this.fireboerd.setVelocity(this.fireboerd.getVelocity().add(this.currentVelocity.multiply(noise)));

        this.updateRotation();

        this.updateTicker();
    }
}