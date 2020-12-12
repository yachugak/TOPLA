package com.yachugak.topla.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yachugak.topla.entity.Plan;
import com.yachugak.topla.entity.Report;
import com.yachugak.topla.entity.Task;
import com.yachugak.topla.entity.TaskHistory;
import com.yachugak.topla.entity.User;
import com.yachugak.topla.repository.TaskHistoryRepository;

@Service
public class TaskHistoryService {
	@Autowired
	private TaskHistoryRepository taskHistoryRepository;
	
	public TaskHistory createNewHistory(Task task, int doTime) {
		TaskHistory newHistory = new TaskHistory();
		this.setTaskUid(newHistory, task);
		this.setRecordedTime(newHistory, new Date());
		this.setDoTime(newHistory, doTime);
		this.setRealTime(newHistory, doTime);
		taskHistoryRepository.saveAndFlush(newHistory);
		
		return newHistory;
		
	}
	
	public void updateHistoryByPlan(int getProgress, Task targetTask, Plan targetPlan) {
		if(getProgress < 0) {
			List<TaskHistory> historyList = this.findByTask(targetTask);
			
			for(TaskHistory history : historyList) {
				if(history.getDoTime() == targetPlan.getDoTime()) {
					this.deleteHistory(history);
					break;
				}
			}
		}
		else {
			this.createNewHistory(targetTask, getProgress);
		}
	}
	
	public void updateHistoryByTask(Task targetTask) {
		List<TaskHistory> searcHistorie = this.findByTask(targetTask);
		
		for(TaskHistory dHistory : searcHistorie) {
			this.deleteHistory(dHistory);
		}
		
	}
	
	public void setTaskUid(TaskHistory history, Task task) {
		history.setTask(task);
	}
	
	public void setRecordedTime(TaskHistory history, Date recordedTime) {
		history.setRecordedTime(recordedTime);
	}
	
	public void setDoTime(TaskHistory history, int doTime) {
		history.setDoTime(doTime);
	}

	public void setRealTime(TaskHistory history, int realTime) {
		history.setRealTime(realTime);
	}
	
	
	//todo 리포트부분에서 체크 해줘야하는부분
	public void setReportUid(TaskHistory history, Report report) {
		history.setReport(report);
	}
	
	
	public void deleteHistory(TaskHistory history) {
		taskHistoryRepository.delete(history);
	}
	
	public List<TaskHistory> findByTask(Task task){
		List<TaskHistory> search = taskHistoryRepository.findByTask(task);
		
		return search;
	}
	
	
	public List<TaskHistory> findByReportUid(Report report){
		List<TaskHistory> search = taskHistoryRepository.findByReport(report);
		
		return search;
	}
	
	public List<TaskHistory> findByRecordedTime(Date recordedTime){
		List<TaskHistory> search = taskHistoryRepository.findByRecordedTime(recordedTime);
		
		return search;
	}
	
	public List<TaskHistory> findByRecordedTimeAndUser(Date recordedTime, User user){
		List<TaskHistory> search = taskHistoryRepository.findByRecordedTimeAndUser(recordedTime, user);
		
		return search;
	}
}
