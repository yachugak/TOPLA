package com.yachugak.topla.remind;

import java.time.OffsetTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
//		this.userEveningPush(offsetTime);
	}
	
	// 작업별 리마인드 푸시
	public void taskRemindPush (Date currentTime) {
		List<Task> targetTasks = remindService.searchTasksToRemind(currentTime);
		List<Map<String, Object>> map = remindService.prepareTaskRemindHttpBodyData(targetTasks);
		remindService.sendHttpRequestToFirebase(map);
	}
	
	// 유저별 아침 푸시 
	public void userMorningPush(OffsetTime offsetTime) {
		List<User> targetUsers = userService.findUserByMorningReportTime(offsetTime);
		List<Map<String, Object>> map = remindService.prepareUserMorningPushHttpBodyData(targetUsers);
		remindService.sendHttpRequestToFirebase(map);
	}
	
	// 유저별 오후 푸시
//	public void userEveningPush(OffsetTime offsetTime) {
//		// TODO
//		List<User> targetUsers = userService.findUserByEveningReportTime(offsetTime);
//		List<Map<String, Object>> map = remindService.prepareUserEveningPushHttpBodyData(targetUsers);
//		remindService.sendHttpRequestToFirebase(map);
//	}
}