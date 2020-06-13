// This is used for sorting pairs of Nodes and their distances in Program.find_n_closest()
public class Entry implements Comparable<Entry> {
    private double dist;
    private Node node;

    public Entry(double dist, Node n) {
        this.dist = dist;
        this.node = n;
    }

    public double get_dist() {
        return this.dist;
    }

    public Node get_node() {
        return this.node;
    }

    @Override
    public int compareTo(Entry other) {
        double x = other.get_dist();
        if (x == this.dist) {
            return 0;
        } else if (this.dist > x) {
            return 1;
        } else {
            return -1;
        }
    }
}