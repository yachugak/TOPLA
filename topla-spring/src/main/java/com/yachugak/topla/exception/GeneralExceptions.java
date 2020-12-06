package com.yachugak.topla.exception;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class GeneralExceptions extends ToplaException {
	public GeneralExceptions(String msg) {
		super("GENERAL", msg);
		ArrayList<String> list = new ArrayList<>();
		list.add(msg);
		this.setArguments(list);
	}
}
