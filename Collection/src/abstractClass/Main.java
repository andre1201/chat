package abstractClass;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
	 ArrayList<Human> h2 = new ArrayList<Human>();
	 for (int i = 0; i < 10; i++) {
	 	h2.add(new Human(i));
	 }
	 h2.remove(3);
	 h2.remove(5);
	 h2.remove(6);
	 for (Human human : h2) {
		 human.create();	
	 }
}

}
