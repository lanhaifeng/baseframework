package com.feng.baseframework.annotation.validate;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IP.IPV4Validator.class)
public @interface IP {

	enum IpType {IPV4,IPV6,IP}

	IpType type() default IpType.IPV4;

	String message() default "IP地址不合法";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

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
}
