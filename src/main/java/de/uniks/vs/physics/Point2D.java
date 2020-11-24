package de.uniks.vs.physics;

import de.uniks.vs.graphmodel.GraphGroupModel;

public class Point2D {

	private GraphGroupModel node;
	private double x;
	private double y;
	private boolean flagX;
	private boolean flagY;
	private String max;

	public Point2D(float x, float y) {
		this.x = x;
		this.y = y;
	}

    public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Point2D(String max, float y, GraphGroupModel rootNode) {
		this.x = rootNode.getWidth();
		this.y = y;
		this.max = max +"X";

//		if (max.equals(Border.HALF))
//			x = rootNode.getScene().getWidth() / 2;

		this.node = rootNode;
		flagX = true;
	}

	public Point2D(float x, String max, GraphGroupModel rootNode) {
		this.x = x;
		this.y = rootNode.getHeight();
		this.max = max +"Y";
		
//		if (max.equals(Border.HALF))
//			y = rootNode.getScene().getHeight() / 2;
		
		node = rootNode;
		flagY = true;
	}



    public double getDynX() {
		if (flagX)
			if (max.equals(Border.HALF))
				return node.getWidth()/2;
			else
				return node.getWidth();
		else
			return getX();
	}
	
	public double getDynY() {
		if (flagY) {
			if (max.equals(Border.HALF))
				return node.getHeight()/2;
			else
				return node.getHeight();
		}
		else
			return getY();

	}

	public double getY() {
		return this.y;
	}

	public double getX() {
		return x;
	}

	public void setLocation(double x, double y) {
			this.x = x;
			this.y = y;
	}

	public double distance(Point2D postion) {
		double a = getX() - postion.getX();
		double b = getY() - postion.getY();
		return Math.sqrt(a * a + b * b);
	}
}
