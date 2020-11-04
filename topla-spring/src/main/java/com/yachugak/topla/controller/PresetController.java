package com.yachugak.topla.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yachugak.topla.dataformat.SchedulePresetDataFormat;
import com.yachugak.topla.entity.SchedulePreset;
import com.yachugak.topla.entity.User;
import com.yachugak.topla.request.CreateSchedulePresetRequestFormat;
import com.yachugak.topla.response.SchedulePresetResponseFormat;
import com.yachugak.topla.service.PresetService;
import com.yachugak.topla.service.UserService;

@RestController
@RequestMapping(path = "${apiUriPrefix}/preset")
@CrossOrigin(origins = "*")
public class PresetController {
	@Autowired 
	private PresetService presetService;
	@Autowired
	private UserService userService;
	
	@GetMapping("")
	@Transactional(readOnly = true)
	public SchedulePresetResponseFormat getSelectedSchedulePreset() {
		// TODO: 현재 유저1만 가리킴. 이후 spring security 세팅후에 변경 예정.		
		User user = userService.findUserById(1L);
		SchedulePresetDataFormat presetFormat = presetService.getSelectedPresetInDataFormat(user);
		
		SchedulePresetResponseFormat response = new SchedulePresetResponseFormat();
		response.setSchedulePreset(presetFormat.getHourList());
		return response;
	}
	
	@GetMapping("/list")
	@Transactional(readOnly = true)
	public List<SchedulePresetResponseFormat> getAllSchedulePreset() {
		// TODO: 현재 유저1만 가리킴. 이후 spring security 세팅후에 변경 예정.
		User user = userService.findUserById(1L);
		
		ArrayList<SchedulePresetResponseFormat> res= new ArrayList<>();
		
		List<SchedulePresetDataFormat> plistDataFormats = presetService.getAllPreset(user);
		for (SchedulePresetDataFormat i : plistDataFormats) {
			SchedulePresetResponseFormat tempFormat = new SchedulePresetResponseFormat();
			tempFormat.setSchedulePreset(i.getHourList());
			res.add(tempFormat);
		}
		return res;
	}
	
	@PostMapping("/create")
	@Transactional(readOnly = false)
	public String createSchedulePreset(@RequestBody CreateSchedulePresetRequestFormat req) {
		// TODO: 현재 유저1의 스케줄 프리셋만 생성함. 이후 spring security 세팅후에 변경예정		
		User user = userService.findUserById(1L);
		
		SchedulePresetDataFormat presetFormat = new SchedulePresetDataFormat();
		presetFormat.setHourList(req.getSchedulePreset());
		SchedulePreset preset = presetService.createSchedulePreset(user, presetFormat);
			
		// TODO: 리뷰 필요. 프리셋 생성시 새 프리셋을 기본값으로.
		return "redirect:/api/preset/select?presetUid=" + preset.getUid();
		}
	}
	
	@DeleteMapping("/{uid}")
	@Transactional(readOnly = false)
	public String deleteSchedulePreset(@PathVariable("uid") long uid) {
		presetService.deletePreset(uid);

		return "ok";
	}
	
	@PutMapping("/{uid}")
	@Transactional(readOnly = false)
	public String updateSchedulePreset(@PathVariable("uid") long uid, @RequestBody CreateSchedulePresetRequestFormat req) {
		SchedulePresetDataFormat presetFormat = presetService.convertHourListToDataFormat(req.getSchedulePreset());
		String encodedPreset = presetFormat.encodeHourListToSchedulePresetString();
		
		SchedulePreset updateTarget = presetService.findPresetByID(uid);
		updateTarget.setPresetCode(encodedPreset);
		
		return "ok";
	}
	
	@PutMapping("/select")
	@Transactional(readOnly = false)
	public String selectSchedulePreset(@RequestParam("presetUid") long presetUid) {
		// TODO: 유저1에만 대응
		User user = userService.findUserById(1L);
		userService.setPresetCode(user, presetUid);
		
		return "ok";
	}
		
}
