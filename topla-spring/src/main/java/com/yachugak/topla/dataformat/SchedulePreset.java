package com.yachugak.topla.dataformat;

import com.yachugak.topla.exception.InvalidArgumentException;

public class SchedulePreset {
	private int[] hourList = new int[7];
	
	
	/// sun = 0 sat = 6
	/// 시간은 분단위
	public int getTimeByHour(int day) {
		if(day < 0 || day > 7) {
			throw new InvalidArgumentException("day", "0~6", ""+day);
		}
		return this.hourList[day];
	}
	
	public void setTimeByHour(int day, int time) {
		if(day < 0 || day > 7) {
			throw new InvalidArgumentException("day", "0~6", ""+day);
		}
		
		if(time < 0) {
			throw new InvalidArgumentException("time", "0이상", ""+time);
		}
		
		this.hourList[day] = time;
	}
}
