package com.yachugak.topla.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yachugak.topla.entity.AuthMapping;
import com.yachugak.topla.entity.User;

@Repository
public interface AuthMappingRepository extends JpaRepository<AuthMapping, Long>{
	public Optional<AuthMapping> findByUser(User user);
	
	public Optional<AuthMapping> findBySecureCode(String secureCode);
}
