package Lists;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.plaf.SliderUI;




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
		System.out.println(list.get(2).getMarka()+list.get(2).getName()+list.get(2).getVolume());
				
		list.set(2, new Car("","",0));
		//��������� ����� �������� �� �����
		 @SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in); // ������ ������ ������ Scanner
		 String[] str = new String[4];
		 for (int i = 0; i < 4; i++) {
			 if(i==0){
				 System.out.print("��� =");
			 }else if (i==1){
				 System.out.print("����� =");
			 }else if (i==2){
				 System.out.print("�������� =");
			 }else if (i==3){
				 System.out.print("��������� �������� =");
			 }
			str[i]=sc.nextLine();
		}
	      Car car2 = new Car(str[0],str[1],Integer.parseInt(str[2]) ,Float.parseFloat(str[3]));
	          System.out.println(car2.toString());
	      sc.close();   
	      System.out.println("��������� �������� = "+car2.getSpeed());
	      Random rand = new Random();//������ �������� � ������
	      boolean dead = false;//���� ��?
	      
	        for (int i = 0; i < rand.nextInt(100); i++) {
	        	int randInt=rand.nextInt(25);//������ ������ ����� �� 0 �� 25
				car2.setSpeed(car2.getSpeed()+randInt);
				 dead = rand.nextBoolean();//������ true or false
				System.out.println("�������� ����������� �� "+randInt+" ������ ���� �������� = "+car2.getSpeed());
			}
	       if(dead || car2.getSpeed()>250){
	    	   System.out.println("�� ������");
	       }else System.out.println("�� ����");
	       
	  
	}	
}
