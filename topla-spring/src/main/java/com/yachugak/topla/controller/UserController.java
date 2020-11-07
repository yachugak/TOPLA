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
		newUser.setMorningReportTime(req.getMorningReportTime());
		newUser.setEveningReportTime(req.getEveningReportTime());
		
		SchedulePreset newPreset = presetService.createSchedulePreset(newUser, presetService.createDefaultSchedulePreset());
		newUser.setSchedulePreset(newPreset);

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
		updateTarget.setEmail(req.getEmail());
		updateTarget.setPassword(req.getPassword());
		updateTarget.setEveningReportTime(req.getEveningReportTime());
		updateTarget.setMorningReportTime(req.getMorningReportTime());
		
		/*
		 	TODO: 리뷰필요. 이 부분에서 선택된 preset의 update를 넣어야하는지?
		 	presetController에 "select"에 정의되어 있는 기능이긴함. 
		 	
		 	만약 한다면,
		 	- 새로운 req 포맷에 presetUid를 정의해서 넣어주고(GetUserResponseFormat), presetService 통해서 preset을 검색해서 update하는 방법
		 	안 한다면, 
		 	- 유저 정보 업데이트는 이거, 프리셋의 선택, 수정은 presetController를 이용하는 방향.
		 	
		 	현재는 안하고 있음.
		*/
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
