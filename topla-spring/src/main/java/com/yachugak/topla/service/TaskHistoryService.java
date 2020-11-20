package com.yachugak.topla.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yachugak.topla.entity.Report;
import com.yachugak.topla.entity.Task;
import com.yachugak.topla.entity.TaskHistory;
import com.yachugak.topla.repository.TaskHistoryRepository;

@Service
public class TaskHistoryService {
	@Autowired
	private TaskHistoryRepository taskHistoryRepository;
	
	public TaskHistory createNewHistory(Task task, int realTime) {
		TaskHistory newHistory = new TaskHistory();
		this.setTaskUid(newHistory, task);
		this.setRecordedTime(newHistory, new Date());
		this.setRealTime(newHistory, realTime);
		taskHistoryRepository.saveAndFlush(newHistory);
		
		return newHistory;
		
	}
	
	public void setTaskUid(TaskHistory history, Task task) {
		history.setTask(task);
	}
	
	public void setRecordedTime(TaskHistory history, Date recordedTime) {
		history.setRecordedTime(recordedTime);
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
	
	public List<TaskHistory> findByTaskUid(Long taskUid){
		List<TaskHistory> search = taskHistoryRepository.findByTaskUid(taskUid);
		
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
}
