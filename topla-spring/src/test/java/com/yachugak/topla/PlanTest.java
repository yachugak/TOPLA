package com.yachugak.topla;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.yachugak.topla.dataformat.SchedulePresetDataFormat;
import com.yachugak.topla.entity.Task;
import com.yachugak.topla.plan.Planizer;
import com.yachugak.topla.plan.TimeTable;

@SpringBootTest
public class PlanTest {
	
	@Test
	public void planTest() {
		Date today = new Date();
		SchedulePresetDataFormat sp = new SchedulePresetDataFormat();
		for(int i = 0; i < 7; i++) {
			sp.setTimeByHour(i, 240);
		}
		ArrayList<Task> tasks = new ArrayList<>();
		Task task1 = new Task();
		task1.setUid(1L); task1.setDueDate(today); task1.setEstimatedTime(120);

		Task task2 = new Task();
		task2.setUid(2L); task2.setDueDate(today); task2.setEstimatedTime(180);

		tasks.add(task1);
		tasks.add(task2);
		
		Planizer planizer = new Planizer(sp, tasks, 0);
		TimeTable tt = planizer.plan();
		
		
		assertEquals(1L, tt.getDay(0).getTaskItems().get(0).getTaskId());
		assertEquals(2L, tt.getDay(0).getTaskItems().get(1).getTaskId());

		assertEquals(120, tt.getDay(0).getTaskItems().get(0).getTime());
		assertEquals(120, tt.getDay(0).getTaskItems().get(1).getTime());
		assertEquals(60, tt.getDay(1).getTaskItems().get(0).getTime());
	}
}
