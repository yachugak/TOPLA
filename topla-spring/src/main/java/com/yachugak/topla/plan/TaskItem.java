package com.yachugak.topla.plan;

import java.util.Date;

public class TaskItem {
	private long taskId;
	private int time;
	private int priority;
	private Date dueDate;
	
	public TaskItem() {
		this.taskId = 0;
		this.time = 0;
	}
	
	public TaskItem(long taskId, int time, int priority, Date dueDate) {
		this.taskId = taskId;
		this.time = time;
		this.priority = priority;
		this.dueDate = dueDate;
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

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
}
