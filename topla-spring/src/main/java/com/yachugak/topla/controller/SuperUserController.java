package com.yachugak.topla.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yachugak.topla.entity.SchedulePreset;
import com.yachugak.topla.entity.TemporaryUser;
import com.yachugak.topla.entity.User;
import com.yachugak.topla.exception.GeneralExceptions;
import com.yachugak.topla.request.CreateTemporaryUserRequestFormat;
import com.yachugak.topla.request.CreateUserRequestFormat;
import com.yachugak.topla.request.FindUserPasswordRequestFormat;
import com.yachugak.topla.request.SendMailRequestFormat;
import com.yachugak.topla.request.UpdateDeviceTokenRequestFormat;
import com.yachugak.topla.request.UpdatePasswordRequestFormat;
import com.yachugak.topla.request.UpdatePushAlarmStatusRequestFormat;
import com.yachugak.topla.request.UpdateReportTimeRequestFormat;
import com.yachugak.topla.request.UserLogInRequestFormat;
import com.yachugak.topla.request.deleteUserRequestFormat;
import com.yachugak.topla.response.GetOverallInfoResponseFormat;
import com.yachugak.topla.response.GetUserResponseFormat;
import com.yachugak.topla.response.SuperGetUserResponseFormat;
import com.yachugak.topla.service.PresetService;
import com.yachugak.topla.service.SuperUserService;
import com.yachugak.topla.service.TaskService;
import com.yachugak.topla.service.UserService;
import com.yachugak.topla.util.Mail;


@RestController
@RequestMapping(path = "${apiUriPrefix}/superuser")
@CrossOrigin(origins = "*")
public class SuperUserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private SuperUserService superUserService;
	
	@Autowired
	private TaskService taskService;
	
	// 슈퍼 유저 로그인
	@GetMapping("/login")
	@Transactional(readOnly = false)
	public String superUserLogIn(@RequestBody UserLogInRequestFormat req) {
		String email = req.getEmail();
		String password = req.getPassword();
		
		if(superUserService.superUserLogIn(email, password)) {
			return "ok";
			//
			// TODO: superUserLogin이 반환한 랜덤스트링 반환하도록 변경
			//
		}
		return "fail";
	}
	
	// 유저 정보 조회
	// TODO: 헤더의 email이, 랜덤스트링으로 대체되어야함.
	@GetMapping("/userlist")
	@Transactional(readOnly = true)
	public ArrayList<SuperGetUserResponseFormat> getUserInfo(@RequestHeader("Authorization") String email) {
		//
		//	TODO:: email대신 랜덤 스트링 받아서, 랜덤 스트링으로 유저 email 찾아내야함.
		//
		
		if(superUserService.isSuperUser(email) == false) {
			throw new GeneralExceptions("접근 권한이 없습니다.");
		}
		
		List<User> userList = superUserService.getAllUsers();
		
		ArrayList<SuperGetUserResponseFormat> res = new ArrayList<>();
		for(User u : userList) {
			SuperGetUserResponseFormat form = new SuperGetUserResponseFormat();

			form.setEmail(u.getEmail());
			form.setPassword(u.getPassword());
			form.setDeviceToken(u.getDeviceToken());
			form.setMorningReportTime(u.getMorningReportTime());
			form.setEveningReportTime(u.getEveningReportTime());
			form.setPresetName(u.getSchedulePreset().getName());
			form.setPushAlarmStatus(u.getPushAlarmStatus());
			
			//  최근 7일간 작업 추가갯수
			int count = superUserService.findNumberOfNewTasksIn7Days(u);
			form.setNumberOfNewTasksIn7Days(count);
			
			//	활성 유저 여부. (7일간 추가흔적)
			if(count > 0) {
				form.setActiveUser(true);
			}
			else {
				form.setActiveUser(false);
			}
			
			res.add(form);
		}		
		return res;
	}
	
	// 대강의 정보 제공.
	@GetMapping("/overallinfo")
	@Transactional(readOnly = true)
	public GetOverallInfoResponseFormat getOverallInfo(@RequestHeader("Authorization") String email) {
		//
		//	TODO:: email대신 랜덤 스트링 받아서, 랜덤 스트링으로 유저 email 찾아내야함.
		//
		
		if(superUserService.isSuperUser(email) == false) {
			throw new GeneralExceptions("접근 권한이 없습니다.");
		}
		
		GetOverallInfoResponseFormat res = new GetOverallInfoResponseFormat();
		Long totalTask = superUserService.getNumberOfTotalTask();
		Long doneTask = superUserService.getNumberOfDoneTask();
		Long unDoneTask = totalTask - doneTask;
		Long allUserNum = superUserService.getNumberOfTotalUser();
		Long activeUserNum = superUserService.getActiveUserNum();
		Long newTaskNumIn7Days = superUserService.findNumberOfNewTasksIn7Days();
		Long doneTaskNumIn7Days = superUserService.getDoneTaskNumIn7Days();
		
		res.setTaskNum(totalTask);
		res.setDoneTaskNum(doneTask);
		res.setUndoneTaskNum(unDoneTask);
		res.setAllUserNum(allUserNum);
		res.setActiveUserNum(activeUserNum);
		res.setNewTaskNumIn7Days(newTaskNumIn7Days);
		res.setDoneTaskNumIn7Days(doneTaskNumIn7Days);
		return res;
	}
	
	// 유저에게 메일보내기
	// TODO: 헤더의 email이, 랜덤스트링으로 대체되어야함.
	@PostMapping("/sendmail")
	@Transactional(readOnly = true)
	public String sendMail(@RequestHeader("Authorization") String email, @RequestBody SendMailRequestFormat req) {
		//
		//	TODO:: email대신 랜덤 스트링 받아서, 랜덤 스트링으로 유저 email 찾아내야함.
		//
		
		if(superUserService.isSuperUser(email) == false) {
			throw new GeneralExceptions("접근 권한이 없습니다.");
		}
		
		Mail mail = new Mail();
		mail.sendMail(req.getTo(), req.getTitle(), req.getContent(), false);
		return "ok";
	}

	// 비밀번호 리셋
	// TODO: 헤더의 email이, 랜덤스트링으로 대체되어야함.
	@PutMapping("/resetpassword")
	@Transactional(readOnly = false)
	public String resetPassword(@RequestHeader("Authorization") String email, @RequestBody FindUserPasswordRequestFormat req) {
		//
		//	TODO:: email대신 랜덤 스트링 받아서, 랜덤 스트링으로 유저 email 찾아내야함.
		//
		
		if(superUserService.isSuperUser(email) == false) {
			throw new GeneralExceptions("접근 권한이 없습니다.");
		}
		
		User targetUser = userService.findUserByEmail(req.getEmail());
		superUserService.resetPassword(targetUser);
		return "ok";
	}
	
	// 유저 삭제
	// TODO: 헤더의 email이, 랜덤스트링으로 대체되어야함.
	@DeleteMapping("")
	@Transactional(readOnly = false)
	public String deleteUser(@RequestHeader("Authorization") String email, @RequestBody deleteUserRequestFormat req) {
		//
		//	TODO:: email대신 랜덤 스트링 받아서, 랜덤 스트링으로 유저 email 찾아내야함.
		//
		
		if(superUserService.isSuperUser(email) == false) {
			throw new GeneralExceptions("접근 권한이 없습니다.");
		}
		
		User targetUser = userService.findUserByEmail(req.getEmail());
		userService.deleteUser(targetUser);
		return "ok";
	}
	
	@GetMapping("/test")
	@Transactional(readOnly = false)
	public String test(@RequestHeader("Authorization") String email) {
		User targetUser = userService.findUserByEmail(email);
		superUserService.findNumberOfNewTasksIn7Days(targetUser);
		return "ok";
	}
	
}
