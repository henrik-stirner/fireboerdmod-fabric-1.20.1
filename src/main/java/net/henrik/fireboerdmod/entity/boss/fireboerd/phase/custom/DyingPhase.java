/*
 * Decompiled with CFR 0.2.1 (FabricMC 53fa44c9).
 */
package net.henrik.fireboerdmod.entity.boss.fireboerd.phase.custom;

import net.henrik.fireboerdmod.entity.boss.fireboerd.FireboerdEntity;
import net.henrik.fireboerdmod.entity.boss.fireboerd.phase.AbstractPhase;
import net.henrik.fireboerdmod.entity.boss.fireboerd.phase.PhaseType;
import net.henrik.fireboerdmod.entity.boss.phase.Phase;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.ai.goal.WanderNearTargetGoal;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import org.jetbrains.annotations.Nullable;

public class DyingPhase
extends AbstractPhase {
    @Nullable
    private Vec3d target;

    public DyingPhase(FireboerdEntity fireboerdEntity) {
        super(fireboerdEntity);
    }

    @Override
    public void initPhaseMoveControl() {
        this.fireboerd.setOnDriveMode();
    }

    @Override
    public void initPhaseGoals() {
        super.initPhaseGoals();

        this.fireboerd.addGoal(2, new MeleeAttackGoal(this.fireboerd, 0.5d, false));
        this.fireboerd.addGoal(3, new WanderNearTargetGoal(this.fireboerd, 0.5d, 8));
    }

    @Override
    public void serverTick() {
        if (this.ticks % 10 == 0) {
            float dx = (this.fireboerd.getRandom().nextFloat() - 0.5f) * 8.0f;
            float dy = (this.fireboerd.getRandom().nextFloat() - 0.5f) * 4.0f;
            float dz = (this.fireboerd.getRandom().nextFloat() - 0.5f) * 8.0f;
            ((ServerWorld)this.fireboerd.getWorld()).spawnParticles(ParticleTypes.LAVA, this.fireboerd.getX() + (double)dx, this.fireboerd.getY() + 2.0 + (double)dy, this.fireboerd.getZ() + (double)dz, 10, 0.2, 0.2, 0.2, 0.0);
        }

        ++this.ticks;
    }

    @Override
    public void beginPhase() {
        super.beginPhase();

        this.target = null;
    }

    @Override
    @Nullable
    public Vec3d getPathTarget() {
        return this.target;
    }

    public PhaseType<DyingPhase> getType() {
        return PhaseType.DYING;
    }
}