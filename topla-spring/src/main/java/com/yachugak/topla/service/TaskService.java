package com.yachugak.topla.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yachugak.topla.entity.Plan;
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
	
	public List<Task> getAllTask(User targetUser){
		if(targetUser == null) {
			throw new InvalidArgumentException("targetUser", "User 객체", null);
		}
		
		return taskRepository.findByUser(targetUser);
	}

	public Task createNewTask(String title, int priority) {

		Task newTask = new Task();
		this.setTitle(newTask, title);
		this.setPriority(newTask, priority);
		this.setEstimatedTime(newTask, 0);
		this.setCreatedDate(newTask, new Date());
		newTask.setProgress(0);	// TODO: initiate 메서드로 나중에 분리?
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
		if(task.getEstimatedTime() == null) {
			throw new InvalidArgumentException("EstimatedTime", "예상시간값", task.getEstimatedTime()+"");
		}
		if(progress < -1 || progress > task.getEstimatedTime()) {
			throw new InvalidArgumentException("progress", "0"+task.getEstimatedTime(), progress+"");
		}
		
		// 미완/해제 시
		if(progress < task.getEstimatedTime()) {
			this.setFinishTime(task, null);
		}
		// 완료시
		else {
			Date time = new Date();
			this.setFinishTime(task, time);
			
			// task 완료시 plan도 완료
			for(Plan p : task.getPlans()) {
				p.setProgress(p.getDoTime());
			}
		}
		
		// 예상시간 0인 작업은 0을 받으면 완료/미완을 구별 불가. 그러므로, task uncheck시 -1 전송.
		if(progress < 0) {
			List<Plan> planList = task.getPlans();
			for(Plan p : planList) {
				p.setProgress(0);
			}
			progress = 0;
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
	
	// task에 할당된 plan을 지우되 목록으로 주어진 list에 존재하는 plan은 지우지 않습니다. witoutList는 삭제하지 않을 plan의 uid를 가집니다.
	public void clearPlanWithout(Task task, List<Long> withoutList) {
		List<Plan> planList = task.getPlans();

		for(Plan p : planList) {
			if(withoutList.indexOf(p.getUid()) < 0) {//witout목록에 없으면 삭제
				planRepository.delete(p);
			}
		}
	}
	
	public void addPlan(Task task, Date doDate, int doTime) {
		if(doTime < 0 || doTime > 1440) {
			throw new InvalidArgumentException("doTime", "0~1440", ""+doTime);
		}
		
		Plan newPlan = new Plan();		
		newPlan.setProgress(0);
		newPlan.setDoDate(doDate);
		newPlan.setDoTime(doTime);
		newPlan.setTask(task);
		
		planRepository.saveAndFlush(newPlan);
		
		if(task.getPlans() == null) {
			List<Plan> temp = new ArrayList<Plan>();
			temp.add(newPlan);
			task.setPlans(temp);
		}
		else {
			task.getPlans().add(newPlan);
		}
	}
	
	public List<Task> getTaskListToPlan(long userUid, Date planStartDate){
		return taskRepository.findTaskToPlan(userUid, planStartDate);
	}

	public void setRemindingTiming(Task newTask, Date remindingTiming) {
		newTask.setRemindingTiming(remindingTiming);
	}
	
	public Task duplicated(Task task) {
		List<Task> search = taskRepository.findByTitleAndDueDateAndUser(task.getTitle(), task.getDueDate(), task.getUser());
		
		if(search.isEmpty()) {
			Task result = new Task();
			result.setUid((long)-1);;
			
			return result;
		}
		
		else {
			return search.get(0);
		}
	}

	// task와 progress 추가감소량을 받아 덧셈뺄셈.
	public void addProgress(Task task, int progressDiff) {
		int prevProgress = task.getProgress();
		int actualProgress = prevProgress + progressDiff;
		this.setProgress(task,  actualProgress);
	}
	
	public List<Task> findTaskByRemindingTiming(Date remindingTiming) {
		return taskRepository.findByRemindingTiming(remindingTiming);
	}
	
}
