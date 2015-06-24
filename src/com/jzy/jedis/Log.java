package com.jzy.jedis;

public  class Log {

	public Long lsn;
	public Long txnID;
	public LogType type;
	public Long tupleID;
	public String value;
	
	public int siteNum; //记录日志的日志节点
	
	public Log(Long lsn, Long txnID, LogType type, 
					Long tupleID, String value, int siteNum) {
		super();
		this.lsn = lsn;
		this.txnID = txnID;
		this.type = type;
		this.tupleID = tupleID;
		this.value = value;
		this.siteNum = siteNum;
	}
	
	public String toString() {
		if (type == LogType.SELECT) return null;
		//不返回siteNum
		return this.lsn.toString() + ":" +
				this.txnID.toString() + ":" +
				this.type.ordinal() + ":" +
				this.tupleID.toString() + ":" +
				this.value;
	}
	
	public Log parseLog(String logEntry) {
		
		
		return null;
	}

	public String generateLog() {

		return null;
	}

	public String toStringWithTxnIDEncoding(String afterEncoding) {
		return this.lsn.toString() + ":" +
				afterEncoding + ":" +
				this.type.ordinal() + ":" +
				this.tupleID.toString() + ":" +
				this.value;
	}

	
	
	
}
