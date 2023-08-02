package net.henrik.fireboerdmod.visual_effect.shape;

import net.minecraft.util.math.Vec3d;

import java.util.List;

interface Shape {
    // resolution means particles per 1 block / 1 block ^ 2 / 1 block ^ 3

    public Vec3d lastOrigin = Vec3d.ZERO;

    public abstract List<Vec3d> calculateCorrespondingPositions(Vec3d origin);
}
