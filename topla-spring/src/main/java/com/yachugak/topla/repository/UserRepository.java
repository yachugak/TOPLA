package com.yachugak.topla.repository;

import java.time.OffsetTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yachugak.topla.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	public Optional<User> findByUid(Long uid);
	
	public Optional<User> findByEmail(String email);
		
	public Optional<User> findByEmailAndPassword(String email, String password);
	
	public List<User> findByMorningReportTime(OffsetTime morningOffsetTime);
}