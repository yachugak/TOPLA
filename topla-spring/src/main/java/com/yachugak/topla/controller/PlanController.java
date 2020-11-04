package com.yachugak.topla.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yachugak.topla.entity.User;
import com.yachugak.topla.service.PlanService;
import com.yachugak.topla.service.UserService;

@RestController
@RequestMapping(path = "${apiUriPrefix}/plan")
@CrossOrigin(origins = "*")
public class PlanController {
	@Autowired
	private PlanService planService;

	@Autowired
	private UserService userService;
	
	@GetMapping("")
	@Transactional(readOnly = false)
	public String planTest() {
		//TODO: 나중에 모든 user 대응하게
		User user = userService.findUserById(1L);
		planService.plan(user, new Date());
		
		return "ok";
	}
}
