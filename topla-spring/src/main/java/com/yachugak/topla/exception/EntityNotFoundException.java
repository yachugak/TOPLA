package com.yachugak.topla.exception;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends ToplaException {
	public EntityNotFoundException(String entityName, long uid) {
		super("ENF", uid+"번 " + entityName + "가 존재하지 않습니다.");
		ArrayList<String> list = new ArrayList<>();
		list.add(entityName);
		list.add(uid+"");
		this.setArguments(list);
	}
}
