package com.yachugak.topla.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yachugak.topla.entity.Task;
import com.yachugak.topla.request.CreateTaskRequestFormat;
import com.yachugak.topla.service.TaskService;

@RestController
@RequestMapping(path = "${apiUriPrefix}/task")
public class TaskController {
	@Autowired
	private TaskService taskService;
	
	@PostMapping("")
	@Transactional(readOnly = false)
	public String createNewTask(@RequestBody CreateTaskRequestFormat req) {
		Task newTask = taskService.createNewTask(req.getTitle(), req.getPriority());
		
		if(req.getDueDate() != null) {
			taskService.setDueDate(newTask, req.getDueDate());
		}

		return "ok";
	}
	
	@PutMapping("/{uid}")
	@Transactional(readOnly = false)
	public String updateTaskDueDate(@PathVariable("uid") long uid, @RequestBody CreateTaskRequestFormat req) {
		Task updateTarget = taskService.findTaskById(uid);
		taskService.setTitle(updateTarget, req.getTitle());
		taskService.setPriority(updateTarget, req.getPriority());
		taskService.setDueDate(updateTarget, req.getDueDate());

		return "ok";
	}
}