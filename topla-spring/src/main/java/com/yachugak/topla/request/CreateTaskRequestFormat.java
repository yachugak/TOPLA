package com.yachugak.topla.request;

import java.util.Date;

public class CreateTaskRequestFormat {
	private String title;
	private int priority;
	private Date dueDate;
	private int estimatedTime;
	private String location;
	private Date remindingTiming;
	private boolean duplicated = false;

	
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
	public boolean getDuplicated() {
		return duplicated;
	}
	public void setDuplicated(boolean duplicated) {
		this.duplicated = duplicated;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}	
	public int getEstimatedTime() {
		return estimatedTime;
	}
	public void setEstimatedTime(int estimatedTime) {
		this.estimatedTime = estimatedTime;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Date getRemindingTiming() {
		return remindingTiming;
	}
	public void setRemindingTiming(Date remindingTiming) {
		this.remindingTiming = remindingTiming;
  }
}
