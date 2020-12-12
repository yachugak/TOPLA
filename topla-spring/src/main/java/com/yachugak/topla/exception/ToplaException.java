package com.yachugak.topla.exception;

import java.util.List;

public abstract class ToplaException extends RuntimeException {
	//프론트엔드에서 파악하기 쉬운 에러 코드
	private String errorCode;

	//프론트엔드에서 유저에게 보여줄 메세지
	private String userMessage;
	
	//에러에 대한 더 많은 정보를 포함하기 위한 배열
	private List<String> arguments;
	
	public ToplaException(String errorCode, String userMessage) {
		super(userMessage);
		this.errorCode = errorCode;
		this.userMessage = userMessage;
		this.arguments = arguments;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getUserMessage() {
		return userMessage;
	}

	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}

	public List<String> getArguments() {
		return arguments;
	}

	public void setArguments(List<String> arguments) {
		this.arguments = arguments;
	}
}
