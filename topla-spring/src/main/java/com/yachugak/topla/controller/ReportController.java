package com.yachugak.topla.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yachugak.topla.entity.Report;
import com.yachugak.topla.entity.TaskHistory;
import com.yachugak.topla.exception.EntityNotFoundException;
import com.yachugak.topla.exception.InvalidArgumentException;
import com.yachugak.topla.request.CreateReportRequestFormat;
import com.yachugak.topla.request.TaskRealtime;
import com.yachugak.topla.response.ReportResponseFormat;
import com.yachugak.topla.service.ReportService;
import com.yachugak.topla.service.TaskHistoryService;

@RestController
@RequestMapping(path = "${apiUriPrefix}/report")
@CrossOrigin(origins = "*")
public class ReportController {
	@Autowired
	private ReportService reportService;
	
	@Autowired
	private TaskHistoryService taskHistoryService;
	
	@PostMapping("")
	@Transactional(readOnly = false)
	public String createNewReport(@RequestBody CreateReportRequestFormat req) {
		if(req.getReviewScore()<1 || req.getReviewScore()>5) {
			throw new InvalidArgumentException("reviewScore", "1~5 사이값", "벗어난 값 ");
		}
		for(TaskRealtime rt : req.getTaskRealtime()) {
			if(rt.getRealTime() == null) {
				throw new NullPointerException();
			}
		}
		Report newReport = reportService.createNewReport(req.getReportedDate());
		reportService.setReviewScore(newReport, req.getReviewScore());
		
		List<TaskHistory> historyList = taskHistoryService.findByRecordedTime(req.getReportedDate());
		
		for (TaskHistory th : historyList) {
			th.setReport(newReport);
			for(TaskRealtime update : req.getTaskRealtime()) {
				if(th.getTask().getUid() == update.getTaskUid()) {
					th.setRealTime(update.getRealTime());
				}
			}
		}
		
		return "ok";
	}
	
	@PutMapping("/update")
	@Transactional(readOnly = false)
	public String updateReport(@RequestBody CreateReportRequestFormat req) {
		Report updateReport = reportService.updateReport(req.getReportedDate(), req.getReviewScore());
		
		if(updateReport.getReviewScore() == -1) {
			throw new EntityNotFoundException("report", -1);
		}
		
		return "ok";
	}
	
	@GetMapping("/statisticsReport")
	@Transactional(readOnly = true)
	public ReportResponseFormat statisticsReport(@RequestHeader("Authorization") String email) {
		ReportResponseFormat result = new ReportResponseFormat(new Date(), taskHistoryService, reportService);
		
		return result;
	}
}
