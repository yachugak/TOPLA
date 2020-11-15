 package com.yachugak.topla.entity;

import java.time.OffsetTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class User {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long uid;
	
	@Column
	private String email;
	
	@Column
	private String password;

	@OneToOne
	@JoinColumn(name = "selected_preset_uid")
	private SchedulePreset schedulePreset;

	@Column
	private OffsetTime morningReportTime;
	
	@Column
	private OffsetTime eveningReportTime;
	
	@Column
	private String deviceToken;
	
	@Column
	private Double totalLossPriority;
	
	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
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

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

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

	public SchedulePreset getSchedulePreset() {
		return schedulePreset;
	}

	public void setSchedulePreset(SchedulePreset schedule_preset) {
		this.schedulePreset = schedule_preset;
	}

	public Double getTotalLossPriority() {
		return totalLossPriority;
	}

	public void setTotalLossPriority(Double totalLossPriority) {
		this.totalLossPriority = totalLossPriority;
	}
	
	
}
