package com.yachugak.topla.request;

import java.time.OffsetTime;

public class UpdateReportTimeRequestFormat {
	private OffsetTime eveningReportTime;
	private OffsetTime morningReportTime;
	
	public OffsetTime getEveningReportTime() {
		return eveningReportTime;
	}
	public void setEveningReportTime(OffsetTime eveningReportTime) {
		this.eveningReportTime = eveningReportTime;
	}
	public OffsetTime getMorningReportTime() {
		return morningReportTime;
	}
	public void setMorningReportTime(OffsetTime morningReportTime) {
		this.morningReportTime = morningReportTime;
	}

}
