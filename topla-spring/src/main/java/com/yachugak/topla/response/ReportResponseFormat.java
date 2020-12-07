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
	
	public ReportResponseFormat(Date today, TaskHistoryService taskHistoryService, ReportService reportService) {
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
			try {
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				targetdate = new Date(calendar.getTimeInMillis());
				
				List<TaskHistory> searcHistories = taskHistoryService.findByRecordedTime(targetdate);
				
				for(TaskHistory targetHistory : searcHistories) {
					dueDate.setTime(targetHistory.getTask().getDueDate());
					recordedDate.setTime(targetHistory.getRecordedTime());
					
					total7daysWorkTime += targetHistory.getRealTime();
					averageWorkDoneInDeadline += dueDate.compareTo(recordedDate);
					
					count++;
				}
			} 
			catch (Exception e) {
				continue;
			}
		}
		
		averageWorkDoneInDeadline /= count * (-1);
		
	}

	public Date getTargetdate() {
		return targetdate;
	}

	public void setTargetdate(Date targetdate) {
		this.targetdate = targetdate;
	}

	public Double getTotal7daysWorkTime() {
		return total7daysWorkTime;
	}

	public void setTotal7daysWorkTime(Double total7daysWorkTime) {
		this.total7daysWorkTime = total7daysWorkTime;
	}

	public Double getAverageWorkDoneInDeadline() {
		return averageWorkDoneInDeadline;
	}

	public void setAverageWorkDoneInDeadline(Double averageWorkDoneInDeadline) {
		this.averageWorkDoneInDeadline = averageWorkDoneInDeadline;
	}
	
	
}
