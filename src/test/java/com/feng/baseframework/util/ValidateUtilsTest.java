package com.feng.baseframework.util;

import com.feng.baseframework.constant.LdapPropertyConfig;
import org.junit.Test;

import static org.junit.Assert.*;

public class ValidateUtilsTest {

	@Test
	public void validateMessages() {
		System.out.println(ValidateUtils.validateMessages(new LdapPropertyConfig()));
	}
}