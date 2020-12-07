package com.yachugak.topla.util;

import javax.mail.internet.MimeMessage;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.yachugak.topla.exception.GeneralExceptions;

public class Mail {
	public void sendMail(String mailReciver, String title, String content, boolean HTMLFlag) {
		String xmlConfigPath = "classpath:MailSender.xml";

		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext(xmlConfigPath);

		JavaMailSenderImpl mailSender = (JavaMailSenderImpl) ctx.getBean("mailSender");

		ctx.close();
		// 보내는 사람
		String from = "topla@syunasoft.com";

		try {
			// 메일 내용 넣을 객체와, 이를 도와주는 Helper 객체 생성
			MimeMessage mail = mailSender.createMimeMessage();
			MimeMessageHelper mailHelper = new MimeMessageHelper(mail, "UTF-8");

			// 메일 내용을 채워줌
			mailHelper.setFrom(from); // 보내는 사람 셋팅
			mailHelper.setTo(mailReciver); // 받는 사람 셋팅
			mailHelper.setSubject(title); // 제목 셋팅
			mailHelper.setText(content, HTMLFlag); // 내용 셋팅

			// 메일 전송
			mailSender.send(mail);

		} catch (Exception e) {
			throw new GeneralExceptions("이메일 전송 중에 문제가 생겼습니다.");
		}
	}
	
	public String createTempUserTitle() {
		return "TOPLA 회원가입 인증 메일입니다.";
	}
	
	public String createTempUserContent(int secureCode) {
		return "<p><span style=\"font-size: 12pt;\">안녕하세요. "
				+ "<span style=\"color: rgb(239, 0, 124);\"><b>TOPLA</b></span>입니다.</span></p><p>"
				+ "<span style=\"font-size: 12pt;\">당신의 이메일인증 코드는</span></p><p>"
				+ "<span style=\"font-size: 24pt; color: rgb(255, 0, 0);\">"
				+ String.valueOf(secureCode)
				+ "</span></p>"
				+ "<p><span style=\"font-size: 12pt;\">입니다.</span></p>"
				+ "<p><span style=\"font-size: 12pt;\">코드는 하루동안 유효합니다.</span></p"
				+ "><p><span style=\"font-size: 12pt;\">반드시 하루 이내에 입력해주시기 바랍니다.</span></p>";
	}
}
