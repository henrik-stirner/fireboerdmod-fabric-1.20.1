package net.henrik.fireboerdmod.entity.boss.fireboerd;

import net.henrik.fireboerdmod.entity.boss.BossEntity;
import net.henrik.fireboerdmod.entity.boss.fireboerd.phase.PhaseManager;
import net.henrik.fireboerdmod.entity.boss.fireboerd.phase.PhaseType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

public class FireboerdEntity extends BossEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    // boss-specific
    private static final BossBar.Color BOSS_BAR_COLOR = BossBar.Color.RED;
    private static final double MAX_HEALTH = 900.0D;

    // phases
    public static final TrackedData<Integer> PHASE_TYPE = DataTracker.registerData(FireboerdEntity.class,
            TrackedDataHandlerRegistry.INTEGER);
    private final PhaseManager phaseManager;

    private static final String PHASE_KEY = "FireboerdPhase";

    public FireboerdEntity(EntityType<? extends FireboerdEntity> entityType, World world) {
        super(entityType, world, BOSS_BAR_COLOR);

        this.phaseManager = new PhaseManager(this);
    }

    // ====================================================================================================
    // defaults
    // ====================================================================================================

    public static DefaultAttributeContainer.Builder setAttributes() {
        return HostileEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, MAX_HEALTH)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1.5f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6f)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.9f);
    }

    /**
    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));

        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.2D, false));
        // ChargeTargetGoal

        // FireBeamGoal
        // ShootBulletGoal
        // ShootFireballGoal (Ghast / Blaze)

        // LookAtTargetGoal

        // FlyGoal
        // FlyRandomlyGoal
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.75f, 1));

        // SitGoal
        this.goalSelector.add(4, new LookAroundGoal(this));

        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }
    */

    // ====================================================================================================
    // animations
    // ====================================================================================================

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        if(tAnimationState.isMoving()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.fireboerd.walk", Animation.LoopType.LOOP));
        }

        tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.fireboerd.idle", Animation.LoopType.LOOP));

        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    // ====================================================================================================
    // other
    // ====================================================================================================

    @Override
    public void tickMovement() {
        super.tickMovement();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        // phases
        this.getDataTracker().startTracking(PHASE_TYPE, PhaseType.SPAWNING.getTypeId());
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt(PHASE_KEY, this.phaseManager.getCurrentPhase().getType().getTypeId());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains(PHASE_KEY)) {
            this.phaseManager.setPhase(PhaseType.getFromId(nbt.getInt(PHASE_KEY)));
        }
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (PHASE_TYPE.equals(data) && this.getWorld().isClient) {
            this.phaseManager.setPhase(PhaseType.getFromId(this.getDataTracker().get(PHASE_TYPE)));
        }
        super.onTrackedDataSet(data);
    }

    public PhaseManager getPhaseManager() {
        return this.phaseManager;
    }
}
