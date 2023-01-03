package com.feng.baseframework.util;

import com.feng.baseframework.model.MailMessage;
import com.feng.baseframework.model.MailServer;
import com.feng.baseframework.security.MailAuthenticator;
import lombok.extern.slf4j.Slf4j;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 * mail-spring-boot
 * 2019/5/10 11:19
 * 发送smtp邮件
 *
 * @author lanhaifeng
 * @since
 **/
@Slf4j
public class MailSmtpSender {

	public void sendTextMail(MailServer mailServer, MailMessage mailMessageInfo) throws Exception{
		//TODO validate
		if(mailServer == null){
			log.error("邮件服务器信息为空");
			return;
		}

		Authenticator authenticator = null;
		if(mailServer.isAuth()){
			authenticator = new MailAuthenticator(mailServer.getSenderMail(), mailServer.getSenderPassword());
		}

		Session sendMailSession = Session.getInstance(mailServer.buildMailInfo(), authenticator);
		sendMailSession.setDebug(true);
		// 根据session创建一个邮件消息
		Message mailMessage = new MimeMessage(sendMailSession);
		// 设置邮件消息的发送者
		mailMessage.setFrom(new InternetAddress(mailServer.getSenderMail()));
		// 创建邮件的接收者地址，并设置到邮件消息中
		mailMessage.setRecipients(Message.RecipientType.TO, mailMessageInfo.getTargetAddress());
		// 设置邮件消息的主题
		mailMessage.setSubject(mailMessageInfo.getSubject());
		// 设置邮件消息发送的时间
		mailMessage.setSentDate(new Date());
		// 设置邮件消息的主要内容
		mailMessage.setText(mailMessageInfo.getContent());
		// 发送邮件
		Transport transport = sendMailSession.getTransport(mailServer.getProtocol());
		transport.connect (mailServer.getHost(), mailServer.getPort(), mailServer.getSenderMail(), mailServer.getSenderPassword());
		transport.sendMessage(mailMessage, mailMessage.getAllRecipients());
		transport.close();
	}

}
