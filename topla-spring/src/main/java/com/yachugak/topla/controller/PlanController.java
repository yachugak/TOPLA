package com.yachugak.topla.controller;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yachugak.topla.entity.Plan;
import com.yachugak.topla.entity.User;
import com.yachugak.topla.request.CheckAsFinishedRequestFormat;
import com.yachugak.topla.service.PlanService;
import com.yachugak.topla.service.TaskService;
import com.yachugak.topla.service.UserService;

@RestController
@RequestMapping(path = "${apiUriPrefix}/plan")
@CrossOrigin(origins = "*")
public class PlanController {
	@Autowired
	private PlanService planService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private TaskService taskService;
	
	@GetMapping("")
	@Transactional(readOnly = false)
	public String planTest() {
		//TODO: 나중에 모든 user 대응하게
		User user = userService.findUserById(1L);
		planService.plan(user, new Date());
		
		return "ok";
	}
	
	@GetMapping("/loss")
	@Transactional(readOnly = true)
	public HashMap<String, Double> getUserLossPriority() {
		
		//userUid를 현재는 받아올수 없으므로 임시로 1로 지정해서 사용
		long userUid = 1;
		
		HashMap<String, Double> res = new HashMap<String, Double>();
		
		User targetUser = userService.findUserById(userUid);
		
		Double lossPriority = userService.getLossPriority(targetUser);
		
		res.put("totalLossPriority", lossPriority);
		
		return res;
}
  
	@PutMapping("/{planUid}/finish")
	@Transactional(readOnly = false)
	public String updateProgress(@PathVariable("planUid") long planUid, @RequestBody CheckAsFinishedRequestFormat req) {
		Plan targetPlan = planService.findPlanById(planUid);
		
		int progress = req.getProgress();
		planService.setProgress(targetPlan, progress);
		
		return "ok";
	}
}
