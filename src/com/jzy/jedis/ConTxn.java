package com.jzy.jedis;

import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class ConTxn extends AbstractTxn implements Runnable {
	private static final int bufferSize = 15;
	private static String[] logBuffer = new String[bufferSize];
	private static int bufferPointer = 0;

	private static Map<String, String> globalDict = new HashMap<String, String>();
	private static int dictPointer = 0;

	private static int commitTimes = 0;

	private static long lsn = 1000;
	
	private static JedisPool pool = new JedisPool("192.168.171.120", 30031);
	static {
		pool.getResource().flushDB();
		System.out.println("------数据库初始化清空成功！！！----");
	}

	public ConTxn(long txnID, int queryCount) {
		super(txnID, queryCount);
	}

	@Override
	public void run() {

		System.out.println("TxnID: " + this.txnID + "线程开始*************");
		for (int i = 0; i < queryCount; i++) {
			
			execute();

			// 日志间的随机时间间隔
			try {
				Thread.sleep((int) Math.random() * 10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		System.out.println("=======================" + txnID + " End");

	}
	
	public void execute()  {
		
			Log log = generateOneQuery(lsn++);
			//System.out.println("事务" + txnID + "正在产生第 " + log.lsn + " 条日志...");
			if (log.type != LogType.SELECT) {
				
				// 全局字典的组提交方式
				if (bufferPointer < bufferSize) {
					//System.out.println(log.type);
					if (!globalDict.containsValue(Long.toString(log.txnID))) {
						globalDict.put(Integer.toString(dictPointer),
								Long.toString(log.txnID));
						++dictPointer;
					}
					logBuffer[bufferPointer] = log.toString() == null?"":log.toString();
					System.out.println("事务" + txnID + "正在写入第 " + log.lsn + " 条日志..." + log.type);
					++bufferPointer ;
					
					//缓冲区充满，提交
					if (bufferPointer == bufferSize) {

						System.out.println(Thread.currentThread().getName() + "^^^^^^^^^^^^^^^^^^^^^^缓冲区满！！！！" + bufferPointer);
						Jedis jedis = pool.getResource();
						
						if(logBuffer == null )return ;
						jedis.rpush("commit", logBuffer);
						System.out.println(Thread.currentThread().getName() + "+++++++++++++++++++++++++++++++++++++++commit成功"  + commitTimes);
						
						jedis.hmset("commit_dict" + Integer.toString(commitTimes++), globalDict);
						System.out.println(Thread.currentThread().getName() + "+++++++++++++++++++++++++++++++++++commit_dict成功" + commitTimes);

						jedis.save();
						System.out.println(Thread.currentThread().getName() +"=======================================save成功"+ commitTimes);
						
//						for(String str : logBuffer) {
//							System.out.println(str);
//						}
//						System.out.println("======================");
						
						globalDict.clear();
						bufferPointer = 0;
						dictPointer = 0;
					}

				} 

			}
		
	}
}
