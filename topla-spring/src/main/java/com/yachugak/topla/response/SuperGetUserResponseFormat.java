package com.yachugak.topla.response;

import java.time.OffsetTime;
import java.util.Date;

public class SuperGetUserResponseFormat {
	private String email;
	private String password;
	private OffsetTime morningReportTime;
	private OffsetTime eveningReportTime;
	private String presetName;
	private String deviceToken;
	private Boolean pushAlarmStatus;
	private Date lastTaskAddedDate;
	private Integer numberOfNewTasksIn7Days;
	private Boolean activeUser;
	
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
	public String getPresetName() {
		return presetName;
	}
	public void setPresetName(String presetName) {
		this.presetName = presetName;
	}
	public String getDeviceToken() {
		return deviceToken;
	}
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	public Boolean getPushAlarmStatus() {
		return pushAlarmStatus;
	}
	public void setPushAlarmStatus(Boolean pushAlarmStatus) {
		this.pushAlarmStatus = pushAlarmStatus;
	}
	public Date getLastTaskAddedDate() {
		return lastTaskAddedDate;
	}
	public void setLastTaskAddedDate(Date lastTaskAddedDate) {
		this.lastTaskAddedDate = lastTaskAddedDate;
	}
	public Integer getNumberOfNewTasksIn7Days() {
		return numberOfNewTasksIn7Days;
	}
	public void setNumberOfNewTasksIn7Days(Integer numberOfNewTasksIn7Days) {
		this.numberOfNewTasksIn7Days = numberOfNewTasksIn7Days;
	}
	public Boolean getActiveUser() {
		return activeUser;
	}
	public void setActiveUser(Boolean activeUser) {
		this.activeUser = activeUser;
	}
	
	
}
