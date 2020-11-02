package com.yachugak.topla.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yachugak.topla.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	public Optional<User> findByUid(Long uid);
	
}
