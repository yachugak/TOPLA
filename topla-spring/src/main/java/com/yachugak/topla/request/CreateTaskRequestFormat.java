package com.yachugak.topla.request;

public class CreateTaskRequestFormat {
	private String title;
	private int priority;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
}
