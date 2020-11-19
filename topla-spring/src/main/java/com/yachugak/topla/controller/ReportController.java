package com.yachugak.topla.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yachugak.topla.entity.Report;
import com.yachugak.topla.exception.EntityNotFoundException;
import com.yachugak.topla.request.CreateReportRequestFormat;
import com.yachugak.topla.request.TaskRealtime;
import com.yachugak.topla.service.ReportService;

@RestController
@RequestMapping(path = "${apiUriPrefix}/report")
@CrossOrigin(origins = "*")
public class ReportController {
	@Autowired
	private ReportService reportService;
	
	@PostMapping("")
	@Transactional(readOnly = false)
	public String createNewReport(@RequestBody CreateReportRequestFormat req) {
		for(TaskRealtime rt : req.getTaskRealtime()) {
			if(rt.getRealTime() == null) {
				throw new NullPointerException();
			}
		}
		Report newReport = reportService.createNewReport(req.getReportedDate());
		
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
}
