package com.yachugak.topla;


import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.mail.internet.MimeMessage;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.transaction.annotation.Transactional;

import com.yachugak.topla.entity.User;
import com.yachugak.topla.repository.UserRepository;
import com.yachugak.topla.service.UserService;
import com.yachugak.topla.util.Mail;

@SpringBootTest
public class MailTest {
	@Autowired 
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	
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
	@Disabled
	public void sendMail() throws Exception {
		String email = "이메일을입력";
		Mail test = new Mail();
		
		test.sendMail(email, "이메일테스트제목", "본문", false);
	}
	
	@Test
	@Disabled
	@Transactional(readOnly = false)
	public void sendTemporalPasswordByEmailTest() {
		int length = 6;
		String email = "이메일을입력";
		User targetUser = userService.findUserByEmail(email);
		String randomCode = Integer.toString(userService.randomCode(length));
		userService.setPassword(targetUser, randomCode);
		userRepository.saveAndFlush(targetUser);
		
		// mail 수신은 직접 확인해봐야함.
		userService.sendTemporalPasswordByEmail(targetUser, randomCode);
		
		User updated = userService.findUserByEmail(email);
		String password = updated.getPassword();
		assertEquals(randomCode, password);
	}
}
