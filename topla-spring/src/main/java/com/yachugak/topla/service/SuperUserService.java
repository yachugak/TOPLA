package com.yachugak.topla.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
	
	public boolean superUserLogIn(String email, String password) {
		String superEmail = "admin@syunasoft.com";
		String superPassword = "iamsuperuser";
	
		if(email.equals(superEmail) && password.equals(superPassword)) {
			//
			// TODO: 여기서 슈퍼유저의 랜덤스트링 리턴하게 변경해야됨
			//
			return true;
		}
		return false;
	}
	
	public List<User> getAllUsers() {
		List<User> userList = userService.getAllUsers();
		return userList;
	}
	
	public boolean isSuperUser(String email) {
		//
		//	TODO: 여기서 이메일 대신에 랜덤 스트링을 받아서, 랜덤 스트링과 비교하게 변경
		// 
		String superEmail = "admin@syunasoft.com";
		
		if(email.equals(superEmail)) {
			return true;
		}
		return false;
	}

	public void resetPassword(User targetUser) {
		String password = "0000";
		SHA256 sha256 = new SHA256();
		password = sha256.getEncrpyt(password);
		userService.setPassword(targetUser, password);
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
