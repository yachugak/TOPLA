package com.yachugak.topla.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yachugak.topla.entity.Plan;
import com.yachugak.topla.entity.Task;
import com.yachugak.topla.entity.User;
import com.yachugak.topla.exception.EntityNotFoundException;
import com.yachugak.topla.exception.InvalidArgumentException;
import com.yachugak.topla.repository.PlanRepository;
import com.yachugak.topla.repository.TaskRepository;
import com.yachugak.topla.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	public User findUserById(long uid) {
		// TODO Auto-generated method stub
		return userRepository.findById(uid).get();
	}
	
	
}
