package com.yachugak.topla.remind;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yachugak.topla.entity.Task;
import com.yachugak.topla.service.RemindService;

@Component
public class ScheduledReminder {
	@Autowired 
	private RemindService remindService;
	
	@Scheduled(cron = "0 * * * * *")
	public void runEveryMin() {
		List<Task> targetTasks = remindService.searchTasksToRemind();
		remindService.sendHttpsRequestToFirebase(targetTasks);

	}
}
