package com.jzy.jedis;

public class ARIESUpdateLog extends ARIESCommLog {
	/*
	 * Update的记录需要的额外字段
	 */
	private long pageID;
	private long length;
	private long offset;
	private Object beforeImage;
	private Object afterImage;
}
