package com.jzy.jedis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * 测试一下下
 * 
 * @author jiangzeyuan
 * @version 1.0 2015-4-15 下午08:14 
 *    
 */

public class JedisClusterTest {

	/**
	 * 
	 * @param host
	 * @param port
	 * @return
	 */
	private static JedisCluster initCluster(String host, int port) {
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		
		//Jedis Cluster will attempt to discover cluster nodes automatically
		jedisClusterNodes.add(new HostAndPort(host, port));
		JedisCluster jc = new JedisCluster(jedisClusterNodes);
		return jc;
	}
	

	
	private static List<Jedis> initSingle(String host, int beginPort, int endPort) {
		
		List<Jedis> list = new ArrayList<Jedis>();
		for (int i = beginPort; i <= endPort; i++) {
			Jedis jedis = new Jedis(host, i);
			list.add(jedis);
		}
		
		return list;
		
	}
	
	public static void main(String[] args) {
		JedisCluster jc = initCluster("192.168.171.120", 30021);
		Map<String, JedisPool> nodes = jc.getClusterNodes();
		for (String key : nodes.keySet()) {
			System.out.println(key + " " + nodes.get(key));
		}
		

		jc.close();
		
		List<Jedis> jedislist = initSingle("192.168.171.120", 30024, 30026);
		for (Jedis jedis : jedislist){
			System.out.println("=====================");
			System.out.println(jedis.info());
		}
		
		
	}
}
