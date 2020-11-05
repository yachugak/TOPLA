package com.yachugak.topla.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yachugak.topla.entity.Task;
import com.yachugak.topla.entity.TaskHistory;
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
		taskHistoryRepository.saveAndFlush(newHistory);
		
		return newHistory;
		
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
	
	
	
	//todo 리포트부분에서 체크 해줘야하는부분
//	public void setReportUid(TaskHistory history, int reportUid) {
//		
//	}
	
	
	public void deleteHistory(TaskHistory history) {
		taskHistoryRepository.delete(history);
	}
	
	public List<TaskHistory> findByTaskUid(Task task){
		List<TaskHistory> search = taskHistoryRepository.findByTaskUid(task.getUid());
		
		return search;
	}
	
	
	//todo 리포트부분에서 체크 해줘야 하는 부분
//	public List<TaskHistory> findByReportUid(Report report){
//		List<TaskHistory> search = taskHistoryRepository.findByReportUid(report.getUid());
//		
//		return search;
//	}
}
