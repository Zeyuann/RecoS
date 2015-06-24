package com.jzy.jedis;

public class ConTxnMain {

	public static void main(String[] args) {

		//产生的事务线程数
		for (int i = 1; i <= 5; i++) {
			(new Thread(new ConTxn(i, 20))).start();
			
			 try {
				Thread.sleep((int)Math.random()*5 + 3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
