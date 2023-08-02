package net.henrik.fireboerdmod.entity.boss.fireboerd.phase.custom;

import net.henrik.fireboerdmod.entity.boss.fireboerd.FireboerdEntity;
import net.henrik.fireboerdmod.entity.boss.fireboerd.phase.AbstractPhase;
import net.henrik.fireboerdmod.entity.boss.fireboerd.phase.PhaseType;
import net.henrik.fireboerdmod.entity.projectile.ErrantFireEntity;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.goal.WanderNearTargetGoal;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class TerrestrialPhase extends AbstractPhase {
    @Nullable
    private Vec3d target;

    public TerrestrialPhase(FireboerdEntity fireboerdEntity) {
        super(fireboerdEntity);
    }

    @Override
    public void initPhaseMoveControl() {
        this.fireboerd.setMoveControl(new MoveControl(this.fireboerd));
    }

    @Override
    public void initPhaseGoals() {
        super.initPhaseGoals();

        this.fireboerd.addGoal(2, new WanderNearTargetGoal(this.fireboerd, 1.0d, 8));
        this.fireboerd.addGoal(3, new WanderAroundFarGoal(this.fireboerd, 1.0d, 0.25f));
    }

    private void summonErrantFires() {
        if (this.fireboerd.getTarget() == null) {
            return;
        }

        for (int i = 0; i < 4; ++i) {
            ErrantFireEntity newErrantFireEntity = new ErrantFireEntity(
                    this.fireboerd.getWorld(),
                    this.fireboerd,
                    this.fireboerd.getTarget(),
                    false,
                    Direction.Axis.pickRandomAxis(Random.create())
            );
        }
    }

    @Override
    public void serverTick() {
        if (this.ticks == 200) {
            if (fireboerd.random.nextDouble() <= 0.6) {  // 60 % of cases
                this.fireboerd.getPhaseManager().setPhase(PhaseType.MELEE_ATTACK);
            } else {  // 40 %
                this.fireboerd.getPhaseManager().setPhase(PhaseType.AERIAL);
            }
        } else if (this.ticks % 50 == 0) {
            if (this.fireboerd.random.nextBoolean()) {
                this.summonErrantFires();
            }
        }

        ++this.ticks;
    }

    @Override
    @Nullable
    public Vec3d getPathTarget() {
        return this.target;
    }

    public PhaseType<TerrestrialPhase> getType() {
        return PhaseType.TERRESTRIAL;
    }
}
