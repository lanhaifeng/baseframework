package com.feng.baseframework.model;

import com.feng.baseframework.constant.MailServerTypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Properties;

/**
 * mail-spring-boot
 * 2019/5/10 11:22
 * mail服务器领域
 *
 * @author lanhaifeng
 * @since
 **/
@Getter
@Setter
@Builder
public class MailServer implements Serializable {

	private static final long serialVersionUID = 216216244807510482L;
	private Boolean active;
	private String mailServerType;
	private String host;
	private int port;
	private String senderMail;
	private String senderPassword;
	private String domain;
	private boolean auth;
	private boolean useSSL;
	private boolean isExchange;
	private String protocol;

	public Properties buildMailInfo(){
		protocol  = "smtp";
		if(useSSL && !isExchange){
			protocol  = "smtps";
		}
		if(isExchange){
		    protocol = "jbexTransport";
        }
		MailServerTypeEnum serverType = MailServerTypeEnum.getEnum(mailServerType);
		switch (serverType){
			case SMTP:
				return buildSmtpMailProperties();
			case EXCHANGE:
				return buildExchangeMailProperties();

		}
		return null;
	}

	private Properties buildSmtpMailProperties(){
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.host", this.host);
		props.setProperty("mail.smtp.starttls.enable", String.valueOf(this.useSSL));
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		if(this.port > 0){
			props.setProperty("mail.smtp.port", String.valueOf(this.port));
			props.setProperty("mail.smtp.socketFactory.port", String.valueOf(this.port));
		}
		if(this.auth){
			if(this.useSSL){
				props.setProperty("mail.smtps.auth", String.valueOf(this.auth));
			}else {
				props.setProperty("mail.smtp.auth", String.valueOf(this.auth));
			}
		}

		props.put("mail.smtp.ssl", String.valueOf(this.useSSL));

		System.out.println(props.toString());
		return props;
	}

	private Properties buildExchangeMailProperties(){
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.host", this.host);
		props.setProperty("mail.smtp.starttls.enable", String.valueOf(this.useSSL));
		props.setProperty("mail.smtp.ssl", String.valueOf(this.useSSL));

		if(useSSL){
			props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.smtp.socketFactory.port", String.valueOf(this.port));
		}

		if(this.port > 0){
			props.setProperty("mail.smtp.port", String.valueOf(this.port));
		}
		if(this.auth){
			if(this.useSSL){
				props.setProperty("mail.smtps.auth", String.valueOf(this.auth));
			}else {
				props.setProperty("mail.smtp.auth", String.valueOf(this.auth));
			}
		}

		if(this.isExchange){
			props.setProperty("mail.smtp.socketFactory.fallback", "false");
			if(StringUtils.isNotBlank(domain)){
				props.setProperty("mail.smtp.auth.ntlm.domain", domain);
			}
		}

		System.out.println(props.toString());
		return props;
	}
}
