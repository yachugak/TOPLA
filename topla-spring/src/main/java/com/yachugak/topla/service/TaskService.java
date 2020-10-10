package com.yachugak.topla.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yachugak.topla.entity.Task;
import com.yachugak.topla.exception.InvalidArgumentException;
import com.yachugak.topla.repository.TaskRepository;

@Service
public class TaskService {
	@Autowired
	private TaskRepository taskRepository;

	public Task createNewTask(String title, int priority) {
		if(title.equals("")) {
			throw new InvalidArgumentException("title", "값 있음", "빈 문자열");
		}
		Task newTask = new Task();
		newTask.setTitle(title);
		newTask.setPriority(priority);
		newTask.setProgress(0);
		newTask.setCreatedDate(new Date());
		taskRepository.saveAndFlush(newTask);
		
		return newTask;
	}

	public void deleteTask(Task taskEntity) {
		taskRepository.delete(taskEntity);
	}

}
