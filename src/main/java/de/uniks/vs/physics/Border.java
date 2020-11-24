package de.uniks.vs.physics;

import de.uniks.vs.graphmodel.GraphGroupModel;
import de.uniks.vs.graphmodel.GraphNodeModel;

public class Border extends PhysicsItem {

	public static String MAX_X = "maxX";
	public static String MAX_Y = "maxY";
	public static String HALF = "half";
    private Line2D line;
    private Point2D root;
	private Vec2D direction;
	private long id;
	private String name;
	private double minDistance = -1;


	public Border(GraphNodeModel node) {
		super(node);
	}

	public Border(GraphGroupModel node, Point2D point2d, Vec2D direction) {
		super(node);
		this.root = point2d;
		this.direction = direction;
		this.line = new Line2D(this.root, new Point2D(this.root.getX()+direction.getY(), this.root.getY()+direction.getY()));
		this.id = node.getGraphItemModelID();
	}

	@Override
	public Point2D getPosition() {
		return new Point2D(root.getDynX(), root.getDynY());
	}
	
	private Vec2D getNormalVector(int normalDirection) {
		return new Vec2D(-direction.getY() * normalDirection, direction.getX() * normalDirection);
	}
	
	@Override
	public Vec2D getEnergyVector(Point2D position, float mass) {
        Point2D ownPos = getPosition();
        double distance = line.ptLineDist(position);
        double energy = repulsiveForce * Math.exp(-distance / 1000);
		double grav = gravity * Math.exp(-distance / 1000) * mass;
        energy = energy -grav;
		int nDirection = getNDirectionTo(position);
		Vec2D vec2d = getNormalVector(nDirection);
		vec2d = normalize(vec2d);
		vec2d = multi(vec2d, energy);

		updateDistanceBorder(distance);

        return vec2d;
    }

    private void updateDistanceBorder(double distance) {

        if (minDistance == -1 || minDistance > distance )
            minDistance = distance;
    }


    public boolean updateParameter() {
        boolean distActiv = false;

//        if (minDistance > 0 && minDistance < 40) {
//            repulsiveForce += 0.001 * (40-minDistance);
//            System.out.println(id +":  min " + minDistance + " " + repulsiveForce);
////            minDistance = -1;
//            distActiv = true;
//        }
//        else if (minDistance > 160) {
//            repulsiveForce -= 0.001 * minDistance;
//            System.out.println(id +":  max " + minDistance + " " + repulsiveForce);
//            distActiv = true;
//        }
        minDistance = -1;
        return distActiv;
    }

	private int getNDirectionTo(Point2D postion) {
		double borderSlope = Double.MAX_VALUE;
		
		if (direction.getX() != 0) {
			borderSlope = direction.getY() / direction.getX();
		}
		else if (root.getX() > postion.getX())
			borderSlope*=-1;
		double m1 = ( (postion.getY() - root.getY()) / (postion.getX() - root.getX()) );
		return borderSlope < m1 ? 1 : -1;
	}

    public String getName() {
        return this.name;
    }

    public Border withName(String name) {
        this.name = name;
        return this;
    }

    public void updateLocationX(float value) {
        root.setLocation(value, root.getY());
        line = new Line2D(this.root, new Point2D(this.root.getX()+direction.getX(), this.root.getY()+direction.getY()));
    }

    public void updateLocationY(float value) {
        root.setLocation(root.getX(), value);
        line = new Line2D(this.root, new Point2D(this.root.getX()+direction.getX(), this.root.getY()+direction.getY()));
    }
}
