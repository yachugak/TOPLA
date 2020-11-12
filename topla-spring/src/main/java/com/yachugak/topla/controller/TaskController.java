package com.yachugak.topla.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yachugak.topla.entity.Task;
import com.yachugak.topla.exception.DuplicatedException;
import com.yachugak.topla.entity.User;
import com.yachugak.topla.request.CheckAsFinishedRequestFormat;
import com.yachugak.topla.request.CreateTaskRequestFormat;
import com.yachugak.topla.response.TaskResponseFormat;
import com.yachugak.topla.service.PlanService;
import com.yachugak.topla.service.TaskService;
import com.yachugak.topla.service.UserService;

@RestController
@RequestMapping(path = "${apiUriPrefix}/task")
@CrossOrigin(origins = "*")
public class TaskController {
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private PlanService planService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("")
	@Transactional(readOnly = false)
	public String createNewTask(@RequestBody CreateTaskRequestFormat req) {
		Task dup = new Task();
		taskService.setTitle(dup, req.getTitle());
		taskService.setDueDate(dup, req.getDueDate());
		Task result = taskService.duplicated(dup);
		
		if(result.getUid() != -1 && !req.getDuplicated()) {
			throw new DuplicatedException(req.getTitle(), result.getTitle());
		}
		
		Task newTask = taskService.createNewTask(1L, req.getTitle(), req.getPriority());
		taskService.setDueDate(newTask, req.getDueDate());
		taskService.setEstimatedTime(newTask, req.getEstimatedTime());
		taskService.setLocation(newTask, req.getLocation());
		taskService.setRemindingTiming(newTask, req.getRemindingTiming());
		
		// 유저1에만 대응. 변경예정
		User user = userService.findUserById(1L);
		planService.plan(user, new Date());

		return "ok";
	}

	@PutMapping("/{uid}")
	@Transactional(readOnly = false)
	public String updateTask(@PathVariable("uid") long uid, @RequestBody CreateTaskRequestFormat req) {		
		Task updateTarget = taskService.findTaskById(uid);
		taskService.setTitle(updateTarget, req.getTitle());
		taskService.setPriority(updateTarget, req.getPriority());
		taskService.setDueDate(updateTarget, req.getDueDate());
		taskService.setEstimatedTime(updateTarget, req.getEstimatedTime());
		taskService.setLocation(updateTarget, req.getLocation());
		taskService.setRemindingTiming(updateTarget, req.getRemindingTiming());

		// 유저1에만 대응.
		User user = userService.findUserById(1L);
		planService.plan(user, new Date());

		return "ok";
	}
	
	@GetMapping("/list")
	@Transactional(readOnly = true)
	public List<TaskResponseFormat> taskList(){
		List<Task> taskList = taskService.getAllTask();
		ArrayList<TaskResponseFormat> resList = new ArrayList<>();
		
		for(Task task : taskList) {
			resList.add(new TaskResponseFormat(task));
		}

		return resList;
	}
	
	@PutMapping("/{uid}/finish")
	@Transactional(readOnly = false)
	public String updateProgress(@PathVariable("uid") long uid, @RequestBody CheckAsFinishedRequestFormat req) {
		// TODO: 나중에 서비스로 내리기.
		Task updateTarget = taskService.findTaskById(uid);
		if(req.getProgress() == updateTarget.getEstimatedTime()) {
			Date time = new Date();
			taskService.setFinishTime(updateTarget, time);
		}
		else {
			taskService.setFinishTime(updateTarget, null);
		}
		
		int progress = req.getProgress();
		if(progress < 0) {
			progress = 0;
		}
		taskService.setProgress(updateTarget, progress);
		
		return "ok";
	}
}
