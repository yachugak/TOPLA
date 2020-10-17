package com.yachugak.topla.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="task")
public class Task {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long uid;
	
	
	@Column
	private String title;
	
	
	@Column
	@Temporal(TemporalType.DATE)
	private Date dueDate;
	
	@Column
	@Temporal(TemporalType.DATE)
	private Date doDate;
	
	@Column
	private Integer doTime;
	
	@Column
	private String location;
	
	@Column
	private Integer priority;
	
	@Column
	private Integer estimatedTime;
	
	@Column
	private Integer realTime;
	
	@Column
	private Integer progress;
	
	@Column
	private String memo;
	
	@Column
	private Date remindingTiming;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date finishDate;
	
	@ManyToOne
	@JoinColumn(name="user_uid")
	private User user;

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

	public Date getDoDate() {
		return doDate;
	}

	public void setDoDate(Date doDate) {
		this.doDate = doDate;
	}

	public Integer getDoTime() {
		return doTime;
	}

	public void setDoTime(Integer doTime) {
		this.doTime = doTime;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
}
