package com.jzy.jedis;

public class ConcurrentTxn extends AbstractTxn implements Runnable{
	
	private ConcurrentTxnManager ctm;
	
	public ConcurrentTxn(long txnID, int queryCount) {
		super(txnID, queryCount);
		ctm = new ConcurrentTxnManager(txnID);
	}
	
	public void run() {
		
		System.out.println("TxnID: " + this.txnID + "线程开始*************");	
		for (int i = 0; i < queryCount; i++) {
			
			Log log = generateOneQuery(ctm.getLSN());
			System.out.println("事务" + txnID + "正在产生第 " + log.lsn + " 条日志..." );
			if (log.type != LogType.SELECT) {
					ctm.managerLog(log);

			}
			
			//日志间的随机时间间隔
			try {
				Thread.sleep( (int)Math.random() * 10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		System.out.println("=======================" + txnID +" End");
		
		
	}
	
}
