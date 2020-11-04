package com.yachugak.topla.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yachugak.topla.entity.SchedulePreset;
import com.yachugak.topla.entity.User;
import com.yachugak.topla.repository.PresetRepository;
import com.yachugak.topla.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PresetRepository presetRepository;
	
	public User findUserById(long uid) {
		return userRepository.findById(uid).get();
	}

	public void setPresetCode(User user, long presetUid) {
		SchedulePreset targetPreset = presetRepository.findById(presetUid).get();
		user.setSchedulePreset(targetPreset);
		return;	
	}
	
	
}
