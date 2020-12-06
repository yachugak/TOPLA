package com.yachugak.topla.controller;

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
import com.yachugak.topla.entity.User;
import com.yachugak.topla.request.CreateUserRequestFormat;
import com.yachugak.topla.request.UpdateDeviceTokenRequestFormat;
import com.yachugak.topla.request.UpdatePasswordRequestFormat;
import com.yachugak.topla.request.UpdatePushAlarmStatusRequestFormat;
import com.yachugak.topla.request.UpdateReportTimeRequestFormat;
import com.yachugak.topla.request.userLogInFormat;
import com.yachugak.topla.response.GetUserResponseFormat;
import com.yachugak.topla.service.PresetService;
import com.yachugak.topla.service.UserService;

@RestController
@RequestMapping(path = "${apiUriPrefix}/user")
@CrossOrigin(origins = "*")
public class UserController {
	@Autowired
	private PresetService presetService;

	@Autowired
	private UserService userService;
	
	@PostMapping("")
	@Transactional(readOnly = false)
	public String createUser(@RequestBody CreateUserRequestFormat req) {
		User newUser = userService.createUser(req.getEmail(), req.getPassword());
		String presetName = "기본 프리셋";
		
		userService.setMorningReportTime(newUser, req.getMorningReportTime());
		userService.setEveningReportTime(newUser, req.getEveningReportTime());
		
		SchedulePreset newPreset = presetService.createSchedulePreset(newUser, presetName, presetService.createDefaultSchedulePreset());
		userService.setSelectedPreset(newUser, newPreset);
	
		return "ok";
	}
	
	
	@GetMapping("")
	@Transactional(readOnly = false)
	public GetUserResponseFormat getUserInfo(@RequestHeader("Authorization") String email) {
		User targetUser = userService.findUserByEmail(email);
		GetUserResponseFormat res = new GetUserResponseFormat();
		res.setEmail(targetUser.getEmail());
		res.setMorningReportTime(targetUser.getMorningReportTime());
		res.setEveningReportTime(targetUser.getEveningReportTime());
		res.setPresetUid(targetUser.getSchedulePreset().getUid());
		
		return res;
	}
	
	@PutMapping("")
	@Transactional(readOnly = false)
	public String updateReportTime(@RequestHeader("Authorization") String email, @RequestBody UpdateReportTimeRequestFormat req) {
		User updateTarget = userService.findUserByEmail(email);
		userService.setMorningReportTime(updateTarget, req.getMorningReportTime());
		userService.setEveningReportTime(updateTarget, req.getEveningReportTime());
		
		return "ok";
	}
	
	@PutMapping("/password")
	@Transactional(readOnly = false)
	public String updatePassword(@RequestHeader("Authorization") String email, @RequestBody UpdatePasswordRequestFormat req) {
		User user = userService.findUserByEmail(email);
		if(userService.isPasswordValid(user, req.getOldPassword())) {
			userService.setPassword(user, req.getNewPassword());
		}
		return "ok";
	}
	
	@PutMapping("/push")
	@Transactional(readOnly = false)
	public String updatePushAlarmStatus(@RequestHeader("Authorization") String email, @RequestBody UpdatePushAlarmStatusRequestFormat req) {
		User user = userService.findUserByEmail(email);
		userService.setPushAlarmStatus(user, req.isPushAlarmStatus());
		
		return "ok";
	}
	
	@DeleteMapping("")
	@Transactional(readOnly = false)
	public String deleteUser(@RequestHeader("Authorization") String email) {
		User targetUser = userService.findUserByEmail(email);
		userService.deleteUser(targetUser);
		
		return "ok";
	}
	
	@PutMapping("/token")
	@Transactional(readOnly = false)
	public String updateDeviceToken(@RequestHeader("Authorization") String email, @RequestBody UpdateDeviceTokenRequestFormat req) {
		User targetUser = userService.findUserByEmail(email);
		userService.setDeviceToken(targetUser, req.getDeviceToken());
		
		return "ok";
	}
	
	@PostMapping("/login")
	@Transactional(readOnly = false)
	public String userLogIn(@RequestBody userLogInFormat req) {
		String email = req.getEmail();
		String password = req.getPassword();
		User targetUser = userService.userLogin(email, password);
		
		return "ok";
	}

}
