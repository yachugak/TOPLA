package com.yachugak.topla.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yachugak.topla.entity.TemporaryUser;

@Repository
public interface TemporaryUserRepository extends JpaRepository<TemporaryUser, Long>{
	public Optional<TemporaryUser> findByEmail(String email);
	
	public Optional<TemporaryUser> findByCreatedDate(Date createdDate);
}
