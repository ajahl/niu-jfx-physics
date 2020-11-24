package de.uniks.vs.physics;

//import de.uniks.vs.ui.GraphCursor;

import de.uniks.vs.graphmodel.GraphCursorModel;
import de.uniks.vs.graphmodel.GraphItemModel;

public class Cursor extends PhysicsItem {

	private Point2D position;

	public Cursor(GraphItemModel node) {
		super(node);
		GraphCursorModel cursor = (GraphCursorModel) node;
		position = new Point2D(cursor.getX(), cursor.getY());
	}

	public Cursor(GraphItemModel node, Point2D position) {
		super(node);
		this.position = position;
	}

	public void setPosition(Point2D position) {
		this.position = position;
	}
	
	public void setPosition(double x, double y) {
		this.position = new Point2D(x, y);
	}	
	
	@Override
	public Point2D getPosition() {
		return new Point2D(position.getDynX(), position.getDynY());
	}
	
//	@Override
//	public Vec2D getEnergyVector(Vec2D postion) {
//		Vec2D ownPos = getPosition();
//		double distance = ownPos.distance(postion);
//		double energy = repulsiveForce * Math.exp(-distance / stepDistance);
//		Vec2D vec2d = new Vec2D(postion.x - ownPos.x, postion.y - ownPos.y);
//		vec2d = normalize(vec2d);
//		vec2d = multi(vec2d, energy);
//
//		return vec2d;
//	}
}
