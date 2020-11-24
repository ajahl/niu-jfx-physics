package de.uniks.vs.physics;

//import de.uniks.vs.ui.GraphGroup;
//import de.uniks.vs.ui.GraphCursor;
//import de.uniks.vs.ui.GraphEdge;
//import de.uniks.vs.ui.GraphNode;
//import de.uniks.vs.ui.manager.ModelManager;

import de.uniks.vs.graphmodel.*;

import java.util.ArrayList;

//import javafx.scene.Group;
//import javafx.scene.Node;
//import javafx.scene.paint.Color;
//import javafx.scene.shape.Line;

public class Physics {
	
	private GraphGroupModel rootNode;
	ArrayList<PhysicsItem> items;
	ArrayList<Border> borders;
//	private ModelManager modelManager;
	private GraphModel graphModel;
	private int speed;
	private double remanence;

    private boolean debugOn = false;
//	private ModelManager manager;


	public Physics() {
		items = new ArrayList<>();
        borders = new ArrayList<>();
		speed = 1;
		remanence = 0.1;
	}

	public Physics withRootNode(GraphGroupModel rootNode) {
		this.rootNode = rootNode;
		
//		Scene scene = rootNode.getScene();
//		double width = scene.getWidth();
//		double height = scene.getHeight();
		
		// -- create border --
		createBorders(this.rootNode);
		
		return this;
	}

	private void createBorders(GraphGroupModel rootNode) {
		Border upperBorder = new Border(rootNode, new Point2D(0,0), new Vec2D(1.f, 0.f)).withName("top");
		upperBorder.withActive(false).withRepulsion(80);
		items.add(upperBorder);
        borders.add(upperBorder);

		Border downBorder = new Border(rootNode, new Point2D(0, Border.MAX_Y, rootNode), new Vec2D(1.f, 0.f)).withName("bottom");
		downBorder.withActive(false).withRepulsion(100).withGravity(1);
		items.add(downBorder);
        borders.add(downBorder);

		Border leftBorder = new Border(rootNode, new Point2D(0, 0), new Vec2D(0.f, 1.f)).withName("left");
		leftBorder.withActive(false).withRepulsion(100);
		items.add(leftBorder);
        borders.add(leftBorder);

//		Border rightBorder = new Border(rootNode, new Point2D(width,0), new Vec2D(0.f, 1.f));
		Border rightBorder = new Border(rootNode, new Point2D(Border.MAX_X, 0, rootNode), new Vec2D(0.f, 1.f)).withName("right");
		rightBorder.withActive(false).withRepulsion(100);
		items.add(rightBorder);
        borders.add(rightBorder);

//		Border middleBorder = new Border(rootNode, new Point2D( 0, Border.HALF, rootNode), new Vec2D(0.f, 1.f)).withId("middle");
//		middleBorder.withActive(false).withRepulsion(0);
//		items.add(middleBorder);
//		borders.add(middleBorder);
	}
	
	public PhysicsItem createPhysics(GraphItemModel node, float power) {
		PhysicsItem item = null;
		
		if (node instanceof GraphNodeModel) {
		 item = new PhysicsItem(node)
				.withRepulsion(power)
				.withActive(true);
		}
		
		else if (node instanceof GraphCursorModel) {
			item = new Cursor(node)
					.withRepulsion(power)
					.withActive(false);
		}
		items.add(item);
		
		return item;
	}
	
	public void removeFromPhysics(long id) {
		PhysicsItem item = getFromPhysics(id);
		if (item != null)
			items.remove(item);
	}

	public PhysicsItem getFromPhysics(long id) {
		PhysicsItem item = null;
		for (PhysicsItem physicsItem : items) {
			if (id == (physicsItem.getNode().getGraphItemModelID())) {
				item = physicsItem;
			}
		}
		return item;
	}
	
	public void removeFromPhysics(GraphNodeModel cursorNode) {
		PhysicsItem item = null;
		
		for (PhysicsItem physicsItem : items) {
			if (physicsItem.getNode() == cursorNode){
				item = physicsItem;
			}
		}
		
		if (item != null)
			items.remove(item);
	}
	
	public void update() {
		
		for(int i = 0; i < speed; i++)
			updatePhysics();
	}

	public void remanence( double limit) {
		
		if (remanence - limit > 0)
			remanence-= 0.0001;
	}
		
	private void updatePhysics() {
		
		for (PhysicsItem current : items) {
			
			if (!current.isActiv())
				continue;
			
			Vec2D energyVector = new Vec2D();
			
			for (PhysicsItem physicsItem : items) {
				
				if (current == physicsItem)
					continue;
				
				Vec2D eVector = physicsItem.getEnergyVector(current.getPosition(), current.getMass());
				energyVector = vecAdd(energyVector, eVector);
			}
			
			updateCurrentNode(current, energyVector);
			updateDependencies(current);
		}

        updateBorder();

    }

	private void updateDependencies(PhysicsItem current) {
		GraphNodeModel jfxGraphNode = (GraphNodeModel) current.getNode();
//		String jfxGraphNodeId = jfxGraphNode.getId();
        GraphNodeModel gNode = getGraphNode(jfxGraphNode);

        if (gNode == null)
            return;

        updateEdges(jfxGraphNode, gNode);

	}

    private void updateBorder( ) {
//        double x = jfxGraphNode.getX();
//        double y = jfxGraphNode.getY();

        ArrayList<Border> borders = getBorders();

        boolean active = false;
        for (Border border : borders) {
            active |= border.updateParameter();
        }

        if (active)
            remanence = 0.5;
    }

	private void updateEdges(GraphNodeModel jfxGraphNode, GraphNodeModel gNode) {

        for (GraphEdgeModel graphEdge : gNode.getInEdges()) {
            updateEdge(jfxGraphNode, graphEdge, "incoming");
        }

        for (GraphEdgeModel graphEdge : gNode.getOutEdges()) {
            updateEdge(jfxGraphNode, graphEdge, "outgoing");
        }
    }

    private void updateEdge(GraphNodeModel jfxGraphNode, GraphEdgeModel graphEdge, String type) {
        double x = jfxGraphNode.getX();
        double y = jfxGraphNode.getY();
        long id = graphEdge.getGraphItemModelID();

        if(graphEdge.getGraphModel() == null)
            return;

//        GraphEdgeModel jfxGraphEdge = getChildEdgeWithID(graphEdge.getGraphModel().getName() +"_"+id);
        GraphEdgeModel jfxGraphEdge = getChildEdgeWithID(id);

        if (jfxGraphEdge == null) {
            return;
        }

        if (type == "incoming") {
            jfxGraphEdge.setEndX(x + jfxGraphNode.getWidth() / 2);
            jfxGraphEdge.setEndY(y + jfxGraphNode.getHeight() / 2);
            jfxGraphEdge.toBack();
        }
        else if (type == "outgoing") {
            jfxGraphEdge.setStartX(x + jfxGraphNode.getWidth()/2);
            jfxGraphEdge.setStartY(y + jfxGraphNode.getHeight()/2);
            jfxGraphEdge.toBack();
        }
    }

    private GraphNodeModel getGraphNode(GraphNodeModel jfxGraphNode) {
//        GraphModel lastModel = modelManager.getLastModel();
        GraphItemModel item = graphModel.getGraphItemWithID(jfxGraphNode.getGraphItemModelID());

        if (!(item instanceof GraphNodeModel)) {
            return null;
        }
        return (GraphNodeModel)item;
    }


    private void updateCurrentNode(PhysicsItem current, Vec2D energyVector) {
		GraphNodeModel rec = (GraphNodeModel) current.getNode();

//			TranslateTransition transition = new TranslateTransition(Duration.millis(20), current.getNode());
//			transition.setToX(rec.getX() + energyVector.getX());
//			transition.setToY(rec.getY() + energyVector.y);
		
		current.setDirectionVector(energyVector);
		
		double x = rec.getX() + energyVector.getX();
		double y = rec.getY() + energyVector.getY();

		if (x < 0)
			x = 1;
		if(y < 0)
			y = 1;
		

		double width = rootNode.getWidth();
		double height = rootNode.getHeight();
		
		if (x > width-60)
			x = width-60;
		if (y > height-30)
			y = height-30;
		
		rec.setX(x);
		rec.setY(y);
//			transition.play();
		
	}
	

//	public void update() {
//		ObservableList<Node> nodes = this.rootNode.getChildren();
//		
//		for (Node node : nodes) {
//			
//				Rectangle rec = (Rectangle) node;
//				double current_x = rec.getX() + rec.getWidth()/2;
//				double current_y = rec.getY() + rec.getHeight()/2;
//				
//				Point2D currentPos = new Point2D(current_x, current_y);
//				Point2D corner = new Point2D(0.0, 0.0);
//				
//				//distance to upper left corner 
//				Vec2D vec2d = fleeVector(currentPos);
//
////				//distance of the other nodes
////				for (Node otherNode : nodes) {
////					
////					if (node != otherNode) {
////						Rectangle otherRec = (Rectangle) otherNode;
//////						vec2d = vecAdd(vec2d, fleeVector(currentPos, new Point2D(otherRec.getX(), otherRec.getY())));
////						vec2d = fleeVector(currentPos, new Point2D(otherRec.getX(), otherRec.getY()));
////						TranslateTransition transition = new TranslateTransition(Duration.millis(8000),rec);
////						System.out.println(vec2d);
////						System.out.println((current_x + vec2d.x) + ",  "  + (current_y + vec2d.y));
////						transition.setToX(vec2d.x);
////						transition.setToY(vec2d.y);
//////				rec.setX(current_x + vec2d.x);
//////				rec.setY(current_y + vec2d.y);
////						transition.play();
////						System.out.println(rec.getX() + ",  "  + rec.getX());
////					}
////				}
//		}
//		
//	}

//	private Vec2D fleeVector(Point2D currentPos) {
//		Scene scene = rootNode.getScene();
//		
//		double width = scene.getWidth();
//		double height = scene.getHeight();
//		
//		double x = currentPos.getX();
//		double y = currentPos.getY();
//		
//		// border
//		// up
//		double distUpBorder = y;
//		
//		// down
//		double distDownBorder = height-y;
//		
//		// left
//		double distLeftBorder = x;
//		
//		// right
//		double distRightBorder = width-x;
//		
//		return null;
//	}

	private Vec2D vecAdd(Vec2D vec2d, Vec2D fleeVector) {
//		System.out.println(vec2d + "   " + fleeVector);
		vec2d.set(vec2d.getX() + (fleeVector.getX() *remanence), vec2d.getY()  + fleeVector.getY()*remanence);
		return vec2d;
	}

	public Physics withModelManager(GraphModel graphModel) {
		this.graphModel = graphModel;
		return this;
	}

	public GraphEdgeModel getChildEdgeWithID(long id) {
        synchronized (this.rootNode) {
            for (GraphItemModel node : this.rootNode.getChildren()) {

                if (node instanceof GraphEdgeModel && node.getGraphItemModelID() ==(id))
                    return (GraphEdgeModel) node;
            }
        }
		return null;
	}
//	private Vec2D fleeVector(Point2D currentPos, Point2D otherPos) {
//		double distance = 1 + currentPos.distance(otherPos);
////		System.out.println(distance + "   " + currentPos + " " + otherPos);
//		double x = - ( otherPos.getX()-currentPos.getX() ) * 10/distance;
//		double y = - ( otherPos.getY()-currentPos.getY() ) * 10/distance;
//		Vec2D vector = new Vec2D(x, y);
//		return vector;
//	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Physics withSpeed(int speed) {
		this.speed = speed;
		return this;
	}
	
	public void setRemanence(double remanence) {
		this.remanence = remanence;
	}

	public ArrayList<PhysicsItem> getItems() {
		return items;
	}


    public void reset() {
		items.clear();
        createBorders(rootNode);
    }

    public ArrayList<Border> getBorders() {
        return borders;
    }

//    public void updateDebug() {
//
//        Group group = getGroupWithID("Debug");
//        group.getChildren().clear();
//
//        for (PhysicsItem item : getItems()) {
//            Point2D postion = item.getPosition();
//            Vec2D vector = item.getVector();
//
//            if (postion == null || vector == null)
//                continue;
//
//            Line line = new Line();
//            line.setStartX(postion.getX());
//            line.setStartY(postion.getY());
//            line.setEndX(postion.getX() + (100 * vector.getX()));
//            line.setEndY(postion.getY() + (100 * vector.getY()));
//            line.setStroke(Color.YELLOW);
//            group.getChildren().add(line);
//        }
//    }

    /*
     * // Blocks until the queue has really any coins inserted
            blockingQueue.get();

            // Synchronize with javaFX thread
            CountDownLatch latch = new CountDownLatch(1);
            Platform.runLater(() -> {
                label.setText(....);
                latch.countDown();
            });

            // Block the Current Thread until the text is refreshed from
            // JavaFX Thread
            latch.await();
     */


//    private  Group getGroupWithID(String id) {
//
//        for ( Node node :rootNode.getChildren()) {
//
//            if (node instanceof Group && node.getId().equals(id))
//                return (Group)node;
//        }
//
//        Group group = new Group();
//        group.setId("Debug");
//        rootNode.getChildren().add(group);
//        return group;
//    }

    public boolean isDebugOn() {
        return debugOn;
    }
}
