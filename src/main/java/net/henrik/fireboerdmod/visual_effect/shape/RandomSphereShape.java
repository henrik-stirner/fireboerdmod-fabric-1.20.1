package net.henrik.fireboerdmod.visual_effect.shape;

import net.minecraft.util.math.Vec3d;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class RandomSphereShape implements Shape {
    public Vec3d lastOrigin;
    private double resolution;

    private double minRadius;
    private double maxRadius;

    private final double volume;
    private final int iterations;

    private final Random random = new Random();

    public RandomSphereShape(double minRadius, double maxRadius, double resolution) {
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
        this.resolution = resolution;

        this.volume = (double) (4 / 3) * Math.PI * Math.pow(maxRadius, 3) -
                (double) (4 / 3) * Math.PI * Math.pow(minRadius, 3);
        this.iterations = (int) (this.volume * this.resolution);
    }

    @Override
    public List<Vec3d> calculateCorrespondingPositions(Vec3d origin) {
        List<Vec3d> points = new LinkedList<>();

        for (int i = 0; i < iterations; ++i) {
            // offset is the used radius and has a random length between minRadius and maxRadius
            double offset = this.random.nextDouble() * (this.maxRadius - this.minRadius) + this.minRadius;
            double theta = random.nextDouble() * 359;
            double phi = random.nextDouble() * 359;

            double x = offset * Math.sin(theta) * Math.cos(phi);
            double y = offset * Math.sin(theta) * Math.sin(phi);
            double z = offset * Math.cos(theta);

            points.add(origin.add(x, y, z));
        }

        this.lastOrigin = origin;
        return points;
    }

    public double getResolution() {
        return this.resolution;
    }

    public void setResolution(double newResolution) {
        this.resolution = newResolution;
    }

    public double getMinRadius() {
        return this.minRadius;
    }

    public void setMinRadius(double newMinRadius) {
        this.minRadius = newMinRadius;
    }

    public double getMaxRadius() {
        return this.maxRadius;
    }

    public void setMaxRadius(double newMaxRadius) {
        this.maxRadius = newMaxRadius;
    }
}