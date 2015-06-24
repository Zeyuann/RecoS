package com.jzy.jedis;

/***
 * 基本的写日志
 * @author jiangzeyuan
 *
 */

public class AbstractTxnMain {
	
	public static void main(String[] args) {
		
		JedisUtil.init();
		JedisUtil.clusterDataInit(10000); //每个节点生成10000个随机值
		long txnCnt = 0;
		long lsnCnt = 0;
		
		Long start = System.currentTimeMillis();
		
		while (txnCnt < 300) {
			AbstractTxn atxn = new AbstractTxn(txnCnt++);
			String logEntry = null;		
			//这个事务生成日志
			for (int i = 0; i < 10; i++) {
				Log log = atxn.generateOneQuery(lsnCnt++);				
				if (log.toString() != null) {
					JedisUtil.singleRpush(log.siteNum, "redo_log", log.toString());
				}
			} //for
			
		} //while
		
		Long end = System.currentTimeMillis();
		System.out.println(end-start);
		
		for (int i = 0; i < Parameters.CLUSTER_LEN; i++) {
			System.out.println("SINGLE_NODE: " + i);
			for (String str : JedisUtil.singleLrange(i, "redo_log")) {
				System.out.println(str);
			}

		}

	}
}