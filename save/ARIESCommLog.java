package com.jzy.jedis;

public class ARIESCommLog {

	
//	public int record_type;
//	public type  type;
//	public String tablename;
//	public String primary_key;
//	public String modified_column_list;
//	public Object before_image;
//	public Object after_image;
	
	/*
	 * 所有日志都含有的公共字段
	 */
	protected long LSN;
	protected long prevLSN;
	protected long transID;
	protected ARIESLogType type;
	

	
}
