package com.yachugak.topla.controller;

import java.util.ArrayList;
import java.util.Date;
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

import com.yachugak.topla.entity.Task;
import com.yachugak.topla.entity.TaskHistory;
import com.yachugak.topla.exception.DuplicatedException;
import com.yachugak.topla.exception.YouAreNotOwnerException;
import com.yachugak.topla.entity.User;
import com.yachugak.topla.request.CheckAsFinishedRequestFormat;
import com.yachugak.topla.request.CreateTaskRequestFormat;
import com.yachugak.topla.response.TaskResponseFormat;
import com.yachugak.topla.service.PlanService;
import com.yachugak.topla.service.TaskHistoryService;
import com.yachugak.topla.service.TaskService;
import com.yachugak.topla.service.UserService;
import com.yachugak.topla.util.DayCalculator;

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
	
	@Autowired
	private TaskHistoryService taskHistoryService;
	
	@PostMapping("")
	@Transactional(readOnly = false)
	public String createNewTask(@RequestHeader("Authorization") String secureCode, @RequestBody CreateTaskRequestFormat req) {
		String email = userService.findEmailbySecureCode(secureCode);
		User user = userService.findUserByEmail(email);
		Task dup = new Task();
		taskService.setTitle(dup, req.getTitle());
		taskService.setDueDate(dup, req.getDueDate());
		taskService.setUser(dup, user);
		Task result = taskService.duplicated(dup);
		
		if(result.getUid() != -1 && !req.getDuplicated()) {
			throw new DuplicatedException(req.getTitle(), result.getTitle());
		}
		
		User targetUser = userService.findUserByEmail(email);
		Task newTask = taskService.createNewTask(targetUser.getUid(), req.getTitle(), req.getPriority());
		taskService.setDueDate(newTask, req.getDueDate());
		taskService.setEstimatedTime(newTask, req.getEstimatedTime());
		taskService.setLocation(newTask, req.getLocation());
		taskService.setRemindingTiming(newTask, req.getRemindingTiming());
		
		planService.plan(targetUser, DayCalculator.getTodayDate());

		return "ok";
	}

	@PutMapping("/{uid}")
	@Transactional(readOnly = false)
	public String updateTask(@PathVariable("uid") long uid, @RequestHeader("Authorization") String secureCode, @RequestBody CreateTaskRequestFormat req) {	
		String email = userService.findEmailbySecureCode(secureCode);	
		Task updateTarget = taskService.findTaskById(uid);
		taskService.setTitle(updateTarget, req.getTitle());
		taskService.setPriority(updateTarget, req.getPriority());
		taskService.setDueDate(updateTarget, req.getDueDate());
		taskService.setEstimatedTime(updateTarget, req.getEstimatedTime());
		taskService.setLocation(updateTarget, req.getLocation());
		taskService.setRemindingTiming(updateTarget, req.getRemindingTiming());

		User user = userService.findUserByEmail(email);
		planService.plan(user, DayCalculator.getTodayDate());

		return "ok";
	}
	
	// 각 유저별로 모든 task 물러옴.
	@GetMapping("/list")
	@Transactional(readOnly = true)
	public List<TaskResponseFormat> taskList(@RequestHeader("Authorization") String secureCode) {
		String email = userService.findEmailbySecureCode(secureCode);
		User targetUser = userService.findUserByEmail(email);
		List<Task> taskList = taskService.getAllTask(targetUser);
		
		ArrayList<TaskResponseFormat> resList = new ArrayList<>();
		for(Task task : taskList) {
			resList.add(new TaskResponseFormat(task));
		}

		return resList;
	}
	
	@PutMapping("/{uid}/finish")
	@Transactional(readOnly = false)
	public String updateProgress(@PathVariable("uid") long uid, @RequestBody CheckAsFinishedRequestFormat req) {
		Task updateTarget = taskService.findTaskById(uid);
		int progress = req.getProgress();
		taskService.setProgress(updateTarget, progress);
		
		if(req.getProgress() < 0) {
			List<TaskHistory> historyList = taskHistoryService.findByTask(updateTarget);
			
			for(TaskHistory history : historyList) {
				taskHistoryService.deleteHistory(history);
			}
		}
		else {
			taskHistoryService.createNewHistory(updateTarget, progress);
		}
		
		return "ok";
	}
	
	@DeleteMapping("/{uid}")
	@Transactional(readOnly = false)
	public String deleteTask(@PathVariable("uid") long uid, @RequestHeader("Authorization") String secureCode) {
		String email = userService.findEmailbySecureCode(secureCode);
		User targetUser = userService.findUserByEmail(email);
		Task targetTask = taskService.findTaskById(uid);
		
		if(userService.taskOwnerTest(targetUser, targetTask)==false) {
			throw new YouAreNotOwnerException("task", uid);
		}
		
		taskService.deleteTask(targetTask);
		taskHistoryService.updateHistoryByTask(targetTask);
		planService.plan(targetUser, DayCalculator.getTodayDate());
		
		return "ok";
	}
}
