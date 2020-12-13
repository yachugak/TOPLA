package com.yachugak.topla.repository;

import java.time.LocalDate;
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
	
	@Query("from Task t left join t.user u where t.title = ?1 and t.dueDate = ?2 and t.user = ?3")
	public List<Task> findByTitleAndDueDateAndUser(String title, Date dueDate, User user);
	
	@Query("from Task t left join t.user u where u.uid = ?1 and t.dueDate >= ?2")
	public List<Task> findTaskToPlan(long userUid, Date planStartDate);
	
	public List<Task> findByTitleContains(String keyword);
	
	public List<Task> findByDueDate(Date date);
	
	public List<Task> findByUser(User user);
	
	public List<Task> findByFinishDate(Date finishDate);

	public List<Task> findByRemindingTiming(Date remindingTiming);

	@Query("from Task t left join t.user u where t.remindingTiming = ?1 and u.pushAlarmStatus = ?2")
	public List<Task> findByRemindingTimingAndPushAlarmStatus(Date currentTime, boolean b);

	@Query("from Task t left join t.user u where u = ?1 and t.createdDate >= ?2 and t.createdDate <= ?3")
	public List<Task> findNewTaskIn7Days(User targetUser, Date startDate, Date endDate);

	@Query("from Task t where t.finishDate > 0")
	public List<Task> findAllDoneTask();

	@Query("from Task t where t.createdDate >= ?1 and t.createdDate <= ?2")
	public List<Task> findNewTaskIn7Days(Date start, Date end);

	@Query("from Task t where t.finishDate >= ?1 and t.finishDate <= ?2")
	public List<Task> findDoneTaskIn7Days(Date start, Date end);

	
}
