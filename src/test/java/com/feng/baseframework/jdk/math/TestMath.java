package com.feng.baseframework.jdk.math;

import org.junit.Test;

/**
 * baseframework
 * 2019/6/18 9:46
 * 测试math包
 *
 * @author lanhaifeng
 * @since
 **/
public class TestMath {

	@Test
	public void testMath(){
		Long[] counts = new Long[]{31258111l,72878864l,72617808l,72477239l,520543l,260286l};
		Long[] sizes = new Long[]{4096l,4096l,4096l,4096l,65536l,65536l};
		for (int i=0; i<counts.length;i++) {
			System.out.println(counts[i]*sizes[i]/1024.0/1024.0/1024.0);
		}
	}
}
