package com.yachugak.topla.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yachugak.topla.entity.Plan;
import com.yachugak.topla.entity.Task;
import com.yachugak.topla.entity.User;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
	public List<Plan> findByTask(Task task);
	public List<Plan> findByDoDateGreaterThanEqual(Date date);
  
	@Query("from Plan p left join p.task t where t.user.uid = ?1 and p.doDate = ?2")
	public List<Plan> findPlanToMorningPush(long userUid, Date doDate);

	@Query("from Plan p left join p.task t left join t.user u where u.uid = ?1 and p.doDate >= ?2")
	public List<Plan> findByUserAndDoDateGreaterThanEqual(long userUid, Date startDate);

}
