package com.yachugak.topla.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yachugak.topla.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

	public Optional<Task> findByTitle(String title);
}
