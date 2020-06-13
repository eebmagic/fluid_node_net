import java.util.ArrayList;

public class Node {
    private CoordPair pos;
    private ArrayList<Node> connected = new ArrayList<Node>();

    private double directionAngle;

    /**
     * Constructor for making node at given position
     * will make a new CoordPair object
     * @param x the x position
     * @param y the y position
     * @param z the z position
     */
    public Node(double x, double y, double z) {
        this.pos = new CoordPair(x, y, z);
        this.directionAngle = (int)(360 * Math.random());
    }

    public Node(int x, int y, int z) {
        this((double)x, (double)y, (double)z);
    }

    public Node(double x, double y) {
        this(x, y, 0);
    }

    public Node(int x, int y) {
        this(x, y, 0);
    }

    /**
     * Constructor for making node with position of given coordinate pair
     * will use the given CoordPair object
     * @param pos the CoordPair object to use for the position of the new node
     */
    public Node(CoordPair pos) {
        this.pos = pos;
    }

    public Node() {
        this(0, 0, 0);
    }

    /**
     * Moves the node by the given distance with the current angle
     * @param distance the distance that the node should move
     */
    public void move(double distance) {
        double realAngle = Math.toRadians(directionAngle);

        double addX = distance * Math.sin(realAngle);
        double addY = distance * Math.cos(realAngle);
        double newX = this.get_pos().get_x() + addX;
        double newY = this.get_pos().get_y() + addY;

        this.set_pos(new CoordPair(newX, newY));
    }

    /**
     * Adds another node to the arraylist of connected nodes
     * @param n the node to add to the connected list
     * @param fill true: add this node to n's connected list; false: no change to n's list
     */
    public void add_connection(Node n, boolean fill) {
        connected.add(n);
        if (fill) {
            n.add_connection(this, false);    
        }
    }

    /**
     * gets the distance to a given node from this node
     * @param n the node to calculate the distance to
     * @return the distance to that node
     */
    public double distance_to(Node n) {
        CoordPair a = this.get_pos();
        CoordPair b = n.get_pos();

        double d2 = ((b.get_y() - a.get_y()) * (b.get_y() - a.get_y())) + ((b.get_x() - a.get_x()) * (b.get_x() - a.get_x()));
        double d = Math.sqrt(d2);
        return d;
    }

    public void add_connection(Node n) {
        add_connection(n, true);
    }

    public CoordPair get_pos() {
        return this.pos;
    }

    public void set_pos(CoordPair newPos) {
        if (newPos == null) {
            throw new IllegalArgumentException("###ERROR: Null position passed to the Node's set_pos() function");
        }
        this.pos = newPos;
    }

    public ArrayList<Node> get_connected() {
        return this.connected;
    }
}