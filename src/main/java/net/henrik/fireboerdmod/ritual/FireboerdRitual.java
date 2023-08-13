package net.henrik.fireboerdmod.ritual;

import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.henrik.fireboerdmod.FireboerdMod;
import net.henrik.fireboerdmod.handler.tick.RitualTickHandler;
import net.henrik.fireboerdmod.visual_effect.SmokeBallEffect;
import net.henrik.fireboerdmod.visual_effect.shape.LineShape;
import net.henrik.fireboerdmod.visual_effect.shape.RandomSphereShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

import java.util.*;
import java.util.function.Predicate;

public class FireboerdRitual extends Ritual {
    private final int RESOLUTION = 5;
    private final LineShape lineShape = new LineShape(this.RESOLUTION);
    private final RandomSphereShape smallBallShape = new RandomSphereShape(0, 1, this.RESOLUTION);


    // for tracing lines
    private final int traceLineDuration = 20;

    private final List<List<Vec3d>> linesToTrace = new LinkedList<>();
    private int traceLineStep = 1;
    private int traceLinePointsPerStep;


    public final Integer duration = 300;

    protected static final List<Identifier> allowedDimensions = List.of(
            new Identifier("overworld")
    );
    protected static final Item finishingBlockItem = Items.BLACK_STAINED_GLASS;

    private final BlockPos originBlockPos;

    protected static final Map<Vec3i, Block> layout = new HashMap<>();

    private static final List<Integer> campfireX = List.of(0, 6, 0, -6);
    private static final List<Integer> campfireZ = List.of(6, 0, -6, 0);

    private static final List<Integer> edgeBLockX = List.of(-1, 1, 1, -1);
    private static final List<Integer> edgeBLockZ = List.of(1, 1, -1, -1);

    private static final List<Vec3i> campfirePositions = new ArrayList<>();

    public FireboerdRitual(World world, BlockPos position) {
        super(world, position.toCenterPos());

        this.originBlockPos = position;

        RitualTickHandler.fireboerdRituals.add(this);
    }

    private static void initLayout() {
        for (int x = -1; x <= 1; ++x) {
            for (int z = -1; z <= 1; ++z) {
                layout.put(new Vec3i(x, -2, z), Blocks.COAL_BLOCK);

                if (!(x == 0 && z == 0)) {
                    layout.put(new Vec3i(x, -1, z), Blocks.COAL_BLOCK);
                }
            }
        }

        List<Integer> coalBlockLinesX = List.of(2, 4, 2);
        List<Integer> coalBlockLinesZ = List.of(1, 0, -1);

        for (int i = 0; i < coalBlockLinesX.size(); ++i) {
            int x = coalBlockLinesX.get(i);
            int z = coalBlockLinesZ.get(i);

            layout.put(new Vec3i(x, -2, z), Blocks.COAL_BLOCK);
            layout.put(new Vec3i(x + 1, -2, z), Blocks.COAL_BLOCK);

            layout.put(new Vec3i(-x, -2, -z), Blocks.COAL_BLOCK);
            layout.put(new Vec3i(-(x + 1), -2, -z), Blocks.COAL_BLOCK);

            layout.put(new Vec3i(z, -2, x), Blocks.COAL_BLOCK);
            layout.put(new Vec3i(z, -2, x + 1), Blocks.COAL_BLOCK);

            layout.put(new Vec3i(-z, -2, -x), Blocks.COAL_BLOCK);
            layout.put(new Vec3i(z, -2, -(x + 1)), Blocks.COAL_BLOCK);
        }

        for (int i = -1; i <= 1; ++i) {
            layout.put(new Vec3i(i, 0, 2), Blocks.POLISHED_BLACKSTONE);
            layout.put(new Vec3i(i, 0, -2), Blocks.POLISHED_BLACKSTONE);
            layout.put(new Vec3i(2, 0, i), Blocks.POLISHED_BLACKSTONE);
            layout.put(new Vec3i(-2, 0, i), Blocks.POLISHED_BLACKSTONE);
        }

        layout.put(new Vec3i(1, 0, 1), Blocks.POLISHED_BLACKSTONE_BRICKS);
        layout.put(new Vec3i(1, 0, -1), Blocks.POLISHED_BLACKSTONE_BRICKS);
        layout.put(new Vec3i(-1, 0, 1), Blocks.POLISHED_BLACKSTONE_BRICKS);
        layout.put(new Vec3i(-1, 0, -1), Blocks.POLISHED_BLACKSTONE_BRICKS);

        layout.put(new Vec3i(0, 0, 1), Blocks.GILDED_BLACKSTONE);
        layout.put(new Vec3i(0, 0, -1), Blocks.GILDED_BLACKSTONE);
        layout.put(new Vec3i(1, 0, 0), Blocks.GILDED_BLACKSTONE);
        layout.put(new Vec3i(-1, 0, 0), Blocks.GILDED_BLACKSTONE);

        for (int i = 0; i < campfireX.size(); ++i) {
            int x = campfireX.get(i);
            int z = campfireZ.get(i);

            layout.put(new Vec3i(x, -1, z), Blocks.CAMPFIRE);
            campfirePositions.add(new Vec3i(x, -1, z));
        }

        layout.put(new Vec3i(0, 0, 0), Blocks.BLACK_STAINED_GLASS);
    }

    public static void createAltar(World world, Vec3i origin) {
        initLayout();

        for (Vec3i position : layout.keySet()) {
            world.setBlockState(new BlockPos(origin.add(position)), layout.get(position).getDefaultState());
        }
    }

    public static boolean checkPossibleAltarCompletion(ItemPlacementContext context, BlockState state) {
        if (!(allowedDimensions.contains(context.getWorld().getRegistryKey().getValue()))) {
            return false;
        }

        if (!context.getStack().isOf(finishingBlockItem)) {
            return false;
        }

        initLayout();
        for (Vec3i position : layout.keySet()) {
            Vec3i positionInWorld = position.add(context.getBlockPos());
            Block block = context.getWorld().getBlockState(new BlockPos(positionInWorld)).getBlock();

            if (!(block == layout.get(position))) {
                return false;
            }
        }

        return true;
    }

    private void drawBall(Vec3d origin, ParticleEffect particleType) {
        List<Vec3d> pointsToDraw = this.smallBallShape.calculateCorrespondingPositions(origin);

        for (Vec3d point : pointsToDraw) {
            ((ServerWorld) this.world).spawnParticles(
                    particleType,
                    point.getX(), point.getY(), point.getZ(),
                    1,
                    0.0d, 0.0d, 0.0d,
                    0.0d
            );
        }
    }

    private void drawLine(Vec3d origin, Vec3d target, ParticleEffect particleType) {
        List<Vec3d> pointsToDraw = this.lineShape.calculateCorrespondingPositions(origin, target);

        for (Vec3d point : pointsToDraw) {
            ((ServerWorld) this.world).spawnParticles(
                    particleType,
                    point.getX(), point.getY(), point.getZ(),
                    1,
                    0.0d, 0.0d, 0.0d,
                    0.0d
            );
        }
    }

    private void traceLine(ParticleEffect particleType) {
        if (this.linesToTrace.size() == 0) {
            FireboerdMod.LOGGER.info("MISERABLE");
            return;
        }

        int firstPointIndex = this.traceLinePointsPerStep * this.traceLineStep;
        int numberOfPoints = firstPointIndex + this.traceLinePointsPerStep <= this.linesToTrace.get(0).size() ?
                this.traceLinePointsPerStep : this.linesToTrace.get(0).size() - firstPointIndex;

        FireboerdMod.LOGGER.info("N: " + numberOfPoints + " T: " + this.ticks);

        for (int i = 0; i < numberOfPoints; ++i) {
            int currentPointIndex = firstPointIndex + i;
            Vec3d currentPoint = this.linesToTrace.get(0).get(currentPointIndex);

            ((ServerWorld) this.world).spawnParticles(
                    particleType,
                    currentPoint.getX(), currentPoint.getY(), currentPoint.getZ(),
                    1,
                    0.0d, 0.0d, 0.0d,
                    0.0d
            );
        }

        if (this.traceLineStep >= this.traceLineDuration) {
            this.linesToTrace.remove(0);
            this.traceLineStep = 1;

            FireboerdMod.LOGGER.info("NEW LINE");
        } else {
            ++this.traceLineStep;
        }
    }

    @Override
    public void onStartTick() {
        if (this.world.isClient()) {
            return;
        }

        if (this.ticks >= this.duration) {
            RitualTickHandler.fireboerdRituals.remove(this);
        }

        if (this.ticks == 0) {
            for (Vec3i campfirePosition : campfirePositions) {
                BlockPos blockPos = this.originBlockPos.add(campfirePosition);
                BlockState blockState = world.getBlockState(blockPos);

                if (blockState.getBlock() == Blocks.CAMPFIRE && blockState.get(CampfireBlock.LIT)) {
                    world.syncWorldEvent(null, WorldEvents.FIRE_EXTINGUISHED, blockPos, 0);
                    CampfireBlock.extinguish(null, world, blockPos, blockState);

                    BlockState updatedBlockState = blockState.with(CampfireBlock.LIT, false);
                    world.setBlockState(blockPos, updatedBlockState, Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);
                    world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Emitter.of(updatedBlockState));
                }
            }
        } else if (this.ticks == 59) {
            for (int i = 0; i < campfireX.size(); ++i) {
                Vec3d origin = this.originBlockPos.add(new Vec3i(edgeBLockX.get(i), -1, edgeBLockZ.get(i))).toCenterPos();
                Vec3d campfire = this.originBlockPos.add(new Vec3i(campfireX.get(i), -1, campfireZ.get(i))).toCenterPos();

                int j = i + 1 < edgeBLockX.size() ? i + 1 : 0;
                Vec3d target = this.originBlockPos.add(new Vec3i(edgeBLockX.get(j), -1, edgeBLockZ.get(j))).toCenterPos();

                this.linesToTrace.add(this.lineShape.calculateCorrespondingPositions(origin, campfire));
                this.linesToTrace.add(this.lineShape.calculateCorrespondingPositions(campfire, target));
            }

            this.traceLinePointsPerStep = Math.round((float) this.linesToTrace.get(0).size() / this.traceLineDuration);
        } else if (60 <= this.ticks && this.ticks <= 60 + this.linesToTrace.size() * this.traceLineDuration) {
            this.traceLine(ParticleTypes.FLAME);
        }

        ++this.ticks;
    }
}
