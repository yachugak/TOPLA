package com.yachugak.topla.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yachugak.topla.entity.Report;
import com.yachugak.topla.entity.Task;
import com.yachugak.topla.entity.TaskHistory;
import com.yachugak.topla.entity.User;

@Repository
public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long>{
	public Optional<TaskHistory> findByUid(long uid);
	
	public List<TaskHistory> findByTask(Task task);
	
	public List<TaskHistory> findByReport(Report report);
	
	public List<TaskHistory> findByRecordedTime(Date recordedTime);
	
	public List<TaskHistory> findByRecordedTimeAndUser(Date recordedTime, User user);
}
