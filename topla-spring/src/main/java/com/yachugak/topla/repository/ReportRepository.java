package com.yachugak.topla.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yachugak.topla.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long>{
	public Optional<Report> findByUid(long uid);
	
	public Optional<Report> findByReportedDate(Date reportedDate);
}
