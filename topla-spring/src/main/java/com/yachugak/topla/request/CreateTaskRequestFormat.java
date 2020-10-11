package com.yachugak.topla.request;

import java.util.Date;

public class CreateTaskRequestFormat {
	private String title;
	private int priority;
	private Date dueDate;
	private int progress;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public int getProgress() {
		return this.progress;
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}
}
