package com.jzy.jedis;

public class SimpleARIESLog {
	private String timestamp;
	private String txnID;
	private String targetKey;
	private String oldValue;
	private String newValue;
	
	public SimpleARIESLog(String timestamp, String txID, String targetKey,
			String oldValue, String newValue) {
		this.timestamp = timestamp;
		this.txnID = txID;
		this.targetKey = targetKey;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public SimpleARIESLog(long timestamp, long txID, String targetKey, 
			String oldValue, String newValue) {
		this.timestamp = Long.toString(timestamp);
		this.txnID = Long.toString(txID);
		this.targetKey = targetKey;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	

	public void writeLog(int targetSite) {
		JedisUtil.singleNodes.get(targetSite-1).rpush("SimpleARIESLog", createLog());
	}
	
	private String createLog() {
		return timestamp + ":" + txnID + ":" + targetKey + ":" + oldValue + ":" + newValue;
	}
	
	private void parseLog(String log) {
		String[] arr = log.split(":");
		this.timestamp = arr[0];
		this.txnID =  arr[1];
		this.targetKey =  arr[2];
		this.oldValue =  arr[3];
		this.newValue =  arr[4];
	}


}
