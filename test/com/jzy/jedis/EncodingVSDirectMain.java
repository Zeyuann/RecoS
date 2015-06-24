package com.jzy.jedis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class EncodingVSDirectMain {
	public static void main(String[] args) {

		EncodingVSDirect evd= new EncodingVSDirect(1, 0, 10000, 10000);
		final CountDownLatch countDownLatch = new CountDownLatch(50);
		long startTime = System.currentTimeMillis();
		
		//产生的事务线程数
		for (int i = 0; i < 1; i++) {
			new Thread(evd, Integer.toString(i)).start();
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
		try {
			FileOutputStream fos = new FileOutputStream(new File("results\\EncodingVSDirectMain_time.dat"));
			fos.write((Long.toString(startTime)).getBytes());
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
