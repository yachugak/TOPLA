package com.yachugak.topla.response;

import java.util.Date;

import com.yachugak.topla.entity.Task;

public class TaskResponseFormat {
	private Long uid;
	private String title;
	private Date dueDate;
	private String location;
	private Integer priority;
	private Integer estimatedTime;
	private Integer realTime;
	private Integer progress;
	private String memo;
	private Date remindingTiming;
	private Date createdDate;
	private Date finishDate;
	
	public TaskResponseFormat() {
	}
	
	public TaskResponseFormat(Task task) {
		uid = task.getUid();
		title = task.getTitle();
		dueDate = task.getDueDate();
		location = task.getLocation();
		priority = task.getPriority();
		estimatedTime = task.getEstimatedTime();
		realTime = task.getRealTime();
		progress = task.getProgress();
		memo = task.getMemo();
		remindingTiming = task.getRemindingTiming();
		createdDate = task.getCreatedDate();
		finishDate = task.getFinishDate();
	}
	
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Integer getEstimatedTime() {
		return estimatedTime;
	}
	public void setEstimatedTime(Integer estimatedTime) {
		this.estimatedTime = estimatedTime;
	}
	public Integer getRealTime() {
		return realTime;
	}
	public void setRealTime(Integer realTime) {
		this.realTime = realTime;
	}
	public Integer getProgress() {
		return progress;
	}
	public void setProgress(Integer progress) {
		this.progress = progress;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Date getRemindingTiming() {
		return remindingTiming;
	}
	public void setRemindingTiming(Date remindingTiming) {
		this.remindingTiming = remindingTiming;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}
}
