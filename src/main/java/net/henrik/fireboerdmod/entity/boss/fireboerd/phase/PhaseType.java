/*
 * Decompiled with CFR 0.2.1 (FabricMC 53fa44c9).
 */
package net.henrik.fireboerdmod.entity.boss.fireboerd.phase;


import net.henrik.fireboerdmod.entity.boss.fireboerd.FireboerdEntity;
import net.henrik.fireboerdmod.entity.boss.fireboerd.phase.custom.*;
import net.henrik.fireboerdmod.entity.boss.phase.Phase;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class PhaseType<T extends Phase> {
    private static PhaseType<?>[] types = new PhaseType[0];
    public static final PhaseType<SpawningPhase> SPAWNING = PhaseType.register(SpawningPhase.class, "Spawning");
    public static final PhaseType<AerialPhase> AERIAL = PhaseType.register(AerialPhase.class, "Aerial");
    public static final PhaseType<ChargeAttackPhase> CHARGE_ATTACK = PhaseType.register(ChargeAttackPhase.class, "ChargeAttack");
    public static final PhaseType<TerrestrialPhase> TERRESTRIAL = PhaseType.register(TerrestrialPhase.class, "Terrestrial");
    public static final PhaseType<MeleeAttackPhase> MELEE_ATTACK = PhaseType.register(MeleeAttackPhase.class, "MeleeAttack");
    public static final PhaseType<DyingPhase> DYING = PhaseType.register(DyingPhase.class, "Dying");
    private final Class<? extends Phase> phaseClass;
    private final int id;
    private final String name;

    private PhaseType(int id, Class<? extends Phase> phaseClass, String name) {
        this.id = id;
        this.phaseClass = phaseClass;
        this.name = name;
    }

    public Phase create(FireboerdEntity fireboerd) {
        try {
            Constructor<? extends Phase> constructor = this.getConstructor();
            return constructor.newInstance(fireboerd);
        } catch (Exception exception) {
            throw new Error(exception);
        }
    }

    protected Constructor<? extends Phase> getConstructor() throws NoSuchMethodException {
        return this.phaseClass.getConstructor(FireboerdEntity.class);
    }

    public int getTypeId() {
        return this.id;
    }

    public String toString() {
        return this.name + " (#" + this.id + ")";
    }

    public static PhaseType<?> getFromId(int id) {
        return types[id];
    }

    public static int count() {
        return types.length;
    }

    private static <T extends Phase> PhaseType<T> register(Class<T> phaseClass, String name) {
        PhaseType<T> phaseType = new PhaseType<T>(types.length, phaseClass, name);
        types = Arrays.copyOf(types, types.length + 1);
        PhaseType.types[phaseType.getTypeId()] = phaseType;
        return phaseType;
    }
}
