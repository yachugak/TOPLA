package com.yachugak.topla.exception;

import java.util.ArrayList;

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
