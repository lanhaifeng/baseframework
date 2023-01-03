1. ValidateUtils
2. 扩展validation-api注解，解决不同情形校验：如集合和自定义方法
3. 结合AOP，实现service参数列表校验
4. validation框架支持嵌套验证，即验证对象中的对象   
    除了外层使用@Valid或@Validated，还是需要在属性上加上@Valid注解，见[@Validated和@Valid区别](wiz://open_document?guid=1de1f05e-bf29-488f-a18f-3762a151bb49&kbguid=&private_kbguid=20266744-3f73-4d15-9eac-279901c14de8)
5. validation框架注解支持在方法上使用，但是只能用于getter方法上才起作用


#### 自定义验证注解
1. 声明注解
```java
@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
//指明验证处理器IPV4Validator
@Constraint(validatedBy = IP.IPV4Validator.class)
public @interface IP {

	enum IpType {IPV4,IPV6,IP}

	IpType type() default IpType.IPV4;

	String message() default "IP地址不合法";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
```

2. 验证实现
```java
class IPV4Validator implements ConstraintValidator<IP, String> {

		//IPV4
		public static final String IPV4 = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$";
		//IPV6
		public static final String IPV6 = "^\\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%.+)?\\s*$";

		private IP ipAnnotation;

		@Override
		public void initialize(IP ipAnnotation) {
			this.ipAnnotation = ipAnnotation;
		}

		@Override
		public boolean isValid(String ip, ConstraintValidatorContext constraintValidatorContext) {
			if (Objects.isNull(ipAnnotation)) {
				return false;
			}
			if(ip == null || "".equals(ip) || "".equals(ip.trim())){
				return true;
			}
			switch (ipAnnotation.type()) {
				case IPV4:
					return isIPV4(ip);
				case IPV6:
					return isIPV6(ip);
				case IP:
					return isIPV4(ip) || isIPV6(ip);
			}
			return false;
		}

		private boolean isIPV4(String ip) {
			return Pattern.matches(IPV4, Optional.ofNullable(ip).orElse(""));
		}

		private boolean isIPV6(String ip) {
			return Pattern.matches(IPV6, Optional.ofNullable(ip).orElse(""));
		}
}
```