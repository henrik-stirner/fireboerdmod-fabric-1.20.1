package net.henrik.fireboerdmod.entity.boss.fireboerd.goal;

import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class FlyAroundGoal extends WanderAroundGoal {

    private final int DEFAULT_CHANCE = 1000;

    public FlyAroundGoal(PathAwareEntity mob, double speed) {
        super(mob, speed);

        this.chance = DEFAULT_CHANCE;
    }

    public FlyAroundGoal(PathAwareEntity mob, double speed, int chance) {
        super(mob, speed, chance);
    }

    @Override
    @Nullable
    protected Vec3d getWanderTarget() {
        Vec3d vec3d = this.mob.getRotationVec(0.0f);

        Vec3d vec3d2 = AboveGroundTargeting.find(this.mob, 12, 4, vec3d.x, vec3d.z, 1.5707964f, 3, 1);
        if (vec3d2 != null) {
            return vec3d2;
        }

        return NoPenaltySolidTargeting.find(this.mob, 12, 4, -2, vec3d.x, vec3d.z, 1.5707963705062866);
    }
}