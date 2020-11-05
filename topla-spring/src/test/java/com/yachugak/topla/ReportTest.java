package com.yachugak.topla;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.yachugak.topla.entity.Report;
import com.yachugak.topla.repository.ReportRepository;
import com.yachugak.topla.service.ReportService;

@SpringBootTest
public class ReportTest {
	@Autowired
	private ReportService reportService;
	
	@Autowired
	private ReportRepository reportRepository;
	
	@Test
	@Transactional(readOnly = false)
	public void createNewReport() {
		Date newDate = new Date();
		newDate.setYear(2020);
		newDate.setMonth(11);
		newDate.setDate(05);
		Report test = new Report();
		
		test = reportService.createNewReport(newDate);
		
		assertNotNull(test);
	}
	
	@Test
	@Transactional(readOnly = false)
	public void updateReport() {
		Date newDate = new Date();
		newDate.setYear(2020);
		newDate.setMonth(11);
		newDate.setDate(05);
		Report test = new Report();
		
		test = reportService.createNewReport(newDate);
		test = reportService.updateReport(newDate, 4);
		
		assertNotNull(test);
	}
	
	@Test
	@Transactional(readOnly = true)
	public void findReport() {
		Report test = new Report();
		test = reportService.findReportById((long)3);
		
		assertEquals(test.getReviewScore(), 5);
	}
}
