/*
 * Decompiled with CFR 0.2.1 (FabricMC 53fa44c9).
 */
package net.henrik.fireboerdmod.entity.boss.fireboerd.phase.custom;

import net.henrik.fireboerdmod.entity.boss.fireboerd.FireboerdEntity;
import net.henrik.fireboerdmod.entity.boss.fireboerd.phase.AbstractPhase;
import net.henrik.fireboerdmod.entity.boss.fireboerd.phase.PhaseType;
import net.henrik.fireboerdmod.entity.boss.phase.Phase;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import org.jetbrains.annotations.Nullable;

public class DyingPhase
extends AbstractPhase {
    @Nullable
    private Vec3d target;
    private int ticks;

    public DyingPhase(FireboerdEntity fireboerdEntity) {
        super(fireboerdEntity);
    }

    @Override
    public void clientTick() {
        if (this.ticks++ % 10 == 0) {
            float f = (this.fireboerd.getRandom().nextFloat() - 0.5f) * 8.0f;
            float g = (this.fireboerd.getRandom().nextFloat() - 0.5f) * 4.0f;
            float h = (this.fireboerd.getRandom().nextFloat() - 0.5f) * 8.0f;
            this.fireboerd.getWorld().addParticle(ParticleTypes.EXPLOSION_EMITTER, this.fireboerd.getX() + (double)f, this.fireboerd.getY() + 2.0 + (double)g, this.fireboerd.getZ() + (double)h, 0.0, 0.0, 0.0);
        }
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

    public PhaseType<DyingPhase> getType() {
        return PhaseType.DYING;
    }
}

