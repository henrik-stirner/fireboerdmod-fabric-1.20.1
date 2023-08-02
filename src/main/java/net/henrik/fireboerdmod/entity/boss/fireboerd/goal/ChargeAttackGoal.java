package net.henrik.fireboerdmod.entity.boss.fireboerd.goal;

import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;

public class ChargeAttackGoal extends MeleeAttackGoal {
    public ChargeAttackGoal(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
        super(mob, speed, pauseWhenMobIdle);
    }
}