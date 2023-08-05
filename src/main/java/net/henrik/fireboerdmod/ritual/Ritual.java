package net.henrik.fireboerdmod.ritual;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class Ritual {
    protected final World world;
    protected final Vec3d position;

    public int ticks = 0;

    public final Integer duration = null;

    protected final Block finishingBlock = null;

    protected final Map<Vec3i, Block> layout = null;

    public Ritual(World world, Vec3d position) {
        this.world = world;
        this.position = position;
    }

    public static boolean checkPossibleAltarCompletion(ItemPlacementContext context, BlockState state) {
        return false;
    }

    public void onStartTick() {

    }

    public void onEndTick() {

    }
}
