package com.yachugak.topla.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yachugak.topla.dataformat.SchedulePresetDataFormat;
import com.yachugak.topla.entity.SchedulePreset;
import com.yachugak.topla.entity.User;
import com.yachugak.topla.exception.EntityNotFoundException;
import com.yachugak.topla.repository.PresetRepository;

@Service
public class PresetService {
	@Autowired
	private PresetRepository presetRepository;
	
	public List<SchedulePresetDataFormat> getAllPreset(User user) {
		List<SchedulePreset> schedulePresetList = presetRepository.findByUserUid(user.getUid());
		if(schedulePresetList.isEmpty()) {
			throw new EntityNotFoundException("schedulePreset", user.getUid());
		}
		
		List<SchedulePresetDataFormat> schedulePresetDataFormatList = new ArrayList<>();
		for(SchedulePreset preset: schedulePresetList) {
			SchedulePresetDataFormat temp = new SchedulePresetDataFormat();
			temp.decode(preset.getPresetCode());
			temp.setPresetUid(preset.getUid());
			schedulePresetDataFormatList.add(temp);
		}
		
		return schedulePresetDataFormatList;
	}

	public SchedulePreset createSchedulePreset(User user, SchedulePresetDataFormat presetDataFormat) {
		SchedulePreset newPreset = new SchedulePreset();
		newPreset.setUser(user);
		newPreset.setPresetCode(presetDataFormat.encodeHourListToSchedulePresetString());
		presetRepository.saveAndFlush(newPreset);
		
		return newPreset;
	}

	public void deletePreset(long uid) {
		presetRepository.deleteById(uid);
		return;
	}

	public SchedulePresetDataFormat getSelectedPresetInDataFormat(User user) {
		String encodedPreset = user.getSchedulePreset().getPresetCode();
		SchedulePresetDataFormat presetFormat = new SchedulePresetDataFormat();
		presetFormat.decode(encodedPreset);
		presetFormat.setPresetUid(user.getSchedulePreset().getUid());
		return presetFormat;
	}
	
	public SchedulePresetDataFormat getSelectedPresetInDataFormat(long uid) {
		SchedulePreset target = presetRepository.findById(uid).get();
		String encodedPreset = target.getPresetCode();
		SchedulePresetDataFormat presetFormat = new SchedulePresetDataFormat();
		presetFormat.decode(encodedPreset);
		presetFormat.setPresetUid(target.getUid());
		return presetFormat;
	}

	public SchedulePresetDataFormat convertHourListToDataFormat(int[] hourList) {
		SchedulePresetDataFormat presetFormat = new SchedulePresetDataFormat();
		presetFormat.setHourList(hourList);
		return presetFormat;
		
	}
	
	public SchedulePresetDataFormat createDefaultSchedulePreset() {
		int[] temp = new int[7];
		for(int i=0; i<7; i++) {
			temp[i] = 180;
		}
		return this.convertHourListToDataFormat(temp);
	}
	
	public SchedulePreset findPresetByID(long uid) {
		return presetRepository.findById(uid).get();
	}	 
}
