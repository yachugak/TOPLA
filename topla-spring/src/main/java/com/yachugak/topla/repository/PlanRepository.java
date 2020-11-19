package com.yachugak.topla.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yachugak.topla.entity.Plan;
import com.yachugak.topla.entity.Task;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
	public List<Plan> findByTask(Task task);

}
