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
import org.springframework.web.bind.annotation.RequestHeader;
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
	public SchedulePresetResponseFormat getSelectedSchedulePreset(@RequestHeader("Authorization") String email) {
		User user = userService.findUserByEmail(email);
		SchedulePreset targetPreset = user.getSchedulePreset(); 
		SchedulePresetDataFormat presetFormat = presetService.convertPresetToDataFormat(targetPreset);
		
		SchedulePresetResponseFormat response = new SchedulePresetResponseFormat();
		response.setPresetUid(targetPreset.getUid());
		response.setPresetName(targetPreset.getName());
		response.setSchedulePreset(presetFormat.getHourList());
		return response;
	}
	
	@GetMapping("/list")
	@Transactional(readOnly = true)
	public List<SchedulePresetResponseFormat> getAllSchedulePreset(@RequestHeader("Authorization") String email) {
		User user = userService.findUserByEmail(email);
		ArrayList<SchedulePresetResponseFormat> res= new ArrayList<>();
		
		List<SchedulePreset> presetList = presetService.getAllPreset(user);
		List<SchedulePresetDataFormat> formatList = presetService.convertPresetListToDataFormats(presetList);
		res = presetService.assemblePresetResponseFormat(presetList, formatList);
		
		return res;
	}
	
	@PostMapping("")
	@Transactional(readOnly = false)
	public String createSchedulePreset(@RequestHeader("Authorization") String email , @RequestBody CreateSchedulePresetRequestFormat req) {
		User user = userService.findUserByEmail(email);
		int[] hourList = req.getSchedulePreset();
		String presetName = req.getPresetName();			
		SchedulePresetDataFormat presetFormat = presetService.convertHourListToDataFormat(hourList);
		
		SchedulePreset preset = presetService.createSchedulePreset(user, presetName, presetFormat);

		return "redirect:/api/preset/select?presetUid=" + preset.getUid();		
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
		String presetName = req.getPresetName();
		SchedulePresetDataFormat presetFormat = presetService.convertHourListToDataFormat(req.getSchedulePreset());
		String encodedPreset = presetFormat.encodeHourListToSchedulePresetString();
		
		SchedulePreset updateTarget = presetService.findPresetByID(uid);
		presetService.setName(updateTarget, presetName);
		presetService.setPresetCode(updateTarget, encodedPreset);
		
		return "ok";
	}
	
	@PutMapping("/select")
	@Transactional(readOnly = false)
	public String selectSchedulePreset(@RequestHeader("Authorization") String email, @RequestParam("presetUid") long presetUid) {
		User user = userService.findUserByEmail(email);
		userService.setSelectedPreset(user, presetUid);
		
		return "ok";
	}
		
}
