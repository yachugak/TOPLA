package com.yachugak.topla.response;

import java.time.OffsetTime;

public class GetUserResponseFormat {
	private String email;
	private String password;
	private OffsetTime morningReportTime;
	private OffsetTime eveningReportTime;
	private long presetUid;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
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
	public long getPresetUid() {
		return presetUid;
	}
	public void setPresetUid(long presetUid) {
		this.presetUid = presetUid;
	}
}
