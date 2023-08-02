package net.henrik.fireboerdmod.entity.boss.fireboerd.phase.custom;

import net.henrik.fireboerdmod.entity.boss.fireboerd.FireboerdEntity;
import net.henrik.fireboerdmod.entity.boss.fireboerd.goal.FlyAroundGoal;
import net.henrik.fireboerdmod.entity.boss.fireboerd.phase.AbstractPhase;
import net.henrik.fireboerdmod.entity.boss.fireboerd.phase.PhaseType;
import net.henrik.fireboerdmod.visual_effect.HomingFireEffect;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.FlyGoal;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class AerialPhase extends AbstractPhase {
    @Nullable
    private Vec3d target;

    public AerialPhase(FireboerdEntity fireboerdEntity) {
        super(fireboerdEntity);
    }

    @Override
    public void initPhaseMoveControl() {
        this.fireboerd.setMoveControl(new FlightMoveControl(this.fireboerd, 10, true));
    }

    @Override
    public void initPhaseGoals() {
        super.initPhaseGoals();

        this.fireboerd.addGoal(2, new FlyAroundGoal(this.fireboerd, 2.0d));
    }

    private void summonHomingFires() {
        if (this.fireboerd.getTarget() == null) {
            return;
        }

        for (int i = 0; i < 4; ++i) {
            new HomingFireEffect(this.fireboerd.getWorld(), this.fireboerd.getPointAroundPlayer(), 60);
        }
    }

    @Override
    public void serverTick() {
        if (this.ticks == 200) {
            if (fireboerd.random.nextDouble() <= 0.6) {  // 60 % of cases
                this.fireboerd.getPhaseManager().setPhase(PhaseType.CHARGE_ATTACK);
            } else {  // 40 %
                this.fireboerd.getPhaseManager().setPhase(PhaseType.TERRESTRIAL);
            }
        } else if (this.ticks % 99 == 0) {
            if (this.fireboerd.random.nextBoolean()) {
                this.summonHomingFires();
            }
        }

        ++this.ticks;
    }

    @Override
    @Nullable
    public Vec3d getPathTarget() {
        return this.target;
    }

    public PhaseType<AerialPhase> getType() {
        return PhaseType.AERIAL;
    }
}