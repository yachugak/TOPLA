package com.yachugak.topla.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yachugak.topla.entity.Plan;
import com.yachugak.topla.entity.Report;
import com.yachugak.topla.entity.Task;
import com.yachugak.topla.entity.User;
import com.yachugak.topla.exception.EntityNotFoundException;
import com.yachugak.topla.exception.InvalidArgumentException;
import com.yachugak.topla.repository.PlanRepository;
import com.yachugak.topla.repository.TaskRepository;
import com.yachugak.topla.repository.UserRepository;

@Service
public class TaskService {
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private PlanRepository planRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public List<Task> getAllTask(){
		// TODO: 현재 리포짓 전부 가져옴. 각 유저에 대한 task로 수정필요.
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
	
	public Task createNewTask(long userUid, String title, int priority) {
		Task newTask = this.createNewTask(title, priority);
		Optional<User> owner = userRepository.findByUid(userUid);
		
		if(owner.isPresent() == false) {
			throw new EntityNotFoundException("User", userUid);
		}
		
		newTask.setUser(owner.get());
		
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
		if(priority < 1 || priority > 3) {
			throw new InvalidArgumentException("priority", "1~3", priority+"");
		}
		task.setPriority(priority);
	}
	
	public void setCreatedDate(Task task, Date date) {
		task.setCreatedDate(date);
	}
	
	public void setProgress(Task task, int progress) {
		if(progress < 0 || progress > 100) {
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
	
	public Task findTaskByTitle(String title) {
		Optional<Task> opTask = taskRepository.findByTitle(title);
		if(opTask.isPresent()==false) {
			throw new EntityExistsException("no task found with the title:"+title);
		}
		return opTask.get();
	}

	public void setFinishTime(Task task, Date finishDate) {
		task.setFinishDate(finishDate);
	}

	public void setLocation(Task task, String location) {
		task.setLocation(location);
	}
	
	// task에 할당된 일정을 지웁니다.
	public void clearPlan(Task task) {
		List<Plan> planList = task.getPlans();
		
		for(Plan p : planList) {
			planRepository.delete(p);
		}
	}
	
	public void addPlan(Task task, Date doDate, int doTime) {
		if(doTime < 0 || doTime > 1440) {
			throw new InvalidArgumentException("doTime", "0~1440", ""+doTime);
		}
		
		Plan newPlan = new Plan();
		newPlan.setDoDate(doDate);
		newPlan.setDoTime(doTime);
		newPlan.setTask(task);
		
		planRepository.saveAndFlush(newPlan);
	}
	
	public List<Task> getTaskListToPlan(long userUid, Date planStartDate){
		return taskRepository.findTaskToPlan(userUid, planStartDate);
	}

	public void setRemindingTiming(Task newTask, Date remindingTiming) {
		newTask.setRemindingTiming(remindingTiming);
	}
	
	public Task duplicated(Task task) {
		List<Task> search = taskRepository.findByTitleContains(task.getTitle());
		
		SimpleDateFormat format1 = new SimpleDateFormat("YYYY-MM-DD");
		
		for(Task t : search) {
			String date1 = format1.format(t.getDueDate());
			String date2 = format1.format(task.getDueDate());
			if(date1.equals(date2)) {
				return t;
			}
		}
		
		Task result = new Task();
		result.setUid((long)-1);;
		
		return result;

	}
	
	public void setReport(Report report) {
		List<Task> search = taskRepository.findByFinishDate(report.getReportedDate());
		
		for(Task t:search) {
			t.setReport(report);
		}
	}
}
