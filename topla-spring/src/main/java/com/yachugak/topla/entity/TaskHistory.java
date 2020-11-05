package com.yachugak.topla.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="task_history")
public class TaskHistory {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long uid;
	
	@Column
	@JoinColumn(name="task_uid")
	private Task task;

	@Column
	@Temporal(TemporalType.DATE)
	private Date recordedTime;
	
	@Column
	private Integer doTime;
	
	
	//todo reportUid를 리포트랑 묶어야함 이건 리포트브랜치에서 만들것
	@Column
	private Long reportUid;
	
	public Long getUid() {
		return uid;
	}

	public Task getTask() {
		return task;
	}

	public Date getRecordedTime() {
		return recordedTime;
	}

	public Integer getDoTime() {
		return doTime;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public void setRecordedTime(Date recordedTime) {
		this.recordedTime = recordedTime;
	}

	public void setDoTime(Integer doTime) {
		this.doTime = doTime;
	}
}
