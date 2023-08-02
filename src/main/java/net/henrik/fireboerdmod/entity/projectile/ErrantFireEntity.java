package net.henrik.fireboerdmod.entity.projectile;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import net.henrik.fireboerdmod.entity.ModEntityTypes;
import net.minecraft.datafixer.fix.ChunkPalettedStorageFix;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;


public class ErrantFireEntity extends ProjectileEntity {
    @Nullable
    private Entity target;
    @Nullable
    private UUID targetUuid;
    private double targetX;
    private double targetY;
    private double targetZ;

    public boolean allowSwitchTarget = false;
    public int playerRadarRange = 10;

    @Nullable
    private Direction direction;
    private int stepCount;
    private final int stepCountBase = 1;
    private final int stepCountMaxRandomFactor = 10;

    public ErrantFireEntity(EntityType<? extends ErrantFireEntity> entityType, World world) {
        super(entityType, world);
    }

    public ErrantFireEntity(World world, LivingEntity owner, @Nullable Entity target, Direction.Axis axis) {
        this(ModEntityTypes.ERRANT_FIRE, world);
        this.setOwner(owner);
        BlockPos blockPos = owner.getBlockPos();
        double d = (double)blockPos.getX() + 0.5;
        double e = (double)blockPos.getY() + 0.5;
        double f = (double)blockPos.getZ() + 0.5;
        this.refreshPositionAndAngles(d, e, f, this.getYaw(), this.getPitch());
        this.target = target;
        this.direction = Direction.UP;
        this.changeTargetDirection(axis);
    }

    public ErrantFireEntity(
            World world, LivingEntity owner, @Nullable Entity target, boolean updateTarget, Direction.Axis axis
    ) {
        this(ModEntityTypes.ERRANT_FIRE, world);
        this.setOwner(owner);
        BlockPos blockPos = owner.getBlockPos();
        double d = (double)blockPos.getX() + 0.5;
        double e = (double)blockPos.getY() + 0.5;
        double f = (double)blockPos.getZ() + 0.5;
        this.refreshPositionAndAngles(d, e, f, this.getYaw(), this.getPitch());
        this.target = target;
        this.allowSwitchTarget = updateTarget;
        this.direction = Direction.UP;
        this.changeTargetDirection(axis);
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.target != null) {
            nbt.putUuid("Target", this.target.getUuid());
        }
        if (this.direction != null) {
            nbt.putInt("Dir", this.direction.getId());
        }
        nbt.putInt("Steps", this.stepCount);
        nbt.putDouble("TXD", this.targetX);
        nbt.putDouble("TYD", this.targetY);
        nbt.putDouble("TZD", this.targetZ);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.stepCount = nbt.getInt("Steps");
        this.targetX = nbt.getDouble("TXD");
        this.targetY = nbt.getDouble("TYD");
        this.targetZ = nbt.getDouble("TZD");
        if (nbt.contains("Dir", NbtElement.NUMBER_TYPE)) {
            this.direction = Direction.byId(nbt.getInt("Dir"));
        }
        if (nbt.containsUuid("Target")) {
            this.targetUuid = nbt.getUuid("Target");
        }
    }

    @Override
    protected void initDataTracker() {
    }

    @Nullable
    private Direction getDirection() {
        return this.direction;
    }

    private void setDirection(@Nullable Direction direction) {
        this.direction = direction;
    }

    private void changeTargetDirection(@Nullable Direction.Axis axis) {
        BlockPos blockPos;
        double d = 0.5;
        if (this.target == null) {
            blockPos = this.getBlockPos().down();
        } else {
            d = (double)this.target.getHeight() * 0.5;
            blockPos = BlockPos.ofFloored(this.target.getX(), this.target.getY() + d, this.target.getZ());
        }
        double e = (double)blockPos.getX() + 0.5;
        double f = (double)blockPos.getY() + d;
        double g = (double)blockPos.getZ() + 0.5;
        Direction direction = null;
        if (!blockPos.isWithinDistance(this.getPos(), 2.0)) {
            BlockPos blockPos2 = this.getBlockPos();
            ArrayList<Direction> list = Lists.newArrayList();
            if (axis != Direction.Axis.X) {
                if (blockPos2.getX() < blockPos.getX() && this.getWorld().isAir(blockPos2.east())) {
                    list.add(Direction.EAST);
                } else if (blockPos2.getX() > blockPos.getX() && this.getWorld().isAir(blockPos2.west())) {
                    list.add(Direction.WEST);
                }
            }
            if (axis != Direction.Axis.Y) {
                if (blockPos2.getY() < blockPos.getY() && this.getWorld().isAir(blockPos2.up())) {
                    list.add(Direction.UP);
                } else if (blockPos2.getY() > blockPos.getY() && this.getWorld().isAir(blockPos2.down())) {
                    list.add(Direction.DOWN);
                }
            }
            if (axis != Direction.Axis.Z) {
                if (blockPos2.getZ() < blockPos.getZ() && this.getWorld().isAir(blockPos2.south())) {
                    list.add(Direction.SOUTH);
                } else if (blockPos2.getZ() > blockPos.getZ() && this.getWorld().isAir(blockPos2.north())) {
                    list.add(Direction.NORTH);
                }
            }
            direction = Direction.random(this.random);
            if (list.isEmpty()) {
                for (int i = 5; !this.getWorld().isAir(blockPos2.offset(direction)) && i > 0; --i) {
                    direction = Direction.random(this.random);
                }
            } else {
                direction = (Direction)list.get(this.random.nextInt(list.size()));
            }
            e = this.getX() + (double)direction.getOffsetX();
            f = this.getY() + (double)direction.getOffsetY();
            g = this.getZ() + (double)direction.getOffsetZ();
        }
        this.setDirection(direction);
        double h = e - this.getX();
        double j = f - this.getY();
        double k = g - this.getZ();
        double l = Math.sqrt(h * h + j * j + k * k);
        if (l == 0.0) {
            this.targetX = 0.0;
            this.targetY = 0.0;
            this.targetZ = 0.0;
        } else {
            this.targetX = h / l * 0.15;
            this.targetY = j / l * 0.15;
            this.targetZ = k / l * 0.15;
        }
        this.velocityDirty = true;
        this.stepCount = this.stepCountBase + this.random.nextInt(this.stepCountMaxRandomFactor) * this.stepCountBase;
    }

    @Override
    public void checkDespawn() {
        if (this.getWorld().getDifficulty() == Difficulty.PEACEFUL) {
            this.discard();
        }
    }

    public void updateTarget() {
        Entity newTarget = this.getWorld().getClosestPlayer(
                this.getX(), this.getY(), this.getZ(),
                playerRadarRange, false
        );

        if (newTarget == null) {
            return;
        }

        this.target = newTarget;
        this.targetUuid = newTarget.getUuid();
    }

    @Override
    public void tick() {
        if (allowSwitchTarget) {
            updateTarget();
        }

        Vec3d vec3d;
        super.tick();
        if (!this.getWorld().isClient) {
            if (this.target == null && this.targetUuid != null) {
                this.target = ((ServerWorld)this.getWorld()).getEntity(this.targetUuid);
                if (this.target == null) {
                    this.targetUuid = null;
                }
            }
            if (!(this.target == null || !this.target.isAlive() || this.target instanceof PlayerEntity && this.target.isSpectator())) {
                this.targetX = MathHelper.clamp(this.targetX * 1.025, -1.0, 1.0);
                this.targetY = MathHelper.clamp(this.targetY * 1.025, -1.0, 1.0);
                this.targetZ = MathHelper.clamp(this.targetZ * 1.025, -1.0, 1.0);
                vec3d = this.getVelocity();
                this.setVelocity(vec3d.add((this.targetX - vec3d.x) * 0.2, (this.targetY - vec3d.y) * 0.2, (this.targetZ - vec3d.z) * 0.2));
            } else if (!this.hasNoGravity()) {
                this.setVelocity(this.getVelocity().add(0.0, -0.04, 0.0));
            }

            HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
            if (hitResult.getType() != HitResult.Type.MISS) {
                this.onCollision(hitResult);
            }
        }

        this.checkBlockCollision();
        vec3d = this.getVelocity();
        this.setPosition(this.getX() + vec3d.x, this.getY() + vec3d.y, this.getZ() + vec3d.z);
        ProjectileUtil.setRotationFromVelocity(this, 0.5f);

        if (this.getWorld().isClient) {
            this.getWorld().addParticle(ParticleTypes.SMOKE, this.getX() - vec3d.x, this.getY() - vec3d.y + 0.15, this.getZ() - vec3d.z, 0.0, 0.0, 0.0);
        } else if (this.target != null && !this.target.isRemoved()) {
            if (this.stepCount > 0) {
                --this.stepCount;
                if (this.stepCount == 0) {
                    this.changeTargetDirection(this.direction == null ? null : this.direction.getAxis());
                }
            }
            if (this.direction != null) {
                BlockPos blockPos = this.getBlockPos();
                Direction.Axis axis = this.direction.getAxis();
                if (this.getWorld().isTopSolid(blockPos.offset(this.direction), this)) {
                    this.changeTargetDirection(axis);
                } else {
                    BlockPos blockPos2 = this.target.getBlockPos();
                    if (axis == Direction.Axis.X && blockPos.getX() == blockPos2.getX() || axis == Direction.Axis.Z && blockPos.getZ() == blockPos2.getZ() || axis == Direction.Axis.Y && blockPos.getY() == blockPos2.getY()) {
                        this.changeTargetDirection(axis);
                    }
                }
            }
        }
    }

    @Override
    protected boolean canHit(Entity entity) {
        return super.canHit(entity) && !entity.noClip;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean shouldRender(double distance) {
        return distance < 16384.0;
    }

    @Override
    public float getBrightnessAtEyes() {
        return 1.0f;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        Entity owner = this.getOwner();

        LivingEntity livingEntity = owner instanceof LivingEntity ? (LivingEntity)owner : null;

        boolean damaged = entity.damage(this.getDamageSources().mobProjectile(this, livingEntity), 4.0f);
        if (damaged) {
            this.applyDamageEffects(livingEntity, entity);
        }
    }

    protected void spawnCollisionParticles() {
        ((ServerWorld)this.getWorld()).spawnParticles(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 15, 0.2, 0.2, 0.2, 0.0);
        ((ServerWorld)this.getWorld()).spawnParticles(ParticleTypes.LAVA, this.getX(), this.getY(), this.getZ(), 5, 0.2, 0.2, 0.2, 0.0);
        ((ServerWorld)this.getWorld()).spawnParticles(ParticleTypes.DRIPPING_LAVA, this.getX(), this.getY(), this.getZ(), 10, 0.2, 0.2, 0.2, 0.0);
    }

    private void destroy() {
        this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 0.5f, 1.0f);
        this.spawnCollisionParticles();

        this.discard();
        this.getWorld().emitGameEvent(GameEvent.ENTITY_DAMAGE, this.getPos(), GameEvent.Emitter.of(this));
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        this.destroy();
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        this.destroy();
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (!this.getWorld().isClient) {
            this.destroy();
        }
        return true;
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        double d = packet.getVelocityX();
        double e = packet.getVelocityY();
        double f = packet.getVelocityZ();
        this.setVelocity(d, e, f);
    }
}