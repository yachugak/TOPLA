package com.yachugak.topla;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.yachugak.topla.entity.User;
import com.yachugak.topla.repository.UserRepository;
import com.yachugak.topla.service.UserService;

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
}



