/*
 * Decompiled with CFR 0.2.1 (FabricMC 53fa44c9).
 */
package net.henrik.fireboerdmod.entity.boss.fireboerd.phase;

import net.henrik.fireboerdmod.entity.boss.BossEntity;
import net.henrik.fireboerdmod.entity.boss.fireboerd.FireboerdEntity;
import net.henrik.fireboerdmod.entity.boss.phase.Phase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPhase implements Phase {
    protected final FireboerdEntity fireboerd;

    public AbstractPhase(FireboerdEntity fireboerd) {
        this.fireboerd = fireboerd;
    }

    @Override
    public void clientTick() {
    }

    @Override
    public void serverTick() {
    }

    @Override
    public void beginPhase() {
    }

    @Override
    public void endPhase() {
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
