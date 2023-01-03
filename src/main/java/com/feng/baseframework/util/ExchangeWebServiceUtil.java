package com.feng.baseframework.util;


import com.feng.baseframework.model.MailMessage;
import com.feng.baseframework.model.MailServer;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * baseframework
 * 2019/5/13 11:02
 * ews发送邮件
 *
 * @author lanhaifeng
 * @since
 **/
public class ExchangeWebServiceUtil {

	private ExchangeService getExchangeService(String mailServer, String user, String password, String domain) {
		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2007_SP1);
		//用户认证信息
		ExchangeCredentials credentials;
		if (domain == null) {
			credentials = new WebCredentials(user, password);
		} else {
			credentials = new WebCredentials(user, password, domain);
		}
		service.setCredentials(credentials);
		try {
			service.setUrl(new URI(mailServer));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return service;
	}

	public void sendTextMail(MailServer mailServer, MailMessage mailMessageInfo) throws Exception {
		ExchangeService service = getExchangeService(mailServer.getHost(), mailServer.getSenderMail(), mailServer.getSenderPassword(), mailServer.getDomain());

		EmailMessage msg = new EmailMessage(service);
		msg.setSubject(mailMessageInfo.getSubject());
		MessageBody body = MessageBody.getMessageBodyFromText(mailMessageInfo.getContent());
		body.setBodyType(BodyType.Text);
		msg.setBody(body);
		for (String toPerson : mailMessageInfo.getTargetMails()) {
			msg.getToRecipients().add(toPerson);
		}
		if (mailMessageInfo.getForwardMails() != null && !mailMessageInfo.getForwardMails().isEmpty()) {
			for (String ccPerson : mailMessageInfo.getForwardMails()) {
				msg.getCcRecipients().add(ccPerson);
			}
		}
		if (mailMessageInfo.getFiles() != null) {
			for (String attachmentPath : mailMessageInfo.getFiles()) {
				msg.getAttachments().addFileAttachment(attachmentPath);
			}
		}
		msg.send();
	}
}
