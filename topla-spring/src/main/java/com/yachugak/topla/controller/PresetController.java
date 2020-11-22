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
		SchedulePresetDataFormat presetFormat = presetService.getSelectedPresetInDataFormat(user);
		
		SchedulePresetResponseFormat response = new SchedulePresetResponseFormat();
		response.setSchedulePreset(presetFormat.getHourList());
		response.setPresetUid(presetFormat.getPresetUid());
		return response;
	}
	
	@GetMapping("/list")
	@Transactional(readOnly = true)
	public List<SchedulePresetResponseFormat> getAllSchedulePreset(@RequestHeader("Authorization") String email) {
		User user = userService.findUserByEmail(email);
		ArrayList<SchedulePresetResponseFormat> res= new ArrayList<>();
		
		List<SchedulePresetDataFormat> plistDataFormats = presetService.getAllPreset(user);
		for (SchedulePresetDataFormat i : plistDataFormats) {
			SchedulePresetResponseFormat tempFormat = new SchedulePresetResponseFormat();
			tempFormat.setSchedulePreset(i.getHourList());
			tempFormat.setPresetUid(i.getPresetUid());
			res.add(tempFormat);
		}
		return res;
	}
	
	@PostMapping("/create")
	@Transactional(readOnly = false)
	public String createSchedulePreset2(@RequestHeader("Authorization") String email , @RequestBody CreateSchedulePresetRequestFormat req) {
		this.createSchedulePreset(email, req);
		return "ok";
	}
	
	// TODO: URI 변경을 위한 오버로딩. 프론트 대응 후 /create 삭제 예정
	@PostMapping("")
	@Transactional(readOnly = false)
	public String createSchedulePreset(@RequestHeader("Authorization") String email , @RequestBody CreateSchedulePresetRequestFormat req) {
		User user = userService.findUserByEmail(email);
		
		SchedulePresetDataFormat presetFormat = new SchedulePresetDataFormat();
		presetFormat.setHourList(req.getSchedulePreset());
		SchedulePreset preset = presetService.createSchedulePreset(user, presetFormat);
			
		// TODO: 리뷰 필요. 프리셋 생성시 새 프리셋을 기본값으로.
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
		SchedulePresetDataFormat presetFormat = presetService.convertHourListToDataFormat(req.getSchedulePreset());
		String encodedPreset = presetFormat.encodeHourListToSchedulePresetString();
		
		SchedulePreset updateTarget = presetService.findPresetByID(uid);
		updateTarget.setPresetCode(encodedPreset);
		
		return "ok";
	}
	
	@PutMapping("/select")
	@Transactional(readOnly = false)
	public String selectSchedulePreset(@RequestHeader("Authorization") String email, @RequestParam("presetUid") long presetUid) {
		User user = userService.findUserByEmail(email);
		userService.setPresetCode(user, presetUid);
		
		return "ok";
	}
		
}
