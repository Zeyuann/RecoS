package com.jzy.jedis;

import static org.junit.Assert.*;

import org.junit.Test;

public class LogTest {

	@Test
	public void toStringTest() {
		Log log = new Log(123l, 456l, LogType.INSERT, 789l, "11111", 0);
		System.out.println(log);
		
	}

}
