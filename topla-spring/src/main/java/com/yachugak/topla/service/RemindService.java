package com.yachugak.topla.service;

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

import com.yachugak.topla.entity.Task;
import com.yachugak.topla.request.Post;

@Service
public class RemindService {
	@Autowired 
	private TaskService taskService;
	
	private final RestTemplate restTemplate;
	
	public RemindService(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}
	
	public List<Task> searchTasksToRemind() {
		Date currentTime = new Date();
		return taskService.findTaskByRemindingTiming(currentTime);
	}
	
	public Post sendHttpsRequestToFirebase(List<Task> targetTasks) {
		String url = "https://fcm.googleapis.com/fcm/send";
		String key = "key=AAAASJTbBXo:APA91bGD4HAw7LNp4iCKcVarpJLqMDQ8zkhcYO0qzjMC3rUYS5og2cDl6Xu-wL5bMnCjF4y44NbGtkCsjOtZ-F5LrkXPMoowLy3rlt1G1cUlVVGynsUOSpX3Mh42AfazmyuR6T0iqbts";
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	    headers.add("Authorization", key);
		
	    
		if(targetTasks.isEmpty()) {
			return null;
		}
		
		for(Task t : targetTasks) {
			// 해당 토큰에 대해, HTTP request를 보내 웹푸시.
			String deviceToken = t.getUser().getDeviceToken();
			String taskTitle = t.getTitle();
			Long taskUid = t.getUid();
			Date taskDueDate = t.getDueDate();
			
			// create a map for post parameters
		    Map<String, Object> map01 = new HashMap<>();
		    Map<String, Object> map02 = new HashMap<>();
		    map01.put("to", deviceToken);
		    map02.put("title", taskTitle);
		    map02.put("taskUid", taskUid);
		    map02.put("taskDueDate", taskDueDate);
		    map02.put("message", "TOPLA 앱에서 확인해보세요~");
		    map01.put("data", map02);
			
		    // build request
			HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map01, headers);
			
			// send POST request
			ResponseEntity<Post> response = this.restTemplate.postForEntity(url, entity, Post.class);

			 // check response status code
		    if (response.getStatusCode() == HttpStatus.CREATED) {
		        return response.getBody();
		    } 
		}
		return null;
	}
	
}
