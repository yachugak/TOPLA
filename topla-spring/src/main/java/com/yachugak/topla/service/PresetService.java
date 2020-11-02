package com.yachugak.topla.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yachugak.topla.dataformat.SchedulePresetDataFormat;
import com.yachugak.topla.entity.SchedulePreset;
import com.yachugak.topla.exception.EntityNotFoundException;
import com.yachugak.topla.repository.PresetRepository;

@Service
public class PresetService {
	@Autowired
	private PresetRepository presetRepository;
	
	public List<SchedulePresetDataFormat> getAllPreset(long userUid) {
		List<SchedulePreset> schedulePresetList = presetRepository.findByUserUid(userUid);
		if(schedulePresetList.isEmpty()) {
			throw new EntityNotFoundException("schedulePreset", userUid);
		}
		
		List<SchedulePresetDataFormat> schedulePresetDataFormatList = new ArrayList<>();
		for(SchedulePreset preset: schedulePresetList) {
			SchedulePresetDataFormat temp = new SchedulePresetDataFormat();
			temp.decode(preset.getPresetCode());
			schedulePresetDataFormatList.add(temp);
		}
		
		return schedulePresetDataFormatList;
	}

	public void createSchedulePreset(SchedulePresetDataFormat presetDataFormat) {
		// TODO: 현재 유저1의 Preset만. 
		SchedulePreset newPreset = new SchedulePreset();
//		newPreset.setUser(user);
		newPreset.setPresetCode(presetDataFormat);
		
		
		presetRepository.saveAndFlush(newPreset);
	}
	
	
	
	
}
