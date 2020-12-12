package com.yachugak.topla.exception;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class YouAreNotOwnerException extends ToplaException {
	public YouAreNotOwnerException(String entityName, long entityId) {
		super("YANO", "현재 작업을 시도하려는 유저가" + entityId + "번 " + entityName + "의 주인이 아닙니다.");
		ArrayList<String> list = new ArrayList<>();
		list.add(entityName);
		list.add(entityId+"");
		this.setArguments(list);
	}
}
