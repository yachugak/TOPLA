package com.yachugak.topla.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
	
		SchedulePresetDataFormat selectedPreset = presetService.getSelectedPresetInDataFormat(user);
		
		logger.debug("선택된 프리셋 정보");
		logger.debug("일요일 " + selectedPreset.getTimeByHour(0)+"분");
		logger.debug("월요일 " + selectedPreset.getTimeByHour(1)+"분");
		logger.debug("화요일 " + selectedPreset.getTimeByHour(2)+"분");
		logger.debug("수요일 " + selectedPreset.getTimeByHour(3)+"분");
		logger.debug("목요일 " + selectedPreset.getTimeByHour(4)+"분");
		logger.debug("금요일 " + selectedPreset.getTimeByHour(5)+"분");
		logger.debug("토요일 " + selectedPreset.getTimeByHour(6)+"분");
		
		List<Task> taskList = taskService.getTaskListToPlan(user.getUid(), planStartDate);
		
		Planizer planizer = new Planizer(selectedPreset, taskList, planStartDate);
		TimeTable doneTimeTable = this.recoverDoneTimeTable(user, planStartDate);
		planizer.setDoneTimeTable(doneTimeTable);
		// TimeTable calculatedPlan = planizer.naivelyOptimizedPlan();
		TimeTable calculatedPlan = planizer.fractionalBinPackingPlan();
		
		//task의 기존 일정 초기화
		int lastDayOffset = calculatedPlan.getLastDayOffset();
		List<Long> withoutList = calculatedPlan.getPlanUidList();
		logger.debug("TimeTable에는 " + (lastDayOffset+1) + "일간의 일정이 담겨 있습니다.");
		for(int dayOffset = 0; dayOffset <= lastDayOffset; dayOffset++) {
			List<TaskItem> todayTaskList = calculatedPlan.getDay(dayOffset).getTaskItems();
			logger.debug("dayOffset " + dayOffset + "에는 " + todayTaskList.size() + "개의 일정 아이템이 담겨 있습니다." );
			for(TaskItem ti : todayTaskList) {
				long taskUid = ti.getTaskId();
				Task targetTask = taskService.findTaskById(taskUid);
				taskService.clearPlanWithout(targetTask, withoutList);
				logger.debug(taskUid + "번 작업의 일정을 일부(또는 전부) 초기화함.");
			}
			
		}

		//새로운 일정 할당
		Date currentDate = new Date(planStartDate.getTime());
		for(int dayOffset = 0; dayOffset <= lastDayOffset; dayOffset++) {
			List<TaskItem> todayTaskList = calculatedPlan.getDay(dayOffset).getTaskItems();
			for(TaskItem ti : todayTaskList) {
				if(ti.getPlnaUid() != null) {
					//이미 등록되어 있는 plan이므로 스킵
					continue;
				}
				long taskUid = ti.getTaskId();
				int doTime = ti.getTime();
				Task targetTask = taskService.findTaskById(taskUid);
				taskService.addPlan(targetTask, currentDate, doTime);
				logger.debug(taskUid + "번 작업에 " + currentDate + "에 " + doTime + "분의 일정을 추가함");
			}
			
			currentDate = nextDate(currentDate);
		}
		
		user.setTotalLossPriority(calculatedPlan.getTotalLossPriority(planStartDate));
		
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
	
	// Plan과 task의 progress 동시 업데이트. 언체크시 progress == -1
	public void setProgress(Plan targetPlan, int progress) {
		if(progress < 0) {
			progress = 0;
		}
		
		int assignedTime = targetPlan.getDoTime();		
		int cappedProgress = this.getCappedProgress(assignedTime, progress);
		int prevProgress = targetPlan.getProgress();		
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
	
	public TimeTable recoverDoneTimeTable(User user, Date startDate){
		TimeTable doneTimeTable = new TimeTable();

		List<Plan> planList = planRepository.findByUserAndDoDateGreaterThanEqual(user.getUid(), startDate);

		for(Plan plan : planList) {
			if(plan.getDoTime() <= plan.getProgress()) { //progress는 doTime을 넘을 수 없지만 혹시 몰라서
				TaskItem newTaskItem = new TaskItem();
				newTaskItem.setTaskId(plan.getTask().getUid());
				newTaskItem.setPlnaUid(plan.getUid());
				newTaskItem.setTime(plan.getDoTime());
				int dayOffset = calDayOffset(startDate, plan.getDoDate());
				doneTimeTable.registerTask(plan.getTask());
				doneTimeTable.addTaskItem(dayOffset, newTaskItem);
			}
		}
		
		return doneTimeTable;
	}
	
	public int calDayOffset(Date planStartDate, Date taskDoDate) {
		long diffInMillies = taskDoDate.getTime() - planStartDate.getTime();
	    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	    
	    return (int)diff;
	}
	
	public List<Plan> findByTask(Task task){
		return planRepository.findByTask(task);
	}


	public List<Plan> findPlanByUserUidAndDoDate(Long userUid, Date doDate) {
		// TODO: 리뷰필요.
		return planRepository.findPlanToMorningPush(userUid, doDate);
		
	}
}
