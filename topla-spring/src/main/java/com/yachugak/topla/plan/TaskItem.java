package com.yachugak.topla.plan;

public class TaskItem {
	private long taskId;
	private int time;
	
	public TaskItem() {
		this.taskId = 0;
		this.time = 0;
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
}
