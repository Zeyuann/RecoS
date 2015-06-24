package com.jzy.jedis;

import redis.clients.jedis.JedisCluster;
import redis.clients.util.JedisClusterCRC16;

public class CRC16Test {
	public static void main(String[] args) {
		int a = 0, b = 0, c = 0;
		int slotsPerNode = JedisCluster.HASHSLOTS / 3;

		
		for (int i = 0; i < 30000; i++) {
			int slot = JedisClusterCRC16.getCRC16(Integer.toString(i)) % JedisCluster.HASHSLOTS;
			if (slot < slotsPerNode) {
				a++;
			} else if (slot > 2*slotsPerNode) {
				c++;
			} else {
				b++;
			}
		}
		
		
		
		System.out.println(a + " " + b+ " " + c);
	}
	

}
