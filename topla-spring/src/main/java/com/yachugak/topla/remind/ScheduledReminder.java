package com.yachugak.topla.remind;

import java.time.OffsetTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yachugak.topla.entity.Task;
import com.yachugak.topla.entity.User;
import com.yachugak.topla.service.RemindService;
import com.yachugak.topla.service.UserService;

@Component
public class ScheduledReminder {
	@Autowired 
	private RemindService remindService;
	@Autowired
	private UserService userService;
	
	@Scheduled(cron = "0 * * * * *")
	public void runEveryMin() {
		Date currentTime = new Date();
		OffsetTime offsetTime = OffsetTime.now();
		
		this.taskRemindPush(currentTime);
		this.userMorningPush(offsetTime);
		this.userEveningPush(offsetTime);
	}
	
	public void taskRemindPush (Date currentTime) {
		List<Task> targetTasks = remindService.searchTasksToRemind(currentTime);
		remindService.sendHttpsRequestToFirebase(targetTasks);
	}
	
	public void userMorningPush(OffsetTime offsetTime) {
		List<User> targetUsers = userService.findUserByMorningReportTime(offsetTime);
		
	}
	
	public void userEveningPush(OffsetTime offsetTime) {
		// TODO
	}
}
