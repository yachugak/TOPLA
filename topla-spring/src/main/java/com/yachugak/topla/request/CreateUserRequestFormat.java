package com.yachugak.topla.request;

import java.time.OffsetTime;


public class CreateUserRequestFormat {
	private String email;
	private String password;
	private String secureCode;
	private OffsetTime eveningReportTime;
	private OffsetTime morningReportTime;
	
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
	public String getSecureCode() {
		return secureCode;
	}
	public void setSecureCode(String secureCode) {
		this.secureCode = secureCode;
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
	
	

}
