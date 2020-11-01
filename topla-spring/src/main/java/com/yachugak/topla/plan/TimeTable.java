package com.yachugak.topla.plan;

import java.util.ArrayList;

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
}
