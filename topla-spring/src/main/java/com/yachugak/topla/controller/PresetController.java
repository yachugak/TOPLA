package com.yachugak.topla.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yachugak.topla.dataformat.SchedulePresetDataFormat;
import com.yachugak.topla.response.SchedulePresetResponseFormat;
import com.yachugak.topla.service.PresetService;

@RestController
@RequestMapping(path = "${apiUriPrefix}/preset")
@CrossOrigin(origins = "*")
public class PresetController {
	@Autowired 
	private PresetService presetService;
	
	
	@GetMapping("")
	@Transactional(readOnly = true)
	public List<SchedulePresetResponseFormat> viewSchedulePreset() {
		// TODO: 현재 유저1만 가리킴. 이후 spring security 세팅후에 변경 예정.
		ArrayList<SchedulePresetResponseFormat> res= new ArrayList<>();
		
		List<SchedulePresetDataFormat> plistDataFormats = presetService.getAllPreset(1L);
		for (SchedulePresetDataFormat i : plistDataFormats) {
			SchedulePresetResponseFormat tempFormat = new SchedulePresetResponseFormat();
			tempFormat.setSchedulePreset(i.getHourList());
			res.add(tempFormat);
		}
		return res;
	}
	
		
}
