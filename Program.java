import java.util.HashSet;
import java.util.ArrayList;
import java.util.Random;
import java.util.PriorityQueue;
import java.util.Collections;

// javafx imports
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.Group;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Program extends Application {
    /**
     * reflects a point around the middle of the given width
     * @param point the point to generate the reflected point from
     * @param width the width of the canvas being reflected on
     * @return the newly generate point
     */
    public CoordPair vert_mirror(CoordPair point, int width) {
        return new CoordPair(width - point.get_x(), point.get_y());
    }

    public CoordPair horiz_mirror(CoordPair point, int height) {
        return new CoordPair(point.get_x(), height - point.get_y());
    }

    public static ArrayList<Node> makeMap(int totalCount, int width, int height) {
        ArrayList<Node> arr = new ArrayList<>();
        Node n;
        for (int i = 0; i < totalCount; i++) {
            int x = (int)(width * Math.random());
            int y = (int)(height * Math.random());
            n = new Node(x, y); /// Defaults nodes to two-dimensional
            arr.add(n);
        }

        return arr;
    }

    /**
     * Method for finding the n number of closest neighboring nodes in a map
     * @param map the ArrayList of Nodes to check
     * @param node the node to start from
     * @param count the number of nodes to include
     * @return a normal java array with size of count filled with nodes
     * NOTE: This might should be moved to Node.java since it's only used from one node at a time.
     *       Also might should use boxes to search through instead of checking whole set of nodes.
     */
    public Node[] find_n_closest(ArrayList<Node> map, Node node, int count) {
        if (count >= map.size()) {
            String msg = new String("ERROR: cannot find n closest nodes when n matches map node count");
            throw new IllegalArgumentException(msg);
        }

        ArrayList<Entry> distances = new ArrayList<>();
        for (Node n : map) {
            Entry entry = new Entry(node.distance_to(n), n);
            distances.add(entry);
        }
        Collections.sort(distances);

        Node[] output = new Node[count];
        for (int i = 0; i < count; i++) {
            output[i] = distances.get(i).get_node();
            
        }

        return output;
    }


    /**
     * Draws two nodes and the line between them
     * @param gc the GraphicsContext to draw in
     * @param a the first Node to draw
     * @param b the second Node to draw
     * @param nodeSize how big the nodes should be drawn to be
     * NOTE: Maybe add color spectrum for length of lines
     */
    public void draw_connection(GraphicsContext gc, Node a, Node b, int nodeSize) {
        double a_x = a.get_pos().get_x();
        double a_y = a.get_pos().get_y();
        double b_x = b.get_pos().get_x();
        double b_y = b.get_pos().get_y();
        gc.fillOval(a_x, a_y, nodeSize, nodeSize);
        gc.fillOval(b_x, b_y, nodeSize, nodeSize);
        gc.strokeLine(a_x + (nodeSize/2), a_y + (nodeSize/2), b_x + (nodeSize/2), b_y + (nodeSize/2));
    }

    /**
     * Draws two nodes and the line between them. And then draws the reflection of the nodes.
     * Allows for "more" nodes to be drawn without too much extra resources.
     * @param gc the GraphicsContext to draw in
     * @param a the first Node to draw
     * @param b the second Node to draw
     * @param nodeSize how big the nodes should be drawn to be
     * @param width the width of the area that the nodes should be reflected around
     */
    public void draw_and_mirror(GraphicsContext gc, Node a, Node b, int nodeSize, int width, int height) {
        draw_connection(gc, a, b, nodeSize);
        Node vert_a = new Node(vert_mirror(a.get_pos(), width));
        Node vert_b = new Node(vert_mirror(b.get_pos(), width));
        Node horiz_a = new Node(horiz_mirror(a.get_pos(), height));
        Node horiz_b = new Node(horiz_mirror(b.get_pos(), height));

        // // Seems to onlly look good with one of these
        draw_connection(gc, vert_a, vert_b, nodeSize);
        // draw_connection(gc, horiz_a, horiz_b, nodeSize);
    }

    /**
     * Iterates over all nodes in the map and draws their n nearest neighbors
     * @param gc the GraphicsContext to draw in
     * @param map the the ArrayList of nodes to draw and find neighbors for
     * @param count the number of neighbors to find for each node
     * @param nodeSize how big the nodes should be drawn
     */
    public void draw_all(GraphicsContext gc, ArrayList<Node> map, int count, int nodeSize) {
        for (Node n : map) {
            Node[] neighbors = find_n_closest(map, n, count);
            for (int i = 0; i < count; i++) {
                // draw_connection(gc, n, neighbors[i], nodeSize);
                draw_and_mirror(gc, n, neighbors[i], nodeSize, screen_width, screen_height);
            }
        }

        // Draw all nodes without drawing connections
        for (Node n : map) {
            gc.fillOval(n.get_pos().get_x(), n.get_pos().get_y(), nodeSize, nodeSize);
        }
    }

    /**
     * Moves all nodes in a list by their set amount (Defined seperately in each Node object)
     *  Node objects have an x and y velocity from -0.5 to 0.5
     * @param map the ArrayList of Nodes to move
     * @param increment number to move/multiply the Node velocities by
     * @param width the screen width to bounch off of
     * @param height the screen height to bounce off of
     */
    public void move_all(ArrayList<Node> map, double increment, int width, int height) {
        for (Node n : map) {
            n.move(increment, width, height);
        }
    }


    /* =============================================================== */
    // Constant Definitions
    final static int screen_width = 1000;
    final static int screen_height = 700;
    final static int nodeSize = 5;
    final static int nodeCount = 220;
    final static int nodeConnections = 5;

    final static int movementIncrement = 5;
    final static int frameTick = 1;
    
    @Override
    public void start(Stage primaryStage) {
        // INITIALIZE NODE MAP //
        ArrayList<Node> map = makeMap(nodeCount, screen_width, screen_height);

        // SETUP SCREEN //
        Group root = new Group();
        Canvas canvas = new Canvas(screen_width, screen_height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        primaryStage.setTitle("Web");
        primaryStage.setScene(new Scene(root, Color.BLACK));
        primaryStage.show();


        // COLOR OPTIONS //
        // // All White
        gc.setStroke(Color.WHITE);
        gc.setFill(Color.WHITE);
        
        // // Blue and Black
        // gc.setStroke(Color.BLUE);
        // gc.setLineWidth(3);


        new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                
                // CLEAR SCREEN //
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                // DRAW AND MOVE NODES //
                draw_all(gc, map, nodeConnections, nodeSize);
                move_all(map, movementIncrement, screen_width, screen_height);
                
                // SET FRAMERATE //
                try {
                    Thread.sleep(frameTick);
                } catch (InterruptedException e) {
                    System.out.println("Caught try block for timing.");
                }

            }
        }.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}