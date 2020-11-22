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
@Table(name="task_history")
public class TaskHistory {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long uid;
	
	@ManyToOne
	@JoinColumn(name="task_uid")
	private Task task;

	@Column
	@Temporal(TemporalType.DATE)
	private Date recordedTime;
	
	@Column
	private Integer doTime;
	
	@Column
	private Integer realTime;
	
	@ManyToOne
	@JoinColumn(name = "report_uid")
	private Report report;
	
	public Long getUid() {
		return uid;
	}

	public Task getTask() {
		return task;
	}

	public Date getRecordedTime() {
		return recordedTime;
	}

	public Integer getRealTime() {
		return realTime;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}
	
	public void setRecordedTime(Date recordedTime) {
		this.recordedTime = recordedTime;
	}

	public void setRealTime(Integer realTime) {
		this.realTime = realTime;
	}

	public Integer getDoTime() {
		return doTime;
	}

	public void setDoTime(Integer doTime) {
		this.doTime = doTime;
	}
	
		
}
