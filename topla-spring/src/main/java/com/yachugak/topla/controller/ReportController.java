package com.yachugak.topla.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yachugak.topla.entity.Report;
import com.yachugak.topla.request.CreateReportRequestFormat;
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
		Report newReport = reportService.createNewReport(req.getReportedDate(), req.getReviewScore());
		
		return "ok";
	}
	
	@PutMapping("/{uid}")
	@Transactional(readOnly = false)
	public String updateReport(@PathVariable("uid") long uid, @RequestBody CreateReportRequestFormat req) {
		Report updateReport = reportService.findReportById(uid);
		reportService.setReportedDate(updateReport, req.getReportedDate());
		reportService.setReviewScore(updateReport, req.getReviewScore());
		
		return "ok";
	}
}
