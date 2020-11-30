package com.yachugak.topla;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.yachugak.topla.entity.Task;
import com.yachugak.topla.entity.TaskHistory;
import com.yachugak.topla.repository.TaskHistoryRepository;
import com.yachugak.topla.repository.TaskRepository;
import com.yachugak.topla.service.TaskHistoryService;
import com.yachugak.topla.service.TaskService;

@SpringBootTest
public class TaskTest {
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private TaskHistoryService taskHistoryService;
	
	@Test
	@Transactional(readOnly = false)
	public void createNewTaskWithPriority() {
		int priority = 2;
		Task newTask =  taskService.createNewTask("이것은 제목", priority);
		Optional<Task> findedTaskOp = taskRepository.findByTitle("이것은 제목");
		if(findedTaskOp.isPresent() == false) {
			fail();
		}
		
		Task findedTask = findedTaskOp.get();
		
		assertEquals(priority, findedTask.getPriority());
	}
	
	@Test
	@Transactional(readOnly = false)
	public void createNewTaskWithoutTitle() {
		boolean exceptionFlag = false;
		try {
			Task newTask1 =  taskService.createNewTask(null, 2);
		}catch(Exception e) {
			exceptionFlag = true;
		}
		assertTrue(exceptionFlag);
	}
	
	@Test
	@Transactional(readOnly = false)
	public void createNewTaskWithEmptyString() {
		boolean exceptionFlag = false;
		try {
			Task newTask2 =  taskService.createNewTask("", 2);
		}catch(Exception e) {
			exceptionFlag = true;
		}
		assertTrue(exceptionFlag);
	}
	
	@Test
	@Transactional(readOnly = false)
	public void createNewTaskWithDueDate() {
		Task task = taskService.createNewTask("마감일 테스트", 1);
		Date testDate = new Date();
		testDate.setYear(2030);
		testDate.setMonth(5);
		testDate.setDate(28);
		taskService.setDueDate(task, testDate);
		
		Task result = taskRepository.findById(task.getUid()).get();
		
		assertEquals(testDate, result.getDueDate());
	}
	
	@Test
	@Transactional(readOnly = false)
	public void updateProgress() {
		Task originalTask = taskService.createNewTask("진행도 업데이트 테스트", 1);
		Task task = taskService.findTaskById(originalTask.getUid());
		task.setProgress(50);
		Task resultTask = taskRepository.findById(task.getUid()).get();
		assertEquals(resultTask.getProgress(), 50);
	}
	
	@Test
	@Transactional(readOnly = false)
	public void createNewTaskWithEstimatedTime() {
		Task task = taskService.createNewTask("예상시간 테스트", 3);
		taskService.setEstimatedTime(task, 60);
		
		Task resultTask = taskRepository.findById(task.getUid()).get();
		assertEquals(60, resultTask.getEstimatedTime());
	}
	
	@Test
	@Transactional(readOnly = false)
	public void updateEstimatedTime() {
		Task originalTask = taskService.createNewTask("예상시간 업데이트 테스트", 1);
		Task task = taskService.findTaskById(originalTask.getUid());
		task.setEstimatedTime(60);
		Task resultTask = taskRepository.findById(task.getUid()).get();
		assertEquals(resultTask.getEstimatedTime(), 60);
	}
	
	@Test
	@Transactional(readOnly = false)
	public void finishTask() {
		Task originalTask = taskService.createNewTask("작업 완료 테스트", 3);
		Task updateTask = taskRepository.findByTitle("작업 완료 테스트").get();
		Date nowDate = new Date();
		taskService.setFinishTime(updateTask, nowDate);
		Task resultTask = taskRepository.findByTitle("작업 완료 테스트").get();
		assertEquals(nowDate, resultTask.getFinishDate());
	}
	
	@Test
	@Transactional(readOnly = false)
	public void updateTaskwithLocation() {
		Task originalTask = taskService.createNewTask("location 테스트", 2);
		Task updateTask = taskRepository.findByTitle("location 테스트").get();
		String location = "경기도 용인시 수지구";
		taskService.setLocation(updateTask, location);
		Task resultTask = taskRepository.findByTitle("location 테스트").get();
		assertEquals("경기도 용인시 수지구", resultTask.getLocation());
	}
	
	@Test
	@Transactional(readOnly = false)
	public void updateTaskwithGPSLocation() {
		Task originalTask = taskService.createNewTask("location 테스트2", 2);
		Task updateTask = taskRepository.findByTitle("location 테스트2").get();
		String location = "!GPS(23.3333, 24.4444)";
		taskService.setLocation(updateTask, location);
		Task resultTask = taskRepository.findByTitle("location 테스트2").get();
		assertEquals("!GPS(23.3333, 24.4444)", resultTask.getLocation());
	}
	
	@Test
	@Transactional(readOnly = false)
	public void uncheckFinishedTask() {
		Task originialTask = taskService.createNewTask("완료작업 해제테스트", 2);
		taskService.setEstimatedTime(originialTask, 100);
		taskService.setProgress(originialTask, 100);
		Task updateTask = taskRepository.findByTitle("완료작업 해제테스트").get();
		taskService.setProgress(updateTask, 50);
		Task resultTask = taskRepository.findByTitle("완료작업 해제테스트").get();
		assertEquals(resultTask.getFinishDate(), null);	
	}
	
	@Test
	@Transactional(readOnly = false)
	public void isduplicate() {
		Task a = taskService.createNewTask(1L, "네갈죽", 3);
		Date date = new Date();
		date.setYear(2020);
		date.setMonth(10);
		date.setDate(11);
		taskService.setDueDate(a, date);
		taskService.setEstimatedTime(a, 80);
		taskService.setLocation(a, "");
		
		
		Task dup = taskService.createNewTask(1L, "네갈죽", 3);
		dup.setDueDate(date);
		Task result = taskService.duplicated(dup);
		assertEquals(result.getUid(), a.getUid());
	}
	
	@Test
	@Transactional(readOnly = false)
	public void isnotduplicate() {
		Task a = taskService.createNewTask(1L, "네갈죽", 3);
		Date date = new Date();
		date.setYear(2020);
		date.setMonth(10);
		date.setDate(11);
		taskService.setDueDate(a, date);
		taskService.setEstimatedTime(a, 80);
		taskService.setLocation(a, "");
		
		
		Task dup = taskService.createNewTask(1L, "네갈", 3);
		dup.setDueDate(date);
		Task result = taskService.duplicated(dup);
		assertNotEquals(result.getUid(), a.getUid());
	}
	
	@Test
	@Transactional(readOnly = false)
	public void makeTaskHistory() {
		Task a = taskService.createNewTask(1L, "반갈죽", 3);
		Date date = new Date();
		date.setYear(2020);
		date.setMonth(10);
		date.setDate(11);
		taskService.setDueDate(a, date);
		taskService.setEstimatedTime(a, 80);
		taskService.setLocation(a, "");
		
		TaskHistory testH;
		
		testH = taskHistoryService.createNewHistory(a, 30);
		
		assertEquals(testH.getTask().getUid(), a.getUid());
	}
}
