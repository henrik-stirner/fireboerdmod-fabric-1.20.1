package net.henrik.fireboerdmod.visual_effect.shape;

import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class RingShape implements Shape {
    public Vec3d lastOrigin;
    private int resolution;

    private double radius;

    public RingShape(double radius, int resolution) {
        this.radius = radius;
        this.resolution = resolution;
    }

    @Override
    public List<Vec3d> calculateCorrespondingPositions(Vec3d origin) {
        List<Vec3d> points = new LinkedList<>();

        int totalPoints = (int) (this.resolution * (2 * Math.PI * this.radius));
        double theta = ((Math.PI*2) / totalPoints);
        for (int i = 1; i <= totalPoints; i++) {
            double angle = (theta * i);  // i -> number of the current point

            points.add(new Vec3d(
                    origin.x + (this.radius * Math.cos(angle)),  // x
                    origin.y,                                       // y
                    origin.z + (this.radius * Math.sin(angle))      // z
            ));
        }

        this.lastOrigin = origin;
        return points;
    }

    public int getResolution() {
        return this.resolution;
    }

    public void setResolution(int newResolution) {
        this.resolution = newResolution;
    }

    public double getRadius() {
        return this.radius;
    }

    public void setRadius(double newRadius) {
        this.radius = newRadius;
    }
}
