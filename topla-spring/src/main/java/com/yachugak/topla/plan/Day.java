package com.yachugak.topla.plan;

import java.util.ArrayList;

// 어느 하루에 속한 작업들의 목록을 다룸
public class Day {
	private ArrayList<TaskItem> taskItems;
	
	public Day() {
		this.taskItems = new ArrayList<TaskItem>();
	}

	public ArrayList<TaskItem> getTaskItems() {
		return taskItems;
	}

	public void setTaskItems(ArrayList<TaskItem> taskItems) {
		this.taskItems = taskItems;
	}
	
}
