package com.yachugak.topla;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.yachugak.topla.entity.User;
import com.yachugak.topla.repository.UserRepository;
import com.yachugak.topla.service.UserService;
import com.yachugak.topla.entity.TemporaryUser;

@SpringBootTest
public class UserTest {
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	
	@Test
	@Transactional(readOnly = false)
	public void updateUserDeviceToken() {	
		User newUser = userService.createUser("이메일입니다", "비번입니다");
		newUser.setDeviceToken("새로운디바이스토큰입니다");	
		userRepository.saveAndFlush(newUser);
		
		User targetUser = userService.findUserByEmail("이메일입니다");
		assertEquals(targetUser.getDeviceToken(), "새로운디바이스토큰입니다");
	}
	
	@Test
	public void randomCode() {
		System.out.println(userService.randomCode(6));
	}
	
	@Test
	@Transactional(readOnly = false)
	@Disabled
	public void createTemporaryUser() {
		try {
			userService.createTemporaryUser("dnrlalth@gmail.com");
			assertTrue(true);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			assertTrue(false);
		}
	}
	
	@Test
	@Transactional(readOnly = false)
	@Disabled
	public void createFailTemporaryUser() {
		try {
			userService.createTemporaryUser("abc@abc.abc");
			
			assertTrue(false);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			assertTrue(true);
		}
	}
	
	@Test
	@Transactional(readOnly = false)
	@Disabled
	public void createUser() {
		String email = "dnrlalth+us@gmail.com";
		String passwd = "asdf";
		int secureCode;
		
		userService.createTemporaryUser(email);
		
		secureCode = userService.findTemporaryUserByEmail(email).getSecureCode();
		
		User user = userService.createUser(email, passwd);
		userService.deleteTempUser(email);
		
		//코드는 이메일에서 직접 확인할것
		System.out.println(secureCode);
		assertEquals(email, user.getEmail());
	}
}



