package com.yachugak.topla.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yachugak.topla.entity.Report;
import com.yachugak.topla.exception.EntityNotFoundException;
import com.yachugak.topla.repository.ReportRepository;

@Service
public class ReportService {
	@Autowired
	private ReportRepository reportRepository;
	
	public Report createNewReport(Date reportedDate, int reviewScore) {
		Report newReport = new Report();
		this.setReportedDate(newReport, reportedDate);
		this.setReviewScore(newReport, reviewScore);
		reportRepository.saveAndFlush(newReport);
		
		return newReport;
	}
	
	public void setReportedDate(Report report, Date date) {
		report.setReportedDate(date);
	}
	
	public void setReviewScore(Report report, int reviewScore) {
		report.setReviewScore(reviewScore);
	}
	
	public Report findReportById(Long uid) {
		Optional<Report> opReport = reportRepository.findById(uid);
		if(opReport.isPresent() == false) {
			throw new EntityNotFoundException("report", uid);
		}
		
		return opReport.get();
	}
}
