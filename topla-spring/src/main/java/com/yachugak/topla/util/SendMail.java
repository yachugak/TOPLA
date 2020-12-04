package com.yachugak.topla.util;

import javax.mail.internet.MimeMessage;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class SendMail {
	public void mailTest(String mailReciver, String subject, String content, boolean HTMLFlag) throws Exception {
		String xmlConfigPath = "classpath:MailSender.xml";

		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext(xmlConfigPath);
		
		JavaMailSenderImpl mailSender = (JavaMailSenderImpl)ctx.getBean("mailSender");
		
		ctx.close();
				// 보내는 사람
				String from = "topla@syunasoft.com";
								
				try {
					// 메일 내용 넣을 객체와, 이를 도와주는 Helper 객체 생성
					MimeMessage mail = mailSender.createMimeMessage();
					MimeMessageHelper mailHelper = new MimeMessageHelper(mail, "UTF-8");

					// 메일 내용을 채워줌
					mailHelper.setFrom(from);	// 보내는 사람 셋팅
					mailHelper.setTo(mailReciver);		// 받는 사람 셋팅
					mailHelper.setSubject(subject);	// 제목 셋팅
					mailHelper.setText(content, HTMLFlag);	// 내용 셋팅

					// 메일 전송
					mailSender.send(mail);
					
				} catch(Exception e) {
					e.printStackTrace();
					throw e;
				}
	}
}
