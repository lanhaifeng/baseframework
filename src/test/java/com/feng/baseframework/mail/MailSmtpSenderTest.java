package com.feng.baseframework.mail;

import com.feng.baseframework.constant.MailServerTypeEnum;
import com.feng.baseframework.model.MailMessage;
import com.feng.baseframework.model.MailServer;
import com.feng.baseframework.util.MailSmtpSender;
import org.junit.Test;

public class MailSmtpSenderTest {

	@Test
	public void sendTextMail() throws Exception {
		MailSmtpSender sender = new MailSmtpSender();

		sender.sendTextMail(buildSmtpMailServer(), buildMailMessage());

		//sender.sendTextMail(buildExchangeMailServer(), buildMailMessage());
	}

	private static MailServer buildExchangeMailServer(){
		return MailServer.builder()
				.mailServerType(MailServerTypeEnum.EXCHANGE.toString())
				.isExchange(true).useSSL(false)
				.host("s.outlook.com").port(25).senderMail("lanhaifeng1991@outlook.com")
				.auth(true).senderPassword("test").domain("").build();
	}

	private static MailServer buildSmtpMailServer(){
		return MailServer.builder()
				.mailServerType(MailServerTypeEnum.SMTP.toString())
				.useSSL(false)
				.host("smtp.qq.com").port(25).senderMail("758764630@qq.com")
				.auth(true).senderPassword("test").domain("").build();
	}

	private static MailMessage buildMailMessage(){
		return MailMessage.builder()
				.subject("Test Email").content("this is a test Text mail").build().addTargetMail("6@qq.com");
	}
}