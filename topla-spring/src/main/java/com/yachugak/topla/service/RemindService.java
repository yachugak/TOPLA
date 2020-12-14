package com.yachugak.topla.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.yachugak.topla.entity.Plan;
import com.yachugak.topla.entity.Task;
import com.yachugak.topla.entity.User;

@Service
public class RemindService {
	@Autowired
	private TaskService taskService;
	@Autowired
	private PlanService planService;
	
	private final RestTemplate restTemplate;

	public RemindService(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	public List<Task> searchTasksToRemind(Date currentTime) {
		return taskService.findTaskByRemindingTimingAndPushAlarmStatus(currentTime);
	}

	public HttpHeaders prepareHeader(String key) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", key);
		return headers;
	}

	public String sendHttpRequestToFirebase(List<Map<String, Object>> dataList) {
		String url = "https://fcm.googleapis.com/fcm/send";
		String key = "key=AAAASJTbBXo:APA91bGD4HAw7LNp4iCKcVarpJLqMDQ8zkhcYO0qzjMC3rUYS5og2cDl6Xu-wL5bMnCjF4y44NbGtkCsjOtZ-F5LrkXPMoowLy3rlt1G1cUlVVGynsUOSpX3Mh42AfazmyuR6T0iqbts";
		HttpHeaders headers = this.prepareHeader(key);

		if (dataList.isEmpty()) {
			return null;
		}

		// 각 "data"를 가지고 HttpBody의 "data" 섹션 완성 및 전송
		for (Map<String, Object> data : dataList) {
			// assemble HttpBody
			Map<String, Object> httpBody = new HashMap<>();
			httpBody.put("to", data.get("deviceToken"));
			httpBody.put("data", data);

			// build Http request
			HttpEntity<Map<String, Object>> entity = new HttpEntity<>(httpBody, headers);

			// send POST request
			ResponseEntity<String> response = this.restTemplate.postForEntity(url, entity, String.class);

			// check response status code
			if (response.getStatusCode() == HttpStatus.CREATED) {
				return response.getBody();
			}
		}
		return null;
	}

	// Remind 푸시알림을 보낼 task의 List를 받아, HttpBody의 "data"를 작성함.
	public List<Map<String, Object>> prepareTaskRemindHttpBodyData(List<Task> targetTasks) {
		List<Map<String, Object>> dataList = new ArrayList<>();

		// 각 task에 대해 map(HTTP Body의 "data")를 만듬.
		for (Task t : targetTasks) {
			String deviceToken = t.getUser().getDeviceToken();
			String taskTitle = t.getTitle();
			Long taskUid = t.getUid();
			Date taskDueDate = t.getDueDate();

			Map<String, Object> data = new HashMap<>();
			data.put("deviceToken", deviceToken);
			data.put("title", taskTitle);
			data.put("taskUid", taskUid);
			data.put("taskDueDate", taskDueDate);
			data.put("message", "TOPLA 앱에서 자세한 내용을 확인해보세요~");
			dataList.add(data);
		}
		return dataList;
	}

	// 아침 푸시알림을 보낼 user의 List를 받아, HttpBody의 "data"를 작성함.
	public List<Map<String, Object>> prepareUserMorningPushHttpBodyData(List<User> targetUsers) {
		List<Map<String, Object>> dataList = new ArrayList<>();

		// 각 User에 대해 map(HTTP Body의 "data")를 만듬.
		for (User u : targetUsers) {			
			String deviceToken = u.getDeviceToken();
			String userEmail = u.getEmail();	
			Date today = new Date();
			String msg;
			
			// 각 유저에게 활당된 plan들 검색.
			List<Plan> planList = planService.findPlanByUserUidAndDoDate(u.getUid(), today);
			
			// TODO: 리뷰 필요. nested for
			msg = this.prepareMorningPushMsg(planList);
						
			Map<String, Object> data = new HashMap<>();
			data.put("deviceToken", deviceToken);
			data.put("email", userEmail);
			data.put("today", today);
			data.put("title", "오늘의 일정!");			
			data.put("message", msg);
			dataList.add(data);	
		}
		return dataList;

	}
	
	// 오늘 배정된 Plan의 List를 받아 메세지를 만듭니다.
	public String prepareMorningPushMsg(List<Plan> planList) {
		String msg = "";
		if(planList.isEmpty()) {
			msg += "일정이 비어있네요~?";
			return msg;
		}
	
		for(Plan p : planList) {
			msg += "- " + p.getTask().getTitle() + "\n";
		}
		return msg;
	}

}
