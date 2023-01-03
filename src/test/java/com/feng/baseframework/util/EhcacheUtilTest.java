package com.feng.baseframework.util;

import com.feng.baseframework.common.MockitoBaseTest;
import io.jsonwebtoken.lang.Assert;
import org.junit.Test;

public class EhcacheUtilTest extends MockitoBaseTest {

	@Test
	public void put() {
		EhcacheUtil.getInstance().putEternal(EhcacheUtil.cacheName, "test", "test");
		Assert.state("test".equals(EhcacheUtil.getInstance().get(EhcacheUtil.cacheName, "test")));
		try {
			Thread.sleep(20000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Assert.state("test".equals(EhcacheUtil.getInstance().get(EhcacheUtil.cacheName, "test")));
	}

	@Test
	public void putEternalTest() {
		Assert.state("test".equals(EhcacheUtil.getInstance().get(EhcacheUtil.cacheName, "test")));
	}
}