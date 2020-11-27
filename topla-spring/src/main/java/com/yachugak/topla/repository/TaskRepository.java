package com.yachugak.topla.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yachugak.topla.entity.Task;
import com.yachugak.topla.entity.User;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

	public Optional<Task> findByTitle(String title);
	
	@Query("from Task t left join t.user u where u.uid = ?1 and t.dueDate >= ?2")
	public List<Task> findTaskToPlan(long userUid, Date planStartDate);
	
	public List<Task> findByTitleContains(String keyword);
	
	public List<Task> findByDueDate(Date date);
	
	public List<Task> findByUser(User user);
	
	public List<Task> findByFinishDate(Date finishDate);

	public List<Task> findByRemindingTiming(Date remindingTiming);
	
}
