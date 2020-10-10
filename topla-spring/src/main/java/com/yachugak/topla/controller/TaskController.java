package com.yachugak.topla.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
		taskService.createNewTask(req.getTitle(), req.getPriority());

		return "ok";
	}
}
