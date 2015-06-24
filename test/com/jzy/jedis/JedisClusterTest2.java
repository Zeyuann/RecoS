package com.jzy.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisClusterTest2  {


	@Test
	public void test() {
	    Set<HostAndPort> jedisClusterNode = new HashSet<HostAndPort>();
	    jedisClusterNode.add(new HostAndPort("192.168.171.120", 30021));
	    JedisCluster jc = new JedisCluster(jedisClusterNode);
	    
	    for (int i = 0; i < 30000; ++i) {
	    	String key = Integer.toString(i);
	    	jc.set(key, key);
	    }
	    
	    
	    for (JedisPool jp : jc.getClusterNodes().values()) {
	    	Jedis jedis = jp.getResource();
	    	System.out.println(jedis.dbSize());
	    	System.out.println(jedis.info());
	    	jedis.set("foo", "bar");
	    	jedis.flushDB();
	    	System.out.println(jedis.dbSize());
	    }
	}

}
