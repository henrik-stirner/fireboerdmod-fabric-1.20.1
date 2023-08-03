package net.henrik.fireboerdmod.entity.boss.fireboerd.phase.custom;

import net.henrik.fireboerdmod.entity.boss.fireboerd.FireboerdEntity;
import net.henrik.fireboerdmod.entity.boss.fireboerd.phase.AbstractPhase;
import net.henrik.fireboerdmod.entity.boss.fireboerd.phase.PhaseType;
import net.henrik.fireboerdmod.entity.projectile.ErrantFireEntity;
import net.henrik.fireboerdmod.visual_effect.HomingFireEffect;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.goal.WanderNearTargetGoal;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class MeleeAttackPhase extends AbstractPhase {
    @Nullable
    private Vec3d target;

    public MeleeAttackPhase(FireboerdEntity fireboerdEntity) {
        super(fireboerdEntity);
    }

    @Override
    public void initPhaseMoveControl() {
        this.fireboerd.setOnDriveMode();
    }

    @Override
    public void initPhaseGoals() {
        this.phaseGoals.put(2, new MeleeAttackGoal(this.fireboerd, FireboerdEntity.ATTACK_SPEED, false));
        this.phaseGoals.put(3, new WanderNearTargetGoal(this.fireboerd, FireboerdEntity.MOVEMENT_SPEED, 8));
    }

    @Override
    public void serverTick() {
        super.serverTick();

        if (this.ticks == 600) {
            this.fireboerd.getPhaseManager().setPhase(PhaseType.TERRESTRIAL);
        }

        ++this.ticks;
    }

    @Override
    @Nullable
    public Vec3d getPathTarget() {
        return this.target;
    }

    public PhaseType<MeleeAttackPhase> getType() {
        return PhaseType.MELEE_ATTACK;
    }
}
