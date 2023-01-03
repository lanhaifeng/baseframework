package com.feng.baseframework.mail;

import com.feng.baseframework.constant.MailServerTypeEnum;
import com.feng.baseframework.model.MailMessage;
import com.feng.baseframework.model.MailServer;
import com.feng.baseframework.util.ExchangeWebServiceUtil;
import org.junit.Test;

public class ExchangeWebServiceUtilTest {

	@Test
	public void sendTextMail() throws Exception {
		ExchangeWebServiceUtil sender = new ExchangeWebServiceUtil();
		MailServer mailServer = null;
		MailMessage mailMessageInfo = null;

		mailServer = MailServer.builder()
				.mailServerType(MailServerTypeEnum.EXCHANGE.toString())
				.isExchange(true).useSSL(true)
				.host("https://s.outlook.com/ews/Exchange.asmx").port(80).senderMail("lanhaifeng1991@outlook.com")
				.auth(false).senderPassword("l13635881608/").domain("qq.com").build();//edxzjksaqtyibfji wwuvhyhxferybfbf
		mailMessageInfo = MailMessage.builder()
				.subject("Test Email").content("this is a test Text mail").build().addTargetMail("lanhaifeng@mchz.com.cn");

		sender.sendTextMail(mailServer, mailMessageInfo);
	}
}