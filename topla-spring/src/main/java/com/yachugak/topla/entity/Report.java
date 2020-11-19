package com.yachugak.topla.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="report")
public class Report {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long uid;
	
	@Column
	@Temporal(TemporalType.DATE)
	private Date reportedDate;
	
	@Column
	private Integer reviewScore;
	
	@OneToMany(mappedBy = "report")
	private List<TaskHistory> taskHistory;
	
	public Long getUid() {
		return uid;
	}

	public Date getReportedDate() {
		return reportedDate;
	}

	public Integer getReviewScore() {
		return reviewScore;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public void setReportedDate(Date reportedDate) {
		this.reportedDate = reportedDate;
	}

	public void setReviewScore(Integer reviewScore) {
		this.reviewScore = reviewScore;
	}

	public List<TaskHistory> getTaskHistory() {
		return taskHistory;
	}

	public void setTaskHistory(List<TaskHistory> taskHistory) {
		this.taskHistory = taskHistory;
	}
	
	
}
