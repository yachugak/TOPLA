package com.yachugak.topla.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yachugak.topla.dataformat.SchedulePresetDataFormat;
import com.yachugak.topla.entity.SchedulePreset;
import com.yachugak.topla.entity.User;
import com.yachugak.topla.exception.EntityNotFoundException;
import com.yachugak.topla.exception.InvalidArgumentException;
import com.yachugak.topla.repository.PresetRepository;
import com.yachugak.topla.response.SchedulePresetResponseFormat;

@Service
public class PresetService {
	@Autowired
	private PresetRepository presetRepository;
	@Autowired
	private UserService userService;

	public List<SchedulePreset> getAllPreset(User user) {
		List<SchedulePreset> presetList = presetRepository.findByUserUid(user.getUid());
		if (presetList.isEmpty()) {
			throw new EntityNotFoundException("schedulePreset", user.getUid());
		}
		return presetList;
	}

	public SchedulePreset createSchedulePreset(User user, String presetName, SchedulePresetDataFormat presetDataFormat) {
		SchedulePreset newPreset = new SchedulePreset();	
		this.setName(newPreset, presetName);
		this.setUser(newPreset, user);
		this.setPresetCode(newPreset, presetDataFormat.encodeHourListToSchedulePresetString());

		presetRepository.saveAndFlush(newPreset);
		return newPreset;
	
	}

	public void setName(SchedulePreset preset, String name) {
		if(preset == null) {
			throw new InvalidArgumentException("preset", "preset 값", preset+"");
		}
		preset.setName(name);
	}
	
	public void setUser(SchedulePreset preset, User user) {
		if(preset == null) {
			throw new InvalidArgumentException("preset", "preset 값", preset+"");
		}
		if(user == null) {
			throw new InvalidArgumentException("user", "user 값", user+"");
		}
		preset.setUser(user);
	}
	
	public void setPresetCode(SchedulePreset preset, String presetCode) {
		if(preset == null) {
			throw new InvalidArgumentException("preset", "preset 값", preset+"");
		}
		if(presetCode == null) {
			throw new InvalidArgumentException("presetCode", "presetCode 값", presetCode+"");
		}
		preset.setPresetCode(presetCode);
	}

	// 유저가 선택해둔 preset을 데이터 포맷의 형태로 반환합니다.
	public SchedulePresetDataFormat getSelectedPresetInDataFormat(User user) {
		SchedulePreset targetPreset = user.getSchedulePreset();
		SchedulePresetDataFormat presetFormat = this.convertPresetToDataFormat(targetPreset);
		
		return presetFormat;
	}

	// Front에서 받은 HourList 배열을 데이터포맷의 형태로 변환합니다.
	public SchedulePresetDataFormat convertHourListToDataFormat(int[] hourList) {
		SchedulePresetDataFormat presetFormat = new SchedulePresetDataFormat();
		presetFormat.setHourList(hourList);
		return presetFormat;

	}

	// Preset 객체를 받아 데이터포맷의 형태로 변환합니다.
	public SchedulePresetDataFormat convertPresetToDataFormat(SchedulePreset preset) {
		SchedulePresetDataFormat presetFormat = new SchedulePresetDataFormat();
		presetFormat.decode(preset.getPresetCode());
		return presetFormat;
	}

	// Preset 객체 리스트를 받아 데이터포맷 리스트의 형태로 변환합니다.
	public List<SchedulePresetDataFormat> convertPresetListToDataFormats(List<SchedulePreset> presetList) {
		List<SchedulePresetDataFormat> presetFormats = new ArrayList<SchedulePresetDataFormat>();
		for (SchedulePreset p : presetList) {
			SchedulePresetDataFormat dataformat = new SchedulePresetDataFormat();
			dataformat.decode(p.getPresetCode());
			presetFormats.add(dataformat);
		}
		return presetFormats;
	}
	
	// preset 객체 리스트와, 해당 리스트의 dataformat을 받아 responseFormat을 작성합니다.
	public ArrayList<SchedulePresetResponseFormat> assemblePresetResponseFormat(List<SchedulePreset> presetList, List<SchedulePresetDataFormat> formatList){
		ArrayList<SchedulePresetResponseFormat> res= new ArrayList<>();
		for(int i=0; i < presetList.size(); i++) {
			SchedulePresetResponseFormat tempFormat = new SchedulePresetResponseFormat();
			tempFormat.setPresetUid(presetList.get(i).getUid());
			tempFormat.setPresetName(presetList.get(i).getName());
			tempFormat.setSchedulePreset(formatList.get(i).getHourList());
			res.add(tempFormat);
		}
		return res;
	}

	// 기본 프리셋을 생성합니다.
	public SchedulePresetDataFormat createDefaultSchedulePreset() {
		int[] temp = new int[7];
		for (int i = 0; i < 7; i++) {
			temp[i] = 180;
		}
		return this.convertHourListToDataFormat(temp);
	}

	public SchedulePreset findPresetByID(long presetUid) {
		SchedulePreset targetPreset = presetRepository.findById(presetUid).orElseThrow(()->new EntityNotFoundException("preset", presetUid+""));
		return targetPreset;
	}

	
	// 선택된 것을 삭제: 프리셋 갯수 확인, 삭제, 재설정
	// 선택 안된것 삭제: 프리셋 갯수 확인, 삭제
	public void deletePreset(long presetUid) {
		User user = this.findPresetByID(presetUid).getUser();
		boolean flag = isThisPresetSelected(user, presetUid);
		
		if(isOnlyPreset(user.getUid())) {
			throw new EntityNotFoundException("userUid(" + user.getUid()+ ")" + "의 프리셋 삭제", "최소 하나의 프리셋이 필요합니다.");
		}
			
		presetRepository.deleteById(presetUid);
		
		// 선택되어있던 프리셋이 삭제시 재설정
		if(flag) {
			this.selectFirstPreset(user);
		}
	}

	// 해당 프리셋이 해당 유저가 선택한 프리셋인지 확인하는 함수.
	private boolean isThisPresetSelected(User user, long presetUid) {
		if(user.getSchedulePreset().getUid() == presetUid) {
			return true;
		}
		return false;
	}

	// 해당 유저가 가진 첫 번째 프리셋을 선택하는 함수.
	private void selectFirstPreset(User user) {
		List<SchedulePreset> presetList = presetRepository.findByUser(user);
		if(presetList.isEmpty()) {
			throw new EntityNotFoundException("presetList", "해당 유저의 Preset이 존재하지 않습니다.");
		}
		SchedulePreset targetPreset = presetRepository.findByUser(user).get(0);
		userService.setSelectedPreset(user, targetPreset);
	}

	// 해당 유저가 프리셋을 한개만 가지고 있다면 true. 복수시 false.
	private boolean isOnlyPreset(long userUid) {
		List<SchedulePreset> plist = presetRepository.findByUserUid(userUid);
		if(plist.size() <= 1) {
			return true;
		}
		return false;
	}
	
	
}
