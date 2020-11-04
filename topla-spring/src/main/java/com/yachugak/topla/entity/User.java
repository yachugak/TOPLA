package com.yachugak.topla.entity;

import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
	private SchedulePreset schedule_preset;
	
	@Column
	@Temporal(TemporalType.TIME)
	private LocalTime report_time;
	
	public LocalTime getReport_time() {
		return report_time;
	}

	public void setReport_time(LocalTime report_time) {
		this.report_time = report_time;
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

	public SchedulePreset getSchedule_preset() {
		return schedule_preset;
	}

	public void setSchedule_preset(SchedulePreset schedule_preset) {
		this.schedule_preset = schedule_preset;
	}
	
}
