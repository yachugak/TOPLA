package com.yachugak.topla.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yachugak.topla.entity.Plan;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

}
