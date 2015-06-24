package com.jzy.jedis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DictEncodingMain {
	public static void main(String[] args) {

		
		DictEncoding de = new DictEncoding(1000, 0, 20, 0);
		
		long startTime = System.currentTimeMillis();
		
		//产生的事务线程数
		for (int i = 0; i < 1000; i++) {
			new Thread(de, Integer.toString(i)).start();
			
			 try {
				Thread.sleep((int)Math.random()*5 + 3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long endTime = System.currentTimeMillis();
		long time = endTime - startTime;
		try {
			FileOutputStream fos = new FileOutputStream(new File("results\\DictEncoding_time.dat"));
			fos.write((Long.toString(time)).getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
