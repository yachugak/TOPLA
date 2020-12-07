package com.yachugak.topla.util;

import javax.mail.internet.MimeMessage;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.yachugak.topla.exception.GeneralExceptions;
import com.yachugak.topla.exception.InvalidArgumentException;

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

	public String createTempPasswordTitle() {
		return "TOPLA 임시 비밀번호 안내 메일입니다.";
	}

	public String createTempPasswordContent(String email, String randomCode) {
		String content = "<!DOCTYPE html>\r\n" + 
				"<head></head>\r\n" + 
				"<body>\r\n" + 
				"    <table dir=\"ltr\">\r\n" + 
				"        <tbody><tr><td style=\"padding:0; font-family:'Malgun Gothic', Gulim, Verdana, Tahoma, sans-serif; font-size:17px; color:#707070;\">TOPLA 계정</td></tr>\r\n" + 
				"        <tr><td style=\"padding:0; font-family:'Malgun Gothic', Gulim, Verdana, Tahoma, sans-serif; font-size:41px; color:#2672ec;\">임시 비밀번호</td></tr>\r\n" + 
				"        <tr><td style=\"padding:0; padding-top:25px; font-family:'Malgun Gothic', Gulim, Verdana, Tahoma, sans-serif; font-size:14px; color:#2a2a2a;\">\r\n" + 
				"                  \r\n" + 
				"                  TOPLA 계정 <a class=\"link\" dir=\"ltr\" style=\"color:#2672ec; text-decoration:none\" rel=\"noreferrer noopener\" target=\"_blank\">"+String.valueOf(email)+"</a>에 대해 다음 임시 비밀번호를 사용하세요.\r\n" + 
				"              </td></tr>\r\n" + 
				"        <tr><td style=\"padding:0; padding-top:25px; font-family:'Malgun Gothic', Gulim, Verdana, Tahoma, sans-serif; font-size:14px; color:#2a2a2a;\">\r\n" + 
				"                  \r\n" + 
				"                  임시 비밀번호: <span style=\"font-family:'Malgun Gothic', Gulim, Verdana, Tahoma, sans-serif; font-size:14px; font-weight:bold; color:#2a2a2a;\">"+String.valueOf(randomCode)+"</span>\r\n" + 
				"              </td></tr>\r\n" + 
				"        <tr><td style=\"padding:0; padding-top:25px; font-family:'Malgun Gothic', Gulim, Verdana, Tahoma, sans-serif; font-size:14px; color:#2a2a2a;\">감사합니다.</td></tr>\r\n" + 
				"        <tr><td style=\"padding:0; font-family:'Malgun Gothic', Gulim, Verdana, Tahoma, sans-serif; font-size:14px; color:#2a2a2a;\">TOPLA 계정 팀 드림</td></tr>\r\n" + 
				"  </tbody></table>\r\n" + 
				"</body>\r\n" + 
				"</html>";
		return content;
	}
}
