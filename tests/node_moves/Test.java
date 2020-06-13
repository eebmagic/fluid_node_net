public class Test {
    public static void main(String[] args) {

        System.out.println("Hello World!");

        Node x = new Node(1, 2);
        System.out.println(x.get_pos());
        x.move(10);
        System.out.println(x.get_pos());

	}
}
