package com.yachugak.topla.request;

import java.util.Date;

public class CreateReportRequestFormat {
	private Date reportedDate;
	private int reviewScore;
	
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
	
}
