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
import com.yachugak.topla.request.CheckAsFinishedRequestFormat;
import com.yachugak.topla.request.CreateTaskRequestFormat;
import com.yachugak.topla.response.TaskResponseFormat;
import com.yachugak.topla.service.PlanService;
import com.yachugak.topla.service.TaskService;

@RestController
@RequestMapping(path = "${apiUriPrefix}/task")
@CrossOrigin(origins = "*")
public class TaskController {
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private PlanService planService;
	
	@PostMapping("")
	@Transactional(readOnly = false)
	public String createNewTask(@RequestBody CreateTaskRequestFormat req) {
		Task newTask = taskService.createNewTask(1L, req.getTitle(), req.getPriority());
		taskService.setDueDate(newTask, req.getDueDate());
		taskService.setEstimatedTime(newTask, req.getEstimatedTime());
		taskService.setLocation(newTask, req.getLocation());
		
		planService.plan(1L, new Date());

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

		planService.plan(1L, new Date());

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
		Task updateTarget = taskService.findTaskById(uid);
		taskService.setProgress(updateTarget, req.getProgress());
		if(req.getProgress() == 100) {
			Date time = new Date();
			taskService.setFinishTime(updateTarget, time);
		}
		else {
			taskService.setFinishTime(updateTarget, null);
		}
		return "ok";
	}
}
