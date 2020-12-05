package com.yachugak.topla.service;

import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yachugak.topla.entity.SchedulePreset;
import com.yachugak.topla.entity.User;
import com.yachugak.topla.exception.DuplicatedException;
import com.yachugak.topla.exception.EntityNotFoundException;
import com.yachugak.topla.exception.GeneralExceptions;
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
	
	public User findUserByEmail(String email) {
		if(email == null) {
			throw new InvalidArgumentException("email", "null값이 아닌 String", email+"");
		}
		if(email.isBlank()) {
			throw new InvalidArgumentException("email", "빈 값이 아닌 String", email+"");
		}
		
		User targetUser = userRepository.findByEmail(email).get();
		if(targetUser == null) {
			throw new javax.persistence.EntityNotFoundException("해당 유저가 존재하지 않음.");
		}
		
		return targetUser;
	}

	public void setSelectedPreset(User user, long presetUid) {
		SchedulePreset targetPreset = presetRepository.findById(presetUid).get();
		user.setSchedulePreset(targetPreset);
		return;	
	}
	
	public void setSelectedPreset(User user, SchedulePreset preset) {
		if(preset == null) {
			throw new InvalidArgumentException("preset", "preset 값", preset+"");
		}
		user.setSchedulePreset(preset);
	}

	public User createUser(String email, String password) {
		User newUser = new User();
		this.setEmail(newUser, email);
		this.setPassword(newUser, password);
		OffsetTime morningOffsetTime = OffsetTime.of(0, 0, 0, 0, ZoneOffset.UTC);
		OffsetTime eveningOffsetTime = OffsetTime.of(21, 0, 0, 0, ZoneOffset.UTC);
		this.setMorningReportTime(newUser, morningOffsetTime);
		this.setEveningReportTime(newUser, eveningOffsetTime);
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
			return;
		}
		user.setEveningReportTime(eveningReportTime);
	}
	
	public void setMorningReportTime(User user, OffsetTime morningReportTime) {
		if(morningReportTime == null) {
			return;
		}
		user.setMorningReportTime(morningReportTime);
	}

	public void deleteUser(User targetUser) {
		userRepository.delete(targetUser);
	}

	public void setDeviceToken(User targetUser, String deviceToken) {
		targetUser.setDeviceToken(deviceToken);
	}
	
	public Double getLossPriority(User targetUser) {
		return targetUser.getTotalLossPriority();
	}

	public User userLogin(String email, String password) {
		Optional<User> targetUser = userRepository.findByEmailAndPassword(email, password);
		if(!targetUser.isPresent()) {
			throw new EntityNotFoundException("user", "유저: "+ email + "가 존재하지 않습니다.");
		}
		return targetUser.get();
	}
	
	public List<User> findUserByMorningReportTime(OffsetTime morningTime) {
		return userRepository.findByMorningReportTimeAndPushAlarmStatus(morningTime, true);
	}

	public boolean isPasswordValid(User user, String password) {
		String actualPassword = user.getPassword();
		String msg = "올바른 비밀번호가 아닙니다.";		
		if(password.equals(actualPassword)) {
			return true;
		}
		else {
			throw new GeneralExceptions(msg);
		}
	}

	public void setPushAlarmStatus(User user, boolean pushAlarmStatus) {
		user.setPushAlarmStatus(pushAlarmStatus);
	}
	
}
