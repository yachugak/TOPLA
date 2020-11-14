package com.yachugak.topla.plan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yachugak.topla.dataformat.SchedulePresetDataFormat;
import com.yachugak.topla.entity.Task;

//일정을 짜는 클래스입니다.
public class Planizer {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private SchedulePresetDataFormat schedulePreset;
	private List<Task> tasks;
	private int day; //0이 일요일
	
	//계산용
	private TimeTable timeTable;
	
	public Planizer(SchedulePresetDataFormat schedulePreset, List<Task> tasks, int day) {
		this.schedulePreset = schedulePreset;
		this.tasks = tasks;
		this.day = day;
	}
	
	public TimeTable plan() {
		//일정 계산 과정을 저장하는 임시 시간표
		this.timeTable = new TimeTable();
		this.timeTable.getDays().add(new Day());
		
		logger.debug(tasks.size() + "개의 작업에 대해 일정을 계산합니다.");
		
		//일정을 마감일 순으로 정렬
		sortTaskByDueDate();

		int nowDayOffset = 0;//현재 작업중인 날짜
		for(Task task : tasks) {
			boolean allocationFlag = false;//현 task의 할당이 성공했는지 기록하는 플래그 변수, flag면 아직 할당 못 했다는 뜻
			int taskLeftTime = task.getEstimatedTime(); //이 작업의 남은 할당 시간
			this.timeTable.registerTask(task);

			while(allocationFlag == false) {
				int allocatedTime = allocateTask(nowDayOffset, task, taskLeftTime);
				
				if(allocatedTime < taskLeftTime) {
					//task가 남은 시간보다 더 커서 일부만 들어갔다면(즉 쪼개졌다면)
					nowDayOffset++;//오늘은 더이상 작업을 넣을 수 없으므로 다음날로 이동하여 계속 작업
					goNextDay();

					taskLeftTime -= allocatedTime;//이 작업의 남은 할당량은 오늘 할당한 만큼을 뺴야 한다.
				}
				else {
					//완전히 다 넣었다면
					allocationFlag = true;//이 task의 할당 작업은 끝, 다음 작업으로 넘어간다.
				}
			}
		}
		
		return this.timeTable;
	}
	
	
	//오늘 날짜에 작업을 할당하는 함수
	//반환값은 오늘 할당에 성공한 시간이다.
	private int allocateTask(int nowDayOffset, Task targetTask, int taskLeftTime) {
		int nowTodayTaskTime = getTodayTaskTime(nowDayOffset);
		int todaySchedulePreset = getTodaySchedulePreset();
		int allocableTime = todaySchedulePreset - nowTodayTaskTime;
		
		long taskId = targetTask.getUid();
		
		if(allocableTime == 0) {
			//오늘 더이상 남은 시간이 없으면 할당하지 않고 넘김
			return 0;
		}
		
		if(allocableTime < taskLeftTime) {
			//남은 시간이 부족하면 남은시간 만큼 할당하고 넘김
			this.timeTable.addTaskItem(nowDayOffset, new TaskItem(taskId, allocableTime));
			return allocableTime;
		}
		else {
			//남은 시간이 충분하면 전부 할당하고 넘김
			this.timeTable.addTaskItem(nowDayOffset, new TaskItem(taskId, taskLeftTime));
			return taskLeftTime;
		}
	}
	
	private void goNextDay() {
		this.day++;
		if(this.day > 6) {
			this.day = 0;
		}
		
		this.timeTable.getDays().add(new Day());
	}
	
	private int getTodayTaskTime(int nowDayOffset) {
		int timeSum = 0;
		List<TaskItem> taskList = this.timeTable.getDay(nowDayOffset).getTaskItems();
		for(TaskItem task : taskList) {
			timeSum += task.getTime();
		}
		
		return timeSum;
	}
	
	private int getTodaySchedulePreset() {
		return this.schedulePreset.getTimeByHour(this.day);
	}
	
	
	

	private void sortTaskByDueDate() {
		Collections.sort(tasks, new Comparator<Task>() {
			@Override
			public int compare(Task t1, Task t2) {
				return t1.getDueDate().compareTo(t2.getDueDate());
			}
		});
	}
}
