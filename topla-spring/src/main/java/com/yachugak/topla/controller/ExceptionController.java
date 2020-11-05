package com.yachugak.topla.controller;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.yachugak.topla.exception.ToplaException;

@ControllerAdvice
@RestController
public class ExceptionController {
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(value = ToplaException.class)
	public HashMap<String, String> errorHandler(ToplaException e) {
		HashMap<String, String> res = new HashMap<>();
		
		res.put("message", e.getMessage());
		res.put("errorCode", e.getErrorCode());
		
		return res;
	}
}
