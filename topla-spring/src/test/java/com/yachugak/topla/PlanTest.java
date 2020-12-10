package com.yachugak.topla;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.yachugak.topla.controller.TaskController;
import com.yachugak.topla.dataformat.SchedulePresetDataFormat;
import com.yachugak.topla.entity.Plan;
import com.yachugak.topla.entity.Task;
import com.yachugak.topla.entity.User;
import com.yachugak.topla.exception.ToplaException;
import com.yachugak.topla.plan.Day;
import com.yachugak.topla.plan.Planizer;
import com.yachugak.topla.plan.TaskItem;
import com.yachugak.topla.plan.TimeTable;
import com.yachugak.topla.repository.PlanRepository;
import com.yachugak.topla.request.CreateTaskRequestFormat;
import com.yachugak.topla.service.PlanService;
import com.yachugak.topla.service.TaskService;
import com.yachugak.topla.service.UserService;
import com.yachugak.topla.util.DayCalculator;

@SpringBootTest
public class PlanTest {
	@Autowired
	private PlanRepository planRepository;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private PlanService planService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TaskController taskController;
	
	@Test
	public void planTest() {
		Date today = new Date();
		SchedulePresetDataFormat sp = new SchedulePresetDataFormat();
		for(int i = 0; i < 7; i++) {
			sp.setTimeByHour(i, 240);
		}
		ArrayList<Task> tasks = new ArrayList<>();
		Task task1 = new Task();
		task1.setUid(1L); task1.setDueDate(today); task1.setEstimatedTime(120); task1.setPriority(2); task1.setProgress(0);

		Task task2 = new Task();
		task2.setUid(2L); task2.setDueDate(today); task2.setEstimatedTime(180); task2.setPriority(3); task2.setProgress(0);

		tasks.add(task1);
		tasks.add(task2);
		
		Planizer planizer = new Planizer(sp, tasks, DayCalculator.makeDate(2020,11,14));
		TimeTable tt = planizer.greedyPlan();
		
		
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
		
		Task taskA = makeTask(1L, 1, 180, DayCalculator.makeDate(2020, 11, 13));
		Task taskB = makeTask(2L, 3, 240, DayCalculator.makeDate(2020, 11, 14));
		Task taskC = makeTask(3L, 2, 120, DayCalculator.makeDate(2020, 11, 15));
		Task taskD = makeTask(4L, 2, 360, DayCalculator.makeDate(2020, 11, 15));
		Task taskE = makeTask(5L, 1, 120, DayCalculator.makeDate(2020, 11, 16));
		Task taskF = makeTask(6L, 2, 60, DayCalculator.makeDate(2020, 11, 17));
		Task taskG = makeTask(7L, 3, 240, DayCalculator.makeDate(2020, 11, 19));
		
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

//		tb.addTaskItem(0, new TaskItem(1L, 120, 1, DayCalculator.makeDate(2020, 11, 13)));
//
//		tb.addTaskItem(1, new TaskItem(1L, 60, 1, DayCalculator.makeDate(2020, 11, 13)));
//		tb.addTaskItem(1, new TaskItem(2L, 120, 3, DayCalculator.makeDate(2020, 11, 14)));
//
//		tb.addTaskItem(2, new TaskItem(2L, 120, 3, DayCalculator.makeDate(2020, 11, 14)));
//
//		tb.addTaskItem(3, new TaskItem(3L, 120, 2, DayCalculator.makeDate(2020, 11, 15)));
//		tb.addTaskItem(3, new TaskItem(4L, 120, 2, DayCalculator.makeDate(2020, 11, 15)));
//
//		tb.addTaskItem(4, new TaskItem(4L, 240, 2, DayCalculator.makeDate(2020, 11, 15)));
//
//		tb.addTaskItem(5, new TaskItem(5L, 120, 1, DayCalculator.makeDate(2020, 11, 16)));
//		tb.addTaskItem(5, new TaskItem(6L, 60, 2, DayCalculator.makeDate(2020, 11, 17)));
//		tb.addTaskItem(5, new TaskItem(7L, 60, 3, DayCalculator.makeDate(2020, 11, 19)));
//
//		tb.addTaskItem(6, new TaskItem(7L, 60, 3, DayCalculator.makeDate(2020, 11, 19)));
//		
//		tb.addTaskItem(7, new TaskItem(7L, 60, 3, DayCalculator.makeDate(2020, 11, 19)));
//
//		tb.addTaskItem(8, new TaskItem(7L, 60, 3, DayCalculator.makeDate(2020, 11, 19)));
		
		double totalLossPriority = tb.getTotalLossPriority(DayCalculator.makeDate(2020,11,12));
		
		assertEquals(5.333333, totalLossPriority, 0.001);
	}
	
	@Test
	public void totalLossPriority0Test() {
		TimeTable tb = new TimeTable();
		
		tb.getDays().add(new Day());
		
		Task taskA = makeTask(1L, 1, 180, DayCalculator.makeDate(2020, 11, 13));
		
		tb.registerTask(taskA);
		tb.addTaskItem(0, new TaskItem(1L, 180));
		
		Date planStartDate = DayCalculator.makeDate(2020, 11, 13);
		
		double totalLossPriority = tb.getTotalLossPriority(planStartDate);
		
		assertEquals(0.0, totalLossPriority, 0.0001);
		
	}
	
	@Test
	@Transactional(readOnly = false)
	public void totalLoss0Test2() {
		Date planStartDate = DayCalculator.makeDate(2020, 12, 9);
		SchedulePresetDataFormat schedulePreset = new SchedulePresetDataFormat();
		schedulePreset.decode("0240024002400240024002400240");
		ArrayList<Task> testTaskList = new ArrayList<>();
		testTaskList.add(makeTask(1L, 2, 120, planStartDate));
		
		Planizer planizer = new Planizer(schedulePreset, testTaskList, planStartDate);
		TimeTable tt = planizer.fractionalBinPackingPlan();
		double totalLoss = tt.getTotalLossPriority(planStartDate);
		
		assertEquals(0.0, totalLoss, 0.0001);
	}
	@Test()
	public void notRegisterdTaskExceptionTest() {
		Task taskA = makeTask(1, 1, 120, DayCalculator.makeDate(2020,11,14));
		TimeTable tt = new TimeTable();
		
		assertThrows(ToplaException.class, ()->tt.addTaskItem(0, new TaskItem(taskA.getUid(), 60)));
	}
	
	@Test
	public void naivelyOptimizedPlanTest() {
		SchedulePresetDataFormat schedulePreset = new SchedulePresetDataFormat();
		schedulePreset.decode("0000018001800000018002400120");
		ArrayList<Task> testTaskList = new ArrayList<>();
		testTaskList.add(makeTask(1L, 2, 120, DayCalculator.makeDate(2020,6,18)));
		testTaskList.add(makeTask(2L, 1, 60, DayCalculator.makeDate(2020,6,19)));
		testTaskList.add(makeTask(3L, 2, 300, DayCalculator.makeDate(2020,6,21)));
		testTaskList.add(makeTask(4L, 2, 60, DayCalculator.makeDate(2020,6,22)));
		testTaskList.add(makeTask(5L, 3, 240, DayCalculator.makeDate(2020,6,22)));
		testTaskList.add(makeTask(6L, 1, 120, DayCalculator.makeDate(2020,6,23)));
		testTaskList.add(makeTask(7L, 2, 120, DayCalculator.makeDate(2020,6,24)));
		testTaskList.add(makeTask(8L, 2, 120, DayCalculator.makeDate(2020,6,25)));
		testTaskList.add(makeTask(9L, 3, 60, DayCalculator.makeDate(2020,6,25)));
		
		Date planStartDate = DayCalculator.makeDate(2020,6,18);
		
		Planizer planizer = new Planizer(schedulePreset, testTaskList, planStartDate);

		TimeTable oldTimeTable = planizer.greedyPlan();
		double oldTotalLoss = oldTimeTable.getTotalLossPriority(planStartDate);
		
		assertEquals(9.0, oldTotalLoss, 0.0001);
		
		planizer = new Planizer(schedulePreset, testTaskList, planStartDate);

		TimeTable timeTable = planizer.naivelyOptimizedPlan();
		double newTotalLoss = timeTable.getTotalLossPriority(planStartDate);
		System.out.println("totalLoss = " + newTotalLoss);

		assertEquals(1.0, newTotalLoss, 0.0001);
		
	}
	
	@Test
	@Transactional(readOnly = false)
	public void repositoryDateConditionTest() {
		Task tempTask = taskService.createNewTask("aa", 1);
		Plan p = new Plan();
		p.setDoDate(DayCalculator.makeDate(2020, 11, 23));
		p.setDoTime(30);
		p.setProgress(0);
		p.setTask(tempTask);
		
		planRepository.saveAndFlush(p);
		
		List<Plan> resultList = planRepository.findByDoDateGreaterThanEqual(DayCalculator.makeDate(2020, 11, 22));
		
		boolean findFlag = false;

		for(Plan resultItem : resultList) {
			if(resultItem.getUid() == p.getUid()) {
				findFlag = true;
				break;
			}
		}
		
		assertTrue(findFlag);
	}

	public void indexOfTest() {
		List<Long> list = new ArrayList<Long>();
		list.add(1L);
		list.add(2L);
		
		assertTrue(list.indexOf(1L)>= 0);
		assertTrue(list.indexOf(2L)>= 0);
		assertTrue(list.indexOf(3L) < 0);
	}

	private Task makeTask(long uid, int priority, int estimatedTime, Date dueDate) {
		Task temp = new Task();
		temp.setUid(uid);
		temp.setPriority(priority);
		temp.setEstimatedTime(estimatedTime);
		temp.setDueDate(dueDate);
		temp.setProgress(0);
		return temp;
	}
	
	
	@Test
	public void alreadyFinishiedTaskPlanTest() {
		SchedulePresetDataFormat schedulePreset = new SchedulePresetDataFormat();
		schedulePreset.decode("0000018001800000018002400120");
		ArrayList<Task> testTaskList = new ArrayList<>();
		testTaskList.add(makeTask(1L, 2, 120, DayCalculator.makeDate(2020,6,18)));
		testTaskList.add(makeTask(2L, 1, 60, DayCalculator.makeDate(2020,6,19)));

		Task finishTargetTask = makeTask(3L, 2, 300, DayCalculator.makeDate(2020,6,21));
		testTaskList.add(finishTargetTask);

		testTaskList.add(makeTask(4L, 2, 60, DayCalculator.makeDate(2020,6,22)));
		testTaskList.add(makeTask(5L, 3, 240, DayCalculator.makeDate(2020,6,22)));
		testTaskList.add(makeTask(6L, 1, 120, DayCalculator.makeDate(2020,6,23)));
		testTaskList.add(makeTask(7L, 2, 120, DayCalculator.makeDate(2020,6,24)));
		testTaskList.add(makeTask(8L, 2, 120, DayCalculator.makeDate(2020,6,25)));
		testTaskList.add(makeTask(9L, 3, 60, DayCalculator.makeDate(2020,6,25)));
		
		Date planStartDate = DayCalculator.makeDate(2020,6,18);
		
		//task id 3번을 19일에 미리 2시간 완료시켜 놓는다.
		Plan alreadyFinishedPlan = new Plan();
		alreadyFinishedPlan.setUid(1L);
		alreadyFinishedPlan.setDoDate(DayCalculator.makeDate(2020, 6, 19));
		alreadyFinishedPlan.setDoTime(120);
		alreadyFinishedPlan.setProgress(120);
		alreadyFinishedPlan.setTask(finishTargetTask);
		finishTargetTask.setProgress(120);
		ArrayList<Plan> temp = new ArrayList<>();
		temp.add(alreadyFinishedPlan);
		finishTargetTask.setPlans(temp);
		
		TimeTable doneTimeTable = new TimeTable();
		doneTimeTable.registerTask(finishTargetTask);
		TaskItem ti = new TaskItem();
		ti.setPlnaUid(alreadyFinishedPlan.getUid());
		ti.setTaskId(alreadyFinishedPlan.getTask().getUid());
		ti.setTime(120);
		doneTimeTable.addTaskItem(0, ti);
		
		Planizer planizer = new Planizer(schedulePreset, testTaskList, planStartDate);
		planizer.setDoneTimeTable(doneTimeTable);
		TimeTable greedyTimeTable = planizer.greedyPlan();
		
		//첫 날 검증
		ArrayList<TaskItem> day0List = greedyTimeTable.getDay(0).getTaskItemsOrderByTaskId();
		assertEquals(1L, day0List.get(0).getTaskId());
		assertEquals(60, day0List.get(0).getTime());
		assertEquals(3L, day0List.get(1).getTaskId());
		assertEquals(120, day0List.get(1).getTime());
		assertEquals(1L, day0List.get(1).getPlnaUid());
		
		//둘 째날 검증
		ArrayList<TaskItem> day1List = greedyTimeTable.getDay(1).getTaskItemsOrderByTaskId();
		assertEquals(1L, day1List.get(0).getTaskId());
		assertEquals(60, day1List.get(0).getTime());
		assertEquals(2L, day1List.get(1).getTaskId());
		assertEquals(60, day1List.get(1).getTime());
		assertEquals(3L, day1List.get(2).getTaskId());
		assertEquals(120, day1List.get(2).getTime());
		assertEquals(null, day1List.get(2).getPlnaUid());
		
		//셋 째날부터는 greedy plan과 동일하여 굳이 검증하지 않음.
	}
	

	@Test
	public void alreadyFinishiedTaskNaivelyOptimizedPlanTest() {
		SchedulePresetDataFormat schedulePreset = new SchedulePresetDataFormat();
		schedulePreset.decode("0000018001800000018002400120");
		ArrayList<Task> testTaskList = new ArrayList<>();
		testTaskList.add(makeTask(1L, 2, 120, DayCalculator.makeDate(2020,6,18)));
		testTaskList.add(makeTask(2L, 1, 60, DayCalculator.makeDate(2020,6,19)));

		Task finishTargetTask = makeTask(3L, 2, 300, DayCalculator.makeDate(2020,6,21));
		testTaskList.add(finishTargetTask);

		testTaskList.add(makeTask(4L, 2, 60, DayCalculator.makeDate(2020,6,22)));
		testTaskList.add(makeTask(5L, 3, 240, DayCalculator.makeDate(2020,6,22)));
		testTaskList.add(makeTask(6L, 1, 120, DayCalculator.makeDate(2020,6,23)));
		testTaskList.add(makeTask(7L, 2, 120, DayCalculator.makeDate(2020,6,24)));
		testTaskList.add(makeTask(8L, 2, 120, DayCalculator.makeDate(2020,6,25)));
		testTaskList.add(makeTask(9L, 3, 60, DayCalculator.makeDate(2020,6,25)));
		
		Date planStartDate = DayCalculator.makeDate(2020,6,18);
		
		//task id 3번을 19일에 미리 2시간 완료시켜 놓는다.
		Plan alreadyFinishedPlan = new Plan();
		alreadyFinishedPlan.setUid(1L);
		alreadyFinishedPlan.setDoDate(DayCalculator.makeDate(2020, 6, 19));
		alreadyFinishedPlan.setDoTime(120);
		alreadyFinishedPlan.setProgress(120);
		alreadyFinishedPlan.setTask(finishTargetTask);
		finishTargetTask.setProgress(120);
		ArrayList<Plan> temp = new ArrayList<>();
		temp.add(alreadyFinishedPlan);
		finishTargetTask.setPlans(temp);
		
		TimeTable doneTimeTable = new TimeTable();
		doneTimeTable.registerTask(finishTargetTask);
		TaskItem ti = new TaskItem();
		ti.setPlnaUid(alreadyFinishedPlan.getUid());
		ti.setTaskId(alreadyFinishedPlan.getTask().getUid());
		ti.setTime(120);
		doneTimeTable.addTaskItem(0, ti);
		
		Planizer planizer = new Planizer(schedulePreset, testTaskList, planStartDate);
		planizer.setDoneTimeTable(doneTimeTable);
		TimeTable naiveTimeTable = planizer.naivelyOptimizedPlan();
		
		assertEquals(2.0, naiveTimeTable.getTotalLossPriority(planStartDate), 0.001);
	}

	@Test
	public void taskItemSortTest() {
		Task task1 = makeTask(1,1, 60, DayCalculator.makeDate(2020, 11, 24)); 
		Task task2 = makeTask(2,1, 60, DayCalculator.makeDate(2020, 11, 24)); 

		TaskItem ti1 = new TaskItem();
		TaskItem ti2 = new TaskItem();
		
		ti1.setTaskId(1);
		ti2.setTaskId(2);
		
		TimeTable tt = new TimeTable();
		tt.registerTask(task1);
		tt.registerTask(task2);

		tt.addTaskItem(0, ti2);
		tt.addTaskItem(0, ti1);
		
		ArrayList<TaskItem> sortedResult = tt.getDay(0).getTaskItemsOrderByTaskId();
		
		assertEquals(1L, sortedResult.get(0).getTaskId());
		assertEquals(2L, sortedResult.get(1).getTaskId());
	}
	
	@Test
	@Transactional(readOnly = false)
	public void realPlanFixTest() {
		User user = userService.findUserByEmail("test@acount.net");
		Date today = DayCalculator.makeDate(2020, 11, 25);
		Date nextDay = DayCalculator.makeDate(2020, 11, 26);
		
		Task task1 = taskService.createNewTask(user.getUid(), "작업1", 2);
		task1.setEstimatedTime(120);
		task1.setDueDate(nextDay);
		
		planService.plan(user, today);
		
		List<Plan> task1PlanList = planService.findByTask(task1);
		task1.setPlans(task1PlanList);
		long planUid = task1PlanList.get(0).getUid();
		
		planService.setProgress(task1PlanList.get(0), 120);
		
		Task task2 = taskService.createNewTask(user.getUid(), "작업2", 1);
		task2.setEstimatedTime(120);
		task2.setDueDate(today);
		
		planService.plan(user, today);
		
		task1PlanList = planService.findByTask(task1);
		task1.setPlans(task1PlanList);
		List<Plan> task2PlanList = planService.findByTask(task2);
		task2.setPlans(task2PlanList);
		
		assertEquals(planUid, task1PlanList.get(0).getUid());
		assertEquals(120, task1PlanList.get(0).getDoTime());
		assertEquals(120, task1PlanList.get(0).getProgress());
		assertTrue(dateEqual(DayCalculator.makeDate(2020, 11, 25), task1PlanList.get(0).getDoDate()));

		assertEquals(60, task2PlanList.get(0).getDoTime());
		assertTrue(dateEqual(DayCalculator.makeDate(2020, 11, 25), task2PlanList.get(0).getDoDate()));
	}
	
	@Test
	public void FractionalBinPackingPlanTest() {
		SchedulePresetDataFormat schedulePreset = new SchedulePresetDataFormat();
		schedulePreset.decode("0000018001800000018002400120");
		ArrayList<Task> testTaskList = new ArrayList<>();
		testTaskList.add(makeTask(1L, 2, 120, DayCalculator.makeDate(2020,6,18)));
		testTaskList.add(makeTask(2L, 1, 60, DayCalculator.makeDate(2020,6,19)));
		testTaskList.add(makeTask(3L, 2, 300, DayCalculator.makeDate(2020,6,21)));
		testTaskList.add(makeTask(4L, 2, 60, DayCalculator.makeDate(2020,6,22)));
		testTaskList.add(makeTask(5L, 3, 240, DayCalculator.makeDate(2020,6,22)));
		testTaskList.add(makeTask(6L, 1, 120, DayCalculator.makeDate(2020,6,23)));
		testTaskList.add(makeTask(7L, 2, 120, DayCalculator.makeDate(2020,6,24)));
		testTaskList.add(makeTask(8L, 2, 120, DayCalculator.makeDate(2020,6,25)));
		testTaskList.add(makeTask(9L, 3, 60, DayCalculator.makeDate(2020,6,25)));
		
		Date planStartDate = DayCalculator.makeDate(2020,6,18);

		Planizer planizer = new Planizer(schedulePreset, testTaskList, planStartDate);
		TimeTable result = planizer.fractionalBinPackingPlan();
		
		assertEquals(1L, result.getDay(0).getTaskItemsOrderByTaskId().get(0).getTaskId());
		assertEquals(120, result.getDay(0).getTaskItemsOrderByTaskId().get(0).getTime());

		assertEquals(2L, result.getDay(0).getTaskItemsOrderByTaskId().get(1).getTaskId());
		assertEquals(60, result.getDay(0).getTaskItemsOrderByTaskId().get(1).getTime());

		assertEquals(4L, result.getDay(1).getTaskItemsOrderByTaskId().get(0).getTaskId());
		assertEquals(60, result.getDay(1).getTaskItemsOrderByTaskId().get(0).getTime());

		assertEquals(5L, result.getDay(1).getTaskItemsOrderByTaskId().get(1).getTaskId());
		assertEquals(120, result.getDay(1).getTaskItemsOrderByTaskId().get(1).getTime());

		assertEquals(9L, result.getDay(1).getTaskItemsOrderByTaskId().get(2).getTaskId());
		assertEquals(60, result.getDay(1).getTaskItemsOrderByTaskId().get(2).getTime());

		assertEquals(5L, result.getDay(2).getTaskItemsOrderByTaskId().get(0).getTaskId());
		assertEquals(120, result.getDay(2).getTaskItemsOrderByTaskId().get(0).getTime());

		assertEquals(7L, result.getDay(4).getTaskItemsOrderByTaskId().get(0).getTaskId());
		assertEquals(120, result.getDay(4).getTaskItemsOrderByTaskId().get(0).getTime());

		assertEquals(8L, result.getDay(4).getTaskItemsOrderByTaskId().get(1).getTaskId());
		assertEquals(60, result.getDay(4).getTaskItemsOrderByTaskId().get(1).getTime());

		assertEquals(6L, result.getDay(5).getTaskItemsOrderByTaskId().get(0).getTaskId());
		assertEquals(120, result.getDay(5).getTaskItemsOrderByTaskId().get(0).getTime());

		assertEquals(8L, result.getDay(5).getTaskItemsOrderByTaskId().get(1).getTaskId());
		assertEquals(60, result.getDay(5).getTaskItemsOrderByTaskId().get(1).getTime());


		assertEquals(2.0, result.getTotalLossPriority(planStartDate), 0.001);
	}

	//doTime이 minus가 되던 상황을 재현
	@Test
	@Transactional(readOnly = false)
	public void planDoTimeMinusCase() {
		CreateTaskRequestFormat req = new CreateTaskRequestFormat();
		req.setTitle("aaa");
		req.setPriority(1);
		req.setDueDate(DayCalculator.makeDate(2020, 11, 27));
		req.setEstimatedTime(240);
		req.setDuplicated(false);
		
		String secureCode = userService.authMapping(userService.findUserByEmail("test@acount.net"));

		String res = this.taskController.createNewTask(secureCode, req);
		
		//예외 안 던져지면 성공
		assertEquals("ok", res);
	}
	
	//중요도 3작업이 마감일밖으로 나갈 때 기존 할당된 plan을 지우는 상황에서 버그가 나는 상황을 재현
	@Test
	public void deleteUnDonePlanOfPriority3Test() {
		SchedulePresetDataFormat schedulePreset = new SchedulePresetDataFormat();
		schedulePreset.decode("0240024002400240024002400240");
		ArrayList<Task> testTaskList = new ArrayList<>();
		testTaskList.add(makeTask(1L, 3, 300, DayCalculator.makeDate(2020,11,28)));
		
		Date planStartDate = DayCalculator.makeDate(2020, 11, 28);

		Planizer planizer = new Planizer(schedulePreset, testTaskList, planStartDate);
		TimeTable result = planizer.fractionalBinPackingPlan();
		
		assertEquals(0, result.getDay(0).getTaskItems().size());
	}


	@Test
	@Transactional(readOnly = false)
	public void planInJanuaryTest() {
		Task task = taskService.createNewTask(1L, "1월 넣기 테스트", 1);
		taskService.setDueDate(task, DayCalculator.makeDate(2021, 1, 15));
		taskService.setProgress(task, 0);
		taskService.setEstimatedTime(task, 60);
		
		assertTrue(task.getUid() != null);
		
		long taskId = task.getUid();

		SchedulePresetDataFormat schedulePreset = new SchedulePresetDataFormat();
		schedulePreset.decode("0240024002400240024002400240");

		ArrayList<Task> testTaskList = new ArrayList<>();
		testTaskList.add(task);
		
		Date planStartDate = DayCalculator.makeDate(2021, 1, 1);

		Planizer planizer = new Planizer(schedulePreset, testTaskList, planStartDate);
		TimeTable result = planizer.fractionalBinPackingPlan();
		
		assertEquals(taskId, result.getDay(0).getTaskItemsOrderByTaskId().get(0).getTaskId());
		assertEquals(60, result.getDay(0).getTaskItemsOrderByTaskId().get(0).getTime());

	}
	
	
	private boolean dateEqual(Date date1, Date date2) {
		if(date1.getYear() != date2.getYear()) {
			return false;
		}
		if(date1.getMonth() != date2.getMonth()) {
			return false;
		}
		if(date1.getDate() != date2.getDate()) {
			return false;
		}
		
		return true;
	}
}
