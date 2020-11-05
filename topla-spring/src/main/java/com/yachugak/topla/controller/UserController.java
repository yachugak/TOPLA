package com.yachugak.topla.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yachugak.topla.dataformat.SchedulePresetDataFormat;
import com.yachugak.topla.entity.SchedulePreset;
import com.yachugak.topla.entity.User;
import com.yachugak.topla.request.CreateUserRequestFormat;
import com.yachugak.topla.service.PlanService;
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
		newUser.setMorningReportTime(req.getMorningReportTime());
		newUser.setEveningReportTime(req.getEveningReportTime());
 
		SchedulePreset newPreset = presetService.createSchedulePreset(newUser, presetService.createDefaultSchedulePreset());
		newUser.setSchedulePreset(newPreset);

		return "ok";
	}
	
	@PutMapping("/{uid}")
	@Transactional(readOnly = false)
	public String updateUserInfo() {
		// TODO
		
		return "ok";
	}
	
	@GetMapping("/{uid}")
	@Transactional(readOnly = false)
	public String getUserInfo() {
		// TODO
		return "ok";
	}
}
