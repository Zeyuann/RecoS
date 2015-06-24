package com.jzy.jedis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class IndirectEncoding implements Runnable{
	private AbstractTxn[] txns;
	private int queryCount;
	
	private static long minTxnID;
	private static long lsn ;
	private static long maxLSN;
	private static final int bufferSize = 250;
	private static final int blockSize =  50;
	private static final int numBlock = bufferSize / blockSize; 
	private static int p1 = 1;
	private static int p2 = bufferSize;
	private static String[] logBuffer = new String[bufferSize];
	private static String[] logBuffer2 = new String[bufferSize];
	private static int bufferPointer = 0;
	private static Map<String, String> localDict = new HashMap<String, String>();
	private static int dictValue = 0;
	private static int commitTimes = 0;
	private static int inBlockPointer = 0;
	private static int blockPointer = 0;;
	
	private static  FileOutputStream fos; 
			
		
	private static JedisPool pool = new JedisPool("192.168.171.120", 30031);
	private static JedisPool pool2 = new JedisPool("192.168.171.120", 30032);
	
	static {
		pool.getResource().flushDB();
		System.out.println("------数据库初始化清空成功！！！----");
		
		pool2.getResource().flushDB();
		System.out.println("------数据库2初始化成功！！！！------");
		

	}
	Jedis jedis, jedis2;

	public IndirectEncoding(int numTxn, long startLSN, int queryCount, long minTxnID) {
		this.txns = new AbstractTxn[numTxn];
		this.lsn = startLSN;
		this.queryCount = queryCount;
		this.minTxnID = minTxnID;
		this.maxLSN = lsn + numTxn * queryCount -1 ;
		
		jedis = pool.getResource();
		jedis2 = pool2.getResource();
		
		try {
			this.fos = new FileOutputStream(new File("results\\DictEncoding.dat"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {

		int threadID = Integer.parseInt(Thread.currentThread().getName());
		this.txns[threadID] = new AbstractTxn(threadID + minTxnID, queryCount);
		
		System.out.println("TxnID: " + threadID + "线程开始*************");
		for (int i = 0; i < queryCount; i++) {		
			
			writeBuffer(threadID);
			
			// 日志间的随机时间间隔
			try {
				Thread.sleep((int) Math.random() * 10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		System.out.println("=======================" + Thread.currentThread().getName() + " End");

	}
	
	public synchronized void writeBuffer(int threadID) {
		
		Log log = txns[threadID].generateOneQuery(lsn++);
		if (log.type != LogType.SELECT) {
			if(inBlockPointer < blockSize	){
				if (!localDict.containsKey(Long.toString(log.txnID))) {
					localDict.put(Long.toString(log.txnID),
							Integer.toString(dictValue));
					++dictValue;
					System.out.println(localDict.get(Long.toString(log.txnID)));
				}

				System.out.println("事务" + (threadID + minTxnID) + "正在写入第 " + log.lsn + " 条日志..." + log.type);
				String compressedLog = log.toStringWithTxnIDEncoding(localDict.get(Long.toString(log.txnID)));
				System.out.println(compressedLog);
				logBuffer[bufferPointer] = compressedLog== null?"":compressedLog;
				logBuffer2[bufferPointer] = log.toString() == null?"":log.toString();
				
				++bufferPointer ;
				++inBlockPointer ;
				
				if (blockSize == inBlockPointer ) {
					jedis.hmset("localDict" + commitTimes + ":"
							+ blockPointer , localDict);					
					inBlockPointer = 0;
					dictValue = 0;
					localDict.clear();
					++blockPointer;
					
				}
				
				if (bufferPointer  == bufferSize) {
					jedis.rpush("commit", logBuffer);
					jedis.save();
					jedis2.rpush("commit", logBuffer2);
					jedis2.save();
					
					inBlockPointer = 0;
					bufferPointer = 0;
					blockPointer = 0;
					++commitTimes;
					
				}
			}
			
			
		}
 
}
}			
