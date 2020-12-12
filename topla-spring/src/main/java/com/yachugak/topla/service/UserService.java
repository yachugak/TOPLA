package com.yachugak.topla.service;

import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yachugak.topla.entity.AuthMapping;
import com.yachugak.topla.entity.SchedulePreset;
import com.yachugak.topla.entity.Task;
import com.yachugak.topla.entity.TemporaryUser;
import com.yachugak.topla.entity.User;
import com.yachugak.topla.exception.DuplicatedException;
import com.yachugak.topla.exception.EntityNotFoundException;
import com.yachugak.topla.exception.GeneralExceptions;
import com.yachugak.topla.exception.InvalidArgumentException;
import com.yachugak.topla.repository.AuthMappingRepository;
import com.yachugak.topla.repository.PresetRepository;
import com.yachugak.topla.repository.TemporaryUserRepository;
import com.yachugak.topla.repository.UserRepository;
import com.yachugak.topla.util.Mail;
import com.yachugak.topla.util.SHA256;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PresetRepository presetRepository;
	
	@Autowired
	private TemporaryUserRepository temporaryUserRepository;
	
	@Autowired
	private AuthMappingRepository authMappingRepository;
	
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
		User targetUser = userRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("email", "이 존재하지 않습니다."));
		
		return targetUser;
	}

	public void setSelectedPreset(User user, long presetUid) {
		SchedulePreset targetPreset = presetRepository.findById(presetUid).orElseThrow(()->new EntityNotFoundException("preset", "이 존재하지 않습니다."));
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
		this.setPushAlarmStatus(newUser, true);
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
		
		SHA256 sha256 = new SHA256();
		password = sha256.getEncrpyt(password);
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

	public String userLogin(String email, String password) {
		SHA256 sha256 = new SHA256();
		password = sha256.getEncrpyt(password);
		
		Optional<User> targetUser = userRepository.findByEmailAndPassword(email, password);
		if(!targetUser.isPresent()) {
			throw new GeneralExceptions("잘못된 Email 혹은 비밀번호입니다.");
		}
		String secureCode = this.authMapping(targetUser.get());
		return secureCode;
	}
	
	public List<User> findUserByMorningReportTime(OffsetTime morningTime) {
		return userRepository.findByMorningReportTimeAndPushAlarmStatus(morningTime, true);
	}

	public boolean isPasswordValid(User user, String password) {
		SHA256 sha256 = new SHA256();
		password = sha256.getEncrpyt(password);
		String actualPassword = user.getPassword();
		
		if(password.equals(actualPassword)) {
			return true;
		}
		else {
			throw new GeneralExceptions("올바른 비밀번호가 아닙니다.");
		}
	}

	public void setPushAlarmStatus(User user, boolean pushAlarmStatus) {
		user.setPushAlarmStatus(pushAlarmStatus);
	}
	
	public boolean taskOwnerTest(User user, Task task) {
		return task.getUser().getUid() == user.getUid();
	}
	
	
	
	
	//----------------여기서부터 Temporary--------------
	public TemporaryUser createTemporaryUser(String email) {
		if(email == null) {
			throw new EntityNotFoundException("user", "유저: "+ email + "가 null값입니다.");
		}
		
		Optional<User> findUser = userRepository.findByEmail(email);
		if(findUser.isPresent()) {
			throw new GeneralExceptions("해당 이메일은 이미 계정이 존재합니다.");
		}
		
		Optional<TemporaryUser> findTUser = temporaryUserRepository.findByEmail(email);
		
		Mail sendMail = new Mail();
		
		String secureCode;
		
		if(findTUser.isEmpty()) {
			TemporaryUser newTemporaryUser = new TemporaryUser();
			secureCode = this.randomCode(6);
			
			sendMail.sendMail(email, sendMail.createTempUserTitle(), sendMail.createTempUserContent(secureCode), true);
			
			newTemporaryUser.setEmail(email);
			newTemporaryUser.setSecureCode(secureCode);
			newTemporaryUser.setCreatedDate(new Date());
			temporaryUserRepository.saveAndFlush(newTemporaryUser);
			
			return newTemporaryUser;
		}
		else {
			secureCode = this.randomCode(6);
			
			sendMail.sendMail(email, sendMail.createTempUserTitle(), sendMail.createTempUserContent(secureCode), true);
			
			findTUser.get().setSecureCode(secureCode);
			findTUser.get().setCreatedDate(new Date());
			
			return findTUser.get();
		}
	}
	
	
	public TemporaryUser findTemporaryUserByEmail(String email) {
		TemporaryUser result = temporaryUserRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("user", "유저: "+ email + "가 존재하지 않습니다."));		
		return result;
	}
	
	public String randomCode(int length) {
		Random random = new Random();
		String secureCode = "";
		for(int sur = 0; sur < length ; sur ++) {
			secureCode += random.nextInt(9);
		}
		return secureCode;
	}
	
	public void deleteTempUser(String email) {
		TemporaryUser target = this.findTemporaryUserByEmail(email);
		
		temporaryUserRepository.delete(target);
	}

	// 등록된 이메일을 통해 임시비밀번호 발급 
	public void sendTemporalPasswordByEmail(User targetUser, String randomCode) {
		String targetEmail = targetUser.getEmail();
		String title ="";
		String content = "";
		boolean HTMLFlag = true;
		
		Mail mail = new Mail();
		title = mail.createTempPasswordTitle();
		content = mail.createTempPasswordContent(targetEmail, randomCode);
		
		mail.sendMail(targetEmail, title, content, HTMLFlag);
		
	}
  
	public List<User> getAllUsers() {
		return userRepository.findAll();
  }
  
	// 유저이메일과 코드 맵핑
	public String authMapping(User user) {
		Optional<AuthMapping> targetAuthUser = authMappingRepository.findByUser(user);
		if(targetAuthUser.isEmpty()) {
			AuthMapping authUser = new AuthMapping();
			String secureCode = this.randomCode(10);
			while(authMappingRepository.findBySecureCode(secureCode).isPresent()) {
				secureCode = this.randomCode(10);
			}
			authUser.setUser(user);
			authUser.setSecureCode(secureCode);
			authMappingRepository.saveAndFlush(authUser);
			
			return secureCode;
		}
		else {
			AuthMapping authUser = targetAuthUser.get();
			String secureCode = this.randomCode(10);
			while(authMappingRepository.findBySecureCode(secureCode).isPresent()) {
				secureCode = this.randomCode(10);
			}
			authUser.setSecureCode(secureCode);
			
			return secureCode;
		}
	}
	
	public String findEmailbySecureCode(String secureCode) {
		Optional<AuthMapping> targetAuthUser = authMappingRepository.findBySecureCode(secureCode);
		if(targetAuthUser.isPresent()) {
			return targetAuthUser.get().getUser().getEmail();
		}
		else {
			throw new GeneralExceptions("해당 보안코드에 부합하는 아이디가 없습니다.");
		}
	}
}
