package com.yachugak.topla.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yachugak.topla.service.PlanService;

@RestController
@RequestMapping(path = "${apiUriPrefix}/plan")
@CrossOrigin(origins = "*")
public class PlanController {
	@Autowired
	private PlanService planService;

	@GetMapping("")
	@Transactional(readOnly = false)
	public String planTest() {
		planService.plan(1L, new Date());
		
		return "ok";
	}
}
