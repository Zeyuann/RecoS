package com.jzy.jedis;

public enum LogType {

	SELECT,
	UPDATE,
	DELETE,//2
	INSERT,
	TXN_START,
	TXN_ABORT,
	TXN_COMMIT
}
