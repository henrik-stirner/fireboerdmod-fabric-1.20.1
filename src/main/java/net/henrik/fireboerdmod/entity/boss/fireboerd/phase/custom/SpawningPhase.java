/*
 * Decompiled with CFR 0.2.1 (FabricMC 53fa44c9).
 */
package net.henrik.fireboerdmod.entity.boss.fireboerd.phase.custom;

import net.henrik.fireboerdmod.entity.boss.fireboerd.FireboerdEntity;
import net.henrik.fireboerdmod.entity.boss.fireboerd.phase.AbstractPhase;
import net.henrik.fireboerdmod.entity.boss.fireboerd.phase.PhaseType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class SpawningPhase
extends AbstractPhase {
    @Nullable
    private Vec3d target;
    private int ticks;

    public SpawningPhase(FireboerdEntity fireboerdEntity) {
        super(fireboerdEntity);
    }

    @Override
    public void clientTick() {
        super.clientTick();
    }

    @Override
    public void serverTick() {
        ++this.ticks;
    }

    @Override
    public void beginPhase() {
        this.target = null;
        this.ticks = 0;
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

