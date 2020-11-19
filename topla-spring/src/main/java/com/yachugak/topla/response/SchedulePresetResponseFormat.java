package com.yachugak.topla.response;

public class SchedulePresetResponseFormat {
	private int[] schedulePreset = new int[7];
	private long presetUid;
	
	public long getPresetUid() {
		return presetUid;
	}

	public void setPresetUid(long presetUid) {
		this.presetUid = presetUid;
	}

	public int[] getSchedulePreset() {
		return schedulePreset;
	}

	public void setSchedulePreset(int[] schedulePreset) {
		this.schedulePreset = schedulePreset;
	}
	
	
}