package com.yachugak.topla.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yachugak.topla.entity.Task;
import com.yachugak.topla.entity.User;
import com.yachugak.topla.exception.GeneralExceptions;
import com.yachugak.topla.repository.PresetRepository;
import com.yachugak.topla.repository.TaskRepository;
import com.yachugak.topla.repository.TemporaryUserRepository;
import com.yachugak.topla.repository.UserRepository;
import com.yachugak.topla.util.DayCalculator;
import com.yachugak.topla.util.SHA256;

@Service
public class SuperUserService {
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PresetRepository presetRepository;
	
	@Autowired
	private TemporaryUserRepository temporaryUserRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	public String superUserLogIn(String email, String password) {
		if(!this.isSuperUser(email)) {
			throw new GeneralExceptions("접근 권한이 없습니다.");
		}
		
		SHA256 sha256 = new SHA256();
		password = sha256.getEncrpyt(password);
		
		Optional<User> target = userRepository.findByEmailAndPassword(email, password);
		if(target.isEmpty()) {
			throw new GeneralExceptions("접근 권한이 없습니다.");
		}
		
		String secureCode = userService.authMapping(target.get());
		return secureCode;
	}
	
	public List<User> getAllUsers() {
		List<User> userList = userService.getAllUsers();
		return userList;
	}
	
	public boolean isSuperUser(String email) {
		String superEmail = "admin@syunasoft.com";
		
		if(email.equals(superEmail)) {
			return true;
		}
		return false;
	}

	public void resetPassword(User targetUser, String newPassword) {
		userService.setPassword(targetUser, newPassword);
	}

	// 전체 유저 대상
	public Long findNumberOfNewTasksIn7Days() {
		List<Date> dateList = DayCalculator.getStartAndEndDateOf7Days();
		
		List<Task> taskList = taskRepository.findNewTaskIn7Days(dateList.get(0), dateList.get(1));
		return (long) taskList.size();
	}
	
	// 단일 유저 대상
	public Integer findNumberOfNewTasksIn7Days(User targetUser) {
		List<Date> dateList = DayCalculator.getStartAndEndDateOf7Days();
		
		List<Task> taskList = taskRepository.findNewTaskIn7Days(targetUser, dateList.get(0), dateList.get(1));
		return taskList.size();
	}
	
	public Long getNumberOfTotalTask() {
		return taskRepository.count();
	}

	public Long getNumberOfDoneTask() {
		return (long) taskRepository.findAllDoneTask().size();
	}

	public Long getNumberOfTotalUser() {
		return userRepository.count();
	}

	// 지난 7일간 작업을 하나라도 추가한 유저는 활성 유저
	public Long getActiveUserNum() {
		long activeUserNum = 0;
		int count = 0;
		
		List<User> userList = userService.getAllUsers();
		for(User u : userList) {
			count = this.findNumberOfNewTasksIn7Days(u);
			if(count > 0) {
				activeUserNum++;
			}
		}
		return activeUserNum;
	}

	public Long getDoneTaskNumIn7Days() {
		List<Date> dateList = DayCalculator.getStartAndEndDateOf7Days();
		
		List<Task> taskList = taskRepository.findDoneTaskIn7Days(dateList.get(0), dateList.get(1));
		return (long) taskList.size();
		
	}

	
	
}
