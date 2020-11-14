package com.yachugak.topla;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.yachugak.topla.dataformat.SchedulePresetDataFormat;
import com.yachugak.topla.entity.Task;
import com.yachugak.topla.plan.Day;
import com.yachugak.topla.plan.Planizer;
import com.yachugak.topla.plan.TaskItem;
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
		task1.setUid(1L); task1.setDueDate(today); task1.setEstimatedTime(120); task1.setPriority(2);

		Task task2 = new Task();
		task2.setUid(2L); task2.setDueDate(today); task2.setEstimatedTime(180); task2.setPriority(3);

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
	
	@Test
	public void totalLossPriorityTest() {
		TimeTable tb = new TimeTable();
		
		for(int i=0; i<=8; i++) {
			tb.getDays().add(new Day());
		}
		
		Task taskA = makeTask(1L, 1, 180, makeDate(2020, 11, 13));
		Task taskB = makeTask(2L, 3, 240, makeDate(2020, 11, 14));
		Task taskC = makeTask(3L, 2, 120, makeDate(2020, 11, 15));
		Task taskD = makeTask(4L, 2, 360, makeDate(2020, 11, 15));
		Task taskE = makeTask(5L, 1, 120, makeDate(2020, 11, 16));
		Task taskF = makeTask(6L, 2, 60, makeDate(2020, 11, 17));
		Task taskG = makeTask(7L, 3, 240, makeDate(2020, 11, 19));
		
		tb.registerTask(taskA);
		tb.registerTask(taskB);
		tb.registerTask(taskC);
		tb.registerTask(taskD);
		tb.registerTask(taskE);
		tb.registerTask(taskF);
		tb.registerTask(taskG);
		
		tb.addTaskItem(0, new TaskItem(taskA.getUid(), 120));

		tb.addTaskItem(1, new TaskItem(taskA.getUid(), 60));
		tb.addTaskItem(1, new TaskItem(taskB.getUid(), 120));

		tb.addTaskItem(2, new TaskItem(taskB.getUid(), 120));

		tb.addTaskItem(3, new TaskItem(taskC.getUid(), 120));
		tb.addTaskItem(3, new TaskItem(taskD.getUid(), 120));

		tb.addTaskItem(4, new TaskItem(taskD.getUid(), 240));

		tb.addTaskItem(5, new TaskItem(taskE.getUid(), 120));
		tb.addTaskItem(5, new TaskItem(taskF.getUid(), 60));
		tb.addTaskItem(5, new TaskItem(taskG.getUid(), 60));

		tb.addTaskItem(6, new TaskItem(taskG.getUid(), 60));
		tb.addTaskItem(7, new TaskItem(taskG.getUid(), 60));
		tb.addTaskItem(8, new TaskItem(taskG.getUid(), 60));

//		tb.addTaskItem(0, new TaskItem(1L, 120, 1, makeDate(2020, 11, 13)));
//
//		tb.addTaskItem(1, new TaskItem(1L, 60, 1, makeDate(2020, 11, 13)));
//		tb.addTaskItem(1, new TaskItem(2L, 120, 3, makeDate(2020, 11, 14)));
//
//		tb.addTaskItem(2, new TaskItem(2L, 120, 3, makeDate(2020, 11, 14)));
//
//		tb.addTaskItem(3, new TaskItem(3L, 120, 2, makeDate(2020, 11, 15)));
//		tb.addTaskItem(3, new TaskItem(4L, 120, 2, makeDate(2020, 11, 15)));
//
//		tb.addTaskItem(4, new TaskItem(4L, 240, 2, makeDate(2020, 11, 15)));
//
//		tb.addTaskItem(5, new TaskItem(5L, 120, 1, makeDate(2020, 11, 16)));
//		tb.addTaskItem(5, new TaskItem(6L, 60, 2, makeDate(2020, 11, 17)));
//		tb.addTaskItem(5, new TaskItem(7L, 60, 3, makeDate(2020, 11, 19)));
//
//		tb.addTaskItem(6, new TaskItem(7L, 60, 3, makeDate(2020, 11, 19)));
//		
//		tb.addTaskItem(7, new TaskItem(7L, 60, 3, makeDate(2020, 11, 19)));
//
//		tb.addTaskItem(8, new TaskItem(7L, 60, 3, makeDate(2020, 11, 19)));
		
		double totalLossPriority = tb.getTotalLossPriority(makeDate(2020,11,12));
		
		assertEquals(5.333333, totalLossPriority, 0.001);
	}
	
	private Date makeDate(int year, int month, int date) {
		Date d = new Date();
		d.setYear(year);
		d.setMonth(month-1);
		d.setDate(date-1);
		
		return d;
	}
	
	private Task makeTask(long uid, int priority, int estimatedTime, Date dueDate) {
		Task temp = new Task();
		temp.setUid(uid);
		temp.setPriority(priority);
		temp.setEstimatedTime(estimatedTime);
		temp.setDueDate(dueDate);
		return temp;
	}
}
