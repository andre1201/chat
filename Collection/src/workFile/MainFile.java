package workFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class MainFile {

	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("1.txt");//������� ����
		byte[] buf = new byte[1024];
		try {
			FileInputStream in = new FileInputStream(file);
			PrintWriter	out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)), true);
			FileReader w = new FileReader(file);
			out.append("1234567890",0,10);//������ � ���� �� 0 �� 10 �������
			out.flush();//������� �����
			out.print("73121");//���������� � �����
			out.flush();
			
			int a = 0;
			while(in.available()!= 0)
				 a =in.read(buf);//���������� ����� ��������
			
			System.out.println(a );
			out.close();
		} catch (IOException e) {
			// TODO ������������� ��������� ���� catch
			e.printStackTrace();	
		}	
	}
}
