package com.yachugak.topla.plan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.yachugak.topla.entity.Task;
import com.yachugak.topla.exception.NotRegisteredTaskException;
import com.yachugak.topla.service.TaskService;

//여러 날들을 포함하는 시간표
public class TimeTable {
	private ArrayList<Day> days;
	private HashMap<Long, TaskInfo> taskInfoDict;
	
	public TimeTable() {
		this.days = new ArrayList<Day>();
		this.taskInfoDict = new HashMap<Long, TaskInfo>();
	}

	public ArrayList<Day> getDays() {
		return days;
	}

	public void setDays(ArrayList<Day> days) {
		this.days = days;
	} 
	
	public Day getDay(int dayOffset) {
		return days.get(dayOffset);
	}
	
	public void addTaskItem(int dayOffset, TaskItem taskItem) {
		long taskId = taskItem.getTaskId();
		
		if(this.taskInfoDict.get(taskId) == null) {
			throw new NotRegisteredTaskException("사전에 등록되지 않은 taskId:"+taskId);
		}
		
		if(this.days.size() <= dayOffset) {
			int pushCount = dayOffset - this.days.size() + 1;
			for(int i = 0; i < pushCount; i++) {
				this.days.add(new Day());
			}
		}
		
		Day day = this.getDay(dayOffset);
		
		day.getTaskItems().add(taskItem);
	}
	
	public int getLastDayOffset() {
		return days.size()-1;
	}
	
	//이 시간표의 총합 손실 중요도를 계산하여 반환합니다.
	public double getTotalLossPriority(Date planStartDate) {
		LocalDate currentDate = this.portToLocalDate(planStartDate);

		//마감일 내에 성공한 시간과 마감일 내에 성공 못한 시간 구하기
		for(int dayOffset = 0; dayOffset < this.days.size(); dayOffset++) {
			Day day = this.days.get(dayOffset);
			for(TaskItem ti : day.getTaskItems()) {
				long taskId = ti.getTaskId();
				int time = ti.getTime();
				LocalDate dueDate = this.getDueDateFromTaskInfoDict(taskId);
				if(currentDate.isAfter(dueDate)) {
					//마감일 안에 하기 실패한 plan
				}
				else {
					//마감일 안에 성공한 plan
					this.addTimeToTaskInfoDict(taskId, time);
				}
			}
			currentDate = currentDate.plusDays(1);
		}
		
		//작업별 손실 중요도 계산하여 합하기
		double totalLossPriority = 0.0;
		for(long taskId : this.taskInfoDict.keySet()) {
			TaskInfo taskInfo = this.taskInfoDict.get(taskId);
			int priority = taskInfo.priority;

			if(priority == 3) {
				if(taskInfo.totalTime - taskInfo.successTime > 0) {
					totalLossPriority += priority;//중요도 3짜리 작업은 단 1분이라도 끝마치치 못 했다면 그냥 중요도 3추가
				}
				else {
					totalLossPriority += 0;
				}
				continue;
			}
			
			//중요도가 3인 아닌 작업들은 비율에 의해 계산
			double failRatio = 1.0 - ((double)taskInfo.successTime / (double)taskInfo.totalTime);
			
			totalLossPriority += ((double)priority) * failRatio;
		}

		return totalLossPriority;
	}
	
	private LocalDate portToLocalDate(Date date) {
		LocalDate ld = LocalDate.of(date.getYear(), date.getMonth(), date.getDate());
		return ld;
	}
	
	public void registerTask(Task task) {
		this.registerTaskInfo(task.getUid(), task.getEstimatedTime(), task.getPriority(), task.getDueDate());
	}
	
	public void registerTask(long taskUid, TaskInfo taskInfo) {
		TaskInfo newTaskInfo = new TaskInfo();
		newTaskInfo.successTime = taskInfo.successTime;
		newTaskInfo.totalTime = taskInfo.totalTime;
		newTaskInfo.priority = taskInfo.priority;
		newTaskInfo.dueDate = taskInfo.dueDate.plusDays(0);//복사
		
		this.taskInfoDict.put(taskUid, taskInfo);
	}
	
	//임시로 estimateTime을 변경한 task의 estimateTime을 사후 조정할 수 있게 해 주는 함수입니다.
	public void setEstimateTimeOfTask(long taskId, int estimatedTime) {
		TaskInfo taskInfo = this.taskInfoDict.get(taskId);
		if(taskInfo == null) {
			throw new NotRegisteredTaskException("사전에 등록되지 않은 taskId:"+taskId);
		}
		
		taskInfo.totalTime = estimatedTime;
	}
	
	private void registerTaskInfo(long taskId, int totalTime, int priority, Date dueDate) {
		TaskInfo taskInfo = new TaskInfo();
		taskInfo.successTime = 0;
		taskInfo.totalTime = totalTime;
		taskInfo.priority = priority;
		taskInfo.setDueDate(dueDate);
		
		this.taskInfoDict.put(taskId, taskInfo);
	}
	
	private void addTimeToTaskInfoDict(long taskId, int successTime) {
		TaskInfo item = this.taskInfoDict.get(taskId);
		if(item == null) {
			throw new NotRegisteredTaskException("사전에 TimeTable에 등록되지 않은 "+taskId+"가 TimeTable에서 발견되었습니다.");
		}
		
		item.successTime += successTime;
	}
	
	private LocalDate getDueDateFromTaskInfoDict(long taskId) {
		TaskInfo item = this.taskInfoDict.get(taskId);
		if(item == null) {
			throw new NotRegisteredTaskException("사전에 TimeTable에 등록되지 않은 "+taskId+"가 TimeTable에서 발견되었습니다.");
		}
		
		return item.dueDate;
	}
	
	public TimeTable copy() {
		TimeTable copyedTimeTable = new TimeTable();
		
		for(int dayOffset = 0; dayOffset < this.days.size(); dayOffset++) {
			List<TaskItem> taskItemList = this.days.get(dayOffset).getTaskItems();
			
			for(TaskItem ti : taskItemList) {
				TaskItem copyedTaskItem = new TaskItem();
				copyedTaskItem.setTaskId(ti.getTaskId());
				copyedTaskItem.setTime(ti.getTime());
				copyedTaskItem.setPlnaUid(ti.getPlnaUid());
				
				TaskInfo taskInfo = this.taskInfoDict.get(ti.getTaskId());
				
				copyedTimeTable.registerTask(ti.getTaskId(), taskInfo);
				
				copyedTimeTable.addTaskItem(dayOffset, copyedTaskItem);
			}
		}
		
		return copyedTimeTable;
	}
}

class TaskInfo{
	public int successTime = 0;
	public int totalTime = 0;
	public int priority = 1;
	public LocalDate dueDate;
	
	public void setDueDate(Date dueDate) {
		this.dueDate = LocalDate.of(dueDate.getYear(), dueDate.getMonth(), dueDate.getDate());
	}
}