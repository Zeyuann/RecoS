package com.jzy.jedis;

import static org.junit.Assert.*;

import org.junit.Test;

public class TxGeneratorTest {

	@Test
	public void simpleUpdateTest() {
		JedisUtil.init();
		JedisUtil.clusterDataInit(5000);
		TxnGenerator tg = new TxnGenerator(10000, 14999);
		tg.simpleUpdate();
		for (String str : JedisUtil.singleNodes.get(2).lrange("SimpleARIESLog", 1, -1)) {
			System.out.println(str);
		}
		//System.out.println(JedisUtil.lj.get(1).lrange("SimpleARIESLog", 1, -1));
	}

	
}
