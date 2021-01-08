package com.feng.baseframework.util;

import org.apache.poi.ss.formula.functions.T;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 2019/04/02 16:11
 * 验证工具类
 *
 * @author lanhaifeng
 * @since
 **/
public class ValidateUtils {
	private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

	public static <T> List<String> validate(T t) {
		return validate(t, null);
	}

	public static <T> List<String> validate(T t, Class groupCls) {
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<T>> constraintViolations = null;
		if(Objects.nonNull(groupCls)){
			constraintViolations = validator.validate(t, groupCls);
		}
		if(Objects.isNull(groupCls)){
			constraintViolations = validator.validate(t);
		}
		constraintViolations = Optional.ofNullable(constraintViolations).orElse(new HashSet<>());

		List<String> messageList = new ArrayList<>();
		for (ConstraintViolation<T> constraintViolation : constraintViolations) {
			messageList.add(constraintViolation.getMessage());
		}
		return messageList;
	}

	public boolean validateResult(T t){
		return !validate(t).isEmpty();
	}

	public boolean validateResult(T t, Class groupCls){
		return !validate(t, groupCls).isEmpty();
	}


	public static Boolean validateRegex(String regex, String val){
		return match(regex, val);
	}

	public static Boolean validateString(String val){
		return validateRegex(V_STRING, val);
	}

	/**
	 * @param regex 正则表达式字符串
	 * @param str 要匹配的字符串
	 * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	 */
	private static boolean match(String regex, String str)
	{
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	//中英文、数字、-、_、.
	public static final String V_STRING = "^(//s&;&;[^//f//n//r//t//v])*|([A-Za-z-_.0-9\\u4E00-\\u9FA5]+)$";
	//手机
	public static final String V_MOBILE="^(1)[0-9]{10}$";
	//邮件
	public static final String V_EMAIL="^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";

	public static final String V_IP_DOMAIN="^((([\\da-fA-F]{1,4}:){7}[\\da-fA-F]{1,4})|(((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)))|([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}$";

}
