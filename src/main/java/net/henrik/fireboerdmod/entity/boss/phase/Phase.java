/*
 * Decompiled with CFR 0.2.1 (FabricMC 53fa44c9).
 */
package net.henrik.fireboerdmod.entity.boss.phase;

import net.henrik.fireboerdmod.entity.boss.fireboerd.phase.PhaseType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public interface Phase {
    public void clientTick();

    public void serverTick();

    public void beginPhase();

    public void endPhase();

    public PhaseType<? extends Phase> getType();

    @Nullable
    public Vec3d getPathTarget();

    public float modifyDamageTaken(DamageSource var1, float var2);
}
