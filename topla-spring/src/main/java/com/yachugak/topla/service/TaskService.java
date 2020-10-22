package com.yachugak.topla.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yachugak.topla.entity.Task;
import com.yachugak.topla.exception.EntityNotFoundException;
import com.yachugak.topla.exception.InvalidArgumentException;
import com.yachugak.topla.repository.TaskRepository;

@Service
public class TaskService {
	@Autowired
	private TaskRepository taskRepository;
	
	public List<Task> getAllTask(){
		return taskRepository.findAll();
	}

	public Task createNewTask(String title, int priority) {

		Task newTask = new Task();
		this.setTitle(newTask, title);
		this.setPriority(newTask, priority);
		this.setProgress(newTask, 0);
		this.setEstimatedTime(newTask, null);
		this.setCreatedDate(newTask, new Date());
		taskRepository.saveAndFlush(newTask);
		
		return newTask;
	}

	public void deleteTask(Task taskEntity) {
		taskRepository.delete(taskEntity);
	}
	
	public void setDueDate(Task task, Date newDate) {
		task.setDueDate(newDate);
	}
	
	public void setTitle(Task task, String title) {
		if(title == null) {
			throw new InvalidArgumentException("title", "값 있음", "null");
		}
		if(title.equals("")) {
			throw new InvalidArgumentException("title", "값 있음", "빈 문자열");
		}

		task.setTitle(title);
	}
	
	public void setPriority(Task task, int priority) {
		if(priority < 0) {
			throw new InvalidArgumentException("priority", "음이 아닌 정수", priority+"");
		}
		task.setPriority(priority);
	}
	
	public void setCreatedDate(Task task, Date date) {
		task.setCreatedDate(date);
	}
	
	public void setProgress(Task task, int progress) {
		if(progress > 0 || progress > 100) {
			throw new InvalidArgumentException("progress", "0~100", progress+"");
		} 
		task.setProgress(progress);
	}

	public Task findTaskById(Long uid) {
		Optional<Task> opTask = taskRepository.findById(uid);
		if(opTask.isPresent() == false) {
			throw new EntityNotFoundException("task", uid);
		}
		
		return opTask.get();
	}

	public void setEstimatedTime(Task task, Integer estimatedTime) {
		if(estimatedTime == null) {
			task.setEstimatedTime(null);
		}
		else if(estimatedTime < 0 || estimatedTime > 36000) {
			throw new InvalidArgumentException("estimatedTime", "0~36000", estimatedTime+"");
		}
		task.setEstimatedTime(estimatedTime);	
	}
}
