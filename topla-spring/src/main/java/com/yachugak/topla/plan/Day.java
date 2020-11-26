package com.yachugak.topla.plan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.yachugak.topla.plan.TaskItem;

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
	
	public ArrayList<TaskItem> getTaskItemsOrderByTaskId(){
		ArrayList<Long> taskUidList = new ArrayList<>();
		
		for(TaskItem ti : this.taskItems) {
			taskUidList.add(ti.getTaskId());
		}
		
		Collections.sort(taskUidList);
		
		ArrayList<TaskItem> sortedTaskItemList = new ArrayList<>();
		
		for(long taskUid : taskUidList) {
			for(TaskItem ti : this.taskItems) {
				if(ti.getTaskId() == taskUid){
					sortedTaskItemList.add(ti);
				}
			}
		}
		
		return sortedTaskItemList;
	}
	
}
