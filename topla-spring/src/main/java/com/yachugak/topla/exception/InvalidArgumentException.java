package com.yachugak.topla.exception;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidArgumentException extends ToplaException {
	public InvalidArgumentException(String argumentName, String expectedValue, String actualValue) {
		super("IAE", argumentName + "은 " + expectedValue + "이여야 하지만 " + actualValue + "입니다.");
		ArrayList<String> list = new ArrayList<>();
		list.add(argumentName);
		list.add(expectedValue);
		list.add(actualValue);
		this.setArguments(list);
	}
}
