import java.util.HashSet;

public class Test {
    public static void main(String[] args) {

        HashSet<Integer> s = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            s.add(i);
        }
    
        System.out.println("Hello World!");
        for (int x : s) {
            System.out.println(x);
        }
	}
}
