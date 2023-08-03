package net.henrik.fireboerdmod.entity.boss.fireboerd;

import net.henrik.fireboerdmod.entity.boss.BossEntity;
import net.henrik.fireboerdmod.entity.boss.fireboerd.control.FlightAndDriveMoveControl;
import net.henrik.fireboerdmod.entity.boss.fireboerd.phase.PhaseManager;
import net.henrik.fireboerdmod.entity.boss.fireboerd.phase.PhaseType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Random;

public class FireboerdEntity extends BossEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    // boss-specific
    private static final BossBar.Color BOSS_BAR_COLOR = BossBar.Color.RED;
    private static final double MAX_HEALTH = 900.0D;

    private static final int EXPERIENCE_POINTS = 100;

    // move control
    protected FlightAndDriveMoveControl moveControl;

    // phases
    public static final TrackedData<Integer> PHASE_TYPE = DataTracker.registerData(FireboerdEntity.class,
            TrackedDataHandlerRegistry.INTEGER);
    private final PhaseManager phaseManager;

    private static final String PHASE_KEY = "FireboerdPhase";

    public FireboerdEntity(EntityType<? extends FireboerdEntity> entityType, World world) {
        super(entityType, world, BOSS_BAR_COLOR);
        this.experiencePoints = EXPERIENCE_POINTS;

        this.moveControl = new FlightAndDriveMoveControl(this, 10, true, true);
        this.phaseManager = new PhaseManager(this);

        this.setPathfindingPenalty(PathNodeType.WATER, -1.0f);
        this.setPathfindingPenalty(PathNodeType.LAVA, 8.0f);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, 0.0f);
        this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, 0.0f);
    }

    // random
    public Random random = new Random();

    // ====================================================================================================
    // defaults
    // ====================================================================================================

    public static DefaultAttributeContainer.Builder setAttributes() {
        return HostileEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, MAX_HEALTH)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 16.0f)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 1.5f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6f)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.9f);
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }

    @Override
    public boolean isFireImmune() { return true; }

    @Override
    public boolean hurtByWater() {
        return true;
    }

    // ====================================================================================================
    // move control
    // ====================================================================================================

//    public void setMoveControl(MoveControl newMoveControl) {
//        this.moveControl = newMoveControl;
//    }

    public void setOnFlightMode() {
        this.moveControl.setOnFlightMode(true);
    }

    public void setOnDriveMode() {
        this.moveControl.setOnFlightMode(false);
    }

    // ====================================================================================================
    // goals
    // ====================================================================================================

    public void addGoal(int priority, Goal goal) {
        this.goalSelector.add(priority, goal);
    }

    public void removeGoal(Goal goal) {
        this.goalSelector.remove(goal);
    }

    public void addTargetGoal(int priority, Goal goal) {
        this.targetSelector.add(priority, goal);
    }

    public void removeTargetGoal(Goal goal) {
        this.targetSelector.remove(goal);
    }

    public void initAlwaysActiveGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    protected void initGoals() {
        this.initAlwaysActiveGoals();
    }

    /**
        this.goalSelector.add(1, new SwimGoal(this));

        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.2D, false));
        // ChargeTargetGoal

        // ProjectileAttackGoal
        // ShootBulletGoal
        // ShootFireballGoal (Ghast / Blaze)

        // LookAtTargetGoal

        // FlyGoal
        // FlyRandomlyGoal
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.75f, 1));

        // SitGoal
        this.goalSelector.add(4, new LookAroundGoal(this));
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
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world);
        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(true);
        birdNavigation.setCanEnterOpenDoors(true);
        return birdNavigation;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();

        if (this.getWorld().isClient()) {
            this.phaseManager.getCurrentPhase().clientTick();
        } else {
            this.phaseManager.getCurrentPhase().serverTick();
        }
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

    // ====================================================================================================
    // defaults
    // ====================================================================================================

    @Override
    public boolean canTarget(LivingEntity target) {
        return target.canTakeDamage();
    }

    @Override
    protected boolean canStartRiding(Entity entity) {
        return false;
    }

    @Override
    public boolean canUsePortals() {
        return false;
    }

    // ====================================================================================================
    // utility
    // ====================================================================================================

    @Nullable
    public Vec3d getPointAroundPlayer() {
        if (this.getTarget() == null) {
            return null;
        }

        double offset = this.random.nextDouble() * (4 - 2) + 2;
        double theta = this.random.nextDouble() * 359;
        double phi = this.random.nextDouble() * 359;

        double x = offset * Math.sin(theta) * Math.cos(phi);
        double y = offset * Math.sin(theta) * Math.sin(phi);
        double z = offset * Math.cos(theta);

        return this.getTarget().getPos().add(x, y, z);
    }
}