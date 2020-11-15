package com.yachugak.topla.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yachugak.topla.entity.TaskHistory;

@Repository
public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long>{
	public Optional<TaskHistory> findByUid(long uid);
	
	public List<TaskHistory> findByTaskUid(long TaskUid);
	
	public List<TaskHistory> findByReport(long ReportUid);
}
