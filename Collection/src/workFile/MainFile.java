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
		File file = new File("1.txt");//Создаем файл
		byte[] buf = new byte[1024];
		try {
			FileInputStream in = new FileInputStream(file);
			PrintWriter	out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)), true);
			FileReader w = new FileReader(file);
			out.append("1234567890",0,10);//запись в файл от 0 до 10 позиции
			out.flush();//стираем поток
			out.print("73121");//добавление в конец
			out.flush();
			
			int a = 0;
			while(in.available()!= 0)
				 a =in.read(buf);//возвращает число символов
			
			System.out.println(a );
			out.close();
		} catch (IOException e) {
			// TODO Автоматически созданный блок catch
			e.printStackTrace();	
		}	
	}
}
