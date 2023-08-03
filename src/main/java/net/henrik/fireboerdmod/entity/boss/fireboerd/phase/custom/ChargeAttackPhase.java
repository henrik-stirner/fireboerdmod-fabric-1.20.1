package net.henrik.fireboerdmod.entity.boss.fireboerd.phase.custom;

import net.henrik.fireboerdmod.entity.boss.fireboerd.FireboerdEntity;
import net.henrik.fireboerdmod.entity.boss.fireboerd.goal.ChargeAttackGoal;
import net.henrik.fireboerdmod.entity.boss.fireboerd.goal.FlyAroundGoal;
import net.henrik.fireboerdmod.entity.boss.fireboerd.phase.AbstractPhase;
import net.henrik.fireboerdmod.entity.boss.fireboerd.phase.PhaseType;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.FlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class ChargeAttackPhase extends AbstractPhase {
    @Nullable
    private Vec3d target;

    public ChargeAttackPhase(FireboerdEntity fireboerdEntity) {
        super(fireboerdEntity);
    }

    @Override
    public void initPhaseMoveControl() {
        this.fireboerd.setOnFlightMode();
    }

    @Override
    public void initPhaseGoals() {
        super.initPhaseGoals();

        this.fireboerd.addGoal(2, new ChargeAttackGoal(this.fireboerd, 2.0d, false));
        this.fireboerd.addGoal(3, new FlyAroundGoal(this.fireboerd, 2.0d));
    }

    @Override
    public void serverTick() {
        if (this.ticks == 600) {
            this.fireboerd.getPhaseManager().setPhase(PhaseType.AERIAL);
        }

        ++this.ticks;
    }

    @Override
    @Nullable
    public Vec3d getPathTarget() {
        return this.target;
    }

    public PhaseType<ChargeAttackPhase> getType() {
        return PhaseType.CHARGE_ATTACK;
    }
}