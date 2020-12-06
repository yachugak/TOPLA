package com.yachugak.topla;


import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.yachugak.topla.util.SendMail;

public class MailTest {

	@Test
	@Disabled
	public void mailTest() throws Exception {
		String xmlConfigPath = "classpath:MailSender.xml";

		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext(xmlConfigPath);
		
		JavaMailSenderImpl mailSender = (JavaMailSenderImpl)ctx.getBean("mailSender");
		
		ctx.close();
		// 메일 제목, 내용
				String subject = "제목입니당";
				String content = "<h1>내용입니당~</h1><br>굳";
				
				// 보내는 사람
				String from = "topla@syunasoft.com";
				
				// 받는 사람
				String[] to = new String[1];
				to[0] = "dnrlalth@gmail.com";
				
				try {
					// 메일 내용 넣을 객체와, 이를 도와주는 Helper 객체 생성
					MimeMessage mail = mailSender.createMimeMessage();
					MimeMessageHelper mailHelper = new MimeMessageHelper(mail, "UTF-8");

					// 메일 내용을 채워줌
					mailHelper.setFrom(from);	// 보내는 사람 셋팅
					mailHelper.setTo(to);		// 받는 사람 셋팅
					mailHelper.setSubject(subject);	// 제목 셋팅
					mailHelper.setText(content, true);	// 내용 셋팅

					// 메일 전송
					mailSender.send(mail);
					
				} catch(Exception e) {
					e.printStackTrace();
					throw e;
				}
	}
	
	@Test
	public void sendMail() throws Exception {
		SendMail test = new SendMail();
		test.mailTest("romakk@gmail.com", "제목이다", "축하해 야근이야", false);
	}
}