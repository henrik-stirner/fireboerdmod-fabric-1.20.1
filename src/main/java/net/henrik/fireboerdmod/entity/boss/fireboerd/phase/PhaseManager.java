/*
 * Decompiled with CFR 0.2.1 (FabricMC 53fa44c9).
 */
package net.henrik.fireboerdmod.entity.boss.fireboerd.phase;

import net.henrik.fireboerdmod.FireboerdMod;
import net.henrik.fireboerdmod.entity.boss.fireboerd.FireboerdEntity;
import net.henrik.fireboerdmod.entity.boss.phase.Phase;
import org.jetbrains.annotations.Nullable;

public class PhaseManager {
    private final FireboerdEntity fireboerd;
    private final Phase[] phases = new Phase[PhaseType.count()];
    @Nullable
    private Phase currentPhase;

    public PhaseManager(FireboerdEntity fireboerd) {
        this.fireboerd = fireboerd;
        this.setPhase(PhaseType.SPAWNING);
    }

    public void setPhase(PhaseType<?> type) {
        if (this.currentPhase != null && type == this.currentPhase.getType()) {
            return;
        }

        if (this.currentPhase != null) {
            this.currentPhase.endPhase();
        }
        this.currentPhase = this.create(type);
        if (!this.fireboerd.getWorld().isClient) {
            this.fireboerd.getDataTracker().set(FireboerdEntity.PHASE_TYPE, type.getTypeId());
        }
        this.currentPhase.beginPhase();

        FireboerdMod.LOGGER.info("Beginning new phase of type " + type + " for entity " + this.fireboerd);
    }

    public @Nullable Phase getCurrentPhase() {
        return this.currentPhase;
    }

    public <T extends Phase> T create(PhaseType<T> type) {
        int i = type.getTypeId();
        if (this.phases[i] == null) {
            this.phases[i] = type.create(this.fireboerd);
        }
        return (T)this.phases[i];
    }
}
