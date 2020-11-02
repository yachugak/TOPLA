package com.yachugak.topla.dataformat;

import com.yachugak.topla.exception.InvalidArgumentException;

public class SchedulePresetDataFormat {
	private int[] hourList = new int[7];
	
	public int[] getHourList() {
		return hourList;
	}

	public void setHourList(int[] hourList) {
		this.hourList = hourList.clone();
	}

	/// sun = 0 sat = 6
	/// 시간은 분단위. 0 ~ 1440
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
		
		if(time < 0 || time > 1440) {
			throw new InvalidArgumentException("time", "0~1440", ""+time);
		}
		
		this.hourList[day] = time;
	}
	
	public String encodeHourListToSchedulePresetString() {
		String encodedString=""; 
		
		// 인코딩 형식: 0000 0000 0000 0000 0000 0000 0000
		for(int day = 0; day < 7; day++) {
			encodedString += String.format("%04d", hourList[day]);
		}
		return encodedString;
	}
	
	public void decode(String encodedString) {
		int day = 0;
		for(int pos = 0; pos <= 24; pos += 4) {
			String decodedString = "";
			decodedString += Character.toString(encodedString.charAt(pos))
							+ Character.toString(encodedString.charAt(pos+1))
							+ Character.toString(encodedString.charAt(pos+2))
							+ Character.toString(encodedString.charAt(pos+3));
			hourList[day++] = Integer.parseInt(decodedString);
		}
		return;
	}
}
