package com.yachugak.topla.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yachugak.topla.entity.Schedule_preset;

@Repository
public interface PresetRepository extends JpaRepository<Schedule_preset, Long>{
	public Optional<Schedule_preset> findByUid(long uid);
	
}
