package com.jzy.jedis;

public class ConcurrentTxnMain {
	public static void main(String[] args) {

		JedisUtil.init();
		
		for (int i = 1; i <= 3; i++) {
			(new Thread(new ConcurrentTxn(i, 15))).start();
			
			 try {
				Thread.sleep((int)Math.random()*5 + 3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
}
