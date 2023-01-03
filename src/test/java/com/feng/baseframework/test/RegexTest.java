package com.feng.baseframework.test;

import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * baseframework
 * 2018/12/11 16:04
 * 正则表达式测试
 *
 * @author lanhaifeng
 * @since
 **/
public class RegexTest {

	@Test
	public void test() {
		String content = "01";
		String pattern = "(-1|0|1|13)";
		boolean isMatch = Pattern.matches(pattern, content);
		System.out.println(isMatch);

		pattern = "([^<>]+|(\\s)?)";
		content = "^";
		isMatch = Pattern.matches(pattern, content);
		System.out.println(isMatch);

        pattern = "\\S\\^";
        content = "b^";
        isMatch = Pattern.matches(pattern, content);
        System.out.println(isMatch);

		pattern = "^(//s&;&;[^//f//n//r//t//v])*|([A-Za-z-_.0-9\\u4E00-\\u9FA5]+)$";
		content = " !";
		isMatch = Pattern.matches(pattern, content);
		System.out.println(isMatch);
	}

	@Test
	public void replaceAll(){
		String value = "<script>alert(1)</script>";
		System.out.println(StringEscapeUtils.escapeHtml(value));
		value = value.replaceAll("eval\\((.*)\\)", "");
		value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		value = value.replaceAll("(?i)<script.*?>.*?<script.*?>", "");
		value = value.replaceAll("(?i)<script.*?>.*?</script.*?>", "");
		value = value.replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", "");
		value = value.replaceAll("(?i)<.*?\\s+on.*?>.*?</.*?>", "");
		System.out.println(value);
		System.out.println("review:*".replace("review:*", "review:y"));
	}

	@Test
	public void regexUrl() {
		String content = "http://192.168.230.206/static/rest/media/favicon";
		String patternStr = "^http:.*/.*(/[^/]+)$";
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(content);
		if(matcher.find()){
			System.out.println(matcher.group(0));
			System.out.println(matcher.group(1));
		}
	}
}
