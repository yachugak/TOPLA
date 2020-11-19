package com.yachugak.topla.exception;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class NotRegisteredTaskException extends ToplaException {
	public NotRegisteredTaskException(String reason) {
		super("TLPF", reason);
		ArrayList<String> list = new ArrayList<>();
		list.add(reason);
		this.setArguments(list);
	}
}
