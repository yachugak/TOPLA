package com.yachugak.topla.service;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yachugak.topla.entity.SchedulePreset;
import com.yachugak.topla.entity.User;
import com.yachugak.topla.exception.DuplicatedException;
import com.yachugak.topla.exception.InvalidArgumentException;
import com.yachugak.topla.repository.PresetRepository;
import com.yachugak.topla.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PresetRepository presetRepository;
	
	public User findUserById(long uid) {
		return userRepository.findById(uid).get();
	}

	public void setPresetCode(User user, long presetUid) {
		SchedulePreset targetPreset = presetRepository.findById(presetUid).get();
		user.setSchedulePreset(targetPreset);
		return;	
	}
	
	public void setPresetCode(User user, SchedulePreset preset) {
		user.setSchedulePreset(preset);
	}

	public User createUser(String email, String password) {
		User newUser = new User();
		this.setEmail(newUser, email);
		this.setPassword(newUser, password);
		userRepository.saveAndFlush(newUser);
		
		return newUser;
	}
	
	public void setEmail(User user, String email) {
		if(email == null) {
			throw new InvalidArgumentException("email", "값 있음", "null");
		}
		if(email.equals("")) {
			throw new InvalidArgumentException("email", "값 있음", "빈 문자열");
		}
		
		Optional<User> searchedUser = userRepository.findByEmail(email);
		if(searchedUser.isPresent() == true) {
			throw new DuplicatedException(email);
		}
		user.setEmail(email);
	}
	
	public void setPassword(User user, String password) {
		if(password == null) {
			throw new InvalidArgumentException("password", "값 있음", "null");
		}
		if(password.equals("")) {
			throw new InvalidArgumentException("password", "값 있음", "빈 문자열");
		}
		user.setPassword(password);
	}
	
	public void setEveningReportTime(User user, OffsetTime eveningReportTime) {
		if(eveningReportTime == null) {
			throw new InvalidArgumentException("eveningReportTime", "값 있음", null);
		}
		user.setEveningReportTime(eveningReportTime);
	}
	
	public void setMorningReportTime(User user, OffsetTime morningReportTime) {
		if(morningReportTime == null) {
			throw new InvalidArgumentException("morningReportTime", "값 있음", null);
		}
		user.setMorningReportTime(morningReportTime);
	}

	public void deleteUser(User targetUser) {
		userRepository.delete(targetUser);
	}
	
}
