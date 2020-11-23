package com.yachugak.topla.response;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.yachugak.topla.entity.Report;
import com.yachugak.topla.entity.TaskHistory;
import com.yachugak.topla.service.ReportService;
import com.yachugak.topla.service.TaskHistoryService;

public class ReportResponseFormat {
	private Date targetdate;
	private Double total7daysWorkTime;
	private Double averageWorkDoneInDeadline;
	
	@Autowired
	private TaskHistoryService taskHistoryService;
	@Autowired
	private ReportService reportService;
	
	public ReportResponseFormat(Date today) {
		int sur = 0;
		int weekDay = 7;
		total7daysWorkTime = 0.0;
		averageWorkDoneInDeadline = 0.0;
		int count = 0;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		
		Calendar dueDate = Calendar.getInstance();
		Calendar recordedDate = Calendar.getInstance();
		
		
		for(; sur < weekDay ; sur++) {
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			targetdate = new Date(calendar.getTimeInMillis());
			
			Report searchReport = reportService.findReportByReportedDate(targetdate);
			
			List<TaskHistory> searcHistories = taskHistoryService.findByReportUid(searchReport);
			
			for(TaskHistory targetHistory : searcHistories) {
				dueDate.setTime(targetHistory.getTask().getDueDate());
				recordedDate.setTime(targetHistory.getRecordedTime());
				
				total7daysWorkTime += targetHistory.getRealTime();
				averageWorkDoneInDeadline += dueDate.compareTo(recordedDate);
				
				count++;
			}
		}
		
		averageWorkDoneInDeadline /= count;
		
	}
	
}
