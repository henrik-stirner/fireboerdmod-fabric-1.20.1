package net.henrik.fireboerdmod.entity.boss.fireboerd.phase.custom;

import net.henrik.fireboerdmod.entity.boss.fireboerd.FireboerdEntity;
import net.henrik.fireboerdmod.entity.boss.fireboerd.goal.ChargeAttackGoal;
import net.henrik.fireboerdmod.entity.boss.fireboerd.goal.FlyAroundTargetGoal;
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
        this.phaseGoals.put(2, new ChargeAttackGoal(this.fireboerd));
        this.phaseGoals.put(3, new FlyAroundTargetGoal(this.fireboerd));
    }

    @Override
    public void serverTick() {
        super.serverTick();

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