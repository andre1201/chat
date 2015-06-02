package abstractClass;

public class Human extends People{
	private int i;
	public Human(int i){
		this.i = i;
	}
	public Human(){ }

	@Override void  create() {
		System.out.println("Human # "+this.i);	
	}
	
}
