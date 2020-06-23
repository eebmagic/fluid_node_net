import java.util.HashSet;
import java.util.Collection;
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


    public static int[] get_bucket_inds(Node n, int width, int height, int bucketRes) {
        int[] out = new int[2];

        int w_size = (int)Math.floor((double)width / bucketRes);
        int h_size = (int)Math.floor((double)height / bucketRes);

        double x = n.get_pos().get_x();
        double y = n.get_pos().get_y();

        int x_cell = (int)Math.floor(x / w_size);
        int y_cell = (int)Math.floor(y / h_size);

        if (x_cell >= bucketRes) {
            x_cell = bucketRes - 1;
        }

        if (y_cell >= bucketRes) {
            y_cell = bucketRes - 1;
        }

        return new int[]{x_cell, y_cell};
    }

    /**
     * Make a map with buckets. Two dimensional ArrayList of HashSet buckets for nodes.
     * @param map the ArrayList of nodes to sort into buckets
     * @param totalCount the number of nodes to put in the bucketed map
     * @param width the max width of the screen / node area
     * @param height the max height of the screen / node area
     * @param bucketRes the number of rows and columns of buckets that there should be
     */
    public static ArrayList<ArrayList<HashSet<Node>>> makeBuckets(ArrayList<Node> map, int width, int height, int bucketRes) {
        // Initialize output buckets as empty arrayLists
        ArrayList<ArrayList<HashSet<Node>>> buckets = new ArrayList<>(bucketRes);
        for (int y = 0; y < bucketRes; y++) {
            ArrayList<HashSet<Node>> row = new ArrayList<HashSet<Node>>(bucketRes);
            for (int x = 0; x < bucketRes; x++) {
                row.add(new HashSet<Node>());
            }
            buckets.add(row);
        }
        
        // Sort all nodes into buckets
        int w_size = (int)Math.floor((double)width / bucketRes);
        int h_size = (int)Math.floor((double)height / bucketRes);

        for (Node n : map) {
            // double x = n.get_pos().get_x();
            // double y = n.get_pos().get_y();

            // int x_cell = (int)Math.floor(x / w_size);
            // int y_cell = (int)Math.floor(y / h_size);
            // // int x_cell = (double)x / w_size;
            // // int y_cell = (double)y / h_size;

            // if (x_cell >= bucketRes) {
            //     x_cell = bucketRes - 1;
            // }

            // if (y_cell >= bucketRes) {
            //     y_cell = bucketRes - 1;
            // }

            // replaced this ^ with that \/
            int[] cell_pos = get_bucket_inds(n, width, height, bucketRes);

            // HashSet<Node> b = buckets.get(y_cell).get(x_cell);
            HashSet<Node> b = buckets.get(cell_pos[1]).get(cell_pos[0]);
            b.add(n);
        }

        return buckets;
    }


    /**
     * method overload for making buckets without a previously made map of nodes
     * @param totalCount the number of nodes to put in the bucketed map
     * @param width the max width of the screen / node area
     * @param height the max height of the screen / node area
     * @param bucketRes the number of rows and columns of buckets that there should be
     */
    public static ArrayList<ArrayList<HashSet<Node>>> makeBuckets(int totalCount, int width, int height, int bucketRes) {
        // Make straight list of nodes
        ArrayList<Node> map = makeMap(totalCount, width, height);
        
        return makeBuckets(map, width, height, bucketRes);
    }


    public Node[] find_n_closest(ArrayList<ArrayList<HashSet<Node>>> buckets, Node n, int count, int width, int height, int bucketRes) {
        int[] cell_pos = get_bucket_inds(n, width, height, bucketRes);
        int y_cell = cell_pos[1];
        int x_cell = cell_pos[0];

        ArrayList<Node> wideSet = new ArrayList<>();
        int search_size = 1;
        do {
            for (int y = y_cell - search_size; y < y_cell + search_size; y++) {
                if (y >= 0 && y < bucketRes) {
                    for (int x = x_cell - search_size; x < x_cell + search_size; x++) {
                        if (x >= 0 && x < bucketRes) {
                            wideSet.addAll(buckets.get(y).get(x));
                        }
                    }
                }
            }
            search_size++;
        } while (wideSet.size() <= count);

        // System.out.printf("count: %d; in subset: %d\n", count, wideSet.size());

        return find_n_closest(wideSet, n, count);
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
    public Node[] find_n_closest(Collection<Node> set, Node sourceNode, int count) {
        if (count >= set.size()) {
            String msg = new String("ERROR: cannot find n closest nodes when n matches set node count");
            throw new IllegalArgumentException(msg);
        }

        ArrayList<Entry> distances = new ArrayList<>();
        for (Node n : set) {
            if (n != sourceNode) {
                Entry entry = new Entry(sourceNode.distance_to(n), n);
                distances.add(entry);
            }
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
                draw_connection(gc, n, neighbors[i], nodeSize);
            }
        }

        // Draw all nodes without drawing connections
        for (Node n : map) {
            gc.fillOval(n.get_pos().get_x(), n.get_pos().get_y(), nodeSize, nodeSize);
        }
    }

    public void draw_all(GraphicsContext gc, ArrayList<Node> map, ArrayList<ArrayList<HashSet<Node>>> buckets, int count, int nodeSize, int width, int height, int bucketRes) {
        for (Node n : map) {
            Node[] neighbors = find_n_closest(buckets, n, count, width, height, bucketRes);
            for (int i = 0; i < count; i++) {
                draw_connection(gc, n, neighbors[i], nodeSize);
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
    final static int screen_width = 1300;
    final static int screen_height = 1000;
    final static int nodeSize = 5;
    final static int nodeCount = 250;
    final static int cell_res = 5;
    final static int nodeConnections = 5;

    final static int movementIncrement = 5;
    final static int frameTick = 1;
    
    @Override
    public void start(Stage primaryStage) {
        // INITIALIZE NODE MAP //
        ArrayList<Node> map = makeMap(nodeCount, screen_width, screen_height);
        // ArrayList<ArrayList<HashSet<Node>>> buckets = makeBuckets(map, screen_width, screen_height, cell_res);

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


                // buckets = makeBuckets(map, screen_width, screen_height, cell_res);
                ArrayList<ArrayList<HashSet<Node>>> buckets = makeBuckets(map, screen_width, screen_height, cell_res);

                // DRAW AND MOVE NODES //
                // draw_all(gc, map, nodeConnections, nodeSize);
                draw_all(gc, map, buckets, nodeConnections, nodeSize, screen_width, screen_height, cell_res);
                move_all(map, movementIncrement, screen_width, screen_height);
                
                // resort buckets
                

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