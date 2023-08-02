package net.henrik.fireboerdmod.visual_effect;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class VisualEffect {
    @Nullable
    protected final World world;
    @Nullable
    protected final Vec3d position;

    public int duration;

    public int ticks = 0;

    public VisualEffect(@Nullable World world, @Nullable Vec3d position, int duration) {
        this.world = world;
        this.position = position;
        this.duration = duration;
    }

    public void onStartTick() {

    }

    public void onEndTick() {

    }

    public abstract Object create(World world, Vec3d position);
}
