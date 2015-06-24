package com.jzy.jedis;

import java.util.Random;

public class GroupCommitMain {
	
	public int entryPerBlock;
	public int blockPerBuffer;
	public int bufferLength; //entryPerBlock * blockPerBuffer
	public int threshold;
	
	private Random rand = new Random(1234);
	
	public void dictionaryEncoding() {
		
	}
	
	
	
	
	public static void main(String[] args) {

		
		JedisUtil.init();

	}
}
