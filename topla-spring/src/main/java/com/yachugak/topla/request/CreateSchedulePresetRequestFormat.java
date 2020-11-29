package com.yachugak.topla.request;

public class CreateSchedulePresetRequestFormat {
	private int[] schedulePreset = new int[7];
	private String presetName;

	public int[] getSchedulePreset() {
		return schedulePreset;
	}

	public void setSchedulePreset(int[] schedulePreset) {
		this.schedulePreset = schedulePreset;
	}

	public String getPresetName() {
		return presetName;
	}

	public void setPresetName(String presetName) {
		this.presetName = presetName;
	}

}
