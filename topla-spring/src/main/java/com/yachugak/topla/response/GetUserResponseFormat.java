package com.yachugak.topla.response;

import java.time.LocalTime;

import com.yachugak.topla.entity.SchedulePreset;

public class GetUserResponseFormat {
	private String email;
	private String password;
	private LocalTime eveningReportTime;
	private LocalTime morningReportTime;
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
	public LocalTime getEveningReportTime() {
		return eveningReportTime;
	}
	public void setEveningReportTime(LocalTime eveningReportTime) {
		this.eveningReportTime = eveningReportTime;
	}
	public LocalTime getMorningReportTime() {
		return morningReportTime;
	}
	public void setMorningReportTime(LocalTime morningReportTime) {
		this.morningReportTime = morningReportTime;
	}
	public long getPresetUid() {
		return presetUid;
	}
	public void setPresetUid(long presetUid) {
		this.presetUid = presetUid;
	}
}
