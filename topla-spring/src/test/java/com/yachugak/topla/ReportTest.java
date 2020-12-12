package com.yachugak.topla;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.CALLS_REAL_METHODS;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.yachugak.topla.entity.Report;
import com.yachugak.topla.entity.TaskHistory;
import com.yachugak.topla.entity.User;
import com.yachugak.topla.repository.ReportRepository;
import com.yachugak.topla.repository.TaskHistoryRepository;
import com.yachugak.topla.repository.UserRepository;
import com.yachugak.topla.response.ReportResponseFormat;
import com.yachugak.topla.service.ReportService;
import com.yachugak.topla.service.TaskHistoryService;
import com.yachugak.topla.util.DayCalculator;

@SpringBootTest
public class ReportTest {
	@Autowired
	private ReportService reportService;
	
	@Autowired
	private ReportRepository reportRepository;
	
	@Autowired
	private TaskHistoryRepository taskHistoryRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TaskHistoryService taskHistoryService;
	
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
	
	@Test
	@Transactional(readOnly = false)
	public void findHistory() {
		List<TaskHistory> test = taskHistoryRepository.findByReport(reportService.findReportById(3L));
		
		assertEquals(2, test.size());
	}
	
	@Test
	@Transactional(readOnly = true)
	public void findHistoryByTimeAndUser() {
		boolean result;
		LocalDate newLocal = LocalDate.of(2020, 11, 19);
		
		Date newDate = java.util.Date.from(newLocal.atStartOfDay()
		         .atZone(ZoneId.systemDefault())
		         .toInstant());
		System.out.println(newDate+"\n"+newLocal);
		
		User user = userRepository.findById(4L).get();
		
		List<TaskHistory> test = taskHistoryRepository.findByRecordedTimeAndUser(newDate, user);
		if(test.isEmpty()) {
			result = false;
		}
		else {
			result = true;
		}
		
		assertTrue(result);
	}
	
	@Test
	@Transactional(readOnly = true)
	public void responseReportTest() {
		LocalDate newLocal = LocalDate.of(2020, 11, 21);
		
		Date newDate = java.util.Date.from(newLocal.atStartOfDay()
		         .atZone(ZoneId.systemDefault())
		         .toInstant());
		User target = userRepository.findById(4L).get();
		ReportResponseFormat test = new ReportResponseFormat(newDate, taskHistoryService, reportService, target);
		
		System.out.println(test.getAverageWorkDoneInDeadline()+"\n"+test.getTotal7daysWorkTime()+"\n"+test.getTargetdate());
	}
	
}
