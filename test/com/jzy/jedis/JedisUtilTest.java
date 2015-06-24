package com.jzy.jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

public class JedisUtilTest {

//	@Test
//	@Ignore
//	public void test() {
//		JedisUtil.init();
//		JedisCluster jc = JedisUtil.getJc();
//		Map<String, JedisPool> map = jc.getClusterNodes();
//		for (Entry<String, JedisPool> entry : map.entrySet()){
//			System.out.println(entry.getKey());
//			System.out.println(entry.getValue());
//		}
//	}
//	
//	@ Test
//	public void clusterDataInitTest() {
//	
//		JedisUtil.init();
//		JedisCluster jc = JedisUtil.getJc();
//		int numPerSite = 1000;
//		JedisUtil.clusterDataInit(numPerSite);
//		for (long i = 0; i < numPerSite*Parameters.CLUSTER_LEN; ++i) {
//			int site = JedisUtil.getHashTable().get(Long.toString(i));
//			try {
//				Jedis jedis = JedisUtil.getJedisFromCluster(site);
//				System.out.println(jedis.get(Long.toString(i)));
//				jedis.close();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
//		
//		
//	}
	
	@Test
	@Ignore
	public void singleNodesRpushTest() {
		JedisUtil.init();
		JedisUtil.singleRpush(0, "test", "hello");
		System.out.println(JedisUtil.singleLrange(0, "test"));
		
	}
	
	@Test
	@Ignore
	public void singleNodesRpush2Test() {
		JedisUtil.init();
		String[] values = {"aaa", "bbb", "ccc"};
		JedisUtil.singleRpush(0, "test", values);
		List<String> res = JedisUtil.singleLrange(0, "test");
		System.out.println(res);
	}
	
	@Test
	@Ignore
	public void randomStringTest() {
		for (int i = 0; i < 20; i++) {
			System.out.println(JedisUtil.randomString(10));
		}
	}

	@Test
	@Ignore
	public void singleHmsetTest() {
		JedisUtil.init();
		Map<String, String> map = new HashMap<String, String>();
		map.put("foo1", "bar1");
		map.put("foo2", "bar2");
		JedisUtil.singleHmset(0, "test", map);
	}
	
	@Test
	public void singleSaveTest() {
		JedisUtil.init();
		String[] values = {"aaa", "bbb", "ccc"};
		JedisUtil.singleRpush(0, "test", values);
		JedisUtil.singleSave(0);
	}

}
