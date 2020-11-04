package com.yachugak.topla.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="report")
public class Report {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long uid;
	
	@Column
	private Date reportedDate;
	
	@Column
	private Integer reviewScore;
	
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
	
	
}
