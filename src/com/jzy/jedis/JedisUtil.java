package com.jzy.jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.util.JedisClusterCRC16;

public class JedisUtil {
	
	public static JedisCluster jc = null;
	public static List<Jedis> clusterNodes = new ArrayList<Jedis>();
	public static List<Jedis> singleNodes = new ArrayList<Jedis>();
	public static Map<String, Integer> hashTable = new HashMap<String, Integer>();
	
	private static Random rand = new Random(1000);
	
//	/*
//	 * 不推荐用以下几个get方法，因为复制了连接，导致每个redis实例的连接数增加，这并不是我们想看到的
//	 */
//	public static JedisCluster getJc() {
//		return jc;
//	}
//
//	public static List<Jedis> getLj() {
//		return singleNodes;
//	}
//	
//	public static Map<String, Integer> getHashTable() {
//		return hashTable;
//	}
	
	/**
	 * 静态初始化方法
	 */
	public static void init(){
		
		/**
		 * 初始化cluster
		 */
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		jedisClusterNodes.add(new HostAndPort(Parameters.HOST, Parameters.CLUSTER_PORT));
		jc = new JedisCluster(jedisClusterNodes);
		for(JedisPool jp : jc.getClusterNodes().values()) {
			//让Cluster始终保持着全局的连接，以免过多的连接打开、关闭。这一点很重要，因为redis是限制连接数目的
			clusterNodes.add(jp.getResource());
			
		}
		flushCluster();

		/**
		 * 初始化single
		 */
		for (int i = 0; i < Parameters.SINGLE_LEN; i++) {
			Jedis jedis = new Jedis(Parameters.HOST, Parameters.SINGLE_BEGIN_PORT + i);
			jedis.flushDB();
			singleNodes.add(jedis);
		}
		
		/**
		 * 清空hashTable
		 */
		hashTable.clear();
		
	}
		
	
	public static void singleSet(int num, String key, String value) {
		singleNodes.get(num).set(key, value);
	}
	
	public static String singleGet (int num, String key) {
		String res = singleNodes.get(num).get(key);
		if ( res != null) {
			return res;
		}
		return null;
	}
	
	public static void singleRpush(int num, String key, String value) {
		singleNodes.get(num).rpush(key, value);
	}
	
	public static void singleRpush(int num, String key, String[] values) {
		singleNodes.get(num).rpush(key, values);
	}
	
	public static List<String> singleLrange(int num, String key) {
		return singleNodes.get(num).lrange(key, 0, -1);
		
	}
	
	public static void singleHmset(int num, String key, Map<String, String> hash) {
		singleNodes.get(num).hmset(key, hash);
	}
	
	public static void singleSave(int num) {
		singleNodes.get(num).save();
	}

	
	/**
	 * 返回一个key的targetSite
	 * @param String
	 * @return
	 */
	public static int targetSite(String key) {
		int slotsPerSite = JedisCluster.HASHSLOTS / Parameters.CLUSTER_LEN; //每个节点负责多少个槽slot	
		int slot = JedisClusterCRC16.getCRC16(key) % JedisCluster.HASHSLOTS; //i这个key在哪个槽里
		return slot / slotsPerSite;
	}
	
	
	/**
	 * 关闭连接
	 */
	public static void destroy() {
		if (jc != null) {
			jc.close();
		}
		if (singleNodes.size() > 0 ) {
			for (Jedis jedis : singleNodes) {
				jedis.close();
			}
		}
	}
	
	
	/**
	 * 构造方法为private
	 */
	private JedisUtil(){}
	
	/**
	 * 清空cluster
	 */
	public static void flushCluster() {
		for (int i = 0; i < clusterNodes.size(); i++) {
			clusterNodes.get(i).flushDB();
		}
	}
	
	public static boolean clusterIsEmpty() {
		for (int i = 0; i < clusterNodes.size(); i++) {
			if (clusterNodes.get(i).dbSize() > 0) return false;
		}
		return true;
	}
	
	/**
	 * 已经废弃
	 * 将cluster中的所有节点单独拿出来
	 * @return 节点的list
	 */
	public static List<Jedis> getAllJedisFromCluster() {
		List<Jedis> list = new ArrayList<Jedis>();
		for(JedisPool jp : jc.getClusterNodes().values()) {
			Jedis jc = jp.getResource();
			list.add(jc);
		}
		return list;
	}
	
	/**
	 * 将cluster中指定的节点单独拿出来
	 * 注意：jc所提供出来的Map<String, JedisPool>并不是按照端口顺序输出的
	 * @param witch 1, 2, 3...表示第几个节点（从连接的那个端口开始算）
	 * @return
	 * @throws Exception 超出了节点数量范围
	 */
	public static Jedis getJedisFromCluster(int which) throws Exception {
		if (which > Parameters.CLUSTER_LEN) {
			throw new Exception("no such node");
		}
		
		int targetPort = Parameters.CLUSTER_PORT + which - 1; //好弱的设计。。
		
		Map<String, JedisPool> map = jc.getClusterNodes();
		for (Entry<String, JedisPool> entry : map.entrySet()){
			String[] split = entry.getKey().split(":"); //e.g 192.168.171.120:30021
			if (targetPort == Integer.parseInt(split[split.length - 1])) {//如果冒号后的那个端口号和targetPort相同
				return entry.getValue().getResource();
			}
		}
		return null;
	}
	

	
	/**
	 * 将空的数据库中装入数据元素，以备某些环境可以用
	 * 放入的key是0, 1, 2... value与key相同
	 */
	public static void clusterDataInit(int numPerSite) {
		if (!clusterIsEmpty()) {
			System.out.println("cluster is not empty");
		} else {
			int slotsPerSite = JedisCluster.HASHSLOTS / Parameters.CLUSTER_LEN; //每个节点负责多少个槽slot
			for (long i = 0; i < Parameters.CLUSTER_LEN * numPerSite; i++) {
				int slot = JedisClusterCRC16.getCRC16(Long.toString(i)) % JedisCluster.HASHSLOTS; //i这个key在哪个槽里
				int targetSite = slot / slotsPerSite + 1;	//i这个key在哪个site里（site的标号从1开始）

				hashTable.put(Long.toString(i), targetSite);//在hashTable中写入key对应的site号，用于判断在哪个节点上写日志
				jc.set(Long.toString(i), Long.toString(i));	//存入<"123", "123">这种键值对
			}
		}
	}

	/**
	 * 生成一个随机的value值，这个值的长度为n个字符
	 * @return 字符串
	 */
	public static String randomString(int n) {
		
		
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        for (int i = 0; i < n; i++) {
        	int num = rand.nextInt(62);
			res += str.charAt(num);
		}
        return res;
	}


}
