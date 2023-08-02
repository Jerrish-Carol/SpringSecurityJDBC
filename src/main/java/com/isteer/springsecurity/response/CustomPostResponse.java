package com.isteer.springsecurity.response;

import java.util.List;

import com.isteer.springsecurity.entities.User;
import com.isteer.springsecurity.entities.UserResult;

import lombok.Data;

@Data
public class CustomPostResponse {

	private UserResult user;

	private long statusCode;

	private String message;

	public CustomPostResponse(long statusCode, String message, UserResult user) {
		super();
	
		this.user= user;
		this.statusCode = statusCode;
		this.message = message;
	}

}
