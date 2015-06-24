package com.jzy.jedis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

public class GroupCommitTest {

	@Test
	//@Ignore
	public void singleCommitVSGroupCommit() throws Exception {
		JedisUtil.init();

		FileOutputStream fos = new FileOutputStream(new File("results\\SingleCommitVSGroupCommit2.dat"));

		
		//仅仅使用一个节点测试
		String testStr = "1348:134:2:11448:JkbEtuifboYHqIKC9lZ";
		String testStr2 = "1348:134:2:11448:JkbEtuifboYHqIKC9lZ3GJ8bjg8G5WnE7MU4pUCrv90V8xAit8"
				+ "1348:134:2:11448:JkbEtuifboYHqIKC9lZ3GJ8bjg8G5WnE7MU4pUCrv90V8xAit8"
				+ "1348:134:2:11448:JkbEtuifboYHqIKC9lZ3GJ8bjg8G5WnE7MU4pUCrv90V8xAit8"
				+ "1348:134:2:11448:JkbEtuifboYHqIKC9lZ3GJ8bjg8G5WnE7MU4pUCrv90V8xAit8";
		for (int i = 1; i < 1000; i = i+20) {
			//单次提交
			long singleStart = System.currentTimeMillis();
			for (int j = 0; j < i; ++j) {
				JedisUtil.singleRpush(0, "singleCommit" + i, testStr);
			}
			long singleEnd = System.currentTimeMillis();
			//System.out.println("sCommit" + i + ": \t" + (singleEnd - singleStart));
			long time = singleEnd - singleStart;
			fos.write((Integer.toString(i)).getBytes());
			fos.write("\t".getBytes());
			fos.write( (Long.toString(time)).getBytes());
			fos.write("\t".getBytes());
			//fos1.write("\r\n".getBytes());
			
			//组提交
			String[] testStrs = new String[i];
			for (int j = 0; j < i; ++j) {
				testStrs[j] = testStr;
			}
			long groupStart = System.currentTimeMillis();
			JedisUtil.singleRpush(0,  "groupCommit" + i, testStrs);
			long groupEnd = System.currentTimeMillis();
			//System.out.println("gCommit" + i + ": \t" + (groupEnd - groupStart));
			long time2 = groupEnd - groupStart;
			fos.write( (Long.toString(time2)).getBytes());
			fos.write("\r\n".getBytes());
		}
		
		fos.close();

		//求一个总的
	}

}
