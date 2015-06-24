package com.jzy.jedis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class StatisticalIndirectEncodingMain {
	public static void main(String[] args) {

		StatisticalIndireceEncoding sie = new StatisticalIndireceEncoding(100, 0, 20, 0);
		
		long startTime = System.currentTimeMillis();
		
		final CountDownLatch countDownLatch = new CountDownLatch(1000);
		
		//产生的事务线程数
		for (int i = 0; i < 100; i++) {
			new Thread(sie, Integer.toString(i)).start();
			countDownLatch.countDown();
			
			 try {
				Thread.sleep((int)Math.random()*5 + 3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			countDownLatch.await();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		long time = endTime - startTime;
		System.out.println(time);
		try {
			FileOutputStream fos = new FileOutputStream(new File("results\\StatisticalDictEncoding_time.dat"));
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
