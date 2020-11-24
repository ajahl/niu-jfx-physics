package de.uniks.vs.physics;

import de.uniks.vs.graphmodel.GraphItemModel;

public class PhysicsItem {

	protected GraphItemModel node;
	protected float repulsiveForce;
	protected float gravity;
	protected float mass;
	protected boolean active;
	private Vec2D vector;

	protected int stepDistance = 100;

	public void setStepDistance(int stepDistance) {
		this.stepDistance = stepDistance;
	}
	
	public int getStepDistance() {
		return stepDistance;
	}

	public PhysicsItem(GraphItemModel node) {
		this.node = node;
	}

	public GraphItemModel getNode() {
		return node;
	}
	
	public Vec2D getVector() {
		return vector;
	}
	
	public PhysicsItem withRepulsion(float power) {
		setRepulsiveForce(power);
		return this;
	}

	public void setRepulsiveForce(float repulsiveForce) {
		this.repulsiveForce = repulsiveForce;
	}

	public float getMass() {
		return mass;
	}

	public PhysicsItem withMass(float mass) {
		setMass(mass);
		return this;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}

	public boolean isActiv() {
		return active;
	}

	public PhysicsItem withActive(boolean value) {
		active = value;
		return this;
	}

	public Point2D getPosition() {
		GraphItemModel rec = (GraphItemModel) node;
		return new Point2D(rec.getX(), rec.getY());
	}

	public float getRepulsiveForce() {
		return repulsiveForce;
	}

	public Vec2D getEnergyVector(Point2D postion, float mass) {
		Point2D ownPos = getPosition();
		double distance = ownPos.distance(postion);
		double energy = repulsiveForce * Math.exp(-distance / stepDistance);
		Vec2D vec2d = new Vec2D(postion.getX() - ownPos.getX(), postion.getY() - ownPos.getY());
		vec2d = normalize(vec2d);
		vec2d = multi(vec2d, energy);

		return vec2d;
	}

	protected Vec2D multi(Vec2D vec2d, double energy) {
		vec2d.set(vec2d.getX()*energy, vec2d.getY()*energy);
		return vec2d;
	}

	protected Vec2D normalize(Vec2D vec2d) {
		double length = vectorLength(vec2d);
		if (length > 0)
			vec2d.set(vec2d.getX()/length, vec2d.getY()/length);
		
		return vec2d;
	}

	private double vectorLength(Vec2D vec2d) {
		double length = Math.sqrt((vec2d.getX())*(vec2d.getX()) + (vec2d.getY())*(vec2d.getY()));
		return length;
	}

	public void setDirectionVector(Vec2D vector) {
		this.vector = vector;
	}

	public PhysicsItem withGravity(float gravity) {
		setGravity(gravity);
		return this;
	}

	public void setGravity(float gravity) {
		this.gravity = gravity;
	}
}
