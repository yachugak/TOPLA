package com.yachugak.topla.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "schedule_preset")
public class Schedule_preset {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long uid;

	@ManyToOne
	@JoinColumn(name = "user_uid")
	private User user;
	
	@Column(name = "preset_code")
	private String preset_code;

	
	public Long getUid() {
		return uid;
	}
	
	public void setUid(Long uid) {
		this.uid = uid;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getPreset_code() {
		return preset_code;
	}

	public void setPreset_code(String preset_code) {
		this.preset_code = preset_code;
	}
	
}
