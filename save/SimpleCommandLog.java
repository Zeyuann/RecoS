package com.jzy.jedis;
/********
 * 
 * 论文上说：
 * command logging不用加锁，多机环境下是全局统一的顺序执行
 * 在事务发生之前，已经将可预知的事务变成存储过程
 *
 */

public class SimpleCommandLog {
	private long timestamp;
	private long txnID;
	private StoredProcedure sp;
	private String[] parameters;
}

/*
 * 需要将一些事务转变成为StoredProcedure
 * 
 */
class StoredProcedure {
	//UPDATE (X,Y) VALUES (5, 6) WHERE id = 5
	private String query;
	//private Parameter[] params;
	
}

