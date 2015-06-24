package com.jzy.jedis;

import java.util.Random;

public class TxnGenerator {
	private int numTx;	//需要模拟的事件总数
	private int maxKey;	//简单的指定当前cluster中最大的key（这些key是按照1,2,3...来的）
	private Random rand = new Random(3000);
	
	
	public TxnGenerator(int numTx, int maxKey) {
		this.numTx = numTx;
		this.maxKey = maxKey;
	}
	
	/**
	 * 执行numTx次simpleUpdate
	 * 每次update只在固定的key范围内找到targetKey，然后做更新
	 * 此方法可以作为完整的测试方法
	 */
	public void simpleUpdate () {
		
		for (long i = 0; i < numTx; i++) {
			
			Long timestamp =  System.currentTimeMillis();
			Long txID = i;
			String targetKey = Integer.toString(rand.nextInt(maxKey));	//在哪个key上做update
			String oldValue = JedisUtil.jc.get(targetKey);
			if (oldValue == null) {
				oldValue = "";
			}
			String newValue = Character.toString(randomChar());
			
			
			SimpleARIESLog sal = new SimpleARIESLog(timestamp, txID, targetKey, oldValue, newValue);
			int targetSite = JedisUtil.hashTable.get(targetKey);
			sal.writeLog(targetSite);
			
			JedisUtil.jc.set(targetKey, newValue);

		}
	}
	
	/**
	 * 完整的增删改查的动作
	 */
	public void CRUD() {
		if (!JedisUtil.clusterIsEmpty()) {
			System.out.println("warning: Cluster is not empty at first."); 
		}
		// TODO
		
	}
	
	/**
	 * 生成一个随机的value值，这个值的长度为一个字符
	 * @return 一个随机的字符
	 */
	private char randomChar() {

        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int num = rand.nextInt(62);
        return str.charAt(num);
	    
	}
	
	
}
