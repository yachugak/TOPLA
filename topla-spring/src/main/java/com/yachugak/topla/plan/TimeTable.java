package com.yachugak.topla.plan;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.yachugak.topla.service.TaskService;

//여러 날들을 포함하는 시간표
public class TimeTable {
	private ArrayList<Day> days;
	
	public TimeTable() {
		this.days = new ArrayList<Day>();
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
		Day day = this.getDay(dayOffset);
		
		day.getTaskItems().add(taskItem);
	}
	
	public int getLastDayOffset() {
		return days.size()-1;
	}
	
	//이 시간표의 총합 손실 중요도를 계산하여 반환합니다.
	public double getTotalLossPriority(Date planStartDate) {
		HashMap<Long, SuccessAndFailTimeOfTask> taskTimeDict = new HashMap<>();
		HashMap<Long, Integer> taskPriorityDict = new HashMap<>();
		ArrayList<Long> taskIdList = new ArrayList<>();
		LocalDate currentDate = this.portToLocalDate(planStartDate);

		//마감일 내에 성공한 시간과 마감일 내에 성공 못한 시간 구하기
		for(int dayOffset = 0; dayOffset < this.days.size(); dayOffset++) {
			Day day = this.days.get(dayOffset);
			for(TaskItem ti : day.getTaskItems()) {
				long taskId = ti.getTaskId();
				this.addTaskIdToList(taskIdList, taskId);
				int priority = ti.getPriority();
				taskPriorityDict.put(taskId, priority);
				LocalDate dueDate = this.portToLocalDate(ti.getDueDate());
				int time = ti.getTime();
				if(currentDate.isAfter(dueDate)) {
					//마감일 안에 하기 실패한 plan
					this.addTimeToDict(taskTimeDict, taskId, 0, time);
				}
				else {
					//마감일 안에 성공한 plan
					this.addTimeToDict(taskTimeDict, taskId, time, 0);
				}
			}
			currentDate = currentDate.plusDays(1);
		}
		
		//작업별 손실 중요도 계산하여 합하기
		double totalLossPriority = 0.0;
		for(long taskId : taskIdList) {
			SuccessAndFailTimeOfTask timeInfo = taskTimeDict.get(taskId);
			int priority = taskPriorityDict.get(taskId);

			if(priority == 3) {
				if(timeInfo.failTime > 0) {
					totalLossPriority += priority;//중요도 3짜리 작업은 단 1분이라도 끝마치치 못 했다면 그냥 중요도 3추가
				}
				else {
					totalLossPriority += 0;
				}
				continue;
			}
			
			//중요도가 3인 아닌 작업들은 비율에 의해 계산
			double failRatio = ((double)timeInfo.failTime) / (double)(timeInfo.successTime+timeInfo.failTime);
			
			totalLossPriority += ((double)priority) * failRatio;
		}

		return totalLossPriority;
	}
	
	private LocalDate portToLocalDate(Date date) {
		LocalDate ld = LocalDate.of(date.getYear(), date.getMonth(), date.getDate());
		return ld;
	}
	
	private void addTimeToDict(HashMap<Long, SuccessAndFailTimeOfTask> taskTimeDict, long taskId, int successTime, int failTime) {
		SuccessAndFailTimeOfTask item = taskTimeDict.get(taskId);
		if(item == null) {
			item = new SuccessAndFailTimeOfTask();
			item.successTime = 0;
			item.failTime = 0;
			taskTimeDict.put(taskId, item);
		}
		
		item.successTime += successTime;
		item.failTime += failTime;
	}
	
	private void addTaskIdToList(List<Long> list, long taskId) {
		if(list.contains(taskId) == false) {
			list.add(taskId);
		}
	}
}

class SuccessAndFailTimeOfTask{
	public int successTime = 0;
	public int failTime = 0;
}