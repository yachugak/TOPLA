package com.yachugak.topla.response;

import java.util.Date;

public class PlanResponseFormat {
	private Date doDate;
	private int doTime;
	private long planUid;
	
	public Date getDoDate() {
		return doDate;
	}
	public void setDoDate(Date doDate) {
		this.doDate = doDate;
	}
	public int getDoTime() {
		return doTime;
	}
	public void setDoTime(int doTime) {
		this.doTime = doTime;
	}
	public long getPlanUid() {
		return planUid;
	}
	public void setPlanUid(long planUid) {
		this.planUid = planUid;
	}
}
