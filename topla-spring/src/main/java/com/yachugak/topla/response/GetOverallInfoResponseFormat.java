package com.yachugak.topla.response;

public class GetOverallInfoResponseFormat {
	private Long taskNum;
	private Long doneTaskNum;
	private Long undoneTaskNum;
	private Long allUserNum;
	private Long activeUserNum;
	private Long newTaskNumIn7Days;
	private Long doneTaskNumIn7Days;
	
	public Long getTaskNum() {
		return taskNum;
	}
	public void setTaskNum(Long taskNum) {
		this.taskNum = taskNum;
	}
	public Long getDoneTaskNum() {
		return doneTaskNum;
	}
	public void setDoneTaskNum(Long doneTaskNum) {
		this.doneTaskNum = doneTaskNum;
	}
	public Long getUndoneTaskNum() {
		return undoneTaskNum;
	}
	public void setUndoneTaskNum(Long undoneTaskNum) {
		this.undoneTaskNum = undoneTaskNum;
	}
	public Long getAllUserNum() {
		return allUserNum;
	}
	public void setAllUserNum(Long allUserNum) {
		this.allUserNum = allUserNum;
	}
	public Long getActiveUserNum() {
		return activeUserNum;
	}
	public void setActiveUserNum(Long activeUserNum) {
		this.activeUserNum = activeUserNum;
	}
	public Long getNewTaskNumIn7Days() {
		return newTaskNumIn7Days;
	}
	public void setNewTaskNumIn7Days(Long newTaskNumIn7Days) {
		this.newTaskNumIn7Days = newTaskNumIn7Days;
	}
	public Long getDoneTaskNumIn7Days() {
		return doneTaskNumIn7Days;
	}
	public void setDoneTaskNumIn7Days(Long doneTaskNumIn7Days) {
		this.doneTaskNumIn7Days = doneTaskNumIn7Days;
	}
	
	
	
}
