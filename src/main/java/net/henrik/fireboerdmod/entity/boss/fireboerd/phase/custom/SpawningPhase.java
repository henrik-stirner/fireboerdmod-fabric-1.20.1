/*
 * Decompiled with CFR 0.2.1 (FabricMC 53fa44c9).
 */
package net.henrik.fireboerdmod.entity.boss.fireboerd.phase.custom;

import net.henrik.fireboerdmod.entity.boss.fireboerd.FireboerdEntity;
import net.henrik.fireboerdmod.entity.boss.fireboerd.phase.AbstractPhase;
import net.henrik.fireboerdmod.entity.boss.fireboerd.phase.PhaseType;
import net.henrik.fireboerdmod.visual_effect.CylindricalFireScannerEffect;
import net.henrik.fireboerdmod.visual_effect.SmokeBallEffect;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.AmbientStandGoal;
import net.minecraft.entity.ai.goal.FlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.WanderNearTargetGoal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class SpawningPhase extends AbstractPhase {
    @Nullable
    private Vec3d target;

    public SpawningPhase(FireboerdEntity fireboerdEntity) {
        super(fireboerdEntity);
    }

    @Override
    public void initPhaseMoveControl() {
        this.fireboerd.setOnFlightMode();
    }

    @Override
    public void initPhaseGoals() {
        super.initPhaseGoals();
    }

    @Override
    public void serverTick() {
        if (this.ticks == 0) {
            for (PlayerEntity playerEntity: this.fireboerd.getWorld().getPlayers()) {
                playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 60),
                        this.fireboerd);
            }

            new SmokeBallEffect(
                    this.fireboerd.getWorld(),
                    this.fireboerd.getPos(),
                    100,
                    6.0d
            );
        } else if (this.ticks == 50) {
            new CylindricalFireScannerEffect(
                    this.fireboerd.getWorld(),
                    this.fireboerd.getPos(),
                    50,
                    6.0d,
                    1
            );
        } else if (this.ticks == 100) {
            this.fireboerd.addGoal(2, new FlyGoal(this.fireboerd, 0.75d));
        } else if (this.ticks == 200) {
            if (fireboerd.random.nextBoolean()) {
                this.fireboerd.getPhaseManager().setPhase(PhaseType.TERRESTRIAL);
            } else {
                this.fireboerd.getPhaseManager().setPhase(PhaseType.AERIAL);
            }
        }

        ++this.ticks;
    }

    @Override
    @Nullable
    public Vec3d getPathTarget() {
        return this.target;
    }

    public PhaseType<SpawningPhase> getType() {
        return PhaseType.SPAWNING;
    }
}