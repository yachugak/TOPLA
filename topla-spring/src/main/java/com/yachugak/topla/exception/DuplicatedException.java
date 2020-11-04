package com.yachugak.topla.exception;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class DuplicatedException extends ToplaException{
	public DuplicatedException(String newTaskName, String taskName) {
		super("DUPLICATED", newTaskName + "는" + taskName + "과 중복됩니다.");
		ArrayList<String> list = new ArrayList<>();
		list.add(newTaskName);
		list.add(taskName);
		this.setArguments(list);
	}
}
