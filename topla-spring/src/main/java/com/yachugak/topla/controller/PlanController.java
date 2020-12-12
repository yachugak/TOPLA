package com.yachugak.topla.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yachugak.topla.entity.Plan;
import com.yachugak.topla.entity.Task;
import com.yachugak.topla.entity.TaskHistory;
import com.yachugak.topla.entity.User;
import com.yachugak.topla.request.CheckAsFinishedRequestFormat;
import com.yachugak.topla.service.PlanService;
import com.yachugak.topla.service.TaskHistoryService;
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
	private TaskHistoryService taskHistoryService;
	
	@GetMapping("")
	@Transactional(readOnly = false)
	public String planTest(@RequestHeader("Authorization") String secureCode) {
		String email = userService.findEmailbySecureCode(secureCode);
		User user = userService.findUserByEmail(email);
		planService.plan(user, new Date());
		
		return "ok";
	}
	
	@GetMapping("/loss")
	@Transactional(readOnly = true)
	public HashMap<String, Double> getUserLossPriority(@RequestHeader("Authorization") String secureCode) {
		String email = userService.findEmailbySecureCode(secureCode);	
		User targetUser = userService.findUserByEmail(email);
		Double lossPriority = userService.getLossPriority(targetUser);
		
		HashMap<String, Double> res = new HashMap<String, Double>();
		res.put("totalLossPriority", lossPriority);
		
		return res;
}
  
	@PutMapping("/{planUid}/finish")
	@Transactional(readOnly = false)
	public String updateProgress(@PathVariable("planUid") long planUid, @RequestBody CheckAsFinishedRequestFormat req) {
		Plan targetPlan = planService.findPlanById(planUid);
		Task targetTask = targetPlan.getTask();
		int progress = req.getProgress();
		planService.setProgress(targetPlan, progress);
		
		taskHistoryService.updateHistoryByPlan(progress, targetTask, targetPlan);
		
		return "ok";
	}
}
