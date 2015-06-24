package com.jzy.jedis;

import java.util.HashMap;
import java.util.Map;

public class ConcurrentTxnManager {
	public static final int bufferSize = 10;
	
	public static String[] logBuffer = new String[bufferSize];
	public static int bufferPointer = 0;
	public static long lsn = 1000;
//	public static Map<Integer, Long> globalDict = new HashMap<Integer, Long>();
	public static Map<String, String> globalDict = new HashMap<String, String>();
	public static int dictPointer = 0;
	public static int saveTimes = 0;

	
	private long txnID;
	public ConcurrentTxnManager(long txnID) {
		this.txnID = txnID;
	}
	public long getLSN(){
		return lsn++;
	}
	public static  void   managerLog(Log log) {
		//System.out.println("正在处理事务" + txnID + "的LSN为" + log.lsn + "的日志");
		//System.out.println(log.toString());
		
		//全局字典的组提交方式
		if( bufferPointer < bufferSize) {
			//System.out.println(log.type);
			if (!globalDict.containsValue(Long.toString(log.txnID)) ) {
				globalDict.put(Integer.toString(dictPointer), Long.toString(log.txnID));
				++dictPointer;
			} 
			logBuffer[bufferPointer++] = log.toString();
			
		} else { //缓冲区满
			
			System.out.println("^^^^^^^^^^^^^^^^^^^^^^缓冲区满！！！！");
			JedisUtil.singleRpush(0, "commit", logBuffer);	
			System.out.println("+++++++++++++++++++++++++++++++++++++++commit成功");
			JedisUtil.singleHmset(0, "commit_dict" + Integer.toString(saveTimes++), globalDict);
			System.out.println("+++++++++++++++++++++++++++++++++++++++commit_dict成功");
			JedisUtil.singleSave(0);
			System.out.println("=======================================save成功");
			//System.exit(0);
			
			
			//System.exit(0);
			globalDict.clear();
			bufferPointer = 0;
			dictPointer = 0;
		}
		
		
		
	}
	
}
