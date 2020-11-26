package com.yachugak.topla.plan;

import java.util.Date;

public class TaskItem {
	private long taskId;
	private int time;
	private Long plnaUid = null;//doneTimeTable에서 쓰기 위한 용도
	
	public TaskItem() {
		this.taskId = 0;
		this.time = 0;
		this.plnaUid = null;
	}
	
	public TaskItem(long taskId, int time) {
		this.taskId = taskId;
		this.time = time;
	}
	
	public long getTaskId() {
		return taskId;
	}
	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public Long getPlnaUid() {
		return plnaUid;
	}
	public void setPlnaUid(Long plnaUid) {
		this.plnaUid = plnaUid;
	}
}
