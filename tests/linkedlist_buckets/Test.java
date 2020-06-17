import java.util.LinkedList;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {

        System.out.println("Hello World!");

        int size = 5;

        // LinkedList<Node>[] arr = new LinkedList<Node>[size * size];
        ArrayList<ArrayList<Node>> ll = new ArrayList<>();
        for (int i = 0; i < size * size; i++) {
            // ll.add(new ArrayList<Node>());
            ll.get(i).add(new Node());
        }


        ArrayList<Node> l = ll.get(0);
        System.out.println(l.get(0).get_pos());
	}
}
