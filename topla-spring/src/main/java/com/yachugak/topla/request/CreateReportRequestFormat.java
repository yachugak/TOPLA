package com.yachugak.topla.request;

import java.util.Date;
import java.util.List;

public class CreateReportRequestFormat {
	private Date reportedDate;
	private int reviewScore;
	private List<TaskRealtime> taskRealtime;
	
	public Date getReportedDate() {
		return reportedDate;
	}
	public int getReviewScore() {
		return reviewScore;
	}
	public void setReportedDate(Date reportedDate) {
		this.reportedDate = reportedDate;
	}
	public void setReviewScore(int reviewScore) {
		this.reviewScore = reviewScore;
	}
	public List<TaskRealtime> getTaskRealtime() {
		return taskRealtime;
	}
	public void setTaskRealtime(List<TaskRealtime> taskRealtime) {
		this.taskRealtime = taskRealtime;
	}
	
}
