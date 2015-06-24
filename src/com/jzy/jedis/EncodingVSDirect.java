package com.jzy.jedis;

import java.awt.peer.SystemTrayPeer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class EncodingVSDirect implements Runnable{
	private AbstractTxn[] txns;
	private int queryCount;
	
	private static long minTxnID;
	private static long lsn ;
	private static long maxLSN;
	private static final int bufferSize = 500;
	private static String[] logBuffer = new String[bufferSize];
	private static String[] logBuffer2 = new String[bufferSize];
	private static int bufferPointer = 0;
	private static Map<String, String> globalDict = new HashMap<String, String>();
	private static int dictValue = 0;
	private static int commitTimes = 0;
	
	private static  FileOutputStream fos; //写图5.2
			
		
	private static JedisPool pool = new JedisPool("192.168.171.120", 30031);
	private static JedisPool pool2 = new JedisPool("192.168.171.120", 30032);
	
	static {
		pool.getResource().flushDB();
		System.out.println("------数据库初始化清空成功！！！----");
		
		pool2.getResource().flushDB();
		System.out.println("------数据库2初始化成功！！！！------");
		

	}
	Jedis jedis, jedis2;

	public EncodingVSDirect(int numTxn, long startLSN, int queryCount, long minTxnID) {
		this.txns = new AbstractTxn[numTxn];
		this.lsn = startLSN;
		this.queryCount = queryCount;
		this.minTxnID = minTxnID;
		this.maxLSN = lsn + numTxn * queryCount -1 ;
		
		jedis = pool.getResource();
		jedis2 = pool2.getResource();
		
		try {
			this.fos = new FileOutputStream(new File("results\\EncodingVSNoEncoding.dat"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {

		long start = System.currentTimeMillis();
		int threadID = Integer.parseInt(Thread.currentThread().getName());
		this.txns[threadID] = new AbstractTxn(threadID + minTxnID, queryCount);
		
		System.out.println("TxnID: " + threadID + "线程开始*************");
		for (int i = 0; i < queryCount; i++) {		
			
			//writeBuffer(threadID);
			writeBufferWithEncoding(threadID);
			
			// 日志间的随机时间间隔
			try {
				Thread.sleep((int) Math.random() * 10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		System.out.println("=======================" + Thread.currentThread().getName() + " End");
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		

	}
	
	public synchronized void writeBuffer(int threadID) {
		
		Log log = txns[threadID].generateOneQuery(lsn++);
		if (log.type != LogType.SELECT) {
			
			// 全局字典的组提交方式
			if (bufferPointer < bufferSize) {
				//System.out.println(log.type);
				
				System.out.println("事务" + (threadID + minTxnID) + "正在写入第 " + log.lsn + " 条日志..." + log.type);
				//String compressedLog = log.toStringWithTxnIDEncoding(globalDict.get(Long.toString(log.txnID)));
				//logBuffer[bufferPointer] = compressedLog== null?"":compressedLog;
				logBuffer2[bufferPointer] = log.toString() == null?"":log.toString();
				
				++bufferPointer ;
				
				
				//缓冲区充满，提交
				if (bufferPointer == bufferSize || log.lsn == maxLSN) {

					System.out.println(Thread.currentThread().getName() + "^^^^^^^^^^^^^^^^^^^^^^缓冲区满！！！！" + bufferPointer);
					
					//压缩

					//jedis.rpush("commit", logBuffer);
					//System.out.println(Thread.currentThread().getName() + "+++++++++++++++++++++++++++++++++++++++commit成功"  + commitTimes);
					
					//jedis.hmset("commit_dict" + Integer.toString(commitTimes), globalDict);
					//System.out.println(Thread.currentThread().getName() + "+++++++++++++++++++++++++++++++++++commit_dict成功" + commitTimes);

					//jedis.save();
					//System.out.println(Thread.currentThread().getName() +"=======================================save成功"+ commitTimes);
					
					//不压缩
					jedis2.rpush("commit", logBuffer2);
					jedis2.save();
					
					
										
					++commitTimes;
					globalDict.clear();
					bufferPointer = 0;
					dictValue = 0;
	
			
				}
			}
		}
	}
	
	public synchronized void writeBufferWithEncoding(int threadID) {
		
		Log log = txns[threadID].generateOneQuery(lsn++);
		if (log.type != LogType.SELECT) {
			
			// 全局字典的组提交方式
			if (bufferPointer < bufferSize) {
				//System.out.println(log.type);
				if (!globalDict.containsKey(Long.toString(log.txnID))) {
					globalDict.put(Long.toString(log.txnID),
							Integer.toString(dictValue));
					++dictValue;
				}
				System.out.println("事务" + (threadID + minTxnID) + "正在写入第 " + log.lsn + " 条日志..." + log.type);
				String compressedLog = log.toStringWithTxnIDEncoding(globalDict.get(Long.toString(log.txnID)));
				logBuffer[bufferPointer] = compressedLog== null?"":compressedLog;
				//logBuffer2[bufferPointer] = log.toString() == null?"":log.toString();
				
				++bufferPointer ;
				
				
				//缓冲区充满，提交
				if (bufferPointer == bufferSize || log.lsn == maxLSN) {

					System.out.println(Thread.currentThread().getName() + "^^^^^^^^^^^^^^^^^^^^^^缓冲区满！！！！" + bufferPointer);
					
					//压缩

					jedis.rpush("commit", logBuffer);
					System.out.println(Thread.currentThread().getName() + "+++++++++++++++++++++++++++++++++++++++commit成功"  + commitTimes);
					
					jedis.hmset("commit_dict" + Integer.toString(commitTimes), globalDict);
					System.out.println(Thread.currentThread().getName() + "+++++++++++++++++++++++++++++++++++commit_dict成功" + commitTimes);

					jedis.save();
					System.out.println(Thread.currentThread().getName() +"=======================================save成功"+ commitTimes);
					
					//不压缩
					//jedis2.rpush("commit", logBuffer2);
					//jedis2.save();
					
					
										
					++commitTimes;
					globalDict.clear();
					bufferPointer = 0;
					dictValue = 0;
	
			
				}
			}
		}
	}
	
	
	

}
			
