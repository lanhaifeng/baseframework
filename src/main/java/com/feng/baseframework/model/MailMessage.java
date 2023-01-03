package com.feng.baseframework.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.mail.internet.InternetAddress;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * mail-spring-boot
 * 2019/5/10 15:01
 * 邮件实体
 *
 * @author lanhaifeng
 * @since
 **/
@Getter
@Setter
@Slf4j
@Builder
public class MailMessage implements Serializable {

	private static final long serialVersionUID = 3896050955948781939L;

	private String subject;
	private String content;
	private List<String> targetMails;
	private List<String> files;
	private List<MailImage> mailImages;
	private List<String> forwardMails;

	public MailMessage addTargetMail(String targetMail){
		if(targetMails == null){
			targetMails = new ArrayList<>();
		}
		if(StringUtils.isNotBlank(targetMail)){
			targetMails.add(targetMail);
		}

		return this;
	}

	public InternetAddress[] getTargetAddress() {
		InternetAddress[] addresses = null;
		if(targetMails != null && !targetMails.isEmpty()){
			HashSet<String> h = new HashSet<>(targetMails);
			targetMails.clear();
			targetMails.addAll(h);

			addresses = new InternetAddress[targetMails.size()];
			for (int i = 0; i < targetMails.size(); i++) {
				if(StringUtils.isNotBlank(targetMails.get(i))){
					try{
						addresses[i] = new InternetAddress(targetMails.get(i));
					}catch(Exception e){
					    log.error("构造邮件接收方异常，错误：" + ExceptionUtils.getFullStackTrace(e));
					}
				}
			}
		}

		return addresses;
	}
}

@Setter
@Getter
class MailImage{
	private String file;
	private String contentId;
}