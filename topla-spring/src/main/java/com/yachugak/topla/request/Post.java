package com.yachugak.topla.request;

import java.io.Serializable;
import java.util.Date;

public class Post implements Serializable {
	private String to;
	Data data;

	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}

	class Data {
		private long taskUid;
		private String title;
		private String message;
		private Date dueDate;
		
		public long getTaskUid() {
			return taskUid;
		}

		public void setTaskUid(long taskUid) {
			this.taskUid = taskUid;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public Date getDueDate() {
			return dueDate;
		}

		public void setDueDate(Date dueDate) {
			this.dueDate = dueDate;
		}
		
	}
}
