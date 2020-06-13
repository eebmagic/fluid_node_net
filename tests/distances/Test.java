public class Test {
    public static void main(String[] args) {

        System.out.println("Hello World!");

        Node a = new Node(79, 2);
        Node b = new Node(73, 66);
        // Node a = new Node(1, 1);
        // Node b = new Node(2, 2);
        double distance = a.distance_to(b);

        System.out.println(a.get_pos());
        System.out.println(b.get_pos());
        System.out.println(distance);
	}
}
