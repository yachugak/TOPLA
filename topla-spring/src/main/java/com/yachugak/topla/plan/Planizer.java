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
import com.yachugak.topla.util.DayCalculator;
import com.yachugak.topla.util.HalfMinutesTime;

import net.bytebuddy.description.type.TypeDescription.Generic.Visitor.Reducing;

//일정을 짜는 클래스입니다.
public class Planizer {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private SchedulePresetDataFormat schedulePreset;
	private List<Task> tasks;
	private int day; //0이 일요일
	private Date planStartDate;
	
	//계산용
	private TimeTable timeTable;
	
	public Planizer(SchedulePresetDataFormat schedulePreset, List<Task> tasks, Date planStartDate) {
		this.schedulePreset = schedulePreset;
		this.tasks = tasks;
		this.planStartDate = planStartDate;
		this.day = planStartDate.getDay();
		this.timeTable = null;
	}
	
	public void setDoneTimeTable(TimeTable doneTimeTable) {
		this.timeTable = doneTimeTable.copy();
	}
	
	public TimeTable greedyPlan() {
		//일정 계산 과정을 저장하는 임시 시간표
		if(this.timeTable == null) {
			this.timeTable = new TimeTable();
			this.timeTable.getDays().add(new Day());
		}
		
		logger.debug(tasks.size() + "개의 작업에 대해 일정을 계산합니다.");
		
		//일정을 마감일 순으로 정렬
		sortTaskByDueDate();

		int nowDayOffset = 0;//현재 작업중인 날짜
		for(Task task : tasks) {
			boolean allocationFlag = false;//현 task의 할당이 성공했는지 기록하는 플래그 변수, flag면 아직 할당 못 했다는 뜻
			int taskLeftTime = task.getEstimatedTime() - task.getProgress(); //이 작업의 남은 할당 시간
			if(taskLeftTime <= 0) {
				continue;
			}
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

	public TimeTable naivelyOptimizedPlan() {
		//일단 마감일 순으로 일정 짜 보고 일정이 터지는지 확인
		Planizer planizer = new Planizer(this.schedulePreset, this.tasks, this.planStartDate);
		if(this.timeTable != null) {
			planizer.setDoneTimeTable(this.timeTable);
		}
		TimeTable greedyResult = planizer.greedyPlan();
		if(greedyResult.getTotalLossPriority(this.planStartDate) <= 0.0) {
			return greedyResult;
		}
		
		//일정이 터졌다면 줄일 것을 찾는다.
		/* loss effect value는 특정 task가 손실 중요도에 얼마나 영향을 미치지는지를 나타내는 지수입니다.
		 * task는 시간이 길고 중요도가 낮을 수록 축소되었을 때 손실 중요도에 영향을 적게 미칩니다.
		 * 따라서 줄일 작업을 선택할 때는 그러한 작업을 선택하는 것이 좋습니다.
		 * loss effect value는 (estimatedTime/priority)로 계산되어 이 수치가 높을수록 줄여도 영향이 작은 task임을 나타냅니다.
		 */
		double maxLossEffectValue = -1.0; //
		Task maxLossEffectTask = null;
		for(Task testTask : this.tasks) {
			int priority = testTask.getPriority();
			int estimatedTime = testTask.getEstimatedTime();
			double lossEffectValue = (double)estimatedTime/(double)priority;
			if(lossEffectValue > maxLossEffectValue) {
				maxLossEffectValue = lossEffectValue;
				maxLossEffectTask = testTask;
			}
		}
		
		logger.debug("축소할 작업: " + maxLossEffectTask.getUid());
		
		//줄일 일정부터 시작해서 그 이후의 일정을 당긴다.
		int originalEstimatedTime = maxLossEffectTask.getEstimatedTime();
		int halfEstimatedTime = HalfMinutesTime.roundUpAndDown(originalEstimatedTime/2);

		//30분 이하라 줄일 수 없으면 그냥 greedyResult를 반환한다.
		if(originalEstimatedTime == halfEstimatedTime) {
			return greedyResult;
		}
		
		//줄였는데 이미 완료된만큼이면 그냥 greedyResult를 반환한다.
		if(halfEstimatedTime <= maxLossEffectTask.getProgress()) {
			return greedyResult;
		}

		maxLossEffectTask.setEstimatedTime(halfEstimatedTime);
		planizer = new Planizer(this.schedulePreset, this.tasks, this.planStartDate);
		if(this.timeTable != null) {
			planizer.setDoneTimeTable(this.timeTable);
		}
		TimeTable reducedResult = planizer.greedyPlan();
		maxLossEffectTask.setEstimatedTime(originalEstimatedTime);
		reducedResult.setEstimateTimeOfTask(maxLossEffectTask.getUid(), originalEstimatedTime);
		
		for(int dayOffset = 0; dayOffset < reducedResult.getDays().size(); dayOffset++) {
			Day d = reducedResult.getDay(dayOffset);
			for(TaskItem ti : d.getTaskItems()) {
				logger.debug("["+dayOffset+"일차] " + ti.getTaskId() + "번 작업을 " + ti.getTime() + "분 합니다.");
			}
		}

		return reducedResult;
	}
	
	public TimeTable fractionalBinPackingPlan() {
		//일단 마감일 순으로 일정 짜 보고 일정이 터지는지 확인
		Planizer planizer = new Planizer(this.schedulePreset, this.tasks, this.planStartDate);
		if(this.timeTable != null) {
			planizer.setDoneTimeTable(this.timeTable);
		}
		TimeTable greedyResult = planizer.greedyPlan();
		if(greedyResult.getTotalLossPriority(this.planStartDate) <= 0.0) {
			return greedyResult;
		}
		
		if(this.timeTable == null) {
			this.timeTable = new TimeTable();
			this.timeTable.getDays().add(new Day());
		}
		
		//일정이 터졌다면
		ArrayList<TaskIdValuePair> valueList = new ArrayList<TaskIdValuePair>();
		
		for(Task task : this.tasks) {
			int priority = task.getPriority();
			int estimatedTime = task.getEstimatedTime();
			int leftDayToDueDate = DayCalculator.calDayOffset(this.planStartDate, task.getDueDate());
			
			double value = (double) priority / (double) estimatedTime;
			value = value / (double) (leftDayToDueDate+1);
			
			valueList.add(new TaskIdValuePair(task.getUid(), value));
		}
		
		//가치 순으로 정렬
		Collections.sort(valueList, Collections.reverseOrder());
		
		int dayOffset0Day = this.day;
		
		//이제 넣자
		for(TaskIdValuePair tivp : valueList) {
			long taskId = tivp.taskId;
			Task task = getTask(taskId);
			int leftTime = 0;
			if(task.getProgress() == null) {
				leftTime = task.getEstimatedTime();
			}
			else {
				leftTime = task.getEstimatedTime() - task.getProgress();
			}
			
			boolean allocationFlag = false;
			int nowDayOffset = 0;
			this.day = dayOffset0Day;
			
			this.timeTable.registerTask(task);
			
			while(leftTime > 0) {
				if(this.isDueOver(nowDayOffset, task)) {
					if(task.getPriority() == 3) {
						this.timeTable.deleteAllUndonTaskItemByTaskId(task.getUid());
					}
					break;
				}
				
				int scheduleLeftTime = this.getScheduleLeftTime(nowDayOffset);
				if(scheduleLeftTime <= 0) {
					nowDayOffset++;
					goNextDay();
					continue;
				}
				
				int allocateTime = this.allocateTask(nowDayOffset, task, leftTime);
				
				leftTime = leftTime - allocateTime;
			}
		}
		
		return this.timeTable;
	}
	
	private int getScheduleLeftTime(int nowDayOffset) {
		int nowTodayTaskTime = getTodayTaskTime(nowDayOffset);
		int todaySchedulePreset = getTodaySchedulePreset();
		
		return todaySchedulePreset - nowTodayTaskTime;
	}

	private boolean isDueOver(int dayOffset, Task task) {
		int leftDay = DayCalculator.calDayOffset(this.planStartDate, task.getDueDate());
		
		if(leftDay < dayOffset) {
			return true;
		}
		
		return false;
	}

	private Task getTask(long taskId) {
		for(Task task : this.tasks) {
			if(task.getUid() == taskId) {
				return task;
			}
		}
		
		return null;
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
		if(nowDayOffset >= this.timeTable.getDays().size()) {
			return 0;
		}
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


class TaskIdValuePair implements Comparable<TaskIdValuePair>{
	public long taskId;
	public double value;
	
	public TaskIdValuePair(long taskId, double value) {
		this.taskId = taskId;
		this.value = value;
	}
	
	@Override
	public int compareTo(TaskIdValuePair compareTarget) {
		if(this.value < compareTarget.value) {
			return -1;
		}
		
		if(this.value == compareTarget.value) {
			return 0;
		}
		
		return 1;
		
	}
}