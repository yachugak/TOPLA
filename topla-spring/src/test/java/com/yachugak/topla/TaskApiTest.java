package com.yachugak.topla;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.yachugak.topla.entity.Task;
import com.yachugak.topla.repository.TaskRepository;
import com.yachugak.topla.service.TaskService;

@SpringBootTest
public class TaskApiTest {
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private TaskRepository taskRepository;
	
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
}