/*
 * Decompiled with CFR 0.2.1 (FabricMC 53fa44c9).
 */
package net.henrik.fireboerdmod.entity.boss.fireboerd.phase;

import it.unimi.dsi.fastutil.Hash;
import net.henrik.fireboerdmod.entity.boss.BossEntity;
import net.henrik.fireboerdmod.entity.boss.fireboerd.FireboerdEntity;
import net.henrik.fireboerdmod.entity.boss.phase.Phase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractPhase implements Phase {
    protected final FireboerdEntity fireboerd;

    protected int ticks = 0;

    protected Map<Integer, Goal> phaseGoals = new HashMap<>();

    public AbstractPhase(FireboerdEntity fireboerd) {
        this.fireboerd = fireboerd;
    }

    public void initPhaseMoveControl() {
    }

    public void initPhaseGoals() {
//        this.fireboerd.clearGoalsAndTasks();
//        this.fireboerd.initAlwaysActiveGoals();
    }

    public void loadPhaseGoals() {
        for (int priority : phaseGoals.keySet()) {
            this.fireboerd.addGoal(priority, phaseGoals.get(priority));
        }
    }

    public void unloadPhaseGoals() {
        for (Goal goal : phaseGoals.values()) {
            this.fireboerd.removeGoal(goal);
        }
    }

    @Override
    public void beginPhase() {
        this.ticks = 0;

        this.initPhaseMoveControl();
        this.initPhaseGoals();
        this.loadPhaseGoals();
    }

    @Override
    public void endPhase() {
        this.unloadPhaseGoals();
    }

    @Override
    public void clientTick() {
    }

    @Override
    public void serverTick() {
        // initiate dying phase when health is below 10%
        if (this.fireboerd.getHealth() < this.fireboerd.getMaxHealth() * 0.1) {
            this.fireboerd.getPhaseManager().setPhase(PhaseType.DYING);
        }
    }

    @Override
    @Nullable
    public Vec3d getPathTarget() {
        return null;
    }

    @Override
    public float modifyDamageTaken(DamageSource damageSource, float damage) {
        return damage;
    }
}
