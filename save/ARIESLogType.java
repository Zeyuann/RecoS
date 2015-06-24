package com.jzy.jedis;

public enum ARIESLogType {
	UPDATE,
	COMMIT,
	ABORT,
	END,	//commit或者abort后会有后续处理动作
	CLRS	//Compensation Lob Records
				
}
