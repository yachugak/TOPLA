package com.yachugak.topla.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yachugak.topla.dataformat.SchedulePresetDataFormat;
import com.yachugak.topla.entity.Plan;
import com.yachugak.topla.entity.Task;
import com.yachugak.topla.entity.User;
import com.yachugak.topla.exception.InvalidArgumentException;
import com.yachugak.topla.plan.Planizer;
import com.yachugak.topla.plan.TaskItem;
import com.yachugak.topla.plan.TimeTable;
import com.yachugak.topla.repository.PlanRepository;


@Service
public class PlanService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PresetService presetService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private PlanRepository planRepository;
	
	public void plan(User user, Date planStartDate) {
		logger.debug("plan 시작합니다.");
		int planStartDay = planStartDate.getDay();
		List<SchedulePresetDataFormat> schedulePresetList = presetService.getAllPreset(user);
		
		//TODO: 후에 선택된 프리셋을 가져오는 API가 생기면 그거 반영할 것
		SchedulePresetDataFormat selectedPreset = schedulePresetList.get(0);
		logger.debug("선택된 프리셋 정보");
		logger.debug("일요일 " + selectedPreset.getTimeByHour(0)+"분");
		logger.debug("월요일 " + selectedPreset.getTimeByHour(1)+"분");
		logger.debug("화요일 " + selectedPreset.getTimeByHour(2)+"분");
		logger.debug("수요일 " + selectedPreset.getTimeByHour(3)+"분");
		logger.debug("목요일 " + selectedPreset.getTimeByHour(4)+"분");
		logger.debug("금요일 " + selectedPreset.getTimeByHour(5)+"분");
		logger.debug("토요일 " + selectedPreset.getTimeByHour(6)+"분");
		
		List<Task> taskList = taskService.getTaskListToPlan(user.getUid(), planStartDate);
		
		Planizer planizer = new Planizer(selectedPreset, taskList, planStartDay);
		TimeTable calculatedPlan = planizer.plan();
		
		//task의 기존 일정 초기화
		int lastDayOffset = calculatedPlan.getLastDayOffset();
		logger.debug("TimeTable에는 " + (lastDayOffset+1) + "일간의 일정이 담겨 있습니다.");
		for(int dayOffset = 0; dayOffset <= lastDayOffset; dayOffset++) {
			List<TaskItem> todayTaskList = calculatedPlan.getDay(dayOffset).getTaskItems();
			logger.debug("dayOffset " + dayOffset + "에는 " + todayTaskList.size() + "개의 일정 아이템이 담겨 있습니다." );
			for(TaskItem ti : todayTaskList) {
				long taskUid = ti.getTaskId();
				Task targetTask = taskService.findTaskById(taskUid);
				taskService.clearPlan(targetTask);
				logger.debug(taskUid + "번 작업의 일정을 초기화함.");
			}
			
		}

		//새로운 일정 할당
		Date currentDate = new Date(planStartDate.getTime());
		for(int dayOffset = 0; dayOffset <= lastDayOffset; dayOffset++) {
			List<TaskItem> todayTaskList = calculatedPlan.getDay(dayOffset).getTaskItems();
			for(TaskItem ti : todayTaskList) {
				long taskUid = ti.getTaskId();
				int doTime = ti.getTime();
				Task targetTask = taskService.findTaskById(taskUid);
				taskService.addPlan(targetTask, currentDate, doTime);
				logger.debug(taskUid + "번 작업에 " + currentDate + "에 " + doTime + "분의 일정을 추가함");
			}
			
			currentDate = nextDate(currentDate);
		}
		
		logger.debug("일정 반영 끝");
	}
	
	
	private Date nextDate(Date targetDate) {
		Calendar c = Calendar.getInstance(); 
		c.setTime(targetDate); 
		c.add(Calendar.DATE, 1);
		return c.getTime();
	}

	public Plan findPlanById(long planUid) {
		return planRepository.findById(planUid).get();
	}
	
	// Plan과 task의 progress 동시 업데이트.
	public void setProgress(Plan targetPlan, int progress) {
		int assignedTime = targetPlan.getDoTime();		
		int cappedProgress = this.getCappedProgress(assignedTime, progress);
		int prevProgress = targetPlan.getProgress(); // TOOD: 오류 시, 얘가 문제일 가능성 농후. null값으로 초기화되는지 확인		
		int progressDiff = cappedProgress - prevProgress;
		
		targetPlan.setProgress(cappedProgress);
		taskService.addProgress(targetPlan.getTask(), progressDiff);
	}

	// 만약, 배정시간보다 많이 했을 경우에 값 조절. 0 <= progress <= doTime
	public int getCappedProgress(int assignedTime, int progress) {
		int cappedProgress = progress;
		if(progress < 0) {
			throw new InvalidArgumentException("progress", "0 이상", progress+"");
		}
		if(progress > assignedTime) {
			cappedProgress = assignedTime;
		}
		return cappedProgress;
	}

}
