package Lists;

import java.awt.List;
import java.util.ArrayList;



public class ListCollection {
	
	private static ArrayList<Car> list = new ArrayList<Car>();
	public static void main(String[] args){
		
		for (int i = 0; i < 3; i++) {//���������� ���������
			list.add(new Car("BMW","BMW XXX",400));
		}
	//�������� ���������� �� ��������� �������
	list.set(1, new Car("","",0));
	
		for(Car car:list) //���� foreach ����� ���� ��������� �� �����
		System.out.println(car.getMarka()+" " 
		+car.getName()+" "+car.getVolume());
		
	//���������� �������� �� �������� �������
		System.out.println(list.get(2).getMarka()+list.get(2).getName()
				+list.get(2).getVolume());
	
	}

	
	
	
}
