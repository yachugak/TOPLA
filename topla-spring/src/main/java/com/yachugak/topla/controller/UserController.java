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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yachugak.topla.entity.SchedulePreset;
import com.yachugak.topla.entity.User;
import com.yachugak.topla.request.CreateUserRequestFormat;
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
		userService.setMorningReportTime(newUser, req.getMorningReportTime());
		userService.setEveningReportTime(newUser, req.getEveningReportTime());

		SchedulePreset newPreset = presetService.createSchedulePreset(newUser, presetService.createDefaultSchedulePreset());
		userService.setPresetCode(newUser, newPreset);
	
		return "ok";
	}
	
	@GetMapping("/{uid}")
	@Transactional(readOnly = false)
	public GetUserResponseFormat getUserInfo(@PathVariable("uid") long uid) {
		User targetUser = userService.findUserById(uid);
		GetUserResponseFormat res = new GetUserResponseFormat();
		res.setEmail(targetUser.getEmail());
		res.setMorningReportTime(targetUser.getMorningReportTime());
		res.setEveningReportTime(targetUser.getEveningReportTime());
		res.setPresetUid(targetUser.getSchedulePreset().getUid());
		
		return res;
	}
	
	@PutMapping("/{uid}")
	@Transactional(readOnly = false)
	public String updateUserInfo(@PathVariable("uid") long uid, @RequestBody CreateUserRequestFormat req) {
		// TODO: 리뷰필요. getUserInfo 이후에 호출된다고 가정?
		User updateTarget = userService.findUserById(uid);
		userService.setEmail(updateTarget, req.getEmail());
		userService.setPassword(updateTarget, req.getPassword());
		userService.setMorningReportTime(updateTarget, req.getMorningReportTime());
		userService.setEveningReportTime(updateTarget, req.getEveningReportTime());
		
		return "ok";
	}
	
	@DeleteMapping("/{uid}")
	@Transactional(readOnly = false)
	public String deleteUser(@PathVariable("uid") long uid) {
		User targetUser = userService.findUserById(uid);
		userService.deleteUser(targetUser);
		
		return "ok";
	}

}
