package com.jzy.jedis;

import java.util.Random;

import redis.clients.jedis.Jedis;

public class AbstractTxn {

	protected Long txnID;
	protected String[] querys;
	protected int queryCount;
	protected static Random rand = new Random(3000);
	
	public AbstractTxn(Long id) {
		this.txnID = id;
	}
	
	public AbstractTxn(Long id, int queryCount) {
		this.txnID = id;
		this.queryCount = queryCount;
	}
	
	/**
	 * 产生一个query需要的资源，产生日志
	 * @return
	 */
	public Log generateOneQuery(long LSN) {
		
		LogType type;	
		Long tupleID = (long)rand.nextInt(50000);
		
		if (JedisUtil.hashTable.containsKey(Long.toString(tupleID))) {
			//如果数据库中有这个key，那么可以选择DELETE或者UPDATE、SELECT
			int randRes = rand.nextInt(10);
			
			if (randRes < 2) {
				type = LogType.SELECT; //select不写日志

			} else if(randRes >7) {
				type = LogType.DELETE; //delete
				
			} else { 
				type = LogType.UPDATE; //update
			}		
			
			
		} else { //INSERT
			type = LogType.INSERT;
		}
		
		String value = JedisUtil.randomString(50);
		return new Log(LSN, txnID, type, tupleID, value, 
							JedisUtil.targetSite(Long.toString(tupleID)));
	}
	
	/**
	 * 待日志成功返回后去执行真正操作
	 * @param log 
	 */
	public void execOneQuery(Log log) {
		String strTupleID = Long.toString(log.tupleID);
		if (log.type == LogType.SELECT) {
			String res = JedisUtil.jc.get(strTupleID);
			
		} else if (log.type == LogType.UPDATE ) {
			JedisUtil.hashTable.put(strTupleID, log.siteNum);
			JedisUtil.jc.set(strTupleID, log.value);
			
		} else if (log.type == LogType.DELETE ) {
			if (JedisUtil.hashTable.containsKey(strTupleID)) {
				JedisUtil.hashTable.remove(strTupleID);
			}
			JedisUtil.jc.del(strTupleID);
			
		} else if (log.type == LogType.INSERT ) {
			JedisUtil.hashTable.put(strTupleID, log.siteNum);
			JedisUtil.jc.set(strTupleID, log.value);
		}
		
	}
				
}
