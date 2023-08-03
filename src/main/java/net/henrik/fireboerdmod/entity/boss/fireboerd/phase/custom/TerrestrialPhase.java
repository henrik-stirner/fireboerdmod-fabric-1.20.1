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
        this.fireboerd.setOnDriveMode();
    }

    @Override
    public void initPhaseGoals() {
        this.phaseGoals.put(2, new WanderNearTargetGoal(this.fireboerd, FireboerdEntity.MOVEMENT_SPEED, 8));
    }

    private void summonErrantFires() {
        if (this.fireboerd.getTarget() == null) {
            return;
        }

        for (int i = 0; i < 16; ++i) {
            ErrantFireEntity newErrantFireEntity = new ErrantFireEntity(
                    this.fireboerd.getWorld(),
                    this.fireboerd,
                    this.fireboerd.getTarget(),
                    false,
                    Direction.Axis.pickRandomAxis(Random.create())
            );
            this.fireboerd.getWorld().spawnEntity(newErrantFireEntity);
        }
    }

    @Override
    public void serverTick() {
        super.serverTick();

        if (this.ticks == 200) {
            if (fireboerd.getRandom().nextDouble() <= 0.6) {  // 60 % of cases
                this.fireboerd.getPhaseManager().setPhase(PhaseType.MELEE_ATTACK);
            } else {  // 40 %
                this.fireboerd.getPhaseManager().setPhase(PhaseType.AERIAL);
            }
        } else if (this.ticks % 50 == 0) {
            if (this.fireboerd.getRandom().nextBoolean()) {
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
