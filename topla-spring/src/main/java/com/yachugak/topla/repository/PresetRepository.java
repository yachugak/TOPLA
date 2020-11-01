package com.yachugak.topla.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yachugak.topla.entity.SchedulePreset;

@Repository
public interface PresetRepository extends JpaRepository<SchedulePreset, Long>{
	public Optional<SchedulePreset> findByUid(long uid);
	
	@Query("from SchedulePreset sp left join sp.user u where u.uid = ?1")
	public List<SchedulePreset> findByUserUid(long userUid);
	
	
}
