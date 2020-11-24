package de.uniks.vs.physics;

public class Vec2D {
    private double x;
    private double y;

    public Vec2D(double x, double y) {

        this.x = x;
        this.y = y;
    }

    public Vec2D() {}

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
