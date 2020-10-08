package com.yachugak.topla.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="test")
public class Test {
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY) //IDENTITY는 PK 생성 전략을 DBMS에 위임하는 전략
	private Long uid;
	
	@Column(name="v_varchar")
	private String vVarchar;
	
	@Column(name="v_date")
	@Temporal(TemporalType.DATE)
	private Date vDate;
	
	@Column(name="v_datetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date vDatetime;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getvVarchar() {
		return vVarchar;
	}

	public void setvVarchar(String vVarchar) {
		this.vVarchar = vVarchar;
	}

	public Date getvDate() {
		return vDate;
	}

	public void setvDate(Date vDate) {
		this.vDate = vDate;
	}

	public Date getvDatetime() {
		return vDatetime;
	}

	public void setvDatetime(Date vDatetime) {
		this.vDatetime = vDatetime;
	}
}
