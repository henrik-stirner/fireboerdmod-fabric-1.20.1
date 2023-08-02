package net.henrik.fireboerdmod.visual_effect.shape;

import net.minecraft.util.math.Vec3d;

import java.util.LinkedList;
import java.util.List;

public class LineShape {
    public Vec3d lastStartPoint;
    public Vec3d lastEndPoint;
    private int resolution;

    public LineShape(int resolution) {
        this.resolution = resolution;
    }

    public List<Vec3d> calculateCorrespondingPositions(Vec3d startPoint, Vec3d endPoint) {
        List<Vec3d> points = new LinkedList<>();

        double length = startPoint.distanceTo(endPoint);
        int totalPoints = (int) (this.resolution * length);

        Vec3d difference = endPoint.subtract(startPoint);
        Vec3d pointSpacing = difference.multiply((float) 1 / totalPoints);  // divide by totalPoints

        for (int i = 1; i <= totalPoints; i++) {
            points.add(startPoint.add(pointSpacing.multiply(i)));
        }

        this.lastStartPoint = startPoint;
        this.lastEndPoint = endPoint;
        return points;
    }

    public int getResolution() {
        return this.resolution;
    }

    public void setResolution(int newResolution) {
        this.resolution = newResolution;
    }
}
