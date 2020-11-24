package de.uniks.vs.physics;

public class Line2D {

    private final double x1;
    private final double y1;
    private final double x2;
    private final double y2;

    public Line2D(Point2D source, Point2D target) {
        this.x1 = source.getX();
        this.y1 = source.getY();
        this.x2 = target.getX();
        this.y2 = target.getY();
    }

    public double ptLineDist(Point2D pt) {
        return ptLineDist(x1, y1, x2, y2, pt.getX(), pt.getY());
    }

    private double ptLineDist(double x1, double y1, double x2, double y2, double px, double py) {
        return (float) Math.sqrt(ptLineDistSq(x1, y1, x2, y2, px, py));
    }

    private double ptLineDistSq(double x1, double y1, double x2, double y2, double px, double py) {
        // Adjust vectors relative to x1,y1
        // x2,y2 becomes relative vector from x1,y1 to end of segment
        x2 -= x1;
        y2 -= y1;
        // px,py becomes relative vector from x1,y1 to test point
        px -= x1;
        py -= y1;
        double dotprod = px * x2 + py * y2;
        // dotprod is the length of the px,py vector
        // projected on the x1,y1=>x2,y2 vector times the
        // length of the x1,y1=>x2,y2 vector
        double projlenSq = dotprod * dotprod / (x2 * x2 + y2 * y2);
        // Distance to line is now the length of the relative point
        // vector minus the length of its projection onto the line
        double lenSq = px * px + py * py - projlenSq;

        if (lenSq < 0f) {
            lenSq = 0f;
        }
        return lenSq;
    }
}
