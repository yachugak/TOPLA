package com.yachugak.topla.response;

public class SchedulePresetResponseFormat {
	private long presetUid;
	private String presetName;
	private int[] schedulePreset = new int[7];

	public long getPresetUid() {
		return presetUid;
	}

	public void setPresetUid(long presetUid) {
		this.presetUid = presetUid;
	}

	public String getPresetName() {
		return presetName;
	}

	public void setPresetName(String presetName) {
		this.presetName = presetName;
	}

	public int[] getSchedulePreset() {
		return schedulePreset;
	}

	public void setSchedulePreset(int[] schedulePreset) {
		this.schedulePreset = schedulePreset;
	}
	
	
}